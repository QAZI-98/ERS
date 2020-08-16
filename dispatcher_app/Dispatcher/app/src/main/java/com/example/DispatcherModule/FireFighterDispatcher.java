        package com.example.DispatcherModule;
        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.app.ActivityCompat;
        import androidx.core.app.NotificationCompat;
        import androidx.core.app.NotificationManagerCompat;
        import androidx.fragment.app.FragmentActivity;

        import android.Manifest;
        import android.Manifest.permission;
        import android.app.AlertDialog;
        import android.app.NotificationChannel;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.location.Criteria;
        import android.location.Location;
        import android.location.LocationManager;
        import android.os.Build;
        import android.os.Bundle;
        import android.provider.Settings;
        import android.telephony.emergency.EmergencyNumber;
        import android.view.LayoutInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.TimePicker;
        import android.widget.Toast;

        import com.directions.route.AbstractRouting;
        import com.directions.route.Route;
        import com.directions.route.RouteException;
        import com.directions.route.Routing;
        import com.directions.route.RoutingListener;
        import com.firebase.geofire.GeoFire;
        import com.firebase.geofire.GeoLocation;
        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.location.FusedLocationProviderClient;
        import com.google.android.gms.location.LocationListener;
        import com.google.android.gms.location.LocationRequest;
        import com.google.android.gms.location.LocationServices;
        import com.google.android.gms.maps.CameraUpdate;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.CameraPosition;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.LatLngBounds;
        import com.google.android.gms.maps.model.MapStyleOptions;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.gms.maps.model.Polyline;
        import com.google.android.gms.maps.model.PolylineOptions;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.material.bottomnavigation.BottomNavigationView;
        import com.google.android.material.snackbar.Snackbar;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.maps.android.SphericalUtil;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.Map;

        import static android.Manifest.permission.*;
        public class FireFighterDispatcher extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, RoutingListener, GoogleApiClient.OnConnectionFailedListener {

    private List<Polyline> polylines;
    Location mLastLocation;
    FirebaseAuth mAuth;
    Button mSignBtn;
    DatabaseReference assignVictimPickupLocationRef;
    private ValueEventListener assignedCustomerPickupLocationRefListener;
    LatLng dipatcherLocationLatLng;
    String UserID;
    private LocationManager mLocationManager;

    private SupportMapFragment mapFragment;
    private boolean isPermission;
    private Boolean isLoggingOut = false;
    LatLng latLng;
    Button settingsButton;
    Marker pickupMarker;
    Button pickButton;
    private LocationManager locationManager;
    double actualDistance;
    boolean reportStatus = false;
    private Marker mPositionMarker;


    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;

    LocationRequest mLocationRequest;

    //private Button mLogout, btnshow, btndistance;

    private String customerId = "";


    public Criteria criteria;
    public String bestProvider;
            BottomNavigationView bottomNavigationView;
  //  ProgressBar progressBar;

            private FusedLocationProviderClient fusedLocationClient;

    private static final String CHANNEL_ID = "Alert_Notification";
            private static final String CHANNEL_NAME= "Alert Notification";
            private static final String CHANNEL_DESC = "Alert Notification Final ";



            @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_fighter_dispatcher);

                bottomNavigationView =(BottomNavigationView) findViewById(R.id.navbot);
                bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.idDistance:
                                if(reportStatus == false)
                                    noShow();
                                else
                                    ShowDistance();

                                return true;
                            case R.id.idLogout:
                                    showLogout();
                                return true;
                            case R.id.idProfile:
                                if(reportStatus == false)
                                    noShow();
                                else
                                    ShowVictimDetails();

                                return true;


                        }

                        return false;
                    }
                });


            //    bottomNavigationView.setSelectedItemId(R.id.Id);
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
                                    CameraPosition cameraPosition = new CameraPosition.Builder()
                                            .target(ll)
                                            .zoom(18)
                                            .zoom(18)
                                            .bearing(0)
                                            .tilt(10)
                                            .build();
                                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                }
                            }
                        });


                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }





//        btnshow = (Button) findViewById(R.id.showDetails);
  //      btndistance = (Button) findViewById(R.id.showDistance);
    //    btnshow.setVisibility(View.GONE);
      //  btndistance.setVisibility(View.GONE);
        dipatcherLocationLatLng = new LatLng(0, 0);
        polylines = new ArrayList<>();
        locationManager = (LocationManager)  this.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
        }
        mLastLocation = locationManager.getLastKnownLocation(bestProvider);
        // FirebaseUser userx = FirebaseAuth.getInstance().getCurrentUser();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mAuth = FirebaseAuth.getInstance();
       // mLogout = (Button) findViewById(R.id.logout);
        /*mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoggingOut = true;

                disconnectDriver();

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(FireFighterDispatcher.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });*/
        //
        //  locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
        getAssignedCustomer();






    }
public void noShow(){
                Toast.makeText(FireFighterDispatcher.this,"Currently no Fire Reported!",Toast.LENGTH_SHORT).show();
}
    public void showLogout(){

        final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(FireFighterDispatcher.this,R.style.AlertDialog);
//                final androidx.appcompat.app.AlertDialog alertDialog = dialog.create();

        View mView = getLayoutInflater().inflate(R.layout.dialog_logout,null);

        Button btnDiscard = (Button)mView.findViewById(R.id.idDiscard);
        Button btnLogout = (Button)mView.findViewById(R.id.idLogout);
        dialog.setView(mView);

        final androidx.appcompat.app.AlertDialog alertDialog =dialog.create();


        alertDialog.setCanceledOnTouchOutside(false);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isLoggingOut = true;

                disconnectDriver();
                actualDistance = 0;
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(FireFighterDispatcher.this, MainActivity.class);
                startActivity(intent);
                alertDialog.dismiss();
                finish();

            }
        });
        btnDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }
    private void getAssignedCustomer(){
        // String driverId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        String driverId ="";
        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
        if(mFirebaseUser != null) {
            driverId = mFirebaseUser.getUid(); //Do what you need to do with the id
        }

        final DatabaseReference assignedCustomerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("FirefighterTeam").child(driverId).child("userRideId");

        assignedCustomerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    customerId = dataSnapshot.getValue().toString();
                   // btnshow.setVisibility(View.VISIBLE);
               //     btndistance.setVisibility(View.VISIBLE);
                    getAssignedCustomerPickupLocation();
                    alertDispatcher();
                    reportStatus = true;
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void alertDispatcher() {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                        .setSmallIcon(R.drawable.alert)
                        .setContentTitle("Fire Alert!")
                        .setContentText("Fire has been reported ")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(1,mBuilder.build());





            }
            LatLng pickupLatLang;
    DatabaseReference assignedCustomerPickupLocationRef;
    private void getAssignedCustomerPickupLocation(){
        assignedCustomerPickupLocationRef = FirebaseDatabase.getInstance().getReference().child("FireRequest").child(customerId).child("l");
        // final DatabaseReference assignedCustomerPickupLocationRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("MedicalTeam").child(customerId).child("userRideId");

        assignedCustomerPickupLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;
                    if(map.get(0) != null){
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(1) != null){
                        locationLng = Double.parseDouble(map.get(1).toString());
                    }
                     pickupLatLang = new LatLng(locationLat,locationLng);
                    pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLatLang).title("pickup location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.komax)));



                    //    Location loc1 = new Location("");
                    //  loc1.setLatitude(pickupLatLang.latitude);
                    //loc1.setLongitude(pickupLatLang.longitude);

                    //Location loc2 = new Location("");
                    //loc2.setLatitude(mLastLocation.getLatitude());
                    //loc2.setLongitude(mLastLocation.getLongitude());

                    // dipatcherLocationLatLng  = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());

                    double distance = SphericalUtil.computeDistanceBetween(pickupLatLang, dipatcherLocationLatLng);
                    getRouteToMarker(pickupLatLang);



                    Toast.makeText(FireFighterDispatcher.this, "Distance " + distance, Toast.LENGTH_SHORT).show();

                    if (distance < 100) {
                        // mRequest.setText("Driver's Here");
                        Toast.makeText(FireFighterDispatcher.this, "Distance " + distance, Toast.LENGTH_SHORT).show();
//                        pickButton.setEnabled(true);

                    } else {
                        Toast.makeText(FireFighterDispatcher.this, "Distance " + distance, Toast.LENGTH_SHORT).show();

                    }
                    double d = distance/1000;
                    actualDistance= round(d,2);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    private void getRouteToMarker(LatLng disLatlng) {

        Routing routing = new Routing.Builder()
                .key("AIzaSyBljaPNDPxsTh5MnpeVwBS9x7rlAwThU08")
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener((RoutingListener) this)
                .alternativeRoutes(false)
                .waypoints(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()), disLatlng)

                .build();
        routing.execute();

    }


    private boolean checkLocation() {
        if (!isLocationEnabled()) {

            showAlert();
        }
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Location Settings is set to 'off' .\n Please Enable Location to use this App ")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        dialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
    }

            protected synchronized void buildGoogleApiClient(){
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
                mGoogleApiClient.connect();
            }
    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }



    @Override
    public void onConnectionSuspended(int i) {

    }



    @Override
    public void onLocationChanged(Location location) {


        if(getApplicationContext()!=null){

            mLastLocation = location;

            dipatcherLocationLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            if (mLastLocation != null) {
//                LatLng currentLatLng = new LatLng (mLastLocation.getLatitude(), mLastLocation.getLongitude());
                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

                if (mPositionMarker == null) {

//                  mPositionMarker =  mMap.addMarker(new MarkerOptions().position(latLng).title("Your Location"));

                } else
                    mPositionMarker.setPosition(latLng);
            }

            //          mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

            // String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            String userId ="";
            FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
            if(mFirebaseUser != null) {
                userId = mFirebaseUser.getUid(); //Do what you need to do with the id
            }

            DatabaseReference refAvailable = FirebaseDatabase.getInstance().getReference("FirefighterTeamAvailable");
            DatabaseReference refWorking = FirebaseDatabase.getInstance().getReference("FirefighterTeamWorking");
            GeoFire geoFireAvailable = new GeoFire(refAvailable);
            GeoFire geoFireWorking = new GeoFire(refWorking);

            switch (customerId) {
                case "":
                    geoFireWorking.removeLocation(userId, new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {

                        }
                    });
                    geoFireAvailable.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {

                        }
                    });
                    break;

                default:
                    geoFireAvailable.removeLocation(userId, new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {

                        }
                    });
                    geoFireWorking.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {

                        }
                    });
                    break;

                //         */
            }




        }


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(FireFighterDispatcher.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);

        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }



    final int LOCATION_REQUEST_CODE = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapFragment.getMapAsync(this);
                } else {
                    Toast.makeText(FireFighterDispatcher.this, "Please provide the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }
    private void disconnectDriver(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("FirefighterTeamAvailable");
        actualDistance = 0;
        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId, new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                Toast.makeText(FireFighterDispatcher.this, "You are now Offline", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isLoggingOut){
            disconnectDriver();
        }
    }
//----------------------------------------------Routes between points methods---------------------------------------


    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};

    @Override
    public void onRoutingFailure(RouteException e) {

        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(pickupLatLang);
        builder.include(dipatcherLocationLatLng);
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int padding = (int)(width*0.2);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,padding);
        mMap.animateCamera(cameraUpdate);

        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

          //  Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRoutingCancelled() {

    }

    private void erasePolylines() {
        for (Polyline line : polylines) {
            line.remove();

        }
        polylines.clear();

    }

    //-------------------------------------------Buttons Implemenation-----------------------

    public void ShowDistance() {


        try{
            if(actualDistance<=1){
                final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(FireFighterDispatcher.this,R.style.AlertDialog);
//                final androidx.appcompat.app.AlertDialog alertDialog = dialog.create();

                View mView = getLayoutInflater().inflate(R.layout.dialog_arrived,null);

             //   progressBar = (ProgressBar)mView.findViewById(R.id.showprogress);
               // progressBar.setVisibility(View.VISIBLE);



                dialog.setTitle("TRIP DETAILS");
                //LayoutInflater inflater = LayoutInflater.from(MedicalTeamDispatcher.this);
                //View trip_layout = inflater.inflate(R.layout.dialog_arrived, null);
                //final TextView edtName = trip_layout.findViewById(R.id.showd);
                // final TextView edtStatus = trip_layout.findViewById(R.id.notarrieved);
                Button btnDone = (Button)mView.findViewById(R.id.button6);
                Button btnPreview = (Button)mView.findViewById(R.id.button5);








                //String fullDistance = actualDistance+"km";
                //edtName.setText(fullDistance);
                //edtStatus.setText("Not Arrived");

                dialog.setView(mView);
                final androidx.appcompat.app.AlertDialog alertDialog =dialog.create();


                alertDialog.setCanceledOnTouchOutside(false);
                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        endRide();
                        alertDialog.dismiss();
                        //  dialog

                        //    alertDialog.dismiss();
                        //                           return;
                    }
                });
                btnPreview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // alertDialog.dismiss();
                        alertDialog.dismiss();
                    }
                });
              //  progressBar.setVisibility(View.INVISIBLE);

                alertDialog.show();

//                dialog.setView(trip_layout);


                //dialog.show();

            }
            else{
                final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(FireFighterDispatcher.this,R.style.AlertDialog);
                View mView = getLayoutInflater().inflate(R.layout.dialog_distance,null);
                dialog.setTitle("TRIP DETAILS");
                dialog.setMessage("Ambulance is approaching you. Currently at the distance below.");
                final TextView edtName = (TextView)mView.findViewById(R.id.showd);
                final TextView edtStatus = (TextView)mView.findViewById(R.id.notarrieved);
                Button btnOk = (Button)mView.findViewById(R.id.button7);
                //double fullDistance = actualDistance;

                edtName.setText(String.valueOf(actualDistance));
                edtStatus.setText("Not Arrived");
                dialog.setView(mView);

                final androidx.appcompat.app.AlertDialog alertDialog =dialog.create();
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                //     dialog.show();
            }
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_LONG).show();

        }


    }
    private void endRide(){
        //   mRideStatus.setText("picked customer");
        erasePolylines();
        reportStatus=false;
        String userId ="";
        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
        if(mFirebaseUser != null) {
            userId = mFirebaseUser.getUid(); //Do what you need to do with the id
        }


        DatabaseReference driverRef  = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("FirefighterTeam").child(userId).child("userRideId");

        // DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(userId).child("customerRequest");
        driverRef.removeValue();
        DatabaseReference ref  = FirebaseDatabase.getInstance().getReference().child("FireRequest");

        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId, new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                Toast.makeText(FireFighterDispatcher.this,"Service Done",Toast.LENGTH_LONG).show();
                if(pickupMarker!=null){
                    pickupMarker.remove();}
            }
        });
        customerId="";

        if(pickupMarker != null){
            pickupMarker.remove();
        }
        if (assignedCustomerPickupLocationRefListener != null){
            assignedCustomerPickupLocationRef.removeEventListener(assignedCustomerPickupLocationRefListener);
        }
     //   btndistance.setVisibility(View.GONE);
      //  btnshow.setVisibility(View.GONE);
        // mCustomerName.setText("");
        //mCustomerPhone.setText("");
        // mCustomerDestination.setText("Destination: --");
        //mCustomerProfileImage.setImageResource(android.R.mipmap.ic_default_user);
    }
    public void ShowVictimDetails() {



        final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(FireFighterDispatcher.this);
        dialog.setIcon(R.drawable.user);

        LayoutInflater inflater = LayoutInflater.from(FireFighterDispatcher.this);
        View profile_layout = inflater.inflate(R.layout.dialog_userdetails, null);
        //progressBar = (ProgressBar)profile_layout.findViewById(R.id.showprogress);
       // progressBar.setVisibility(View.VISIBLE);
        final TextView edtName = profile_layout.findViewById(R.id.txtName);
        final TextView edtEmail = profile_layout.findViewById(R.id.txtEmail);
        final TextView edtPhone = profile_layout.findViewById(R.id.txtPhone);
        final TextView edtBlood = profile_layout.findViewById(R.id.txtBlood);
        final TextView edtMedical = profile_layout.findViewById(R.id.txtMedical);
        final TextView edtAllergy = profile_layout.findViewById(R.id.txtAllergy);
        final TextView edtContactName = profile_layout.findViewById(R.id.txtPersonName);
        final TextView edtContact = profile_layout.findViewById(R.id.txtPersonContact);






        DatabaseReference userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Victim").child(customerId);

        userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if ((map.get("firstname") != null)&&(map.get("lastname") != null) ){
                        String fname = map.get("firstname").toString();
                        String lname = map.get("lastname").toString();

                        edtName.setText(fname+" "+lname);
                    }
                    if (map.get("phone") != null) {

                        edtPhone.setText(map.get("phone").toString());

                    }
                    else{
                        edtPhone.setText("Not Available");

                    }
                    if (map.get("email") != null) {

                        edtEmail.setText(map.get("email").toString());

                    }


                    if (map.get("bloodType") != null) {
                        edtBlood.setText(map.get("bloodType").toString());

                    } else {
                        edtBlood.setText("Not Available");

                    }
                    if (map.get("emergencyContactPhone") != null) {
                        edtContact.setText(map.get("emergencyContactPhone").toString());

                    }
                    else{
                        edtContact.setText("Not Available");

                    }
                    if (map.get("emergencyContactname")!=null){
                        edtContactName.setText(map.get("emergencyContactname").toString());

                    }
                    else{
                        edtContactName.setText("Not Available");

                    }
                    if (map.get("medicalCondition")!=null){
                        edtMedical.setText(map.get("medicalCondition").toString().replace("[", "").replace("]", ""));

                        //edtMedical.setText(map.get("medicalCondition").toString());
                    }
                    else {
                        edtMedical.setText("Not Available");
                    }
                    if (map.get("allergy")!=null){
                        edtAllergy.setText(map.get("allergy").toString().replace("[", "").replace("]", ""));

                    //    edtAllergy.setText(map.get("allergy").toString());
                    }
                    else{
                        edtAllergy.setText("Not Available");

                    }
                //    progressBar.setVisibility(View.INVISIBLE);
                }
            } @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});
//profile image


        dialog.setView(profile_layout);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.setNegativeButton("CANCEL",new DialogInterface.OnClickListener()

        {
            @Override
            public void onClick (DialogInterface dialogInterface,int i){
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }



    public void ShowPickedDialog() {


        final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(FireFighterDispatcher.this);
        dialog.setIcon(R.drawable.cars);
        dialog.setTitle("ALERT");
        dialog.setMessage("Have you picked up the victim ?");
        LayoutInflater inflater = LayoutInflater.from(FireFighterDispatcher.this);
        final View pick_layout = inflater.inflate(R.layout.pickeduserlayout, null);


        dialog.setView(pick_layout);
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final DatabaseReference assignVictimRef = FirebaseDatabase.getInstance().getReference().child("FireRequest");

                GeoFire geoFire = new GeoFire(assignVictimRef);
                geoFire.removeLocation(UserID, new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        Toast.makeText(FireFighterDispatcher.this, "User Picked Congratulation!!!!", Toast.LENGTH_SHORT).show();
                        Snackbar.make(pick_layout, "Victim Picked!!", Snackbar.LENGTH_SHORT).show();

                    }
                });
            }
        });

        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();




    }
    public void showDropDialog(){
        final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(FireFighterDispatcher.this);
        dialog.setIcon(R.drawable.hospital);

        dialog.setTitle("ALERT");
        dialog.setMessage("Have you droped the victim to nearby Hospital?");
        LayoutInflater inflater = LayoutInflater.from(FireFighterDispatcher.this);
        final View pick_layout = inflater.inflate(R.layout.pickeduserlayout, null);


        dialog.setView(pick_layout);
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                erasePolylines();

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("FireRequest").child(userId).child("userRideId");
                driverRef.removeValue();

                UserID = "";
//        rideDistance = 0;

                if (pickupMarker != null) {
                    pickupMarker.remove();
                }
                if (assignedCustomerPickupLocationRefListener != null) {
                    assignVictimPickupLocationRef.removeEventListener(assignedCustomerPickupLocationRefListener);
                }

            }
        });

        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();



    }
    public void PickDialoge(View view){
        ShowPickedDialog();


    }

            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        }
