package com.application.cityspots;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class Act_Settings extends AppCompatActivity {

    User currentUser;
    EditText fullname;
    EditText city;
    Button update;
    String errorMessage = "-1";
    DBHandler db;
    List<String> spinnerArray = new ArrayList<String>();
    Spinner spotFilter;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        db = new DBHandler(this);
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        fullname = findViewById(R.id.txtFullName);
        city = findViewById(R.id.txtCity);
        update = findViewById(R.id.btnUpdate);

        spotFilter = findViewById(R.id.spnSpotFilter);
        fullname.setText(currentUser.getFullName());
        city.setText(currentUser.getCity());

        //Spinner
        if (currentUser.getDefaultSpotFilter().equalsIgnoreCase("Type")) {
            spinnerArray.add("Type");
            spinnerArray.add("Location");
        } else {
            spinnerArray.add("Location");
            spinnerArray.add("Type");
        }


        adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spotFilter.setAdapter(adapter);
        spotFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        update.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        fullname = findViewById(R.id.txtFullName);
                        city = findViewById(R.id.txtCity);

                        if (fullname.getText().toString().isEmpty()) {
                            errorMessage = "Please enter full name";
                        } else if (city.getText().toString().isEmpty()) {
                            errorMessage = "Please enter city";
                        } else {
                            errorMessage = "-1";
                            currentUser.setCity(city.getText().toString());
                            currentUser.setFullName(fullname.getText().toString());
                            Log.println(Log.DEBUG, "Updated Fullname", currentUser.getFullName());
                            Log.println(Log.DEBUG, "Updated City", currentUser.getCity());
                            try {
                                Log.println(Log.DEBUG, "Updated Fullname", currentUser.getFullName());
                                Log.println(Log.DEBUG, "Updated City", currentUser.getCity());
                                db.updateUser(currentUser);
                                Toast.makeText(Act_Settings.this, "Settings Updated!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Act_Settings.this, Act_Landing.class);
                                intent.putExtra("currentUser", currentUser);
                                startActivity(intent);
                            } catch (Exception e) {
                                errorMessage = "Something went wrong... our gremlins are working on it.";
                            }
                        }
                        if (errorMessage != "-1") {
                            Toast toast = Toast.makeText(Act_Settings.this, errorMessage, Toast.LENGTH_LONG);
                            View toastView = toast.getView();
                            toastView.setBackgroundColor(Color.parseColor("#779933"));
                            toast.show();
                        }
                    }
                });


    }
}
