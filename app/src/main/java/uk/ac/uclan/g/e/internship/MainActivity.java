package uk.ac.uclan.g.e.internship;

import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static boolean soundEnable = true;
    private Notification notificationBluetooth;
    private NotificationManager notifManager;
    private BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_ENABLE_BT=0;
    private Set<BluetoothDevice> devices; // this will contain the list of bluetooth device
    private ImageButton button_sound;


    //  http://www.tutos-android.com/utilisation-bluetooth-application-android
    // http://developer.android.com/guide/topics/connectivity/bluetooth.html
    // animation: http://developer.android.com/guide/topics/graphics/prop-animation.html
    // https://www.youtube.com/watch?v=uP9Bbzk_sB0
    // http://stackoverflow.com/questions/1520887/how-to-pause-sleep-thread-or-process-in-android

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.init();
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Bluetooth application
        // != nul if bluetooth is available on this device




        if(bluetoothAdapter == null){
            notifManager.notify(1, notificationBluetooth);
            Toast.makeText(getApplicationContext(),"Bluetooth isn't available on this device ",
                    Toast.LENGTH_LONG).show();

        }else{
            if (!bluetoothAdapter.isEnabled()) { // if Bluetooth isn't activate yet
                bluetoothAdapter.enable(); // automatic activation of Bluetooth
                devices = bluetoothAdapter.getBondedDevices();
                Toast.makeText(getApplicationContext(),"Bluetooth activated",
                        Toast.LENGTH_LONG).show();
            }else{
                devices = bluetoothAdapter.getBondedDevices();
                // all devices known
                for (BluetoothDevice blueDevice : devices) {
                    Toast.makeText(getApplicationContext(), "Device = " + blueDevice.getName(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_ENABLE_BT)
            return; // end of method
        if (resultCode == RESULT_OK) {
            Toast.makeText(getApplicationContext(),"Bluetooth is activated",
                    Toast.LENGTH_LONG).show();
            bluetoothSeekEnable(true);
            // L'utilisation a activé le bluetooth
        } else {
            Toast.makeText(getApplicationContext(),"Bluetooth is required to play",
                    Toast.LENGTH_LONG).show();
            // L'utilisation n'a pas activé le bluetooth
        }
    }

    /**
     * give the state of button_sound
     * @return soundEnable
     */
    public static boolean getStateSound(){
        return MainActivity.soundEnable;
    }

    /**
     * to enable or disable button_sound
     * if sate == false button_sound is disable
     * @param state
     */
    public static void setSoundState(boolean state){

        MainActivity.soundEnable=state;
    }

    /**
     * initialisation of notification system
     * there is only one notification for now
     * which appears if bluetooth isn't available on the device
     */
    private void setNotification (){
        notificationBluetooth =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.warning_icone)
                        .setContentTitle("Error")
                        .setContentText("Bluetooth isn't available on this device").build();
        // the workseet method didn't work
        //

        notifManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    /**
     * uses several other function
     */
    private void init(){ // initialisation function
        this.setNotification(); // implementation of notification
        this.addClikListener(); // implementation of Listener on buttons
        this.bluetoothSeekEnable(true);
        this.bluetoothManagement(); // implementation of bluetooth
    }

    private void autoConnect(){
        // connection to the band
    }


    private void bluetoothManagement(){ // initialisation of bluetooth
        // empty


    }

    private void bluetoothSeekEnable(boolean state){
        Intent discoverableIntent;
        if(state){
            discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);

        }else{
            discoverableIntent = new Intent(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        }
        startActivity(discoverableIntent);
    }

    /**
     * add all listener on Button and ImageButton
     */
    private void addClikListener(){
        View.OnClickListener imgButtonHandler = new View.OnClickListener() {
            // create a View.Onclick Listener which will be use by button_sound
            public void onClick(View v) {
                AudioManager amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
                            // audio managment
                if(MainActivity.getStateSound()){ // if the sound is enable
                    MainActivity.setSoundState(false); // disable sound
                    button_sound.setImageResource(R.drawable.icone_sound_mute);
                    // load image which coresponds at sound_inactive
                    amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                            // mute the sound (Music only)
                }else {
                    MainActivity.setSoundState(true);
                    amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                            // unmute the sound (Music only)
                    button_sound.setImageResource(R.drawable.sound_active);
                        // load the image sou
                }
            }
        };

        this.button_sound = (ImageButton) findViewById(R.id.button_main_sound_state);
        this.button_sound.setOnClickListener(imgButtonHandler);

        final Button playButton = (Button) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() { // Add action
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter == null) { // if bluetooth isn't available
                    notifManager.notify(1, notificationBluetooth);
                    // a notification appear with ID = 1
                    Toast.makeText(getApplicationContext(),"Bluetooth isn't available on this device ",
                            Toast.LENGTH_LONG).show();
                }else{
                    if (!bluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                            /* dialog which ask the permission to use Bluetooth
                            the answer will be treated by the function onActivityResult
                             */


                        Intent intent = new Intent(MainActivity.this, Video.class); // changer d'activité
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(MainActivity.this, Video.class); // changer d'activité
                        startActivity(intent);

                    }
                    /*
                    Intent nextActivity = new Intent(getApplicationContext(), //nextActity);
                    startActivity(nextActivity);
                    */
                }
            }
        });
    }

}





 /*
                        other choice
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Bluetooth is required to continue, ")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                      bluetoothAdapter.enable();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();// dismisses the dialog with no actions
                                        Toast.makeText(MainActivity.this, "bonne partie alors!", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show(); */