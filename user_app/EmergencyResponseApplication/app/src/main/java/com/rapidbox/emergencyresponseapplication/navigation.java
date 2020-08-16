package com.rapidbox.emergencyresponseapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class navigation extends AppCompatActivity {
    ImageView imageviewx;
    Button mBtn;
    LocationManager lm;
    locationReceiver mLocationReceiver;
    protected static final String TAG = "newsfeed";


    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);


        checkLocationEnabledreceiver();
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        imageviewx = (ImageView)findViewById(R.id.iv);
        imageviewx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(navigation.this, dashboard.class);
                //i.putExtra("reportType","medical");
                startActivity(i);
                finish();

            }
        });
    mBtn = (Button)findViewById(R.id.linearBtn);
    mBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(navigation.this,Linear_regression.class);
            startActivity(i);

        }
    });
    }
    public void dectectRoutesShow(View view){
        Intent i = new Intent(navigation.this,MapsActivityA.class);
        startActivity(i);

    }
    public void MarkAreas(View view){

        Intent i = new Intent(navigation.this,Area.class);
        //Intent i = new Intent(navigation.this,fuzzyDisplay.class);

        startActivity(i);

    }
    public void checkLocationEnabledreceiver(){
        registerReceiver(mLocationReceiver= new locationReceiver(new LocationCallBack() {
            @Override
            public void onLocationTriggered() {
                //Location state changed
                //           buildAlertMessageNoGps();
                displayLocationSettingsRequest(navigation.this);
//                }//
            }
        }),new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));


    }
    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(navigation.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayLocationSettingsRequest(this);
    }
}
