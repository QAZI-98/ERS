package com.rapidbox.emergencyresponseapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.arsy.maps_library.MapRipple;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.google.android.gms.location.LocationListener;



public class accident extends FragmentActivity implements OnMapReadyCallback, LocationListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    LatLng pickupLocation;
    MapRipple mapRipple;
    private GoogleMap mMap;
    Marker pickupMarker;
    LatLng latLng;
   // LocationManager lm;
    Location mLastLocation;
   // Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
     locationReceiver mLocationReceiver;
    SharedPreferences prefs;
    String dispatcherID;
    boolean confirm;
    ProgressDialog showLoad;
    LocationManager lm;
    GoogleMap googleMap;
    protected static final String TAG = "accident";
    ConstraintLayout.LayoutParams rlp;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayLocationSettingsRequest(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

      //  if (mapRipple.isAnimationRunning()) {
        //    mapRipple.stopRippleMapAnimation();
        //}
        // mapRipple.stopRippleMapAnimation();
      //  unregisterReceiver(mLocationReceiver);

    }

    @Override
    protected void onPause() {
        super.onPause();
       /* if (mapRipple.isAnimationRunning()) {
            mapRipple.stopRippleMapAnimation();
        } else {
            mapRipple.startRippleMapAnimation();

        }*/
//        buildAlertMessageNoGps();

  //      checkLocationEnabledreceiver();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mapRipple.isAnimationRunning()) {
                mapRipple.stopRippleMapAnimation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        prefs = getSharedPreferences("Values", MODE_PRIVATE);
        dispatcherID = prefs.getString("dispatcherID","no");
        confirm = prefs.getBoolean("confirmReport",false);
       // showLoad = new ProgressDialog(accident.this,R.style.AppCompatAlertDialogStyle);
        //showLoad.setMessage("Wait...");
        //showLoad.show();

        checkLocationEnabledreceiver();
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        //lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

     /*   if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }*/
            if (confirm == true) {
                //showLoad = new ProgressDialog(accident.this,R.style.AppCompatAlertDialogStyle);
                //showLoad.setMessage("Wait...");
                //showLoad.show();
                Intent i = new Intent(accident.this, trackingDispatcher.class);
                //      i.putExtra("reportType","medical");
                startActivity(i);
                //showLoad.dismiss();
            } else {
                Dexter.withActivity(accident.this)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                //                mRequestLocationUpdate = true;
                                //setLocation();

                                //    fusedLocationClient = LocationServices.getFusedLocationProviderClient(accident.this);
                                setContentView(R.layout.activity_accident);
                                pickupLocation = new LatLng(0.0, 0.0);
                                Toast.makeText(accident.this, "Permission is granted!!", Toast.LENGTH_SHORT).show();


                                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                        .findFragmentById(R.id.map);

                             /*  googleMap = mapFragment.getContext();

                                // Setting a click event handler for the map
                                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                                    @Override
                                    public void onMapClick(LatLng latLng) {

                                        // Creating a marker
                                        MarkerOptions markerOptions = new MarkerOptions();

                                        // Setting the position for the marker
                                        markerOptions.position(latLng);

                                        // Setting the title for the marker.
                                        // This will be displayed on taping the marker
                                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                                        // Clears the previously touched position
                                        googleMap.clear();

                                        // Animating to the touched position
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                                        // Placing a marker on the touched position
                                        googleMap.addMarker(markerOptions);
                                    }
                                });
                                    */
                                mapFragment.getMapAsync(accident.this);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                //                  openSettings();
                                Toast.makeText(accident.this, "Permission is denied", Toast.LENGTH_SHORT).show();
                                Intent ii = new Intent(accident.this, dashboard.class);
                                startActivity(ii);
                                finish();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();

            }

        }



    private void buildAlertMessageNoGps() {
        //Toast.makeText(this, "called", Toast.LENGTH_SHORT).show();
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
                        Intent i = new Intent(accident.this, dashboard.class);
                        startActivity(i);
                        dialog.cancel();

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();}
        else{
            Toast.makeText(this, "Location is enabled", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(false);
      //  showLoad.hide();
        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));

        setMapLongClick(mMap);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
//                Location location = mMap.getMyLocation();

  //                  if (location != null)
    //                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
      //              else {
//                        latLng = new LatLng(0.0, 0.0);
        //            }

                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {


                        return null;

                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        View v = getLayoutInflater().inflate(R.layout.custom_window_info, null);
                       final TextView info2 = (TextView) v.findViewById(R.id.title);
                       final TextView info3 = (TextView) v.findViewById(R.id.snippet);

                        info2.setText("PICKUP LOCATION");
                        info3.setText("Please stay near to your pickup location");
                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                Bundle bundle = new Bundle();
                                bundle.putString("title",info2.getText().toString());
                                bundle.putString("body",info3.getText().toString());


                            }
                        });
                        return v;

                    }
                });


  //              mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                mMap.animateCamera(CameraUpdateFactory.zoomTo(18));



                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);

            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);

        }




    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, accident.this);

        }
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {



        mLastLocation = location;
       // if (mCurrLocationMarker != null) {
         //   mCurrLocationMarker.remove();
        //}
        if (pickupMarker!=null){

            pickupMarker.remove();
            mapRipple.stopRippleMapAnimation();

        }

        //Place current location marker
         latLng = new LatLng(location.getLatitude(), location.getLongitude());
      //  Toast.makeText(this, ""+mLastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        pickupMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapmarker)));
//       mapRipple.stopRippleMapAnimation();
        mapRipple = new MapRipple(mMap, latLng, accident.this);

        rippleMapEffect();
        mapRipple.startRippleMapAnimation();


        mapRipple.withLatLng(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));

        // mCurrLocationMarker = mMap.addMarker(markerOptions);
        //Projection projection = mMap.getProjection();
        //LatLng markerPosition = markerOptions.position(latLng).getPosition();
        //Point markerpoint = projection.toScreenLocation(markerPosition);
        //Point targetPoint = new Point(markerpoint.x,markerpoint.y);
        //LatLng targetPosition = projection.fromScreenLocation(targetPoint);

        //move map camera
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(18)
                .bearing(0)
                .tilt(10)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,accident.this);

        }

    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

/*    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case REQUEST_CODE:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    fetchLocation();
                }
                break;

        }
    }*/

    public void reportCrash(View view){
        try {

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("MedicalRequest");
            GeoFire geoFire = new GeoFire(ref) ;
            geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    Toast.makeText(accident.this, "Ambulance Requested ", Toast.LENGTH_SHORT).show();

                }
            });
            pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
          //  mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup here"));
            //           getClosestDispatcher();
            String laat= String.valueOf(pickupLocation.latitude);
            String loong= String.valueOf(pickupLocation.longitude);
            String medicalreport = "medical";
            Intent i = new Intent(accident.this, reportaccidentchecking.class);
            i.putExtra("reportType",medicalreport);
            i.putExtra("Latitude",laat);
            i.putExtra("Longitude",loong);

            //    i.putExtra("Longitude",pickupLocation.longitude);

            mapRipple.stopRippleMapAnimation();

            startActivity(i);
        }catch (Exception x){

            Toast.makeText(accident.this, "error: "+x.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public void reportCrime(View view){
        try {

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("PoliceRequest");
            GeoFire geoFire = new GeoFire(ref) ;
            geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    Toast.makeText(accident.this, "Police has been requested!! ", Toast.LENGTH_SHORT).show();

                }
            });
            pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup here"));
            //           getClosestDispatcher();
            String laat= String.valueOf(pickupLocation.latitude);
            String loong= String.valueOf(pickupLocation.longitude);
            String crimeReport = "crime";
            Intent i = new Intent(accident.this, reportaccidentchecking.class);
            i.putExtra("reportType",crimeReport);
            i.putExtra("Latitude",laat);
            i.putExtra("Longitude",loong);

            mapRipple.stopRippleMapAnimation();

            //    i.putExtra("Longitude",pickupLocation.longitude);
            startActivity(i);
        }catch (Exception x){

            Toast.makeText(accident.this, "error: "+x.toString(), Toast.LENGTH_SHORT).show();
        }

    }
    public void reportFire(View view){
//        Toast.makeText(accident.this, "In report fire", Toast.LENGTH_SHORT).show();

        try {

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("FireRequest");
            GeoFire geoFire = new GeoFire(ref) ;
            geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    //Toast.makeText(accident.this, "Firefighter has been requested!! ", Toast.LENGTH_SHORT).show();

                }
            });
            pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Fire Location"));
            //           getClosestDispatcher();
            String laat= String.valueOf(pickupLocation.latitude);
            String loong= String.valueOf(pickupLocation.longitude);
            String fireReport = "fire";
            Intent i = new Intent(accident.this, reportaccidentchecking.class);
            i.putExtra("reportType",fireReport);
            i.putExtra("Latitude",laat);
            i.putExtra("Longitude",loong);

            mapRipple.stopRippleMapAnimation();

            //    i.putExtra("Longitude",pickupLocation.longitude);
            startActivity(i);
        }catch (Exception x){

            Toast.makeText(accident.this, "error: "+x.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public void rippleMapEffect(){
        //mapRipple = new MapRipple(mMap, latLng,accident.this);
        mapRipple.withNumberOfRipples(2);
        mapRipple.withFillColor(Color.RED);
        mapRipple.withStrokeColor(Color.BLACK);
        mapRipple.withStrokewidth(10) ;     // 10dp
        mapRipple.withDistance(500) ;     // 1000 metres radius
        mapRipple.withRippleDuration(6000) ;   //12000ms
        mapRipple.withTransparency(0.5f);



    }
    int count=0;
   LatLng destination;
    private void setMapLongClick(final GoogleMap map) {
       // mapRipple.stopRippleMapAnimation();

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                count++;


                if (count == 1) {


                    //    map.addMarker(new MarkerOptions()
                      //      .position(latLng)
                      //      .title("your destination")
                    //        .icon(BitmapDescriptorFactory.defaultMarker
                  //                  (BitmapDescriptorFactory.HUE_BLUE)));
                    destination=new LatLng(latLng.latitude,latLng.longitude);
                    CircleOptions circleOptions = new CircleOptions();

                    Marker newpickupMarker;

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Person Location");

                    newpickupMarker = map.addMarker(new MarkerOptions().position(latLng).title("").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapmarker)));

                    // pickupLocation.latitude = destination.latitude;
                    mLastLocation.setLatitude(destination.latitude);
                     mLastLocation.setLongitude(destination.longitude);
                    // Specifying the center of the circle
                    circleOptions.center(latLng);

                    // Radius of the circle
                    circleOptions.radius(100);

                    // Border color of the circle
                    circleOptions.strokeColor(Color.BLUE);
                    // Fill color of the circle
                    // Border width of the circle
                    circleOptions.strokeWidth(1);
                    // Adding the circle to the GoogleMap

                    mMap.addCircle(circleOptions);
                    mapRipple.stopRippleMapAnimation();
                    pickupMarker.remove();
                    mapRipple = new MapRipple(mMap, latLng, accident.this);
                   rippleMapEffect();
                    mapRipple.startRippleMapAnimation();

                    // destinationset=true;
                    Toast.makeText(accident.this, "Different Location Selected!", Toast.LENGTH_SHORT).show();

                    //  tv2.setText("Tracking in progress");
                }
                else{
                  //  Toast.makeText(MapsActivityA.this, "Destination already set!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    public void procedureDetails(View view) {

        try{
            final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(accident.this,R.style.AlertDialog);

            dialog.setTitle("Different Location Report Procedure");
            dialog.setCancelable(false);
          //  dialog.setIcon()

            dialog.setMessage("If you want to Report Emergency for someone else at different location the procedure is your information " +
                    "will be used to reach out and different location will be first to reach.");
            LayoutInflater inflater = LayoutInflater.from(accident.this);
            View trip_layout = inflater.inflate(R.layout.procedurelayout, null);
            dialog.setView(trip_layout);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });


            dialog.show();
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_LONG).show();

        }

    }
    public void checkLocationEnabledreceiver(){
        registerReceiver(mLocationReceiver= new locationReceiver(new LocationCallBack() {
            @Override
            public void onLocationTriggered() {
                //Location state changed
                //           buildAlertMessageNoGps();
                displayLocationSettingsRequest(accident.this);
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
                            status.startResolutionForResult(accident.this, REQUEST_CHECK_SETTINGS);
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





}


