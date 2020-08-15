package com.rapidbox.emergencyresponseapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class reportaccidentchecking extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_CODE = 101;
    Location currentLocation;
    LocationRequest locationRequest;
    private GoogleMap mMap;
    double latitudeuser,longitudeuser;
    double latitudedispatch,longitudedispatch;
    Intent accidentActivityIntent;
    String reportType;
    double pickupLatitude;
    double pickupLongitude;
    TextView reportTitleText,reportBodyText;
    ImageView reportTitleImage;
    private boolean dispatcherFound = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportaccidentchecking);

      //  prefs = getSharedPreferences("Values", MODE_PRIVATE);
       // dispatcherID = prefs.getString("dispatcherID","no");
        //      dispatcherID = String.valueOf(getIntent().getExtras().getString("dispatcherID"));

    //    reportTypeS= prefs.getString("reportType","no");
      //  pickupLatitude = Double.valueOf(prefs.getFloat("pickupLatitude",0.0f));
       // pickupLongitude = Double.valueOf(prefs.getFloat("pickupLongitude",0.0f));


        reportTitleText = (TextView)findViewById(R.id.reportTitle);
            reportBodyText = (TextView)findViewById(R.id.reportBody);
            reportTitleImage = (ImageView)findViewById(R.id.reportImage);
     //-------------------------------------------------------------------------------------

          pickupLatitude = Double.valueOf(getIntent().getExtras().getString("Latitude"));
        pickupLongitude = Double.valueOf(getIntent().getExtras().getString("Longitude"));
        reportType = String.valueOf(getIntent().getExtras().getString("reportType"));



      if(reportType.equals("medical")){
          reportTitleImage.setImageResource(R.drawable.accidenttitle);
        reportTitleText.setText("Accident Reporting");
        reportBodyText.setText("Sending your Accident Request to nearby response Team");
       getClosestDispatcher();
      }
      else if(reportType.equals("crime")) {
          reportTitleImage.setImageResource(R.drawable.handcuffs);
          reportTitleText.setText("Crime Reporting");
          reportBodyText.setText("Sending your Crime Request to nearby Police Team");
          getClosestDispatcher();

      }
      else if(reportType.equals("fire")) {
          reportTitleImage.setImageResource(R.drawable.firefinal);
          reportTitleText.setText("Fire Reporting");
          reportBodyText.setText("Sending your Request to Firefighters at your current Location.");
          getClosestDispatcher();

      }
    }
    private int radius =1;

    String dispatcherFoundId;

    private void getClosestDispatcher(){
        DatabaseReference dispatcherLocation =FirebaseDatabase.getInstance().getReference();
        if(reportType.equals("medical")) {
            dispatcherLocation = FirebaseDatabase.getInstance().getReference().child("MedicalTeamAvailable");
        }
        else if(reportType.equals("crime")) {
            dispatcherLocation = FirebaseDatabase.getInstance().getReference().child("PoliceTeamAvailable");
        }
        else if(reportType.equals("fire")) {
            dispatcherLocation = FirebaseDatabase.getInstance().getReference().child("FirefighterTeamAvailable");
        }


        GeoFire geoFire = new GeoFire(dispatcherLocation);
       final SharedPreferences.Editor editor = getSharedPreferences("Values", MODE_PRIVATE).edit();

        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLatitude,pickupLongitude),radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if(!dispatcherFound){
                    dispatcherFound =true;
                    dispatcherFoundId=key;

                    String rad = String.valueOf(radius);
                    String laat= String.valueOf(pickupLatitude);
                    String loong= String.valueOf(pickupLongitude);
                    String requestFoundPolice= "police";
                    String requestFoundMedical = "medical";
                    String requestFoundFire = "fire";
                    Intent i = new Intent(reportaccidentchecking.this, reportconfirnmation.class);

                    if(reportType.equals("medical")){
                        i.putExtra("reportType",requestFoundMedical);

                    }
                    else if(reportType.equals("crime")){
                        i.putExtra("reportType",requestFoundPolice);

                    }
                    else if(reportType.equals("fire")){
                        i.putExtra("reportType",requestFoundFire);

                    }

                    editor.putString("dispatcherID", dispatcherFoundId);
                    editor.putString("reportType", reportType);

                    editor.putFloat("pickupLatitude",Float.valueOf(laat));
                    editor.putFloat("pickupLongitude", Float.valueOf(loong));
                    editor.apply();

                    startActivity(i);

                    finish();

                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

                if(!dispatcherFound){

                    radius++;
                    getClosestDispatcher(); // recursive call for increasing radius
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

    }
    public void storeSharedData(){
    }
public void callMEthod(){

    Intent ii = new Intent(reportaccidentchecking.this, reportconfirnmation.class);
    startActivity(ii);


    }
boolean allowback = false;
    @Override
    public void onBackPressed() {

        if (allowback) {
            super.onBackPressed();}
        else{

        }
    }
}



