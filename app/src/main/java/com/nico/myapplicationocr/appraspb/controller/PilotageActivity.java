package com.nico.myapplicationocr.appraspb.controller;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nico.myapplicationocr.appraspb.R;

public class PilotageActivity extends AppCompatActivity {

    private static TextView mTextViewTitleControle;
    private static EditText mEditTextCommande;
    private static TextView mTextViewReponseRaspb;
    private static Button mButtonEnvoyer;
    private static TextView mTextViewListeCommandes;
    private static ImageView mImageLed;
    private static Switch mSwitchLed;
    private static SeekBar mSeekBarMoteur;

    private static String addrMAC;
    BluetoothAdapter myBluetooth;
    private BluetoothSocket mmSocket;

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
        mTextViewReponseRaspb = findViewById(R.id.reponseRaspb2);
        mButtonEnvoyer = findViewById(R.id.buttonSendCommande);
        mTextViewListeCommandes = findViewById(R.id.textListeCommandes);
        mImageLed = findViewById(R.id.imageViewLed);
        mSwitchLed = findViewById(R.id.switchLed);
        mSeekBarMoteur = findViewById(R.id.seekBarMoteur);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        Intent newint = getIntent();
        addrMAC = newint.getStringExtra("AddrMAC");

        BluetoothDevice dev = myBluetooth.getRemoteDevice(addrMAC);
        Toast.makeText(getApplicationContext(), dev.getName()+" : "+dev.getAddress(), Toast.LENGTH_LONG).show();
        Log.d(TAG, "Start activity avec : "+ dev.getName()+" : "+dev.getAddress());

        ConnectThread connexionThread = new ConnectThread(dev, myBluetooth);
        connexionThread.start();

        mButtonEnvoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connexionThread.sendCommande(mEditTextCommande.getText().toString(), mTextViewReponseRaspb);
            }
        });

        mSwitchLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((Switch) view).isChecked();
                if (checked){
                    connexionThread.sendCommandeLed("led1", mImageLed);
                }else{
                    connexionThread.sendCommandeLed("led0", mImageLed);
                }
            }
        });

        mSeekBarMoteur.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int valeurMoteur = 10;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                valeurMoteur = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String commande = "moteur "+valeurMoteur;
                connexionThread.sendCommandeMoteur(commande);
            }
        });

    }
}