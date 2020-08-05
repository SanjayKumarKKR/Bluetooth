package com.example.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;

    private TextView mStatusBleTv, mPairedTv;
    ImageView mBlueIV;
    Button mOnBtn, mOffBtn, mDiscoverBtn, mPairedBtn,next;
    BluetoothAdapter bluetoothAdapter,myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStatusBleTv = findViewById(R.id.statusBluetoothTv);
        mPairedTv = findViewById(R.id.pairTv);
        mBlueIV = findViewById(R.id.bluetoothIv);
        mOnBtn = findViewById(R.id.onButn);
        mOffBtn = findViewById(R.id.offButn);
        mDiscoverBtn = findViewById(R.id.discoverableBtn);
        mPairedBtn = findViewById(R.id.PairedBtn);
        next = findViewById(R.id.next);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        myAdapter = BluetoothAdapter.getDefaultAdapter();
        myAdapter.startDiscovery();
        if (bluetoothAdapter == null) {
            mStatusBleTv.setText("Bluetooth is not available");
        } else {
            mStatusBleTv.setText("Bluetooth is  available");


            if (bluetoothAdapter.isEnabled()) {
                mBlueIV.setImageResource(R.drawable.bluetooth_on_foreground);
            } else {
                mBlueIV.setImageResource(R.drawable.bluetooth_off_foreground);

            }
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(),Discovery.class);
                        startActivity(i);
                }
            });
            mOnBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!bluetoothAdapter.isEnabled()) {
                        showToast("Turning on Bluetooth..");
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(intent, REQUEST_ENABLE_BT);
                    } else {
                        showToast("Bluetooth is already on");

                    }
                }
            });

            mDiscoverBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!bluetoothAdapter.isDiscovering()) {
                        showToast("Making Your Device Discoverable");
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                        startActivityForResult(intent, REQUEST_DISCOVER_BT);
                    }
                }
            });
            mOffBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bluetoothAdapter.isEnabled()) {
                        bluetoothAdapter.disable();
                        showToast("Turning  Bluetooth off");
                        mBlueIV.setImageResource(R.drawable.bluetooth_off_foreground);
                    } else {
                        showToast("Bluetooth is already off");

                    }
                }
            });

            mPairedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bluetoothAdapter.isEnabled()) {
                        mPairedTv.setText("Paired Devices");
                        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();

                        for (BluetoothDevice device : devices) {
                            mPairedTv.append("\n Device : " + device.getName() + " , " + device);
                        }
                    } else {
                        showToast("Turn On bluetooth to get paired devices");
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    mBlueIV.setImageResource(R.drawable.bluetooth_on_foreground);
                    showToast("Bluetooth is On");
                } else {
                    showToast("Bluetooth is Off");

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}