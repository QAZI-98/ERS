package com.rapidbox.emergencyresponseapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class splashScreen extends AppCompatActivity {

    SharedPreferences prefs;
    String dispatcherID;
    boolean confirm;
    int permissionLocation;





    public void insert_LR_data(int i,String var) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("incident");

        Random random = new Random();

        int r=random.nextInt(3);
        String sub;


        if(r==0)
            sub="accident";
        else if (r==1)
            sub="crime";
        else if (r==2)
            sub="other type";
        else
            sub="crime";
        String key = databaseReference.push().getKey();
        databaseReference
                .child(key)
                .setValue(new incident
                        (
                                "data inserted for LR",
                                "https://firebasestorage.googleapis.com/v0/b/emergencyresponseapplica-ae858.appspot.com/o/incident%2F1593201880460.jpg?alt=media&token=99b2abab-8908-4a03-983f-b1bf7b345807",
                                "Ahmad Khan",
                                sub+" Naval Colony", ""+key,
                                "ahmadkhan@gmail.com",
                                "24.9461255", "66.9400352",
                                "21:00", Integer.toString(i) +"/"+var+"/20",
                                ""+sub
                        ));
        //     Log.i("unique",Integer.toString(i)+" "+sub);




    }



/*    public void fire(int len,int day)
    {

        for (int i=0;i<len;i++){
            insert_LR_data(day, "06");
        }

    }
*/




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        permissionLocation = ContextCompat.checkSelfPermission(splashScreen.this, Manifest.permission.ACCESS_FINE_LOCATION);


 /*       fire(1,9);
        fire(1,10);
        fire(1,11);
        fire(2,12);
        fire(2,13);
        fire(2,14);
        fire(3,15);
        fire(3,16);
        fire(3,17);
        fire(4,18);//29
        fire(4,19);
        fire(4,20);
        fire(5,21);
        fire(5,22);
        fire(5,23);
        fire(6,24);
        fire(6,25);
        fire(6,26);
        fire(7,27);

*/


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


        if (inNetworkAvailable() == true) {

            if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                prefs = getSharedPreferences("Values", MODE_PRIVATE);
                confirm = prefs.getBoolean("confirmReport", false);
                dispatcherID = prefs.getString("dispatcherID", "no");


                //Intent i = new Intent(splashScreen.this, dashboard.class);
                //      i.putExtra("reportType","medical");
                Intent i = new Intent(splashScreen.this, PromptLocationEnable.class);

                startActivity(i);
                finish();


            }
            else {
                prefs = getSharedPreferences("Values", MODE_PRIVATE);
                confirm = prefs.getBoolean("confirmReport", false);
                dispatcherID = prefs.getString("dispatcherID", "no");


                //Intent i = new Intent(splashScreen.this, dashboard.class);
                //      i.putExtra("reportType","medical");
                Intent i = new Intent(splashScreen.this, dashboard.class);

                startActivity(i);
                finish();


            }




         /*   if (ContextCompat.checkSelfPermission(splashScreen.this,
                    Manifest.permission.ACCESS_FINE_LOCATION )
                    != PackageManager.PERMISSION_GRANTED &&ContextCompat.checkSelfPermission(splashScreen.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION )!=PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(splashScreen.this,
                        Manifest.permission.ACCESS_FINE_LOCATION )){
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                   // ActivityCompat.requestPermissions(splashScreen.this,
                     //       new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                       //     MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the

                    // result of the request.

//                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    prefs = getSharedPreferences("Values", MODE_PRIVATE);
                    confirm = prefs.getBoolean("confirmReport", false);
                    dispatcherID = prefs.getString("dispatcherID", "no");


                    //Intent i = new Intent(splashScreen.this, dashboard.class);
                    //      i.putExtra("reportType","medical");
                    Intent i = new Intent(splashScreen.this, PromptLocationEnable.class);

                    startActivity(i);
                    finish();

                }
            } else {
                // Permission has already been granted
            }
*/

        }
        else {

            Toast.makeText(splashScreen.this, "NetWork Not Available", Toast.LENGTH_LONG).show();

        }

            }},4000);

    }



    boolean inNetworkAvailable(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo()!=null &&cm.getActiveNetworkInfo().isConnected();

    }
}


