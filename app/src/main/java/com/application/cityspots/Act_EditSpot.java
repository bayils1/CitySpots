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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class Act_EditSpot extends AppCompatActivity {

    public Intent cameraIntent;
    ImageView cameraImage;
    EditText spotName;
    EditText spotTag;
    Bitmap photo;
    Spot spot;
    String spotID;
    Button addSpot;
    Button photoButton;
    Button deleteSpot;
    String errorMessage = "-1";
    DBHandler db;
    Spinner spotType;
    List<String> spotTypes = new ArrayList<String>();
    ArrayAdapter<String> typeAdapter;
    Spinner spotLocation;
    final List<String> spotLocations = new ArrayList<String>();
    ArrayAdapter<String> locationAdapter;
    //Spinner spotType;
    final List<String> spotTypesSpinner = new ArrayList<String>();
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
        intialiseSpinners();
        //Log.println(Log.DEBUG, "testing", spotID);


        cameraImage = this.findViewById(R.id.cameraImage);
        photoButton = this.findViewById(R.id.btnCamera);
        addSpot = this.findViewById(R.id.btnEdit);
        deleteSpot = this.findViewById(R.id.btnDelete);
        spotName = this.findViewById(R.id.txtSpotName);
        spotType = this.findViewById(R.id.spnType);
        spotTag = this.findViewById(R.id.txtSpotTag);
        spotLocation = this.findViewById(R.id.spnLocation);

        spotTag.setText(spot.getTag());
        spotName.setText(spot.getSpotName());
        cameraImage.setImageBitmap(spot.getPhoto());

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
                } else if (spotType.getSelectedItem().toString().isEmpty()) {
                    errorMessage = "Please enter spot type";
                } else if (spotTag.getText().toString().isEmpty()) {
                    errorMessage = "Please enter spot tag";
                } else if (spotLocation.getSelectedItem().toString().isEmpty()) {
                    errorMessage = "Please enter the location.";
                } else {
                    if (!imageTaken) {
                        photo = spot.getPhoto();
                    }
                    errorMessage = "-1";
                    if (spotType.getSelectedItem().toString().contains("Nature")) {
                        spot = new NatureSpot(spotName.getText().toString(), spotTag.getText().toString(), spotType.getSelectedItem().toString(), photo, spotLocation.getSelectedItem().toString());
                        spot.setSpotID(spotID);
                    } else {
                        spot = new Spot(spotName.getText().toString(), spotTag.getText().toString(), spotType.getSelectedItem().toString(), photo, spotLocation.getSelectedItem().toString());
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
    public void intialiseSpinners(){
        spotType = this.findViewById(R.id.spnType);
        for (String defaultType : Global.defaultSpotTypes()){
            spotTypesSpinner.add(defaultType);
        }
        try {
            List<Type> userSpotTypes = db.getSpotTypes(currentUser.getUserID());
            for (Type userType : userSpotTypes) {
                spotTypesSpinner.add(userType.getTypeName());
            }
        } catch (Exception ignored) {
        }
        typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spotTypesSpinner);
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

        for (int i=0;i<spotType.getCount();i++){
            if (spotType.getItemAtPosition(i).toString().equalsIgnoreCase(spot.getSpotType())){
                spotType.setSelection(i);
            }
        }

        spotLocation = this.findViewById(R.id.spnLocation);
        for (String defaultType : Global.defaultLocations()){
            spotLocations.add(defaultType);
        }
        try {
            List<Location> userLocations = db.getLocations(currentUser.getUserID());
            for (Location userLocation : userLocations) {
                spotLocations.add(userLocation.getLocationName());
            }
        } catch (Exception ignored) {
        }
        locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spotLocations);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spotLocation.setAdapter(locationAdapter);
        spotLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        for (int i=0;i<spotLocation.getCount();i++){
            if (spotLocation.getItemAtPosition(i).toString().equalsIgnoreCase(spot.getLocation())){
                spotLocation.setSelection(i);
            }
        }
    }
}