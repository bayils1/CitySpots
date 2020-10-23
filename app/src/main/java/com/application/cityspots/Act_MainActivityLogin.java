package com.application.cityspots;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Act_MainActivityLogin extends AppCompatActivity {
    InputMethodManager imm;
    EditText username;
    EditText password;
    DBHandler db;
    Button loginButton;
    Button registerButton;
    User currentUser;
    Button Test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Objs
        db = new DBHandler(this);
        loginButton = findViewById(R.id.btnLogin);
        registerButton = findViewById(R.id.btnRegister);
        Test = findViewById(R.id.button);
        boolean runCreateTestUser = false;

        if (runCreateTestUser){//(!db.validUser("test")) {
            db.createTestUser();
            Log.println(Log.DEBUG, "DB", "No test user existed");
            String testUserID = db.getUser("test","test").getUserID();
            Type testType = new Type("Test Spot", testUserID);
            Location testLocation = new Location("Test Location",testUserID);
            db.createSpotType(testType);
            db.createSpotLocation(testLocation);
        } else
            Log.println(Log.DEBUG, "DB", "test user exists");

        //Hide key
        imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);

        //OnClickListener - Login
        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        username = findViewById(R.id.txtUserName);
                        password = findViewById(R.id.txtLoginPassword);
                        if (db.validUser(username.getText().toString())) {
                            if (db.validPassword(username.getText().toString(), password.getText().toString())) {
                                currentUser = db.getUser(username.getText().toString(), password.getText().toString());
                                Toast.makeText(Act_MainActivityLogin.this, "Hello " + currentUser.getFullName() + "!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Act_MainActivityLogin.this, Act_Landing.class);
                                intent.putExtra("currentUser", currentUser);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Act_MainActivityLogin.this,
                                        "Wrong Password" + "!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(Act_MainActivityLogin.this,
                                    "Username not Registered" + "!", Toast.LENGTH_LONG).show();

                        }
                    }
                }
        );


        //OnClickListener - Register
        registerButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        Intent intent = new Intent(Act_MainActivityLogin.this, Act_Register.class);
                        startActivity(intent);
                    }
                }

        );

        //OnClickListener - TEST BUTTON ONLY
        //Test.setVisibility(View.INVISIBLE);

        Test.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        currentUser = db.getUser("test", "test");
                        Intent intent = new Intent(Act_MainActivityLogin.this, Act_Landing.class);
                        intent.putExtra("currentUser", currentUser);
                        startActivity(intent);
                    }
                }

        );

    }
}