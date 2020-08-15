package com.rapidbox.emergencyresponseapplication;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class tab1basicinfo extends Fragment {
    Button updateNumber;
    TextInputEditText usernameTxt;
    TextInputEditText useremailTxt;
    TextInputLayout phoneLayout;
    TextInputEditText userPhoneNo;

    FirebaseAuth firebaseAuth;
     DatabaseReference userDatabaseReference;
    FirebaseUser user;
    String fname;
    String lname;

    String getEmail;
    String phoneVar;
    RelativeLayout mSnackLayout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                final View rootview = inflater.inflate(R.layout.tab1,container,false);


      //  requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        //setProgressBarIndeterminateVisibility(true);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        usernameTxt = (TextInputEditText) rootview.findViewById(R.id.name);
        useremailTxt= (TextInputEditText) rootview.findViewById(R.id.email);
        userPhoneNo = (TextInputEditText) rootview.findViewById(R.id.phone);
        phoneLayout = (TextInputLayout) rootview.findViewById(R.id.PhoneLayout);
       // userPhoneNo.setFocusable(false);
        mSnackLayout = (RelativeLayout)rootview.findViewById(R.id.constraintLayout);
      //  userPhoneNo.setInputType(InputType.TYPE_NULL);
        updateNumber = (Button)rootview.findViewById(R.id.Update);

        updateNumber.setEnabled(false);
        userPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               updateNumber.setEnabled(true);


            }
        });
        String userID = user.getUid();



        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Victim").child(userID);
       // must be below otherwise app will crash
        getUserInformation();

               updateNumber.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       savaUserInformation();
                      // Toast.makeText(tab1basicinfo.super.getContext(), "Phone Updated!!", Toast.LENGTH_SHORT).show();
                       Snackbar snackbarx = Snackbar
                               .make(mSnackLayout, "Phone Updated Successfully", Snackbar.LENGTH_SHORT);

                       snackbarx.show();


                   }
               });
            return rootview;
        }

    private void savaUserInformation() {
        String phoneNo = userPhoneNo.getText().toString();
        Map userInfo = new HashMap();
        userInfo.put("phone",phoneNo);
        userDatabaseReference.updateChildren(userInfo);

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
                        getEmail = map.get("email").toString();
                        usernameTxt.setText(fname+" "+lname);
                        useremailTxt.setText(getEmail);

                    }
                    if(map.get("phone")!=null){

                        phoneVar = map.get("phone").toString();
                        userPhoneNo.setText(phoneVar);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}


