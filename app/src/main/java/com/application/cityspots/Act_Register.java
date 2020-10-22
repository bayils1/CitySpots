package com.application.cityspots;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressWarnings("ALL")
public class Act_Register extends AppCompatActivity {

    User newUser;
    Button register;
    EditText username;
    EditText password;
    EditText fullname;
    EditText city;
    String errorMessage = "-1";
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new DBHandler(this);
        register = (Button) findViewById(R.id.btnRegister);
        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        username = findViewById(R.id.txtUserName);
                        password = findViewById(R.id.txtLoginPassword);
                        fullname = findViewById(R.id.txtFullName);
                        city = findViewById(R.id.txtCity);

                        if (username.getText().toString().isEmpty()) {
                            errorMessage = "Please enter username";
                        } else if (password.getText().toString().isEmpty()) {
                            errorMessage = "Please enter password";
                        } else if (fullname.getText().toString().isEmpty()) {
                            errorMessage = "Please enter full name";
                        } else if (city.getText().toString().isEmpty()) {
                            errorMessage = "Please enter city";
                        } else {
                            if (db.validUser(username.getText().toString())) {
                                errorMessage = "Username " + username.getText().toString() + " is taken";
                            } else {
                                errorMessage = "-1";
                                newUser = new User(fullname.getText().toString(),
                                        username.getText().toString(),
                                        password.getText().toString(),
                                        city.getText().toString());
                                try {
                                    Log.println(Log.DEBUG, "NewUser Username", newUser.getUsername());
                                    db.createUser(newUser);
                                    Toast.makeText(Act_Register.this, "Welcome " + newUser.getFullName() + "!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Act_Register.this, Act_Landing.class);
                                    intent.putExtra("currentUser", newUser);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    errorMessage = "Something went wrong... our gremlins are working on it.";
                                }
                            }
                        }
                        if (errorMessage != "-1") {
                            Toast toast = Toast.makeText(Act_Register.this, errorMessage, Toast.LENGTH_LONG);
                            View toastView = toast.getView();
                            toastView.setBackgroundColor(Color.parseColor("#779933"));
                            toast.show();
                        }
                    }

                }
        );

    }
}