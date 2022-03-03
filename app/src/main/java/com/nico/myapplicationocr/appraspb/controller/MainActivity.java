package com.nico.myapplicationocr.appraspb.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nico.myapplicationocr.appraspb.R;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static TextView mTextViewHello;
    private static TextView mTextViewNotice;
    private static Button mButtonHome;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewHello = findViewById(R.id.textHello);
        mTextViewNotice = findViewById(R.id.textNotice);
        mButtonHome = findViewById(R.id.main_button);

        mButtonHome.setEnabled(false);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast toastBTincompatible = Toast.makeText(MainActivity.this, "Bluetooth non compatible avec cet appareil !", Toast.LENGTH_LONG);
            toastBTincompatible.show();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            mButtonHome.setEnabled(true);
        }

        mButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }else {
                    Intent pilotageActivityIntent = new Intent(MainActivity.this, PilotageActivity.class);
                    startActivity(pilotageActivityIntent);
                }
            }
        });


    }
}