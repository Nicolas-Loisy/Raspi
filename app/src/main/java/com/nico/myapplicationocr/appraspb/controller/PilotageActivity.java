package com.nico.myapplicationocr.appraspb.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nico.myapplicationocr.appraspb.R;

public class PilotageActivity extends AppCompatActivity {

    private static TextView mTextViewTitleControle;
    private static EditText mEditTextCommande;
    private static Button mButtonEnvoyer;
    private static TextView mTextViewListeCommandes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilotage);

        mTextViewTitleControle = findViewById(R.id.textEcranControle);
        mEditTextCommande = findViewById(R.id.edittextCommande);
        mButtonEnvoyer = findViewById(R.id.buttonSendCommande);
        mTextViewListeCommandes = findViewById(R.id.textListeCommandes);


    }
}