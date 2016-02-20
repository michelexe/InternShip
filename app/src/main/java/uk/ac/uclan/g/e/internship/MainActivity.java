package uk.ac.uclan.g.e.internship;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_ENABLE_BT=0;
    private Set<BluetoothDevice> devices; // this will contain the list of bluetooth device

    //  http://www.tutos-android.com/utilisation-bluetooth-application-android

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Bluetooth application  != if bluetooth is activated

        final Button playButton = (Button) findViewById(R.id.playButton);

        final Notification notificationBluetooth =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.warning_icone)
                        .setContentTitle("Error")
                        .setContentText("Bluetooth isn't available on this device").build();
            // the workseet method didn't work
        //

        final NotificationManager notifManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(bluetoothAdapter == null){
            notifManager.notify(1, notificationBluetooth);
            Toast.makeText(getApplicationContext(),"Bluetooth isn't available on this device ",
                    Toast.LENGTH_LONG).show();

        }else{
            if (!bluetoothAdapter.isEnabled()) { // if Bluetooth isn't activate yet
                bluetoothAdapter.enable(); // automatic activation of Bluetooth
                Toast.makeText(getApplicationContext(),"Bluetooth activated",
                        Toast.LENGTH_LONG).show();
            }else{
                devices = bluetoothAdapter.getBondedDevices();
                // all known device
                for (BluetoothDevice blueDevice : devices) {
                    Toast.makeText(getApplicationContext(), "Device = " + blueDevice.getName(),
                            Toast.LENGTH_SHORT).show();
                }
            }

        }


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
                            /* dialog which ask for the permission to use Bluetooth
                            the answer will be treated by the function onActivityResult
                             */

                    }
                    /*
                    Intent nextActivity = new Intent(getApplicationContext(), //nextActity);
                    startActivity(nextActivity);
                    */
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_ENABLE_BT)
            return; // end of method
        if (resultCode == RESULT_OK) {
            Toast.makeText(getApplicationContext(),"Bluetooth is activated",
                    Toast.LENGTH_LONG).show();
            // L'utilisation a activé le bluetooth
        } else {
            Toast.makeText(getApplicationContext(),"Bluetooth is required to play",
                    Toast.LENGTH_LONG).show();
            // L'utilisation n'a pas activé le bluetooth
        }
    }


    private void autoConnect(){
        // connection to the band
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