package com.example.fabiokawai.imagetransferqr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static com.example.fabiokawai.imagetransferqr.MainActivity.mFirebaseDatabase;


public class DownloadActivity extends AppCompatActivity {

    private TextView txtResultCode;
    private ImageView imageView;
    private LinearLayout layout;

    private DatabaseReference mImageQrDatabaseReference;
    private BitmapConverter converter;

    private int imageSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        converter = new BitmapConverter();
        imageView = (ImageView) findViewById(R.id.imageView);
        txtResultCode = (TextView) findViewById(R.id.txtResultCode);
        layout = (LinearLayout) findViewById(R.id.linearLayout);

        IntentIntegrator integrator = new IntentIntegrator(DownloadActivity.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();

    }

    public void btnSave(View view) {
        //SALVAR IMAGEM NA GALERIA
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");

            } else {
                imageSize = layout.getWidth() - 150;
                txtResultCode.setText(result.getContents());
                getImageFromDatabase(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private String getImageFromDatabase(String childKey){
        mImageQrDatabaseReference = mFirebaseDatabase.getReference().child("qrMessage").child(childKey).child("imageString");
        mImageQrDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imageString = dataSnapshot.getValue(String.class);
                Bitmap imageBitmap = converter.stringToBitmap(imageString);
                imageView.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, imageSize, imageSize, false));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return null;
    }



}
