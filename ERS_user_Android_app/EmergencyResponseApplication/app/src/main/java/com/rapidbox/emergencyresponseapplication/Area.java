package com.rapidbox.emergencyresponseapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arsy.maps_library.MapRadar;
import com.arsy.maps_library.MapRipple;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Area extends FragmentActivity implements OnMapReadyCallback, LocationListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient client;
    ImageView currentLoc,markButton;
    Location lastLocation;
    LatLng areaLatLng ;
    LatLng latLng;
    String userID;
    Marker mDispatcherMarker;
    DatabaseReference userDatabaseReference;
    LocationManager lm;
    //int countClick;
    locationReceiver mLocationReceiver;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker pickupMarker;

    @Override
    protected void onResume() {
        super.onResume();
        buildAlertMessageNoGps();

    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

       //lm = (LocationManager)Area.this.getSystemService(Context.LOCATION_SERVICE);

        checkLocationEnabledreceiver();
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

      /*  boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(Area.this)
                    .setCancelable(false)
                    .setMessage("Please Enable Location Services!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Area.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .show();
        }
        else{*/


      //  countClick =0;
        try {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            userID = user.getUid();

            //  currentLoc = (ImageView) findViewById(R.id.getCurrentLocation);
            markButton = (ImageView) findViewById(R.id.markLocation);
            client = LocationServices.getFusedLocationProviderClient(Area.this);

            userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("UserAreas").child(userID);
            if (ActivityCompat.checkSelfPermission(Area.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Area.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
//            client.getLastLocation().addOnSuccessListener(Area.this, new OnSuccessListener<Location>() {
  //              @Override
    //            public void onSuccess(Location location) {

            areaLatLng= new LatLng(0.0, 0.0);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(Area.this);
////////////////////
                    //  countClick++;
                 //   lastLocation = location;
                   // areaLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
      //          }
        //    });

        }
        catch (Exception e){
            Toast.makeText(Area.this,"Error"+e.getMessage(),Toast.LENGTH_LONG);
        }
            /*currentLoc.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                          //  Intent i = new Intent(Area.this,fuzzyDisplay.class);
                           // startActivity(i);
                  try {


                    if (ActivityCompat.checkSelfPermission(Area.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Area.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    client.getLastLocation().addOnSuccessListener(Area.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.map);
                            mapFragment.getMapAsync(Area.this);

                          //  countClick++;
                            lastLocation = location;
                            areaLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

                        }
                    });
                }
              catch (Exception e){
                  Toast.makeText(Area.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
              }
                }
            });
*/
            markButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    try {
                   /*     if (lastLocation != null) {
                            GeoFire geoFire = new GeoFire(userDatabaseReference);
                            geoFire.setLocation(userID, new GeoLocation(lastLocation.getLatitude(), lastLocation.getLongitude()), new GeoFire.CompletionListener() {
                                @Override
                                public void onComplete(String key, DatabaseError error) {


                                    Toast.makeText(Area.this, "Area saved!! ", Toast.LENGTH_SHORT).show();

                                }
                            });
                        } else {
                            Toast.makeText(Area.this, "You have not specified the area!! ", Toast.LENGTH_SHORT).show();


                        }
                        */
                        Intent i = new Intent(Area.this,fuzzyDisplay.class);
                        startActivity(i);
                    }


                    catch (Exception e) {

                        Toast.makeText(Area.this, "Error!! " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            });
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
                            Intent i = new Intent(Area.this, navigation.class);
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

    MapRadar mapRadar;


    @Override
    public void onMapReady(GoogleMap googleMap) {
       /* mMap = googleMap;
        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(areaLatLng.latitude,areaLatLng.longitude);

        radarAnimation(sydney);
        mDispatcherMarker = mMap.addMarker(new MarkerOptions().position(sydney).title("Your Area").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapmarker)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    */
        mMap = googleMap;
     //   showLoad.hide();
        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));

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

                        info2.setText("Your Current Area");
                        info3.setText("Press Statistics to see safety.");
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


    public void radarAnimation(LatLng latLngx){
    //    if(countClick==0) {
            mapRadar = new MapRadar(mMap, latLngx, Area.this);
            mapRadar.withDistance(1000);
            mapRadar.withOuterCircleStrokeColor(0xfccd29);
            mapRadar.withRadarColors(0x00fccd29, 0xfffccd29);
            //withRadarColors() have two parameters, startColor and tailColor respectively
            //startColor should start with transparency, here 00 in front of fccd29 indicates fully transparent
            //tailColor should end with opaqueness, here f in front of fccd29 indicates fully opaque
            mapRadar.startRadarAnimation();      //in onMapReadyCallBack
      //  }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) Area.this);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onLocationChanged(Location location) {



        mLastLocation = location;
        // if (mCurrLocationMarker != null) {
        //   mCurrLocationMarker.remove();
        //}
        if (pickupMarker!=null){

            pickupMarker.remove();
          //  mapRipple.stopRippleMapAnimation();

        }

        //Place current location marker
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        radarAnimation(latLng);
        //  Toast.makeText(this, ""+mLastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        pickupMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mapmarker)));
       // mapRipple = new MapRipple(mMap, latLng, accident.this);

        //rippleMapEffect();
        //mapRipple.startRippleMapAnimation();


        //mapRipple.withLatLng(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));

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
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,Area.this);

        }

    }


}
