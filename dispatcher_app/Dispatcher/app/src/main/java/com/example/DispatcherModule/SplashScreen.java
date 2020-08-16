package com.example.DispatcherModule;

import androidx.appcompat.app.AppCompatActivity;
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

public class SplashScreen extends AppCompatActivity {
    SharedPreferences prefs;
    String dispatcherID;
    boolean confirm;
    int permissionLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        permissionLocation = ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.ACCESS_FINE_LOCATION);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (inNetworkAvailable() == true) {
                    if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                        //prefs = getSharedPreferences("Values", MODE_PRIVATE);
                        //confirm = prefs.getBoolean("confirmReport", false);
                       // dispatcherID = prefs.getString("dispatcherID", "no");


                        //Intent i = new Intent(splashScreen.this, dashboard.class);
                        //      i.putExtra("reportType","medical");
                        Intent i = new Intent(SplashScreen.this, PermissionRequest.class);

                        startActivity(i);
                        finish();


                    }

                    else {
                        //prefs = getSharedPreferences("Values", MODE_PRIVATE);
                        //confirm = prefs.getBoolean("confirmReport", false);
                        //dispatcherID = prefs.getString("dispatcherID", "no");


                        //Intent i = new Intent(splashScreen.this, dashboard.class);
                        //      i.putExtra("reportType","medical");
                        Intent i = new Intent(SplashScreen.this, MainActivity.class);

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

                    Toast.makeText(SplashScreen.this, "NetWork Not Available", Toast.LENGTH_LONG).show();

                }

            }},4000);

    }


    boolean inNetworkAvailable(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo()!=null &&cm.getActiveNetworkInfo().isConnected();

    }

}
