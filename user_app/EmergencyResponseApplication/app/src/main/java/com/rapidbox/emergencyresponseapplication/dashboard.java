package com.rapidbox.emergencyresponseapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class dashboard extends AppCompatActivity {

    boolean checkCrime=false;
    sql sss;
    boolean checkMedical = false;
    boolean checkFire=false;
    FirebaseAuth mAuth;
    SharedPreferences prefs;
    String dispatcherID;
    RelativeLayout rr,rr2,rr3,rr4,rr5,rr6;
    private FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    BottomNavigationView bottomNavigationView;
    Switch mEnableSwitch;
Button mPanic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        getSupportActionBar().setElevation(0);
        firebaseAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public  void  onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                if(user==null){
                    Intent intent = new Intent(dashboard.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }


        };
        mPanic = (Button)findViewById(R.id.img2);
        mEnableSwitch = (Switch)findViewById(R.id.switch_enable);
        mPanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accidentClick();
            }
        });
        mEnableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mPanic.setEnabled(true);
                }
                else{
                    mPanic.setEnabled(false);
                }

            }
        });
       // startService(new Intent(this, route_service.class));

      // FirebaseDatabase aAuth = FirebaseDatabase.getInstance().getInstance().getCurrentUser().getUid();
        //sss=new sql(this);
        //sss.fetch("create table routes(id INTEGER PRIMARY KEY AUTOINCREMENT,email TEXT,latitude TEXT,longitude TEXT)");
        rr= (RelativeLayout) findViewById(R.id.allow33d);
        rr2= (RelativeLayout) findViewById(R.id.allow33dd);
       // rr3= (RelativeLayout) findViewById(R.id.allow33dd);
        rr4= (RelativeLayout) findViewById(R.id.newss);
        rr5= (RelativeLayout) findViewById(R.id.routeNavigation);
        rr6= (RelativeLayout) findViewById(R.id.historyI);

        rr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




            Intent i = new Intent(dashboard.this,profile.class);
            startActivity(i);

            }
        });
        rr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this,notificationAlert.class);
                startActivity(i);
                           }
        });



        rr4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this,newsfeed.class);
                startActivity(i);
            }
        });
        rr5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(dashboard.this,navigation.class);
                startActivity(i);
            }
        });
        rr6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this,historyActivity.class);
                startActivity(i);
            }
        });


        prefs = getSharedPreferences("Values", MODE_PRIVATE);

        dispatcherID = prefs.getString("dispatcherID","no");


/*
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent i = new Intent(dashboard.this,MainActivity.class);
            startActivity(i);
            finish();
        } else {

            // No user is signed in
        }*/
       bottomNavigationView =(BottomNavigationView) findViewById(R.id.navbot);
        bottomNavigationView.setSelectedItemId(R.id.idHome);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
               switch (menuItem.getItemId()) {
                   case R.id.idHome:
                       Intent i = new Intent(dashboard.this, dashboard.class);
                       startActivity(i);
                       return true;
                   case R.id.idPost:
                       Intent iPost = new Intent(dashboard.this, addpost.class);
                        startActivity(iPost);

                       return true;
                   case R.id.idProfile:
                       Intent x = new Intent(dashboard.this, profile.class);
                       startActivity(x);
                        return true;


               }

               return false;
           }
       });
    }


    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
      //  super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu,menu);
       return true;


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                Intent i = new Intent(dashboard.this,profile.class);
                startActivity(i);
                return true;

            case R.id.item2:
                Intent x = new Intent(dashboard.this,howitworks.class);
                startActivity(x);

                return true;

            case R.id.item3:
                logout();
                    return true;

        }


        return super.onOptionsItemSelected(item);

    }


    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }


    public void logout(){
            signOut();
            Intent i = new Intent(dashboard.this,MainActivity.class);
            startActivity(i);
            finish();
    }


    public boolean previousMedicalReportFinalize() {
      String  userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference medicalref = FirebaseDatabase.getInstance().getReference("MedicalRequest").child(userId);

        //   String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        medicalref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    checkMedical = true;

                } else {

                    checkMedical = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    return checkMedical;
    }
    public boolean previousFireReportFinalize() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference fireref = FirebaseDatabase.getInstance().getReference("FireRequest").child(userId);

        //   String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fireref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    checkFire = true;

                } else {

                    checkFire = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return checkFire;
    }

    public boolean previousCrimeReportFinalize() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference fireref = FirebaseDatabase.getInstance().getReference("PoliceRequest").child(userId);

        //   String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fireref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    checkCrime = true;

                } else {

                    checkCrime = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return checkCrime;
    }


    public void signOut() {
        // [START auth_sign_out]
        FirebaseAuth.getInstance().signOut();
        firebaseAuth.addAuthStateListener(mAuthListener);


        // [END auth_sign_out]
    }
    public boolean checkDisID(){
        if(dispatcherID.equals("no")){

            return true;
        }else {
            return false;
        }


    }
    public void accidentClick()
    {


            Intent i = new Intent(dashboard.this, accident.class);
            startActivity(i);



    }
    public void profileshow(View view){
        Intent i = new Intent(dashboard.this,profile.class);
        startActivity(i);



    }
    public void showCrime(View view){
        Intent i = new Intent(dashboard.this,crimereport.class);
        startActivity(i);



    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(mAuthListener);
    }





    public void predictionShow(View view){
        Intent i = new Intent(dashboard.this,prediction.class);
        startActivity(i);


    }
    public void navigationShow(View view){
        Intent i = new Intent(dashboard.this,navigation.class);
        startActivity(i);


    }

}
