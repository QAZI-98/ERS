package com.rapidbox.emergencyresponseapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class fuzzyDisplay extends AppCompatActivity {



    LatLng curlatlng;
    Double radius;
    int accidents;
    int hazards;
    int other;
    int crimes;
    int upperlimit = 10;
    double micro_accident;
    double micro_hazard;
    double micro_crime;
    double micro_other;
    int no_of_parameters = 4;
    double result;
    ProgressBar pb1;
    TextView et0, et1, et2, et3, et4, et5, et6, et7,et8;
    DatabaseReference dbr;
    ValueEventListener valueEventListener;
    SimpleDateFormat sdf;
    long diffindays;
    Date firstDate;
    Date secondDate;
    int temp;
    int value;
    BarChart barChart;
    EditText search_txt;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuzzy_display);
        et0 = findViewById(R.id.et0m3a);
        et1 = findViewById(R.id.et1m3a);
        et2 = findViewById(R.id.index);
      //  et3 = findViewById(R.id.et3m3a);
       // et4 = findViewById(R.id.et4m3a);
       // et5 = findViewById(R.id.et5m3a);
       // et7 = findViewById(R.id.et6m3a);
        et6 = findViewById(R.id.loc);
        et8=findViewById(R.id.total);
        pb1 = findViewById(R.id.pbm3a);
         barChart = (BarChart)findViewById(R.id.bar_chart);
         barChart.setDrawBarShadow(false);
         barChart.setDrawValueAboveBar(true);
         barChart.setMaxVisibleValueCount(50);
         barChart.setPinchZoom(false);
         barChart.setDrawGridBackground(true);
         barChart.setBackgroundColor(Color.rgb(48, 48, 48));
         barChart.setGridBackgroundColor(Color.rgb(48, 48, 48));
        barChart.getAxisLeft().setTextColor(Color.rgb(197, 197, 197));
        barChart.getXAxis().setTextColor(Color.rgb(197, 197, 197));
        barChart.getLegend().setTextColor(Color.rgb(197, 197, 197));
        barChart.setDrawValueAboveBar(true);
        //barChart.color
        dbr= FirebaseDatabase.getInstance().getReference("incident");

        btn = findViewById(R.id.search_btn);
        FusedLocationProviderClient cli = LocationServices.getFusedLocationProviderClient(this);
        search_txt = (EditText)findViewById(R.id.search_txt);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                String location_to_search = search_txt.getText().toString();
                if (location_to_search.trim().isEmpty())
                    Toast.makeText(fuzzyDisplay.this, "enter area!", Toast.LENGTH_SHORT).show();
                else
                    search(location_to_search);

            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        cli.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {

                double lat = location.getLatitude();
                double lon = location.getLongitude();
                curlatlng = new LatLng(lat, lon);


             //   Geocoder geocoder;
                String fullAddress = reverseGeocoding(curlatlng);
           //     List<Address> addresses;
               // geocoder = new Geocoder(fuzzyDisplay.this, Locale.getDefault());

                try {

                 //   addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    Log.i("aa","3hehehe");
                    et6.setText(fullAddress);
                    Log.i("aa","3hehehe");

                } catch (Exception e) {
                    Log.i("my teag2", "");

                }
                Log.i("aa","hehehe");
                get(8);
                Log.i("aa","2hehehe");


            }
        });


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
            Log.i(Double.toString(address.getLatitude()),Double.toString(address.getLongitude()));
            setter(address.getLatitude(),address.getLongitude());
            et6.setText(location_to_search);
            Toast.makeText(this, "Found!", Toast.LENGTH_SHORT).show();
            get(8);
        }
        else
            Toast.makeText(this, "No results found!", Toast.LENGTH_SHORT).show();

    }
    public String setter(double lat,double lon)
    {
        curlatlng = new LatLng(lat, lon);
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(fuzzyDisplay.this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            get(8);
            return  addresses.get(0).getThoroughfare() + " " + addresses.get(0).getLocality() + " " + addresses.get(0).getCountryName();
        } catch (IOException e) {
            Toast.makeText(this, "something went wronge", Toast.LENGTH_SHORT).show();
        }
        return "";


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.duration_types, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.normal_map:
                get(2);
                return true;
            case R.id.hybrid_map:
                get(8);
                return true;
            case R.id.satellite_map:
                get(32);
                return true;
            case R.id.terrain_map:
                get(365);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public double get_micro_value(double variable) {
        double micro_var = (upperlimit - variable);
        variable = (variable == 0) ? 1 : variable;
        micro_var /= (10 * variable);
        return micro_var;
    }

    public double defuzify(double var_one, double var_two, double var_three,double var_four,double micro_noh, double micro_nof, double micro_noa,double micro_other) {
        var_one = (var_one == 0) ? 1 : var_one;
        var_two = (var_two == 0) ? 1 : var_two;
        var_three = (var_three == 0) ? 1 : var_three;
        var_four= (var_four == 0) ? 1 : var_four;
        var_one = (var_one > 10) ? 10 : var_one;
        var_two = (var_two > 10) ? 10 : var_two;
        var_three = (var_three > 10) ? 10 : var_three;
        var_four = (var_four > 10) ? 10 : var_four;


        double defuz = var_one * micro_noh;
        defuz += var_two * micro_nof;
        defuz += var_three * micro_noa;
        defuz += var_four * micro_other;
        defuz /= no_of_parameters;
        String safety_value = "";
        if (defuz == 1) {
            safety_value = "Totally Safe";
            et2.setTextColor(Color.rgb(0, 153, 153));
            pb1.getProgressDrawable().setColorFilter(Color.rgb(0, 153, 153), PorterDuff.Mode.SRC_IN);

        } else if (defuz >= 0.9) {
            safety_value = "Slighty Unsafe";
            et2.setTextColor(Color.rgb(255, 102, 0));
        //    pb1.setBackgroundColor(Color.rgb(255,102,0));
            pb1.getProgressDrawable().setColorFilter(Color.rgb(255,102,0), PorterDuff.Mode.SRC_IN);
        } else if (defuz >= 0.79) {
            safety_value = "Unsafe";
            et2.setTextColor(Color.rgb(255, 0, 0));
            pb1.getProgressDrawable().setColorFilter(Color.rgb(255,0,0), PorterDuff.Mode.SRC_IN);

        } else if (defuz > 0) {
            safety_value = "Extremely Unsafe";
            et2.setTextColor(Color.rgb(153, 0, 0));
            pb1.getProgressDrawable().setColorFilter(Color.rgb(153,0,0), PorterDuff.Mode.SRC_IN);

        } else {
            safety_value = "Totally Unsafe";
            et2.setTextColor(Color.rgb(128, 0, 0));
            pb1.getProgressDrawable().setColorFilter(Color.rgb(128,0,0), PorterDuff.Mode.SRC_IN);

        }
        et2.setText(safety_value);
        return defuz;
    }

    public void get(int days) {

        temp=days;
        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                accidents = 0;
                hazards = 0;
                crimes = 0;
                other = 0;
                sdf=new SimpleDateFormat("d/MM/yy");

                String date_toaday = sdf.format(new Date());
                try {
                    secondDate = sdf.parse(date_toaday);
                } catch (Exception e) {
                }
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    try {
                        firstDate = sdf.parse(ds.child("date").getValue(String.class));
                        diffindays = secondDate.getTime() - firstDate.getTime();
                        diffindays = diffindays / (24 * 60 * 60 * 1000);


                    if (diffindays<temp) {
                              // causing error might be due to ZERO
                        //   Toast.makeText(Main3Activity.this, "inside", Toast.LENGTH_SHORT).show();
                        double slat = Double.parseDouble(ds.child("latitude").getValue(String.class));
                        double slong = Double.parseDouble(ds.child("longitude").getValue(String.class));
                        LatLng incident_latlng = new LatLng(slat, slong);
                        radius = SphericalUtil.computeDistanceBetween(curlatlng, incident_latlng);
                        if (radius < 400) {
                            Log.i("a", "aaaa");
                            if (ds.child("subtype").getValue(String.class).equals("hazard"))
                                hazards++;
                            else if (ds.child("subtype").getValue(String.class).equals("crime"))
                                crimes++;
                            else if (ds.child("subtype").getValue(String.class).equals("accident"))
                                accidents++;
                            else
                                other++;
                        }
                        micro_accident = get_micro_value(accidents);
                        micro_hazard = get_micro_value(hazards);
                        micro_crime = get_micro_value(crimes);
                        micro_other = get_micro_value(other);
                        result = defuzify(accidents, hazards, crimes,other,micro_accident, micro_hazard, micro_crime,micro_other);
                        value = Double.valueOf(result * 100).intValue();
                        Log.i("fuzzy tag", micro_accident+value+"\n# of accident=" + Double.toString(accidents) + "\n# of crimes=" + Double.toString(crimes) + "\n# of hazards=" + Double.toString(hazards));
                        //   Toast.makeText(Main3Activity.this, "ahahaha", Toast.LENGTH_SHORT).show();
                        ArrayList<BarEntry> entries= new ArrayList<>();

                        entries.add(new BarEntry(0,accidents));
                        entries.add(new BarEntry(1,crimes));
                        entries.add(new BarEntry(2,hazards));
                        entries.add(new BarEntry(3,other));

                        BarDataSet barDataSet = new BarDataSet(entries,"Emergencies");

                        BarData data = new BarData(barDataSet);
                       // barChart.set;
                        data.setBarWidth(0.9f);
                        barChart.setData(data);
                        String[] emergencytypes = new String[]{"Accidents","Crimes","Hazards","Others"};
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(emergencytypes));
                        xAxis.setPosition(XAxis.XAxisPosition.TOP);
                        xAxis.setGranularity(1f);
                        xAxis.setDrawAxisLine(false);
                        xAxis.setDrawGridLines(false);

                                               barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        barChart.animateY(5000);
                        et1.setText(Integer.toString(value) + "%");
                        //et3.setText("Accidents:" + Integer.toString(accidents));
                        //et4.setText("Crimes:" + Integer.toString(crimes));
                        //et5.setText("Hazards:" + Integer.toString(hazards));
                       // et7.setText("Others:" + Integer.toString(other));
                        int total=accidents+hazards+crimes+other;
                        et8.setText("Total incidents: "+total);
                        pb1.setProgress(value);

                    }
                    } catch (Exception e) {
                        Toast.makeText(fuzzyDisplay.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                Log.i("fuzzy tag", "\n# of accident=" + Double.toString(accidents) + "\n# of crimes=" + Double.toString(crimes) + "\n# of hazards=" + Double.toString(hazards));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        dbr.addListenerForSingleValueEvent(valueEventListener);

        Log.i("a","aaaa");

    }
    public String reverseGeocoding(LatLng geoLatLng){
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





}

