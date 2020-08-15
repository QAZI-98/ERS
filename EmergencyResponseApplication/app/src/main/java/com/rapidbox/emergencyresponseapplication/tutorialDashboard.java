package com.rapidbox.emergencyresponseapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class tutorialDashboard extends AppCompatActivity {
    Button dash;
    Button quick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_dashboard);




        dash = (Button) findViewById(R.id.dashboardBtn);
        quick = (Button) findViewById(R.id.quickguideBtn);

        dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i =  new Intent(tutorialDashboard.this, dashboard.class);
                startActivity(i);

            }
        });
        quick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i =  new Intent(tutorialDashboard.this, tutorialDashboard.class);
                startActivity(i);

            }
        });

    }

}
