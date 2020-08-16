package com.rapidbox.emergencyresponseapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.maps.android.SphericalUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MapsActivityA extends FragmentActivity implements OnMapReadyCallback {



    private GoogleMap mMap;
    list list;
    private FusedLocationProviderClient client;
    LatLng currentlocation;
    LatLng startingLocation;
    LatLng destination;
    boolean startinglocationpickedup=false;
    boolean destinationset=false;
    int ii;
    private int count = 0;
//    Button b1;
    //EditText et1;
    String tag="mapactivity";
    String latitude;
    String longitude;
   // TextView tv2;
    boolean checkdistance;
    boolean check_if_location_already_saved;
    boolean check_if_reached;
    sql sss;
    FirebaseAuth firebaseAuth;
    String email;

    SearchView searchView;

//Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_activity);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        searchView = findViewById(R.id.search_location);
        firebaseAuth=FirebaseAuth.getInstance();
        email=firebaseAuth.getCurrentUser().getEmail();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String loc = searchView.getQuery().toString();
                search(loc);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
              //  String loc = searchView.getQuery().toString();
               // search(loc);
                return false;
            }
        });



   //  toolbar = findViewById(R.id.toolbartxt);
   //  toolbar.setTitle("Tracking Route");
//     toolbar.setTitleTextColor(Color.parseColor("#fffff"));
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setCancelable(false)
                    .setMessage("Please Enable Location Services!!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            MapsActivityA.this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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



        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(MapsActivityA.this);
            mapFragment.getMapAsync(this);
        }
        // mapFragment.getMapAsync(this);
        client = LocationServices.getFusedLocationProviderClient(this);
        sss=new sql(this);


    }

    public boolean get_distance_from_destination(LatLng current)
    {
        Double  distance = SphericalUtil.computeDistanceBetween(current,destination);
        if (distance<100)
        {
            Log.i(tag+"status","reached");
            Toast.makeText(this, "you have reached your destination", Toast.LENGTH_SHORT).show();
            //tv2.setText("you have reached your destination");
            Toast.makeText(this, "Reached!", Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            Log.i(tag+"status", "not reached");
        }
        return false;

    }
    private void request_location_permission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},1);
    }



    private void setMapLongClick(final GoogleMap map) {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                count++;
                if (count == 1) {
                    map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("your destination")
                            .icon(BitmapDescriptorFactory.defaultMarker
                                    (BitmapDescriptorFactory.HUE_BLUE)));
                    destination=new LatLng(latLng.latitude,latLng.longitude);
                    CircleOptions circleOptions = new CircleOptions();

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
                    destinationset=true;
                    Toast.makeText(MapsActivityA.this, "destination set Tracking in progress", Toast.LENGTH_SHORT).show();

                    //  tv2.setText("Tracking in progress");
                }
                else{
                    Toast.makeText(MapsActivityA.this, "Destination already set!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       // tv2 = findViewById(R.id.tv1);
        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));
        request_location_permission();
        Toast.makeText(MapsActivityA.this, "Tracking started press & hold to set destination", Toast.LENGTH_SHORT).show();

        // tv2.setText("Tracking started press & hold to set destination");

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(24.882198, 67.067150);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10F));
        try {
            mMap.setMyLocationEnabled(true);

        } catch (SecurityException e) {
            Toast.makeText(this, "turn on location permission", Toast.LENGTH_SHORT).show();
        }
        setMapLongClick(mMap);

       // b1 = findViewById(R.id.b1);
     //   et1 = findViewById(R.id.et1);

      /*  b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to hide keyboard after button click
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                String location_to_search = et1.getText().toString();
                if (location_to_search.trim().isEmpty())
                    Toast.makeText(MapsActivityA.this, "enter address!", Toast.LENGTH_SHORT).show();
                else
                    search(location_to_search);

            }
        });
*/

        if (ActivityCompat.checkSelfPermission(MapsActivityA.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        client.getLastLocation().addOnSuccessListener(MapsActivityA.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    startinglocationpickedup=true;
                    Toast.makeText(MapsActivityA.this, "Tracking stared", Toast.LENGTH_SHORT).show();
                    String latitude=Double.toString(location.getLatitude());
                    String longitude=Double.toString(location.getLongitude());
                    list = new list();
                    list.add(latitude,longitude);

                    insert(email,latitude,longitude);
                    startingLocation=new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(startingLocation).title("Starting Position")
                            .icon(BitmapDescriptorFactory.defaultMarker
                                    (BitmapDescriptorFactory.HUE_CYAN)));

                    Log.i(tag+"First listener", latitude+" "+longitude);
                    if (destinationset)
                        get_distance_from_destination(startingLocation);
                }
            }
        });


        ii=0;



        final Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!check_if_reached) {

                    if (ActivityCompat.checkSelfPermission(MapsActivityA.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    if (startinglocationpickedup) {
                        client.getLastLocation().addOnSuccessListener(MapsActivityA.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {

                                    latitude = Double.toString(location.getLatitude());
                                    longitude = Double.toString(location.getLongitude());
                                    Log.i(tag+" without check", latitude + " " + longitude +" check#"+ii);
                                    ii++;
                                    check_if_location_already_saved = list.search(latitude, longitude);
                                    if (!check_if_location_already_saved) {

                                        checkdistance=list.check_distance_betwen_points(location.getLatitude(),location.getLongitude());
                                        if (checkdistance){
                                            list.add(latitude, longitude);
                                            insert(email,latitude,longitude);

                                            currentlocation = new LatLng(location.getLatitude(), location.getLongitude());
                                            mMap.addMarker(new MarkerOptions().position(currentlocation).title("your route")
                                                    .icon(BitmapDescriptorFactory.defaultMarker
                                                            (BitmapDescriptorFactory.HUE_AZURE)));
                                            Log.i(tag + " flag", latitude + " " + longitude);

                                            if (destinationset) {
                                                check_if_reached = get_distance_from_destination(currentlocation);

                                            }
                                        }

                                    }

                                }

                            }

                        });

                    }

                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.i(tag+"mtag","arrived");
            }
        });
        t1.start();

    }
    public void insert(String email,String latitude,String longitude)
    {
        boolean flag=sss.insert(email,latitude,longitude);
        if (flag)
            Log.i("tag","inserted");
        else
            Log.i("tag","not inserted");

    }



    public void search(String tosearch) {

        List<Address> addresslist = null;
        Geocoder geocoder = new Geocoder(this);
        try {
            addresslist = geocoder.getFromLocationName(tosearch, 1);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Address address = null;
        if (addresslist != null) {
            if (addresslist.size() != 0) {
                try {
                    address = addresslist.get(0);
                    LatLng search_result = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(search_result, 10f));
                    mMap.addMarker(new MarkerOptions()
                            .position(search_result)
                            .title("search result:" + tosearch).icon(BitmapDescriptorFactory.defaultMarker
                                    (BitmapDescriptorFactory.HUE_MAGENTA))
                    );
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(this, "no result found", Toast.LENGTH_SHORT).show();
            }

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_types, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Change the map type based on the user's selection.
        switch (item.getItemId()) {
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
