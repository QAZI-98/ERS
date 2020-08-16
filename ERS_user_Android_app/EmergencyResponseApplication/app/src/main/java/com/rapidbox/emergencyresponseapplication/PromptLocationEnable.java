package com.rapidbox.emergencyresponseapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PromptLocationEnable extends AppCompatActivity {

    int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    locationReceiver mLocationReceiver;
    LocationManager lm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt_location_enable);

        Button prompt = (Button) findViewById(R.id.button2);
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        prompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(PromptLocationEnable.this, "Clicked!!", Toast.LENGTH_SHORT).show();
                //   requestPermission();
                ActivityCompat.requestPermissions(PromptLocationEnable.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            }


        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MY_PERMISSIONS_REQUEST_LOCATION){
            if(grantResults.length>0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Intent i = new Intent(PromptLocationEnable.this, MainActivity.class);

                startActivity(i);
                finish();
             //   statusCheck();
            }
            else{
                  Toast.makeText(PromptLocationEnable.this, "Permission Denied!!", Toast.LENGTH_SHORT).show();

            }
        }

    }
    public void checkLocationEnabledreceiver(){
        registerReceiver(mLocationReceiver= new locationReceiver(new LocationCallBack() {
            @Override
            public void onLocationTriggered() {
                //Location state changed
                buildAlertMessageNoGps();
//                }
            }
        }),new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));


    }

    private void buildAlertMessageNoGps() {
        Toast.makeText(this, "called", Toast.LENGTH_SHORT).show();
        boolean gps_enabled;
        boolean network_enabled;
        //LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!gps_enabled && !network_enabled){

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your Location seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                            Intent i = new Intent(PromptLocationEnable.this, dashboard.class);
                            //i.putExtra("reportType","medical");
                            startActivity(i);
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();}
        else{
            Toast.makeText(this, "Location is enabled", Toast.LENGTH_SHORT).show();
        }
    }
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

}