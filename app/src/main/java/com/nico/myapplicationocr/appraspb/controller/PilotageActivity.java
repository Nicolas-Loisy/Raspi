package com.nico.myapplicationocr.appraspb.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nico.myapplicationocr.appraspb.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class PilotageActivity extends AppCompatActivity {

    private static TextView mTextViewTitleControle;
    private static EditText mEditTextCommande;
    private static Button mButtonEnvoyer;
    private static TextView mTextViewListeCommandes;

    private static String addrMAC;
    BluetoothAdapter myBluetooth;

    private BluetoothSocket mmSocket;
    //private final BluetoothDevice mmDevice;


    //private static final UUID MY_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");
    private static final String TAG = "MainActivity";

    public PilotageActivity() {
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilotage);

        mTextViewTitleControle = findViewById(R.id.textEcranControle);
        mEditTextCommande = findViewById(R.id.edittextCommande);
        mButtonEnvoyer = findViewById(R.id.buttonSendCommande);
        mTextViewListeCommandes = findViewById(R.id.textListeCommandes);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();


        Intent newint = getIntent();
        addrMAC = newint.getStringExtra("AddrMAC");

        BluetoothDevice dev = myBluetooth.getRemoteDevice(addrMAC);
        Toast.makeText(getApplicationContext(), dev.getName()+" : "+dev.getAddress(), Toast.LENGTH_LONG).show();
        Log.d(TAG, "Start activity avec : "+ dev.getName()+" : "+dev.getAddress());


        ConnectThread connexionThread = new ConnectThread(dev, myBluetooth);
        connexionThread.start();
        //connexionThread.run();




        /*Bug 5sec*/
        /*
        boolean connected = true;
        try{
            mmSocket = dev.createRfcommSocketToServiceRecord(MY_UUID);
            mmSocket.connect();
            //Toast.makeText(getApplicationContext(),"La connexion est un succès!!", Toast.LENGTH_LONG).show();
        }
        catch(IOException e){
            e.printStackTrace();
            connected = false;
        }
        if(connected){
            Log.e(TAG, "Connexion reussie!!");

        }else{
            Log.e(TAG, "echec de la connexion");
        }*/










        /* TESTS */

        //BluetoothSocket tmp = null;
       /* try {
            mmSocket = dev.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
            Toast.makeText(getApplicationContext(), "err1", Toast.LENGTH_LONG).show();
        }
        //mmSocket = tmp;
        //Toast.makeText(getApplicationContext(), "yes1 "+mmSocket.toString(), Toast.LENGTH_LONG).show();

        if (!mmSocket.isConnected()) {
            //Toast.makeText(getApplicationContext(), "yes2 "+mmSocket.toString(), Toast.LENGTH_LONG).show();
            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
                Toast.makeText(this, "connect work", Toast.LENGTH_SHORT).show();

            } catch (IOException connectException) {
                //mmSocket.close();
                Log.e(TAG, "Erreur connexion !!!!!!!!!!", connectException);
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
            }
        }*/

        //Toast.makeText(getApplicationContext(), addrMAC, Toast.LENGTH_SHORT).show();

        //new ConnectThread(dev).start();
        //ConnectThread m = new ConnectThread(dev);

        //AsyncTask<Void, Void, Void> CoBT = new ConnectBT();
        //CoBT.execute();

        //new ConnectBT().execute();


        //myUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP);
        //myThreadConnectBTdevice = new ThreadConnectBTdevice(dev);
        //myThreadConnectBTdevice.start();
    }

}