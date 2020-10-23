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

public class Act_EditSpotTypes extends AppCompatActivity {

    DBHandler db;
    User currentUser;
    Spinner spotType;
    Button delete;
    Button update;
    EditText editSpotType;
    List<String> spotList;
    List<Type> userSpotTypes;
    ArrayAdapter adapter;
    String errorMessage;
    Type newType;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_spot_types);
        db = new DBHandler(this);
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        spotType = this.findViewById(R.id.spnType);
        delete = findViewById(R.id.btnDelete);
        update = findViewById(R.id.btnUpdate);
        editSpotType = findViewById(R.id.txtSpotType);
        spotList = new ArrayList<String>();
        spotList.add("Add new Spot");
        errorMessage = "-1";

        try {
            userSpotTypes = db.getSpotTypes(currentUser.getUserID());
            for (Type userType : userSpotTypes) {
                spotList.add(userType.getTypeName());
            }
        } catch (Exception ignored) {
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spotList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spotType.setAdapter(adapter);
        spotType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    delete.setVisibility(View.INVISIBLE);
                    editSpotType.setText("");
                    update.setText(R.string.add);
                } else {
                    delete.setVisibility(View.VISIBLE);
                    for (Type userType : userSpotTypes) {
                        if (spotType.getSelectedItem() == userType.getTypeName()) {
                            newType = userType;
                        }
                    }
                    update.setText(R.string.update);
                    editSpotType.setText(newType.getTypeName());

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
                        if (db.validSpotTypeDelete(currentUser.getUserID(), newType.getTypeName())) {
                            errorMessage = "-1";
                            Log.println(Log.DEBUG, "Valid Delete", "Yes");
                            db.deleteSpotType(newType.getTypeID());
                            Intent intent = new Intent(Act_EditSpotTypes.this, Act_Settings.class);
                            intent.putExtra("currentUser", currentUser);
                            startActivity(intent);
                            Toast toast = Toast.makeText(Act_EditSpotTypes.this, "Spot Type deleted.", Toast.LENGTH_LONG);
                            View toastView = toast.getView();
                            toastView.setBackgroundColor(Color.parseColor("#779933"));
                            toast.show();
                        } else {
                            errorMessage = "Spot Type is being used - please delete/change spots using this type.";
                            Log.println(Log.DEBUG, "Valid Delete", "No");
                        }
                        if (errorMessage != "-1") {
                            Toast toast = Toast.makeText(Act_EditSpotTypes.this, errorMessage, Toast.LENGTH_LONG);
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
                        if (editSpotType.getText().toString().isEmpty()) {
                            errorMessage = "Please enter a spot Type";
                        } else {
                            if (spotType.getSelectedItem().toString().equalsIgnoreCase("Add new Spot")) {
                                errorMessage = "-1";
                                newType = new Type(editSpotType.getText().toString(), currentUser.getUserID());
                                Log.println(Log.DEBUG, "Edited Type", "New Type");
                                if(db.validSpotType(newType)){
                                    db.createSpotType(newType);
                                    Intent intent = new Intent(Act_EditSpotTypes.this, Act_Settings.class);
                                    intent.putExtra("currentUser", currentUser);
                                    startActivity(intent);
                                    Toast toast = Toast.makeText(Act_EditSpotTypes.this, "Spot Type Added.", Toast.LENGTH_LONG);
                                    View toastView = toast.getView();
                                    toastView.setBackgroundColor(Color.parseColor("#779933"));
                                    toast.show();
                                }
                                else{
                                    errorMessage = "Spot Type Already Exists!";
                                    Log.println(Log.DEBUG, "Edited Type", "Type Already Exists");
                                }

                            } else {
                                errorMessage = "-1";
                                String oldTypeName = spotType.getSelectedItem().toString();
                                newType.setTypeName(editSpotType.getText().toString());
                                Log.println(Log.DEBUG, "Edited Type", newType.getTypeID() + " " + newType.getTypeName());
                                if(db.validSpotType(newType)){
                                    db.updateSpotType(newType, oldTypeName);
                                    Intent intent = new Intent(Act_EditSpotTypes.this, Act_Landing.class);
                                    intent.putExtra("currentUser", currentUser);
                                    startActivity(intent);
                                    Toast toast = Toast.makeText(Act_EditSpotTypes.this, "Spot Type Updated.", Toast.LENGTH_LONG);
                                    View toastView = toast.getView();
                                    toastView.setBackgroundColor(Color.parseColor("#779933"));
                                    toast.show();
                                }
                                else{
                                    errorMessage = "Spot Type Already Exists!";
                                    Log.println(Log.DEBUG, "Edited Type", "Type Already Exists");
                                }
                            }
                        }
                        if (errorMessage != "-1") {
                            Toast toast = Toast.makeText(Act_EditSpotTypes.this, errorMessage, Toast.LENGTH_LONG);
                            View toastView = toast.getView();
                            toastView.setBackgroundColor(Color.parseColor("#779933"));
                            toast.show();
                        }
                    }


                    //Intent intent = new Intent(Act_EditSpotTypes.this, Act_Landing.class);
                    //intent.putExtra("currentUser", currentUser);
                    //startActivity(intent);

                }


        );

    }
}
