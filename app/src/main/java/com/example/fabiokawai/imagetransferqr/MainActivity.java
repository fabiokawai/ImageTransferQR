package com.example.fabiokawai.imagetransferqr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    public static FirebaseDatabase mFirebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void uploadClick(View view) {
        Intent i = new Intent(this, UploadActivity.class);
        startActivity(i);
    }

    public void downloadClick(View view) {
        Intent i = new Intent(this, DownloadActivity.class);
        startActivity(i);
    }
}
