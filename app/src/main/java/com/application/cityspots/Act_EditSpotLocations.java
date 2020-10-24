package com.application.cityspots;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Act_EditSpotLocations extends AppCompatActivity {

    DBHandler db;
    User currentUser;
    Spinner spotLocation;
    Button delete;
    Button update;
    EditText editSpotLocation;
    List<String> spotList;
    List<Location> userSpotLocations;
    ArrayAdapter adapter;
    String errorMessage;
    Location newLocation;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_spot_locations);
        db = new DBHandler(this);
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        spotLocation = this.findViewById(R.id.spnLocation);
        delete = findViewById(R.id.btnDelete);
        update = findViewById(R.id.btnUpdate);
        editSpotLocation = findViewById(R.id.txtSpotLocation);
        spotList = new ArrayList<String>();
        spotList.add("Add new Location");
        errorMessage = "-1";

        try {
            userSpotLocations = db.getSpotLocations(currentUser.getUserID());
            for (Location userLocation : userSpotLocations) {
                spotList.add(userLocation.getLocationName());
            }
        } catch (Exception ignored) {
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spotList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spotLocation.setAdapter(adapter);
        spotLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    delete.setVisibility(View.INVISIBLE);
                    editSpotLocation.setText("");
                    update.setText(R.string.add);
                } else {
                    delete.setVisibility(View.VISIBLE);
                    for (Location userLocation : userSpotLocations) {
                        if (spotLocation.getSelectedItem() == userLocation.getLocationName()) {
                            newLocation = userLocation;
                        }
                    }
                    update.setText(R.string.update);
                    editSpotLocation.setText(newLocation.getLocationName());

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (db.validSpotLocationDelete(currentUser.getUserID(), spotLocation.getSelectedItem().toString())) {
                            errorMessage = "-1";
                            Log.println(Log.DEBUG, "Valid Delete", "Yes");
                            db.deleteSpotLocation(newLocation.getLocationID());
                            Intent intent = new Intent(Act_EditSpotLocations.this, Act_Settings.class);
                            intent.putExtra("currentUser", currentUser);
                            startActivity(intent);
                            Toast toast = Toast.makeText(Act_EditSpotLocations.this, "Spot Location deleted.", Toast.LENGTH_LONG);
                            View toastView = toast.getView();
                            toastView.setBackgroundColor(Color.parseColor("#779933"));
                            toast.show();
                        } else {
                            errorMessage = "Spot Location is being used - please delete/change spots using this Location.";
                            Log.println(Log.DEBUG, "Valid Delete", "No");
                        }
                        if (errorMessage != "-1") {
                            Toast toast = Toast.makeText(Act_EditSpotLocations.this, errorMessage, Toast.LENGTH_LONG);
                            View toastView = toast.getView();
                            toastView.setBackgroundColor(Color.parseColor("#779933"));
                            toast.show();
                        }

                    }
                }

        );

        update.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (editSpotLocation.getText().toString().isEmpty()) {
                            errorMessage = "Please enter a spot Location";
                        } else {
                            if (spotLocation.getSelectedItem().toString().equalsIgnoreCase("Add new Location")) {
                                errorMessage = "-1";
                                newLocation = new Location(editSpotLocation.getText().toString(), currentUser.getUserID());
                                Log.println(Log.DEBUG, "Edited Location", "New Location");
                                if (db.validSpotLocation(newLocation)) {
                                    db.createSpotLocation(newLocation);
                                    Intent intent = new Intent(Act_EditSpotLocations.this, Act_Settings.class);
                                    intent.putExtra("currentUser", currentUser);
                                    startActivity(intent);
                                    Toast toast = Toast.makeText(Act_EditSpotLocations.this, "Spot Location Added.", Toast.LENGTH_LONG);
                                    View toastView = toast.getView();
                                    toastView.setBackgroundColor(Color.parseColor("#779933"));
                                    toast.show();
                                } else {
                                    errorMessage = "Spot Location Already Exists!";
                                    Log.println(Log.DEBUG, "Edited Location", "Location Already Exists");
                                }

                            } else {
                                errorMessage = "-1";
                                String oldLocationName = spotLocation.getSelectedItem().toString();

                                newLocation.setLocationName(editSpotLocation.getText().toString());
                                Log.println(Log.DEBUG, "Edited Location", newLocation.getLocationID() + " " + newLocation.getLocationName());
                                if (db.validSpotLocation(newLocation)) {

                                    db.updateSpotLocation(newLocation, oldLocationName);
                                    Intent intent = new Intent(Act_EditSpotLocations.this, Act_Landing.class);
                                    intent.putExtra("currentUser", currentUser);
                                    startActivity(intent);
                                    Toast toast = Toast.makeText(Act_EditSpotLocations.this, "Spot Location Updated.", Toast.LENGTH_LONG);
                                    View toastView = toast.getView();
                                    toastView.setBackgroundColor(Color.parseColor("#779933"));
                                    toast.show();
                                } else {
                                    errorMessage = "Spot Location Already Exists!";
                                    Log.println(Log.DEBUG, "Edited Location", "Location Already Exists");
                                }
                            }
                        }
                        if (errorMessage != "-1") {
                            Toast toast = Toast.makeText(Act_EditSpotLocations.this, errorMessage, Toast.LENGTH_LONG);
                            View toastView = toast.getView();
                            toastView.setBackgroundColor(Color.parseColor("#779933"));
                            toast.show();
                        }
                    }


                    //Intent intent = new Intent(Act_EditSpotLocations.this, Act_Landing.class);
                    //intent.putExtra("currentUser", currentUser);
                    //startActivity(intent);

                }


        );

    }
}
