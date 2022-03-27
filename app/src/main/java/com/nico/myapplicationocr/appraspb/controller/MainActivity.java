package com.nico.myapplicationocr.appraspb.controller;

import static android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nico.myapplicationocr.appraspb.R;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final String TAG = "MainActivity";
    private static TextView mTextViewHello;
    private static TextView mTextViewNotice;

    //liste historique device appaire
    private static Button mButtonHome;
    private static ListView devicelist;
    private Set<BluetoothDevice> listAppairageAppareils;

    private BluetoothAdapter mBluetoothAdapter = null;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mTextViewHello = findViewById(R.id.textHello);
        mTextViewNotice = findViewById(R.id.textNotice);

        mButtonHome = findViewById(R.id.main_button);
        devicelist = (ListView) findViewById(R.id.ListViewAppareilsBT);

        mButtonHome.setEnabled(false);

        if (mBluetoothAdapter == null) {
            // Bluetooth incompatible
            Toast toastBTincompatible = Toast.makeText(MainActivity.this, "Bluetooth non compatible avec cet appareil !", Toast.LENGTH_LONG);
            toastBTincompatible.show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            mButtonHome.setEnabled(true);
        }

        //liste historique device appaire
        mButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    listeAppareils();
                }
            }

            //historique appareils appaire
            private void listeAppareils() {
                listAppairageAppareils = mBluetoothAdapter.getBondedDevices();
                ArrayList list = new ArrayList();
                if (listAppairageAppareils.size() > 0) {
                    for (BluetoothDevice bt : listAppairageAppareils) {
                        list.add(bt.getName() + "\n" + bt.getAddress());

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Aucun appareils bluetooth appairé trouvé !", Toast.LENGTH_LONG).show();
                }
                ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, list);
                devicelist.setAdapter(adapter);
                devicelist.setOnItemClickListener(ListClickListener);
            }

            private AdapterView.OnItemClickListener ListClickListener = new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView adapterView, View view, int arg2, long arg3) {
                    mBluetoothAdapter.startDiscovery();

                    //listAppairageAppareils.
                    String info = ((TextView) view).getText().toString();
                    String addrMac = info.substring(info.length() - 17); //Nom de l'appareil + addr MAC -> on recupere juste l'addr MAC de 17 caracteres

                    BluetoothDevice dev = mBluetoothAdapter.getRemoteDevice(addrMac);

                    Intent pilotageActivityIntent = new Intent(MainActivity.this, PilotageActivity.class); //creation new intent
                    pilotageActivityIntent.putExtra("AddrMAC", addrMac); //ajout addr MAC

                    startActivity(pilotageActivityIntent);
                }
            };
        });
    }
}
