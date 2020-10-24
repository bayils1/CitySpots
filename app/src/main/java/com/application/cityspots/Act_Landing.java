package com.application.cityspots;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Act_Landing extends AppCompatActivity {

    List<Spot> spotList;
    Button addSpot;
    Button settings;
    Spinner spotType;
    User currentUser;
    LinearLayout parentLayout;
    DBHandler db;
    List<String> spinnerArray = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        db = new DBHandler(this);
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        spotType = findViewById(R.id.spnSpotType);

        //Navigation from register will mean userID is null.
        if (currentUser.getUserID() == null) {
            currentUser = db.getUser(currentUser.getUsername(), currentUser.getPassword());
        }
        //Log.println(Log.DEBUG,"List Size", " A" + Integer.toString(db.spotCount(currentUser.getUserID())));

        settings = findViewById(R.id.btnSettings);
        settings.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        Intent intent = new Intent(Act_Landing.this, Act_Settings.class);
                        intent.putExtra("currentUser", currentUser);
                        startActivity(intent);
                    }
                }

        );

        addSpot = findViewById(R.id.btnCamera);
        addSpot.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        Intent intent = new Intent(Act_Landing.this, Act_NewSpot.class);
                        intent.putExtra("currentUser", currentUser);
                        startActivity(intent);
                    }
                }

        );

        if (currentUser.getDefaultSpotFilter().equalsIgnoreCase("Type")) {
            spinnerArray = db.getUserSpotTypes(currentUser.getUserID());
        } else {
            spinnerArray = db.getUserLocationTypes(currentUser.getUserID());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spotType.setAdapter(adapter);
        //Parent

        spotType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                parentLayout = findViewById(R.id.NestedScrollParent);
                if (db.spotCount(currentUser.getUserID()) == 0) {
                    //Log.println(Log.DEBUG,"List Size", "" + db.spotCount(currentUser.getUserID()));
                    TextView text = new TextView(Act_Landing.this);
                    text.setPadding(50, 50, 5, 50);
                    text.setText(getString(R.string.no_spots_message));
                    parentLayout.addView(text);
                } else {
                    parentLayout.removeAllViews();
                    if (currentUser.getDefaultSpotFilter().equalsIgnoreCase("Type")) {
                        spotList = db.getUserSpotBySpotType(currentUser.getUserID(), spotType.getSelectedItem().toString());
                    } else {
                        spotList = db.getUserSpotBySpotLocation(currentUser.getUserID(), spotType.getSelectedItem().toString());
                    }
                    Spot s = new Spot(spotList.get(0));
                    StringBuilder textHTML = new StringBuilder();
                    byte[] photobyte;
                    for (int i = 0; i < spotList.size(); i++) {
                        if (spotList.get(i).getSpotName() != null) {
                            textHTML.append("<b>").append(spotList.get(i).getSpotName()).append("</b><p><p>").append("<list><li>Tag: ").append(spotList.get(i).getTag().startsWith("#") ? spotList.get(i).getTag() : "#" + spotList.get(i).getTag()).append("</li><p>").append("<li>Location: ").append(spotList.get(i).getLocation()).append("</li></list>");
                            photobyte = spotList.get(i).getByteArray();
                            addCard(photobyte, textHTML.toString(), spotList.get(i).getSpotID());
                        }
                        textHTML = new StringBuilder();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void addCard(byte[] image, String text, final String spotID) {
        //Log.println(Log.DEBUG, "spotid", spotID);

        //Layout Params
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(300, 300);
        LinearLayout.LayoutParams matchParentWrap = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        Bitmap mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);


        //Edit Button
        Button testButton = new Button(Act_Landing.this);
        //testButton.setPadding(500,0,0,0);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(150, 150);
        testButton.setLayoutParams(buttonParams);
        testButton.setPadding(-1, -1, -1, -1);
        testButton.setX(900);
        testButton.setY(10);
        testButton.setId(View.generateViewId());
        testButton.setText("✎");
        testButton.setTextSize(20);
        testButton.setBackgroundColor(Color.argb(1, 255, 255, 255));
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle throwTo = new Bundle();
                throwTo.putSerializable("currentUser", currentUser);
                //Just the ID for Spot, as this object contains Bitmap data, which cannot be serialized.
                throwTo.putSerializable("spot", spotID);
                Intent intent = new Intent(Act_Landing.this, Act_EditSpot.class);
                intent.putExtras(throwTo);
                startActivity(intent);
            }
        });

        //cameraImage.setImageBitmap(mutableBitmap);

        ImageView cardImage = new ImageView(Act_Landing.this);
        cardImage.setImageBitmap(mutableBitmap);
        cardImage.setPadding(0, 10, 0, 10);
        cardImage.setLayoutParams(imageParams);

        //TextView Settings
        TextView cardText = new TextView(Act_Landing.this);
        cardText.setPadding(310, 5, 150, 5);
        cardText.setText(Html.fromHtml(text));

        //CardView Settings
        CardView cv2 = new CardView(Act_Landing.this);
        //cv2.setBackgroundColor(Color.GREEN);
        cv2.setLayoutParams(matchParentWrap);
        cv2.setPadding(0, 0, 0, 0);

        //AdViews
        cv2.addView(cardImage);
        cv2.addView(cardText);
        cv2.addView(testButton);
        parentLayout.addView(cv2);
    }

}