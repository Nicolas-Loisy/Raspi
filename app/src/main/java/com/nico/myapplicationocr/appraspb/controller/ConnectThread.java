package com.nico.myapplicationocr.appraspb.controller;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.nico.myapplicationocr.appraspb.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ConnectThread extends Thread {
    private static final String TAG = "ConnectThread";
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    //private static final UUID MY_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb"); 00805F9B34FB
    //{e0cbf06c-cd8b-4647-bb8a-263b43f0f974}
    //private static final UUID MY_UUID = UUID.fromString("e0cbf06c-cd8b-4647-bb8a-263b43f0f974");
    private static final UUID MY_UUID = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");
    private final BluetoothAdapter bluetoothAdapter;

    private OutputStream outputStream; //envoyer
    private InputStream inputStream; //reception

    @SuppressLint("MissingPermission")
    public ConnectThread(BluetoothDevice device, BluetoothAdapter btAdapter) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;
        bluetoothAdapter = btAdapter;
        mmDevice = device;

        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            Log.e(TAG,""+tmp.getRemoteDevice());
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }
        mmSocket = tmp;
    }

    @SuppressLint("MissingPermission")
    public void run() {
        // Cancel discovery because it otherwise slows down the connection.
        bluetoothAdapter.cancelDiscovery();

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();
        } catch (IOException connectException) {
            Log.e(TAG, "Erreur connexion!");
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Could not close the client socket", closeException);
            }
            return;
        }


        try {
            outputStream = mmSocket.getOutputStream();
            inputStream = mmSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        Log.e(TAG, "Connecte");
        //manageMyConnectedSocket(mmSocket);
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }


    // ligne commande
    public void sendCommande(String commande, TextView reponse){
        try {
            outputStream.write(commande.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        receiveReponse(reponse);
    }
    public void receiveReponse(TextView reponse){
        byte[] buffer = new byte[512];
        try {
            int numBytes = inputStream.read(buffer);
            String message = new String(buffer, 0, numBytes);
            reponse.setText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Button Led
    public void sendCommandeLed(String commande, ImageView etatLed){
        try {
            outputStream.write(commande.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        receiveReponseEtatLed(etatLed);
    }
    public void receiveReponseEtatLed(ImageView etatLed){
        byte[] buffer = new byte[512];
        try {
            int numBytes = inputStream.read(buffer);
            String message = new String(buffer, 0, numBytes);
            if (message.equalsIgnoreCase("led on")){
                etatLed.setImageResource(R.drawable.led1onv);
            }else{
                etatLed.setImageResource(R.drawable.led1off2v);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // SeekBar Moteur
    public void sendCommandeMoteur(String commande){
        try {
            outputStream.write(commande.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        receiveReponseEtatMoteur();
    }
    public void receiveReponseEtatMoteur(){
        byte[] buffer = new byte[512];
        try {
            int numBytes = inputStream.read(buffer);
            String message = new String(buffer, 0, numBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}