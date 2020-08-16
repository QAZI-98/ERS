package com.rapidbox.emergencyresponseapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.rapidbox.emergencyresponseapplication.ui.dates_list;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Linear_regression extends AppCompatActivity {


    LatLng current_latlong,incident_latlong;
    public FusedLocationProviderClient cli;

    EditText search_txt;
    Button search_btn;
    TextView result,desc,address_txt;;
    dates_list dlist;
    regression_list _list;
    DatabaseReference databaseReference;
    ProgressBar pbar;

    ValueEventListener valueEventListener;

    int no_of_accidents=0,
            no_of_crimes=0,
            no_of_hazards=0,
            no_of_others=0,
            count=0,
            totalcount=0,
            input,diff,prediction=0;

    long tomorrows_date=0,difference;

    String date,subtype,location_to_search;

    double incident_lat,
            incident_long,
            distance;

    Boolean location_fected,data_fetched;

    SimpleDateFormat sdf;

    Date first_incident_date,
            incident_date,
            todays_date;
    ScatterChart barChart;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.prediction_options, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.normal_map:
                result.setText("");
                desc.setText("");
                fetch(Integer.parseInt(Long.toString(tomorrows_date)), "incidents predicted for\ntomorrow",1);
                return true;
            case R.id.hybrid_map:
                result.setText("");
                desc.setText("");
                fetch(Integer.parseInt(Long.toString(tomorrows_date)), "incidents predicted for\nthe next week",7);
                return true;
            case R.id.satellite_map:
                result.setText("");
                desc.setText("");
                fetch(Integer.parseInt(Long.toString(tomorrows_date)), "incidents predicted for\nthe next month",30);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_regression);

        cli = LocationServices.getFusedLocationProviderClient(this);
        search_txt = findViewById(R.id.search_txt);
        search_btn = findViewById(R.id.search_btn);
        result = findViewById(R.id.number_of);
        desc = findViewById(R.id.description_text);
        address_txt = findViewById(R.id.loc);
        pbar=findViewById(R.id.progressBar1);


        barChart = findViewById(R.id.bar_chart);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        sdf = new SimpleDateFormat("d/MM/yy");


        try {
            first_incident_date = sdf.parse("9/06/20");
            todays_date = sdf.parse(sdf.format(new Date()));
            tomorrows_date = todays_date.getTime() - first_incident_date.getTime();
            tomorrows_date = tomorrows_date / (24 * 60 * 60 * 1000);
            tomorrows_date++;

        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        cli.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {

                if (location != null) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    address_txt.setText(setter(lat, lon));
                    // Toast.makeText(Main5Activity.this, "date "+Long.toString(tomorrows_date), Toast.LENGTH_SHORT).show();


                    fetch(Integer.parseInt(Long.toString(tomorrows_date)), "incidents predicted for\ntomorrow",1);


                }
            }
        });

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                location_to_search = search_txt.getText().toString();
                if (location_to_search.trim().isEmpty())
                    Toast.makeText(Linear_regression.this, "enter area!", Toast.LENGTH_SHORT).show();
                else
                    search(location_to_search);
            }
        });




    }

    public String setter(double lat,double lon)
    {
        current_latlong = new LatLng(lat, lon);
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(Linear_regression.this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return  addresses.get(0).getAddressLine(0) ;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";


    }


    private void search(String location_to_search) {
        List<Address> addressList=new ArrayList<>();
        Geocoder geocoder=new Geocoder(this);
        Address address;
        try {
            addressList= geocoder.getFromLocationName(location_to_search,1);
        } catch (IOException e) {
            Toast.makeText(this, "Could not fetch any results", Toast.LENGTH_SHORT).show();
        }
        if (addressList!=null && addressList.size()>0)
        {
            address=addressList.get(0);
            setter(address.getLatitude(),address.getLongitude());
            address_txt.setText(location_to_search);
            desc.setText("");
            result.setText("");
            fetch(Integer.parseInt(Long.toString(tomorrows_date)), "incidents predicted for\ntomorrow",1);

        }
        else
            Toast.makeText(this, "No results found!", Toast.LENGTH_SHORT).show();

    }

    public void fetch(final int inp, final String sent, final int no_of_days_to_predit){

        input=inp;
        dlist=new dates_list();
        _list=new regression_list();
        databaseReference= FirebaseDatabase.getInstance().getReference("incident");
        valueEventListener=new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    //todo remove nested loop (2 consec loops)

                    date = ds.child("date").getValue(String.class);
                    if (dlist.insert(date)) {
                        for (DataSnapshot ds1 : dataSnapshot.getChildren()) {

                            subtype = ds1.child("subtype").getValue(String.class);
                            if (ds1.child("date").getValue(String.class).equals(date)) {

                                incident_lat = Double.parseDouble(ds1.child("latitude").getValue(String.class));
                                incident_long = Double.parseDouble(ds1.child("longitude").getValue(String.class));
                                incident_latlong = new LatLng(incident_lat, incident_long);
                                distance = SphericalUtil.computeDistanceBetween(current_latlong, incident_latlong);


                                if (distance < 501) {
                                    try {
                                        incident_date = sdf.parse(date);
                                    }
                                    catch (ParseException pe) {

                                    }
                                    difference = incident_date.getTime() - first_incident_date.getTime();
                                    difference=difference/(24*60*60*1000);
                                    count++;

                                }
                            }
                        }
                        if (count!=0) {
                            totalcount++;
                            diff=Integer.parseInt(Long.toString(difference));
                            _list.insert(diff, count);
                        }
                        count = 0;

                    }

                }
                Log.e("count",String.valueOf(totalcount));
                if (totalcount>10) {

                    data_fetched=true;
                    _list.count();
                    for (int i=0;i<no_of_days_to_predit;i++) {
                        prediction += _list.get_mean(input + i);
                        Log.i("predicted",String.valueOf(prediction));
                    }
                    result.setText(String.valueOf(prediction));
                    desc.setText(sent);
                    prediction=0;


                }

                else{
                    Toast.makeText(Linear_regression.this, "Not enough data to predict", Toast.LENGTH_SHORT).show();
                }
                totalcount=0;

                ArrayList entries= _list.traverse();


                ScatterDataSet scatterDataSet = new ScatterDataSet(entries, "emergency");
                ScatterData scatterData = new ScatterData(scatterDataSet);

                barChart.setData(scatterData);
                scatterDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                scatterDataSet.setValueTextColor(Color.BLACK);
                scatterDataSet.setValueTextSize(8f);
                String[] emergencytypes = new String[]{"Accidents","Crimes","Hazards","Others"};
                XAxis xAxis = barChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.TOP);
                xAxis.setGranularity(1f);
                xAxis.setDrawAxisLine(false);
                xAxis.setDrawGridLines(false);
                barChart.animateY(2000);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
        databaseReference.addListenerForSingleValueEvent(valueEventListener);




    }





}
