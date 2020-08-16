package com.rapidbox.emergencyresponseapplication;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.load.engine.Resource;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class tab3emergencycontacts  extends Fragment implements dialog_contactinformation.dialog_contactinformation_interface{

    TextInputEditText usernameTxt;
    TextInputEditText userPhoneTxt;


    String fname;
    String lname;
    String phoneVar;

    ConstraintLayout mSnackLayout;
    LinearLayout mShowLinearLayout;
    TextView mShowName,mShowPhone,mShowNothing,mShowPhoneNothing;
//   public DatabaseReference userDatabaseReference;
DatabaseReference userDatabaseReference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.tab3,container,false);



        mShowLinearLayout = (LinearLayout)rootview.findViewById(R.id.showUserDialog);
        mShowName  = (TextView)rootview.findViewById(R.id.showPersonNameID);
        mShowPhone  = (TextView)rootview.findViewById(R.id.showPersonNumberID);
        mShowNothing = (TextView)rootview.findViewById(R.id.nothingSelectedID);
        mShowPhoneNothing = (TextView)rootview.findViewById(R.id.showNumberNothingID);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Victim").child(userID);
        getUserInformation();

        mSnackLayout = (ConstraintLayout)rootview.findViewById(R.id.constraintLayout);
        mShowLinearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            dialog_contactinformation dc = new dialog_contactinformation();
            dc.show(getFragmentManager(),"example dialog");
               dc.setDialogResult(new dialog_contactinformation.dialog_contactinformation_interface() {
                   @Override
                   public void applyTexts(String pname, String pphone) {
                      // Toast.makeText(tab3emergencycontacts.super.getContext(),"Updated! ",Toast.LENGTH_LONG).show();
                       Snackbar snackbarx = Snackbar
                               .make(mSnackLayout, "Contact Person Phone Successfully Added", Snackbar.LENGTH_SHORT);

                       snackbarx.show();
                       String x = pname;
                       String y = pphone;


                       mShowName.setText(x.trim());
                       mShowPhone.setText(y);
                        mShowNothing.setText("Name");
                       mShowNothing.setTextColor(ContextCompat.getColor(tab3emergencycontacts.super.getContext(), R.color.finalColor));

                       mShowPhoneNothing.setText("Phone");
                       savaUserInformation(x,y);

                   }
               });
            }
        });




        /*FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        usernameTxt = (TextInputEditText) rootview.findViewById(R.id.name);
        userPhoneTxt = (TextInputEditText) rootview.findViewById(R.id.phone);

        updateBtn = (Button)rootview.findViewById(R.id.Update);
        String userID = user.getUid();
       userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Victim").child(userID);
        getUserInformation();
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoad = new ProgressDialog(tab3emergencycontacts.super.getContext(),R.style.AppCompatAlertDialogStyle);
                showLoad.setMessage("Wait...");
                showLoad.show();
                savaUserInformation();
                showLoad.hide();
                Toast.makeText(tab3emergencycontacts.super.getContext(), "Emergency Contact Updated!!", Toast.LENGTH_SHORT).show();
            }
        });*/
        return rootview;
    }
    private void savaUserInformation(String x,String y) {
        //String userName = mShowName.getText().toString();
        //String phoneNo = mShowPhone.getText().toString();

        Map userInfo = new HashMap();
       userInfo.put("emergencyContactname",x);
        userInfo.put("emergencyContactPhone",y);
        userDatabaseReference.updateChildren(userInfo);

    }
    private void getUserInformation() {
        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()&&dataSnapshot.getChildrenCount()>1)
                {
                    Map<String,Object> map = (Map<String, Object>)dataSnapshot.getValue();
                    if(map.get("emergencyContactname")!=null){
                        fname = map.get("emergencyContactname").toString();
                        mShowName.setText(fname);

                    }
                    else{
                        mShowName.setText("");
                        mShowPhone.setText("");
                        mShowPhoneNothing.setText("");

                        mShowNothing.setText("No Emergency Contact");
                        mShowNothing.setTextColor(ContextCompat.getColor(tab3emergencycontacts.super.getContext(), R.color.themecolor));
                    }
                    if(map.get("emergencyContactPhone")!=null){

                        phoneVar = map.get("emergencyContactPhone").toString();
                        mShowPhone.setText(phoneVar);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public void applyTexts(String pname, String pphone) {
  //      String x = pname;
    //    String y = pphone;
      //  Toast.makeText(tab3emergencycontacts.super.getContext()," "+x+" "+y+"neechy wala",Toast.LENGTH_LONG).show();

    }
}