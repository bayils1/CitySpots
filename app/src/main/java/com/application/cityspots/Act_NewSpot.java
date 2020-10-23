package com.application.cityspots;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("ALL")
public class Act_NewSpot extends AppCompatActivity {

    public Intent cameraIntent;
    ImageView cameraImage;
    EditText spotName;
    EditText spotTag;
    EditText spotLocation;
    Bitmap photo;
    Spot spot;
    Button addSpot;
    Button photoButton;
    String errorMessage = "-1";
    DBHandler db;
    Spinner spotType;
    List<String> spotTypes = new ArrayList<String>();


    
    ArrayAdapter<String> typeAdapter;
    boolean imageTaken;
    byte[] byteArray;
    User currentUser;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_spot);
        cameraImage = this.findViewById(R.id.cameraImage);
        photoButton = this.findViewById(R.id.btnCamera);
        addSpot = this.findViewById(R.id.btnEdit);
        spotName = this.findViewById(R.id.txtSpotName);
        spotTag = this.findViewById(R.id.txtSpotTag);
        spotLocation = this.findViewById(R.id.txtSpotLocation);


        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        db = new DBHandler(this);
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

        spotType = this.findViewById(R.id.spnType);
        for (String defaultType : Global.defaultSpotTypes()){
            spotTypes.add(defaultType);
        }
        try {
            List<Type> userSpotTypes = db.getSpotTypes(currentUser.getUserID());
            for (Type userType : userSpotTypes) {
                spotTypes.add(userType.getTypeName());
            }
        } catch (Exception e) {
        }
        typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spotTypes);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spotType.setAdapter(typeAdapter);
        spotType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addSpot.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (spotName.getText().toString().isEmpty()) {
                    errorMessage = "Please enter spot name";
                } else if (spotType.getSelectedItem().toString().isEmpty()) {
                    errorMessage = "Please enter spot type";
                } else if (spotTag.getText().toString().isEmpty()) {
                    errorMessage = "Please enter spot tag";
                } else if (spotLocation.getText().toString().isEmpty()) {
                    errorMessage = "Please enter the location.";
                } else if (!imageTaken) {
                    errorMessage = "Please take an Image";
                } else {
                    errorMessage = "-1";
                    Log.println(Log.DEBUG, "NewSpot", spotType.getSelectedItem().toString());
                    if (spotType.getSelectedItem().toString().contains("Nature")) {
                        spot = new NatureSpot(spotName.getText().toString(), spotTag.getText().toString(), spotType.getSelectedItem().toString(), photo, spotLocation.getText().toString());
                    } else
                        spot = new Spot(spotName.getText().toString(), spotTag.getText().toString(), spotType.getSelectedItem().toString(), photo, spotLocation.getText().toString());

                    try {
                        Log.println(Log.DEBUG, "NewSpot", spot.getSpotType());
                        Log.println(Log.DEBUG, "NewSpot", spot.getSpotName());
                        Log.println(Log.DEBUG, "NewSpot", spot.getLocation());
                        Log.println(Log.DEBUG, "NewSpot", Arrays.toString(spot.getByteArray()));
                        db.createSpot(spot, currentUser);
                        Intent intent = new Intent(Act_NewSpot.this, Act_Landing.class);
                        intent.putExtra("currentUser", currentUser);
                        startActivity(intent);
                    } catch (Exception e) {
                        errorMessage = "Something went wrong... our gremlins are working on it.";
                    }
                }
                if (!errorMessage.equals("-1")) {
                    Toast toast = Toast.makeText(Act_NewSpot.this, errorMessage, Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    Objects.requireNonNull(toastView).setBackgroundColor(Color.parseColor("#779933"));
                    toast.show();
                }
            }
        });
    }

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