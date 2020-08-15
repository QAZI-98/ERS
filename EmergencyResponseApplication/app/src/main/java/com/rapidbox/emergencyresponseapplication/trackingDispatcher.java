package com.rapidbox.emergencyresponseapplication;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.rapidbox.emergencyresponseapplication.App.CHANNEL_1_ID;

public class trackingDispatcher extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, RoutingListener {
    private static final int REQUEST_CODE = 101;
    Location currentLocation;
    LocationRequest locationRequest;
    TextView distanceDisplay;

    private GoogleMap mMap;
    String dispatcherID;
GoogleApiClient googleApiClient;
Double pickupLatitude;
    Double pickupLongitude;

    ImageView stateView;
    RelativeLayout relativeLayoutDrop;

    String UserID = "";
    private LinearLayout mDispatcherInfo;
    private ImageView mDispatcherProfileImage;
    private TextView mDispatcherName,mDispatcherPhone,mDispatcherVehical;
    Button picked;
    String reportTypeL;
    double distance=0.0;

    private LatLng pickupLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    double actualDistance;
    SharedPreferences prefs;
    Marker mDispatcherMarker;
    TextView mdispatcherName,mdispatcherState;
    int status =0;
    private List<Polyline> polylines;
    LatLng disLatlng;

    locationReceiver mLocationReceiver;

    String pickGeocode;
    String dropGeocode;
    ProgressDialog showLoad;

    List<Address> addresses = null;
    private NotificationManagerCompat notificationManagerCompat;

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mLocationReceiver);

    }

    @Override
    protected void onPause() {
        super.onPause();
        checkLocationEnabledreceiver();
    }

    @Override
    public void onBackPressed() {
       // Toast.makeText(trackingDispatcher.this,"Pressed!!!",Toast.LENGTH_LONG);

        final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(trackingDispatcher.this,R.style.AlertDialog);

        dialog.setTitle("Alert");
        dialog.setCancelable(false);

        dialog.setMessage("Are you sure you want to Exit ?");
        LayoutInflater inflater = LayoutInflater.from(trackingDispatcher.this);
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                trackingDispatcher.super.onBackPressed();
                dialogInterface.dismiss();
                Intent ii = new Intent(trackingDispatcher.this,dashboard.class);
                startActivity(ii);
                finish();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tracking_dispatcher);



    prefs = getSharedPreferences("Values", MODE_PRIVATE);
        dispatcherID = prefs.getString("dispatcherID","no");
        reportTypeL= prefs.getString("reportType","no");
      //  showLoad = new ProgressDialog(trackingDispatcher.this,R.style.AppCompatAlertDialogStyle);
       // showLoad.setMessage("Wait...");
        //showLoad.show();

        checkLocationEnabledreceiver();


        pickupLatitude = Double.valueOf(prefs.getFloat("pickupLatitude",0.0f));
        pickupLongitude = Double.valueOf(prefs.getFloat("pickupLongitude",0.0f));






        //mdispatcherName = (TextView)findViewById(R.id.dispatcherWho);
        //mdispatcherState = (TextView)findViewById(R.id.stateDispatcher);
        stateView = (ImageView)findViewById(R.id.dropRel);
        stateView.setClickable(true);
        notificationManagerCompat = NotificationManagerCompat.from(this);
        //  mDispatcherMarker = new MarkerOptions();

       /* if (getIntent().getExtras() != null) {

               dispatcherID = String.valueOf(getIntent().getExtras().getString("dispatcherID"));
               reportTypeL = String.valueOf(getIntent().getExtras().getString("reportType"));

               pickupLatitude = Double.valueOf(getIntent().getExtras().getString("pickupLatitude"));
               pickupLongitude = Double.valueOf(getIntent().getExtras().getString("pickupLongitude"));


           }*/

       //--------------------------------------------------------------------------------
        //if(status==0) {
        polylines = new ArrayList<>();

   // Toast.makeText(trackingDispatcher.this, "" + dispatcherID, Toast.LENGTH_SHORT).show();
//    pickupLocation = new LatLng(0.0,0.0);
        pickupLocation = new LatLng(pickupLatitude, pickupLongitude);

        fetchLocation();
        distanceDisplay = (TextView)findViewById(R.id.distanceView);

        DatabaseReference disRef=FirebaseDatabase.getInstance().getReference();
        if(reportTypeL.equals("medical"))
        {
           // mdispatcherState.setText("Drop");
            //mdispatcherName.setText("Ambulance");
             disRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("MedicalTeam").child(dispatcherID);
        }
        else if(reportTypeL.equals("crime")){
            //mdispatcherName.setText("Police");
            //mdispatcherState.setText("Help Provided");

            disRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("PoliceTeam").child(dispatcherID);

        }
        else if(reportTypeL.equals("fire")){
//            mdispatcherName.setText("Firefighter");
  //          mdispatcherState.setText("Fire Extinguished");
            disRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("FirefighterTeam").child(dispatcherID);

        }
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        HashMap map = new HashMap();
        map.put("userRideId", userID);
        disRef.updateChildren(map);
    //Toast.makeText(this, "puted "+dispatcherID, Toast.LENGTH_SHORT).show();


        getDispatcherLocation();
    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    locationRequest = new LocationRequest();
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

      //  showLoad.hide();

//    mDispatcherInfo = (LinearLayout)findViewById(R.id.dispatcherInfo);
    mDispatcherName = (TextView) findViewById(R.id.dispatcherName);
    mDispatcherPhone= (TextView) findViewById(R.id.dispatcherPhone);
   // mDispatcherVehical = (TextView)findViewById(R.id.dispatcherVehical);
   // getDispatcherInfo();
        }


    //  private OnBackPressedDispatcherOwner requireActivity() {

      //  return onBackPressed();
    //}
    //}

    private void getDispatcherInfo() {

  //      mDispatcherInfo.setVisibility(View.VISIBLE);
        DatabaseReference userDatabaseReference = FirebaseDatabase.getInstance().getReference();
        if(reportTypeL.equals("medical")) {
               userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("MedicalTeam").child(dispatcherID);
        }
        else if(reportTypeL.equals("crime")) {
            userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("PoliceTeam").child(dispatcherID);
        }
        else if(reportTypeL.equals("fire")) {
            userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("FirefighterTeam").child(dispatcherID);
        }

        userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("name") != null) {
                        String fname = map.get("name").toString();
                        mDispatcherName.setText(fname);


                    }
                    if (map.get("phone") != null) {

                        mDispatcherPhone.setText(map.get("phone").toString());

                    }

                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


    });
    }
    DatabaseReference disLocationRef;
   private ValueEventListener disLocationRefListener;

    private void getDispatcherLocation() {
       // ++status;
         disLocationRef = FirebaseDatabase.getInstance().getReference();
        if(reportTypeL.equals("medical")) {

            disLocationRef = FirebaseDatabase.getInstance().getReference().child("MedicalTeamAvailable").child(dispatcherID).child("l");
        }
        else if(reportTypeL.equals("crime")) {

            disLocationRef = FirebaseDatabase.getInstance().getReference().child("PoliceTeamAvailable").child(dispatcherID).child("l");
        }
        else if(reportTypeL.equals("fire")) {

            disLocationRef = FirebaseDatabase.getInstance().getReference().child("FirefighterTeamAvailable").child(dispatcherID).child("l");
        }
  // // int i =0;
        disLocationRefListener= disLocationRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
    //            Toast.makeText(trackingDispatcher.this, ""+i++, Toast.LENGTH_SHORT).show();

                if (dataSnapshot.exists()) {
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLong = 0;
                    if (map.get(0) != null) {
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1) != null) {
                        locationLong = Double.parseDouble(map.get(1).toString());

                    }
                     if (mDispatcherMarker != null) {
                        mDispatcherMarker.remove();
                    }
                    disLatlng = new LatLng(locationLat, locationLong);
                    getRouteToMarker(disLatlng);

                   // float distance = loc1.distanceTo(loc2);
                 //   Toast.makeText(trackingDispatcher.this, "Lat "+locationLat+" Long "+locationLong, Toast.LENGTH_SHORT).show();

                 //   pickupLatitude = Double.valueOf(prefs.getFloat("pickupLatitude",0.0f));
                   // pickupLongitude = Double.valueOf(prefs.getFloat("pickupLongitude",0.0f));
                   // LatLng disLatlng = new LatLng(pickupLatitude, pickupLongitude);

                    distance = SphericalUtil.computeDistanceBetween(pickupLocation,disLatlng);
                   // Toast.makeText(trackingDispatcher.this, "Distance "+distance, Toast.LENGTH_SHORT).show();
                 //   Toast.makeText(trackingDispatcher.this, "pick "+pickupLocation, Toast.LENGTH_SHORT).show();

                    //distanceDisplay.setText(distance);



                    if(distance<100){
                        if (reportTypeL.equals("medical")){
                        stateView.setClickable(true);
                            alertShowpick();}
                       else if (reportTypeL.equals("crime")){
                            stateView.setClickable(true);
                            alertShowPolice();}
                        else if (reportTypeL.equals("fire")){
                            stateView.setClickable(true);
                            alertShowFireFighter();}


                    }else{
                       // distanceDisplay.setText("Response Team Approaching you: "+distance/1000+" km");
                    }
                    double d = distance/1000;
                   actualDistance= round(d,2);



                    if (mMap != null) {

                        if(reportTypeL.equals("medical")) {
                            mDispatcherMarker = mMap.addMarker(new MarkerOptions().position(disLatlng).title("Your Ambulance").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_ambulance)));
                        }
                        else if(reportTypeL.equals("crime")) {
                            mDispatcherMarker = mMap.addMarker(new MarkerOptions().position(disLatlng).title("Police Team").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_policecar)));
                        }
                       else if(reportTypeL.equals("fire")) {
                            mDispatcherMarker = mMap.addMarker(new MarkerOptions().position(disLatlng).title("Firefighter Team").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_fighter)));
                        }




                    }
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    int st = 0;
    int showAlertOnlyOnceFirefighter = 0;
    int showAlertOnlyOnceCrime = 0;

    public void alertShowFireFighter() {
        if (showAlertOnlyOnceFirefighter == 0){
            distanceDisplay.setText("FireFighters Arrieved ");
            Notification notification = new NotificationCompat.Builder(trackingDispatcher.this, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.fire)
                    .setContentTitle("Firefighter Arrived")
                    .setContentText("Firefighter has arrived at trouble location.")
                    .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .build();
            notificationManagerCompat.notify(1, notification);
            ShowVictimPicked();
            showAlertOnlyOnceFirefighter++;}
    }
    public void alertShowPolice() {
        if (showAlertOnlyOnceCrime == 0){
            distanceDisplay.setText("FireFighters Arrieved ");
            Notification notification = new NotificationCompat.Builder(trackingDispatcher.this, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ambulance)
                    .setContentTitle("Police Arrived")
                    .setContentText("Police has arrived at your Location.")
                    .setContentText("Police has arrived at your Location.")
                    .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .build();
            notificationManagerCompat.notify(1, notification);
            ShowVictimPicked();
            showAlertOnlyOnceCrime++;}
    }



    public void alertShowpick() {
        if (st == 0){
            distanceDisplay.setText("Dispatcher Arrieved ");
        Notification notification = new NotificationCompat.Builder(trackingDispatcher.this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ambulance)
                .setContentTitle("Ambulance Arrived")
                .setContentText("Ambulance has arrived at your location.")
                .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManagerCompat.notify(1, notification);
        ShowVictimPicked();
        st++;}
    }
    public void RecordHistory(){

         DatabaseReference dispatcherRef = FirebaseDatabase.getInstance().getReference();
         String userIDx = FirebaseAuth.getInstance().getCurrentUser().getUid();
         Toast.makeText(this, ""+userIDx, Toast.LENGTH_SHORT).show();
         DatabaseReference VictimRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Victim").child(userIDx).child("history");
         String val = "";
         String typeReport = "";
         if (reportTypeL.equals("medical")) {
             dispatcherRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("MedicalTeam").child(dispatcherID);
             val = "medicalTeam";
             typeReport = "Accident";
         } else if (reportTypeL.equals("crime")) {

             dispatcherRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("PoliceTeam").child(dispatcherID);
             val = "crimeTeam";
             typeReport = "Crime";
         } else if (reportTypeL.equals("fire")) {

             dispatcherRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("FirefighterTeam").child(dispatcherID);
             val = "firefighterTeam";
             typeReport = "Fire";
         }
         DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference().child("history");
        String requestId = historyRef.push().getKey();
        dispatcherRef.child("history").child(requestId).setValue(true);
        VictimRef.child(requestId).setValue(true);
        HashMap map = new HashMap();
        map.put("reportType",typeReport);
        map.put(val, dispatcherID);
        map.put("victimId", userIDx);
        map.put("timestamp", getcurrentTimeStamp());
          map.put("pickup",pickGeocode);
          map.put("drop",dropGeocode);

        historyRef.child(requestId).updateChildren(map);

        //  LatLng dropLngLat = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
//         String pickGeocode = reverseGeocodin(pickupLocation);
  //       String dropGeocode = reverseGeocodin(dropLngLat);
       //  Toast.makeText(this, ""+pickGeocode, Toast.LENGTH_SHORT).show();


       //  map.put("pickup",pickGeocode);
       //  map.put("drop",dropGeocode);


     //}catch (Exception x){
       //  Toast.makeText(trackingDispatcher.this,x.getMessage(),Toast.LENGTH_SHORT).show();
     //}


    }
    public String reverseGeocodin(LatLng geoLatLng){
        String strAddress = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {

            addresses = geocoder.getFromLocation(
                    geoLatLng.latitude,
                    geoLatLng.longitude,
                    // In this sample, get just a single address.
                    1);
            strAddress = addresses.get(0).getAddressLine(0);


        }catch (IOException ioException) {
            Toast.makeText(this, ""+ioException.getMessage(), Toast.LENGTH_SHORT).show();
            // Catch network or other I/O problems.
          //  Log.e(TAG, errorMessage, ioException);
        }



        return strAddress;


    }
    private Long getcurrentTimeStamp() {
        Long timestamp = System.currentTimeMillis()/1000;
        return  timestamp;

    }

    private void endRide(){
        DatabaseReference disRef =  FirebaseDatabase.getInstance().getReference();
        DatabaseReference assignVictimRef = FirebaseDatabase.getInstance().getReference();
        //LatLng dropLngLat = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

          pickGeocode = reverseGeocodin(pickupLocation);
         dropGeocode = reverseGeocodin(disLatlng);

        //String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
       if(reportTypeL.equals("medical")) {
            disRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("MedicalTeam").child(dispatcherID).child("userRideId");
           assignVictimRef = FirebaseDatabase.getInstance().getReference().child("MedicalRequest");
       }
        else if(reportTypeL.equals("crime")) {

           disRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("PoliceTeam").child(dispatcherID).child("userRideId");

           assignVictimRef = FirebaseDatabase.getInstance().getReference().child("PoliceRequest");

       }
       else if(reportTypeL.equals("fire")) {

           disRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("FirefighterTeam").child(dispatcherID).child("userRideId");

           assignVictimRef = FirebaseDatabase.getInstance().getReference().child("FireRequest");

       }
        disRef.removeValue();
        erasePolylines();
        RecordHistory();
       // disRef.removeValue();
//         DatabaseReference assignVictimRef = FirebaseDatabase.getInstance().getReference().child("MedicalRequest");

        GeoFire geoFire = new GeoFire(assignVictimRef);
        geoFire.removeLocation(UserID, new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                Toast.makeText(trackingDispatcher.this,"Picked by dispatcher!!!!", Toast.LENGTH_SHORT).show();
               // Snackbar.make(relativeLayout, "Victim Picked!!", Snackbar.LENGTH_SHORT).show();
               // recordRide();
            }
        });

        //userId="";




        if (disLocationRefListener != null){
            disLocationRef.removeEventListener(disLocationRefListener);
        }
       }


       /*
    public void recordRide(){
try {
    String userIDx = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference dispatcherref = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("MedicalTeam").child(dispatcherID).child("history");
    DatabaseReference userref = FirebaseDatabase.getInstance().getReference().child("Users").child("Victim").child(userIDx).child("history");
    DatabaseReference historyref = FirebaseDatabase.getInstance().getReference().child("history");
    String requestID = historyref.push().getKey(); // unique id

    dispatcherref.child(requestID).setValue(true);
    userref.child(requestID).setValue(true);

    HashMap map = new HashMap();
    map.put("victimID", userIDx);
    //  if(reportTypeL.equals("medical")) {
    map.put("medicalTeamID", dispatcherref);
    // }
    map.put("reportTypeName", reportTypeL);
    historyref.updateChildren(map);



}catch (Exception e){
    Toast.makeText(trackingDispatcher.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
}

    }
*/










    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    private void fetchLocation() {


        if (ActivityCompat.checkSelfPermission(trackingDispatcher.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(trackingDispatcher.this, new String[]

                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;

        } if(locationRequest!=null) {

            try {

                Task<Location> task = fusedLocationProviderClient.getLastLocation();

                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            if (ActivityCompat.checkSelfPermission(trackingDispatcher.this,
                                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                currentLocation = location;
                                locationRequest = new LocationRequest();
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                             //   Toast.makeText(trackingDispatcher.this, currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();

                                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                        .findFragmentById(R.id.map);
                                mapFragment.getMapAsync(trackingDispatcher.this);
                            }

                        }
                    }


                });
            } catch (Exception e) {
                Toast.makeText(trackingDispatcher.this, "" + e, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));
        // Add a marker in Sydney and move the camera

        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if(pickupLatitude!=null&&pickupLongitude!=null && disLatlng!=null){
            pickupLocation = new LatLng(pickupLatitude, pickupLongitude);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(pickupLocation);
            builder.include(disLatlng);
            LatLngBounds bounds = builder.build();
            int width = getResources().getDisplayMetrics().widthPixels;
            int padding = (int)(width*0.2);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,padding);
            mMap.animateCamera(cameraUpdate);

        }


        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
    }
    private synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(trackingDispatcher.this)
                .addOnConnectionFailedListener(trackingDispatcher.this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
        }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

public void pickedVictimShow(View view){
        ShowVictimPicked();

}

    private void ShowVictimPicked() {

        try{
            final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(trackingDispatcher.this,R.style.AlertDialog);

            dialog.setTitle("ALERT");
            dialog.setIcon(R.drawable.problem);

            dialog.setMessage("Dispatcher have Arrived at your Location ?");
            LayoutInflater inflater = LayoutInflater.from(trackingDispatcher.this);
            dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    endRide();
                    prefs.edit().clear().commit();
                    Intent ii = new Intent(trackingDispatcher.this,thankyouNote.class);
                    startActivity(ii);
                    finish();
                    dialogInterface.dismiss();
     //                   finish();
                }
            });
            dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
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

    public void showDispatcherInfoDet(View view){
    ShowDispatcherDetails();

}
public void showTripDetails(View view){

        tripDetails();
}


    private void tripDetails() {

        try{
        final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(trackingDispatcher.this,R.style.AlertDialog);

        dialog.setTitle("TRIP DETAILS");
        dialog.setIcon(R.drawable.movearrows);


        dialog.setMessage("Ambulance is approaching you. Currently at the distance below.");
        LayoutInflater inflater = LayoutInflater.from(trackingDispatcher.this);
        View trip_layout = inflater.inflate(R.layout.triplayout, null);
        final TextView edtName = trip_layout.findViewById(R.id.distanceTxt);
        String fullDistance = actualDistance+"km";
        edtName.setText(fullDistance);

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

    public void ShowDispatcherDetails() {

        try {


            final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(trackingDispatcher.this,R.style.AlertDialog);
            dialog.setIcon(R.drawable.userlast);

            dialog.setTitle("DISPATCHER PROFILE");
            dialog.setMessage("The below information is about the dispatcher person.");
            LayoutInflater inflater = LayoutInflater.from(trackingDispatcher.this);
            View profile_layout = inflater.inflate(R.layout.dispatcherlayout, null);
            final TextView edtName = profile_layout.findViewById(R.id.dispatcherName);
            final TextView edtPhone = profile_layout.findViewById(R.id.dispatcherPhone);
           // final TextView edtVehical = profile_layout.findViewById(R.id.dispatcherVehical);

            DatabaseReference userDatabaseReference = FirebaseDatabase.getInstance().getReference();
            if (reportTypeL.equals("medical")) {
                userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("MedicalTeam").child(dispatcherID);
            } else if (reportTypeL.equals("crime")) {
                userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("PoliceTeam").child(dispatcherID);
            } else if (reportTypeL.equals("fire")) {
                userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("FirefighterTeam").child(dispatcherID);
            }

            userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                        if (map.get("name") != null) {
                            String fname = map.get("name").toString();
                            edtName.setText(fname);


                        }
                        if (map.get("phone") != null) {

                            edtPhone.setText(map.get("phone").toString());

                        }

                    }
                    else{

                        Toast.makeText(trackingDispatcher.this,"Not Available ",Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });
            dialog.setView(profile_layout);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
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
      //  LatLng ll = new LatLng(24.8888,66.9399);
    private void getRouteToMarker(LatLng disLatlng) {

        Routing routing = new Routing.Builder()
                .key("AIzaSyBljaPNDPxsTh5MnpeVwBS9x7rlAwThU08")
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(new LatLng(pickupLocation.latitude, pickupLocation.longitude), disLatlng)
                .build();
        routing.execute();

    }

    private void erasePolylines() {
        for (Polyline line : polylines) {
            line.remove();

        }
        polylines.clear();

    }
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
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

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

        //    Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();
        }

        }

    @Override
    public void onRoutingCancelled() {

    }

    public void checkLocationEnabledreceiver(){
        registerReceiver(mLocationReceiver= new locationReceiver(new LocationCallBack() {
            @Override
            public void onLocationTriggered() {
                //Location state changed
                buildAlertMessageNoGps();

            }
        }),new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));


    }
    private void buildAlertMessageNoGps() {
        boolean gps_enabled;
        boolean network_enabled;
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

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
                            Toast.makeText(trackingDispatcher.this, "Location must be provided!!", Toast.LENGTH_SHORT).show();
                          checkLocationEnabledreceiver();
                            //Intent i = new Intent(trackingDispatcher.this, dashboard.class);
                            //      i.putExtra("reportType","medical");
                            //startActivity(i);
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();}
        else{
            Toast.makeText(this, "Location is enabled", Toast.LENGTH_SHORT).show();
        }
    }
}

