package uk.ac.uclan.g.e.internship;

import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private BluetoothAdapter bluetoothAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Bluetooth application  != if bluetooth is activated

        final Button playButton = (Button) findViewById(R.id.playButton);

        final NotificationCompat.Builder notificationBluetooth =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.warning_icone)
                        .setContentTitle("Error")
                        .setContentText("Bluetooth isn't activated");

        final NotificationManager notifManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        playButton.setOnClickListener(new View.OnClickListener() { // Add action
            @Override
            public void onClick(View v) {
                if (bluetoothAdapter == null) { // if bluetooth isn't running
                    notifManager.notify(1, notificationBluetooth.build());
                }
            }
        });


    }
}
