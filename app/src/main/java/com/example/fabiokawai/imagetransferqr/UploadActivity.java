package com.example.fabiokawai.imagetransferqr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import static com.example.fabiokawai.imagetransferqr.MainActivity.mFirebaseDatabase;

public class UploadActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private BitmapConverter converter;

    private DatabaseReference mImageQrDatabaseReference;

    private ChildEventListener mChildEventListener;

    private String keyValue = "";

    private TextView txtKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        txtKey = (TextView) findViewById(R.id.txtKey);

        mImageQrDatabaseReference = mFirebaseDatabase.getReference().child("qrMessage");

        converter = new BitmapConverter();

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                txtKey.setText(keyValue);
                /*

                Add to ImageView

                Save to Camera Roll

                 */
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };

        mImageQrDatabaseReference.addChildEventListener(mChildEventListener);

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();

            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Image img = new Image(converter.toString(imageBitmap));
            mImageQrDatabaseReference = mFirebaseDatabase.getReference().child("qrMessage");

            keyValue = mImageQrDatabaseReference.push().getKey();
            mImageQrDatabaseReference.child(keyValue).setValue(img);

        }
    }


}
