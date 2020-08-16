package com.rapidbox.emergencyresponseapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class reportconfirnmation extends AppCompatActivity {
    String dispatcherID;
    TextView distanceValue;
    Double pickupLatitude;
    Double pickupLongitude;
    String reportTypeS;
    int ts;
    SharedPreferences prefs;
    String valueReport;
    TextView reportTitle, reportBody;
    String val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportconfirnmation);
        prefs = getSharedPreferences("Values", MODE_PRIVATE);
        valueReport = getIntent().getStringExtra("reportType");

        dispatcherID = prefs.getString("dispatcherID","no");
        reportTypeS= prefs.getString("reportType","no");
        pickupLatitude = Double.valueOf(prefs.getFloat("pickupLatitude",0.0f));
        pickupLongitude = Double.valueOf(prefs.getFloat("pickupLongitude",0.0f));
        if(reportTypeS.equals("medical")){

            val = "Ambulance";
        }
        else if(reportTypeS.equals("crime")){
            val = "Police";
        }
        else if(reportTypeS.equals("fire")){
            val = "Firefighter";
        }
        showConfirmationMessageBox();
       // showConfirmDialogeAnimated();

        /*

  //      dispatcherID = String.valueOf(getIntent().getExtras().getString("dispatcherID"));




        reportTitle = (TextView)findViewById(R.id.foundTeamTitle);
        reportBody = (TextView)findViewById(R.id.foundTeamBody);

        if(reportTypeS.equals("medical")){

            reportTitle.setText("Found Ambulance");
            reportBody.setText("We have found an Ambulance near your current Location. ");
        }
        else if(reportTypeS.equals("crime")){
            reportTitle.setText("Found Police");
            reportBody.setText("We have found an Police Team near your current Location ");
        }
        else if(reportTypeS.equals("fire")){
            reportTitle.setText("Found Firefighters");
            reportBody.setText("We have found an Firefighters Team near your current Location ");
        }



    //    distanceValue = (TextView)findViewById(R.id.distancevalue);
     //    ts =Integer.parseInt(getIntent().getExtras().getString("radius"));

        Toast.makeText(reportconfirnmation.this,""+dispatcherID,Toast.LENGTH_SHORT).show();
//        distanceValue.setText(String.valueOf("0"));
*/
    }

   /* public void openMap(View view) {
        String laat = String.valueOf(pickupLatitude);
        String loong = String.valueOf(pickupLongitude);

        Intent i = new Intent(reportconfirnmation.this, trackingDispatcher.class);
       /* i.putExtra("reportType",reportTypeS);
        i.putExtra("dispatcherID",dispatcherID);
        i.putExtra("pickupLatitude",laat);
        i.putExtra("pickupLongitude",loong);

        startActivity(i);


    }*/

    public void cancelMap() {

        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("Users");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        if (dispatcherID != null) {
            if (reportTypeS.equals("medical")) {
                driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("MedicalTeam").child(dispatcherID);
                ref = FirebaseDatabase.getInstance().getReference("MedicalRequest");
            } else if (reportTypeS.equals("crime")) {
                driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("PoliceTeam").child(dispatcherID);
                ref = FirebaseDatabase.getInstance().getReference("PoliceRequest");

            } else if (reportTypeS.equals("fire")) {
                driverRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("FirefighterTeam").child(dispatcherID);
                ref = FirebaseDatabase.getInstance().getReference("FireRequest");

            }

            driverRef.setValue(true);
            dispatcherID = null;

        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //   DatabaseReference ref = FirebaseDatabase.getInstance().getReference("MedicalRequest");

        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId, new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                Toast.makeText(reportconfirnmation.this, "Cancelled Request", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(reportconfirnmation.this, dashboard.class);
                startActivity(i);
                finish();
            }
        });


    }

     androidx.appcompat.app.AlertDialog.Builder dialog;

    private void showConfirmationMessageBox() {
        final SharedPreferences.Editor editor = getSharedPreferences("Values", MODE_PRIVATE).edit();

        try {


            dialog = new androidx.appcompat.app.AlertDialog.Builder(reportconfirnmation.this, R.style.AlertDialog);

            dialog.setTitle(""+val+" Found");
                    dialog.setMessage("Are you sure you want to call "+val);
            LayoutInflater inflater = LayoutInflater.from(reportconfirnmation.this);
            View trip_layout = inflater.inflate(R.layout.confirmoperation_layout, null);
            dialog.setView(trip_layout);

            dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                    editor.putBoolean("confirmReport",true);
                    Intent ii = new Intent(reportconfirnmation.this, trackingDispatcher.class);
                    editor.apply();
                    startActivity(ii);

                    finish();
                    // dialogInterface.dismiss();
                }
            });
            dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    cancelMap();
                    prefs.edit().clear().commit();
                    finish();

                }
            });

            dialog.setCancelable(false);

            dialog.show();
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();

        }


    }
    public void showConfirmDialogeAnimated(){

       SweetAlertDialog sw =new SweetAlertDialog(reportconfirnmation.this, SweetAlertDialog.WARNING_TYPE)
               .setTitleText(""+val+" Found")
                .setContentText("Are you sure you want to call "+val)
                .setConfirmText("Yes!")

                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        Intent ii = new Intent(reportconfirnmation.this, trackingDispatcher.class);
                        startActivity(ii);
                        finish();
                    }
                })
                .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        cancelMap();
                        prefs.edit().clear().commit();
                        sDialog.dismissWithAnimation();
                        Intent ii = new Intent(reportconfirnmation.this, accident.class);
                        startActivity(ii);
                        finish();
                    }
                });
                sw.setCanceledOnTouchOutside(false);
                sw.show();


    }
}