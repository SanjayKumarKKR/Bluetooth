package com.example.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Discovery extends AppCompatActivity {
    //private final BroadcastReceiver FoundReceiver = null;
    protected ArrayList<String> foundDevices;
    private ListView foundDevicesListView;
    private ArrayAdapter<String> btArrayAdapter;
    HashMap<String,String> map;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);
        final BluetoothAdapter myBlueToothAdapter = BluetoothAdapter.getDefaultAdapter();
        foundDevices = new ArrayList<String>();
        map=new HashMap<String, String>();
        final Button scanb = (Button) findViewById(R.id.button_id);
        final ListView foundDevicesListView = (ListView) findViewById(R.id.mobile_list);

        btArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, foundDevices);

        foundDevicesListView.setAdapter(btArrayAdapter);

        //Turn on Bluetooth
        if (myBlueToothAdapter == null)
            Toast.makeText(Discovery.this, "Your device doesnt support Bluetooth", Toast.LENGTH_LONG).show();
        else if (!myBlueToothAdapter.isEnabled()) {
            Intent BtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(BtIntent, 0);
            Toast.makeText(Discovery.this, "Turning on Bluetooth", Toast.LENGTH_LONG).show();
        }

        // Quick permission check
        int permissionCheck = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
        }
        if (permissionCheck != 0) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }


        scanb.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
//                btArrayAdapter.clear();
                myBlueToothAdapter.startDiscovery();
                Toast.makeText(Discovery.this, "Scanning Devices", Toast.LENGTH_LONG).show();

            }
        });

        registerReceiver(FoundReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        IntentFilter filter = new IntentFilter(
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(FoundReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(FoundReceiver);
    }


    private final BroadcastReceiver FoundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            // When discovery finds a new device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if(!map.containsKey(device.getName())){
                    Date date =new Date();
                    map.put(device.getName(),date+"");
                    Log.d("Sanjay",date+"");
                    foundDevices.add(device.getName()+" "+map.get(device.getName()));
                    btArrayAdapter.notifyDataSetChanged();
                }

            }

            // When discovery cycle finished
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (foundDevices == null || foundDevices.isEmpty()) {
                    Toast.makeText(Discovery.this, "No Devices", Toast.LENGTH_LONG).show();
                }
            }

        }
    };


}