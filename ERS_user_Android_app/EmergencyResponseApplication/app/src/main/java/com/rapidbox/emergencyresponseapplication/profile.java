package com.rapidbox.emergencyresponseapplication;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.TextView;

import com.rapidbox.emergencyresponseapplication.ui.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class profile extends AppCompatActivity {
    DatabaseReference userDatabaseReference;
    FirebaseUser user;
    String  fname;
    String lname;
    TextView userName;
   // ProgressDialog showLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
      //  showLoad = new ProgressDialog(profile.this,R.style.AppCompatAlertDialogStyle);
//        showLoad.setMessage("Wait...");
  //      showLoad.show();
       // showLoad.setCanceledOnTouch Outside(false);

        final ProgressDialog dialog = ProgressDialog.show(profile.this, "Please  wait..", "Loading data..", true);
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                userName = (TextView)findViewById(R.id.usernametext);

                TabLayout tabs = findViewById(R.id.tabs);

                String userID = user.getUid();
                userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Victim").child(userID);

                getUserInformation();
                SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(profile.this, getSupportFragmentManager());
                ViewPager viewPager = findViewById(R.id.view_pager);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

                viewPager.setAdapter(sectionsPagerAdapter);

                tabs.setupWithViewPager(viewPager);
                dialog.dismiss();



            }},4000);


    }
    private void getUserInformation() {
        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()&&dataSnapshot.getChildrenCount()>1)
                {
                    Map<String,Object> map = (Map<String, Object>)dataSnapshot.getValue();
                    if(map.get("firstname")!=null && map.get("lastname")!=null){
                        fname = map.get("firstname").toString();
                        lname = map.get("lastname").toString();
                        String fullName = fname+" "+lname;
                        userName.setText(fullName);


                    }

                }
               //
                //
                // showLoad.hide();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}