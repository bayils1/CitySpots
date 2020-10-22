package com.application.cityspots;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings("unused")
public class Act_EditSpot extends AppCompatActivity {

    public Intent cameraIntent;
    ImageView cameraImage;
    EditText spotName;
    EditText spotType;
    EditText spotTag;
    EditText spotLocation;
    Bitmap photo;
    Spot spot;
    String spotID;
    Button addSpot;
    Button photoButton;
    Button deleteSpot;
    String errorMessage = "-1";
    DBHandler db;
    boolean imageTaken;
    byte[] byteArray;
    User currentUser;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_spot);
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        spotID = (String) getIntent().getSerializableExtra("spot");

        db = new DBHandler(this);
        spot = new Spot(db.getSpot(spotID));
        //Log.println(Log.DEBUG, "testing", spotID);


        cameraImage = this.findViewById(R.id.cameraImage);
        photoButton = this.findViewById(R.id.btnCamera);
        addSpot = this.findViewById(R.id.btnEdit);
        deleteSpot = this.findViewById(R.id.btnDelete);
        spotName = this.findViewById(R.id.txtSpotName);
        spotType = this.findViewById(R.id.txtSpotType);
        spotTag = this.findViewById(R.id.txtSpotTag);
        spotLocation = this.findViewById(R.id.txtSpotLocation);

        spotTag.setText(spot.getTag());
        spotName.setText(spot.getSpotName());
        cameraImage.setImageBitmap(spot.getPhoto());
        spotType.setText(spot.getSpotType());
        spotLocation.setText(spot.getLocation());

        photoButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        deleteSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    db.deleteSpot(spotID);
                    Intent intent = new Intent(Act_EditSpot.this, Act_Landing.class);
                    intent.putExtra("currentUser", currentUser);
                    startActivity(intent);
                } catch (Exception e) {
                    errorMessage = "Something went wrong... our gremlins are working on it.";
                }
            }
        });

        addSpot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (spotName.getText().toString().isEmpty()) {
                    errorMessage = "Please enter spot name";
                } else if (spotType.getText().toString().isEmpty()) {
                    errorMessage = "Please enter spot type";
                } else if (spotTag.getText().toString().isEmpty()) {
                    errorMessage = "Please enter spot tag";
                } else if (spotLocation.getText().toString().isEmpty()) {
                    errorMessage = "Please enter the location.";
                } else {
                    if (!imageTaken) {
                        photo = spot.getPhoto();
                    }
                    errorMessage = "-1";
                    if (spotType.getText().toString().contains("Nature")) {
                        spot = new NatureSpot(spotName.getText().toString(), spotTag.getText().toString(), spotType.getText().toString(), photo, spotLocation.getText().toString());
                        spot.setSpotID(spotID);
                    } else {
                        spot = new Spot(spotName.getText().toString(), spotTag.getText().toString(), spotType.getText().toString(), photo, spotLocation.getText().toString());
                    }
                    spot.setSpotID(spotID);
                    try {
                        Log.println(Log.DEBUG, "NewSpot", spot.getSpotType());
                        Log.println(Log.DEBUG, "NewSpot", spot.getSpotName());
                        Log.println(Log.DEBUG, "NewSpot", spot.getLocation());
                        Log.println(Log.DEBUG, "NewSpot", Arrays.toString(spot.getByteArray()));
                        db.updateSpot(spot);
                        Intent intent = new Intent(Act_EditSpot.this, Act_Landing.class);
                        intent.putExtra("currentUser", currentUser);
                        startActivity(intent);
                    } catch (Exception e) {
                        errorMessage = "Something went wrong... our gremlins are working on it.";
                    }
                }
                if (!errorMessage.equals("-1")) {
                    Toast toast = Toast.makeText(Act_EditSpot.this, errorMessage, Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    assert toastView != null;
                    toastView.setBackgroundColor(Color.parseColor("#779933"));
                    toast.show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            if (requestCode == CAMERA_REQUEST) {
                photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                cameraImage.setImageBitmap(photo);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
                imageTaken = true;
            }
        }
    }
}