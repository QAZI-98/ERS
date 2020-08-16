package com.rapidbox.emergencyresponseapplication;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.IntentFilter;
        import android.content.IntentSender;
        import android.database.Cursor;
        import android.location.LocationManager;
        import android.os.Bundle;
        import android.content.Intent;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.WindowManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.constraintlayout.widget.ConstraintLayout;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.bumptech.glide.Glide;
        import com.firebase.ui.database.FirebaseRecyclerAdapter;

        import com.firebase.ui.database.FirebaseRecyclerOptions;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.common.api.PendingResult;
        import com.google.android.gms.common.api.ResultCallback;
        import com.google.android.gms.common.api.Status;
        import com.google.android.gms.location.LocationRequest;
        import com.google.android.gms.location.LocationServices;
        import com.google.android.gms.location.LocationSettingsRequest;
        import com.google.android.gms.location.LocationSettingsResult;
        import com.google.android.gms.location.LocationSettingsStatusCodes;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.material.floatingactionbutton.FloatingActionButton;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DatabaseReference;

        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.Query;
        import com.google.maps.android.SphericalUtil;


public class newsfeed extends AppCompatActivity {


    private EditText et1;
    private Button button2;
    private FloatingActionButton fbtn;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private  DatabaseReference fb_incident_reference;
    private FirebaseAuth  auth;
    locationReceiver mLocationReceiver;
    sql sql_query;
    String temp_uri;
    String temp_desc;

    String name;
    LocationManager lm;
    protected static final String TAG = "newsfeed";

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    @Override
    protected void onStart() {
        super.onStart();
        //adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        // adapter.stopListening();
    }
    @Override
    protected void onResume() {
        super.onResume();
        displayLocationSettingsRequest(this);
    }

    public boolean route(String slat,String slong)
    {
        Cursor cursor = sql_query.fetch("select latitude,longitude from routes where email= '"+auth.getCurrentUser().getEmail()+"'");
        boolean check=false;
        if (cursor.getCount() != 0 && !(check)) {
            while (cursor.moveToNext() && !(check)) {
                String lat = cursor.getString(0);
                String longe = cursor.getString(1);

                LatLng current=new LatLng(Double.parseDouble(lat),Double.parseDouble(longe));
                LatLng destination=new LatLng(Double.parseDouble(slat),Double.parseDouble(slong));

                Double  distance = SphericalUtil.computeDistanceBetween(current,destination);
                if (distance<100) {

                    //////////////add code from here
                    /////till here
                    check = true;
                    return true;


                }
            }

        }
        return false;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);
        // for hiding keyboard when load
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

       // checkLocationEnabledreceiver();
//        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        checkLocationEnabledreceiver();
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        sql_query=new sql(newsfeed.this);
        auth =FirebaseAuth.getInstance();
        fb_incident_reference=FirebaseDatabase
                .getInstance().getReference("incident");

        et1=findViewById(R.id.searchtext);
        fbtn = findViewById(R.id.addpost_fbtn);
        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(newsfeed.this,addpost.class);
                //change here
                finish();
                startActivity(i);
            }
        });
        button2=findViewById(R.id.b1);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(et1.getText().toString());
                linearLayoutManager.setReverseLayout(true);
                linearLayoutManager.setStackFromEnd(true);
            }
        });



        recyclerView = findViewById(R.id.list);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        fetch();

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
                            Intent i = new Intent(newsfeed.this, dashboard.class);
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

    private void search(String text)
    {
        if (text.trim().isEmpty()) {
            fetch();
            Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
        }
        else {
            Query firebaseSearchQuery = fb_incident_reference.orderByChild("type").startAt(text).endAt(text + "\uf8ff");
            FirebaseRecyclerOptions<incident> options;
            options = new FirebaseRecyclerOptions.Builder<incident>().setQuery(firebaseSearchQuery, incident.class).build();
            adapter = new FirebaseRecyclerAdapter<incident, ViewHolder>(options) {
                @NonNull
                @Override
                public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.list_item, parent, false);

                    return new ViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final incident model) {
                    String title=model.gettype();
                    holder.setTxtTitle(title);
                    holder.setTxtDesc(model.getTime()+" "+model.getDate());
                    holder.settxtname(model.getname());
                    holder.setimguri(model.getimguri());
                    boolean rcheck=route(model.getlatitude(),model.getlongitude());
                    if (rcheck)
                        holder.settxtstatus("near your route!");

                    holder.root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            temp_uri=model.getimguri();
                            temp_desc=model.gettext();

                            AlertDialog.Builder alertadd = new AlertDialog.Builder(newsfeed.this);
                            LayoutInflater factory = LayoutInflater.from(newsfeed.this);
                            final View imageview = factory.inflate(R.layout.image_zoom, null);
                            ImageView iv= imageview.findViewById(R.id.enlarge);
                            TextView tv=imageview.findViewById(R.id.description);
                            Glide.with(newsfeed.this).load(temp_uri).into(iv);
                            alertadd.setView(imageview);
                            tv.setText(temp_desc);

                            alertadd.setNeutralButton("Close!", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dlg, int sumthin) {

                                }
                            });

                            alertadd.show();

                        }
                    });

                }

            };
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter.startListening();
            //set adapter to firebase recycler view
            recyclerView.setAdapter(adapter);
        }
    }



    private void fetch() {
        Query query = fb_incident_reference;

        final FirebaseRecyclerOptions<incident> options= new FirebaseRecyclerOptions
                .Builder<incident>()
                .setQuery(query, incident.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<incident, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item, parent, false);

                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(ViewHolder holder, final int position, final incident model) {

                String title=model.gettype();
                holder.setTxtTitle(title);
                holder.setTxtDesc(model.getTime()+" "+model.getDate());
                holder.settxtname(model.getname());
                holder.setimguri(model.getimguri());

                boolean rcheck=route(model.getlatitude(),model.getlongitude());
                if (rcheck)
                {holder.settxtstatus("near your route!");}
                else{
                    holder.settxtstatus("");

                }



                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        temp_uri=model.getimguri();
                        temp_desc=model.gettext();

                        AlertDialog.Builder alertadd = new AlertDialog.Builder(newsfeed.this);
                        LayoutInflater factory = LayoutInflater.from(newsfeed.this);
                        final View imageview = factory.inflate(R.layout.image_zoom, null);
                        ImageView iv= imageview.findViewById(R.id.enlarge);
                        TextView tv=imageview.findViewById(R.id.description);
                        Glide.with(newsfeed.this).load(temp_uri).into(iv);
                        alertadd.setView(imageview);
                        tv.setText(temp_desc);

                        alertadd.setNeutralButton("Close!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int sumthin) {

                            }
                        });

                        alertadd.show();

                    }
                });
            }

        };
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public TextView txtTitle;
        public TextView txtDesc;
        public TextView txtname;
        public ImageView imageview;
        public TextView state;




        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.list_root);
            txtTitle = itemView.findViewById(R.id.list_title);
            txtDesc = itemView.findViewById(R.id.list_desc);
            txtname=itemView.findViewById(R.id.list_name);
            imageview=itemView.findViewById(R.id.iv2);
            state=itemView.findViewById(R.id.status);
        }



        public void setTxtTitle(String string) {
            txtTitle.setText(string);
        }
        public void setTxtDesc(String string) {
            txtDesc.setText(string);
        }
        public void settxtname(String string) {txtname.setText(string);}
        public void setimguri(String uri) { Glide.with(newsfeed.this).load(uri).into(imageview);}

        public void settxtstatus(String string) {
            state.setVisibility(View.VISIBLE);

            state.setText(string);}

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
                            status.startResolutionForResult(newsfeed.this, REQUEST_CHECK_SETTINGS);
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
    public void checkLocationEnabledreceiver(){
        registerReceiver(mLocationReceiver= new locationReceiver(new LocationCallBack() {
            @Override
            public void onLocationTriggered() {
                //Location state changed
                //           buildAlertMessageNoGps();
                displayLocationSettingsRequest(newsfeed.this);
//                }//
            }
        }),new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));


    }



}