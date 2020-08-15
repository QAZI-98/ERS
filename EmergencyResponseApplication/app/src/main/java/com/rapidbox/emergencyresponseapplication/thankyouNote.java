package com.rapidbox.emergencyresponseapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class thankyouNote extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou_note);
    }
    public void dashboardOpen(View view){
        Intent i = new Intent(thankyouNote.this,dashboard.class);
        startActivity(i);
        finish();
    }
    public void historyOpen(View view){
        Intent i = new Intent(thankyouNote.this,historyActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        /*Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);*/
        Intent i = new Intent(thankyouNote.this,dashboard.class);
        startActivity(i);
        //  super.onBackPressed();
    }

}
