package com.rapidbox.emergencyresponseapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
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

public class tab2medicalinfo  extends Fragment implements dialog_allergy.dialog_allergy_interface,dialog_medical.dialog_medical_interface {
    Spinner spinner,allergiesSpinner,medicalConditionSpinner;
    String[] options = {"Select Options","AB+","AB-","B+","B-","A+","A-","O+","O-"};
  String[] allergiesOption= {"Select Options","Drug Allergy","Food Allergy","Skin Allergy","Dust Allergy"};
    String[] conditionOption= {"Select Options","Alzeimers","Cancer","Cholesterol","Cold&Flu","Depression","Diabetes","Pregnant"};

    //  String[] allergies = {"Select Options","Drug Allergy","Food Allergy","B+","B-","A+","A-","O+","O-"};

    String selectedSpinner;
    String selectAllergy;
    String selectedCondition;
    Button updateBtn;
    //
    DatabaseReference userDatabaseReference;
LinearLayout mAllergy;
    LinearLayout mMedical;
    LinearLayout mBlood;
    TextView mBloodTxt,mAllergyTxt,mCondition;
    TextView mSelectedBloodTxt,mSelectedCondition,mSelectedAllergy;
    ConstraintLayout mSnackLayout;


    @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.tab2,container,false);
        mAllergy = (LinearLayout) rootview.findViewById(R.id.linearLayout2);
        mMedical = (LinearLayout) rootview.findViewById(R.id.linear_medical);
        mBlood = (LinearLayout) rootview.findViewById(R.id.linearLayout);
        mBloodTxt = (TextView)rootview.findViewById(R.id.showBloodType);
        mAllergyTxt = (TextView)rootview.findViewById(R.id.showAllergyType);
        mCondition = (TextView)rootview.findViewById(R.id.showMedicalType);
        mSelectedBloodTxt = (TextView)rootview.findViewById(R.id.selectedBlood);
        mSelectedCondition = (TextView)rootview.findViewById(R.id.selectedCondition);
        mSelectedAllergy = (TextView)rootview.findViewById(R.id.selectedAllergy);
        mSnackLayout = (ConstraintLayout)rootview.findViewById(R.id.constraintLayout);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Victim").child(userID);
        getUserInformation();

      /*  if(mBloodTxt.equals("")){
            mSelectedBloodTxt.setText("Please Specify Blood Type");

        }*/
        final Map userInfo = new HashMap();

        mBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mBloodTxt.getText().toString().isEmpty()){
                dialog_blood dm = new dialog_blood();
                dm.show(getFragmentManager(), "example dialog");
                dm.setDialogResult(new dialog_blood.dialog_blood_interface() {
                    @Override
                    public void applyTexts(List<String> mSelectedItems) {
                       // Toast.makeText(tab2medicalinfo.super.getContext(), "Updated!", Toast.LENGTH_SHORT).show();
                        Snackbar snackbarx = Snackbar
                                .make(mSnackLayout, "Blood Group has been successfully Updated", Snackbar.LENGTH_SHORT);

                        snackbarx.show();

                        userInfo.put("bloodType",mSelectedItems.toString());
                        userDatabaseReference.updateChildren(userInfo);
                        String bloodValue="";
                        for(String x : mSelectedItems){
                            bloodValue += x+""+mSelectedItems.toString();
                        }
                        if(mBloodTxt.getText().toString() == bloodValue){
                            //Toast.makeText(tab2medicalinfo.super.getContext(), "Matched!!!" , Toast.LENGTH_SHORT).show();

                            mBloodTxt.setText(bloodValue);

                        }

                    }
                });
                }
                else{
                   String selectedBlood = mBloodTxt.getText().toString();
                    final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(tab2medicalinfo.super.getContext(),R.style.AlertDialog);

                    dialog.setTitle("ALERT");
                    dialog.setCancelable(false);
                    dialog.setMessage("Your Blood Group is "+selectedBlood);

                    LayoutInflater inflater = LayoutInflater.from(tab2medicalinfo.super.getContext());
                    dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //
                        }
                    });


                    dialog.show();

                }
            }
        });

        mMedical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_medical dm = new dialog_medical();
                dm.show(getFragmentManager(), "example dialog");
                dm.setDialogResult(new dialog_medical.dialog_medical_interface() {
                    @Override
                    public void applyTexts(List<String> mSelectedItems) {
                       // Toast.makeText(tab2medicalinfo.super.getContext(), "Updated!", Toast.LENGTH_SHORT).show();

                        Snackbar snackbarx = Snackbar
                                .make(mSnackLayout, "Medical Condition has been successfully Updated", Snackbar.LENGTH_SHORT);

                        snackbarx.show();

                        userInfo.put("medicalCondition",mSelectedItems.toString());
                        userDatabaseReference.updateChildren(userInfo);

                        String medicalValue="";
                        for(String x : mSelectedItems){
                            medicalValue += x+""+mSelectedItems.toString();
                        }
                        if(mCondition.getText().toString() == medicalValue) {

                            mCondition.setText(medicalValue);
                        }
                    }
                });
            }
        });
        mAllergy.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        dialog_allergy da = new dialog_allergy();
        da.show(getFragmentManager(),"example dialog");
        da.setDialogResult(new dialog_allergy.dialog_allergy_interface() {
            @Override
            public void applyTexts(List<String> mSelectedItems) {
             //   Toast.makeText(tab2medicalinfo.super.getContext(), "Updated", Toast.LENGTH_SHORT).show();
                Snackbar snackbarx = Snackbar
                        .make(mSnackLayout, "Allergy has been successfully Updated", Snackbar.LENGTH_SHORT);

                snackbarx.show();
                userInfo.put("allergy",mSelectedItems.toString());
                userDatabaseReference.updateChildren(userInfo);
                String allergyValue="";
                for(String x : mSelectedItems){
                    allergyValue += x+""+mSelectedItems.toString();
                }
                if(mAllergyTxt.getText().toString() == allergyValue) {

                    mAllergyTxt.setText(allergyValue);
                }

            }
        });
             /*
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_allergy,null);
        CheckBox mCheckBox = view.findViewById(R.id.checkbox_drug);

        builder.setView(view)
                .setTitle("")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
        AlertDialog mDialog = builder.create();
        mDialog.show();
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               /* boolean checked = (buttonView).isChecked();

                switch(buttonView.getId()) {
                    case R.id.checkbox_drug:
                        if (checked){
                            Toast.makeText(tab2medicalinfo.super.getContext(), "drug Checked!!!!", Toast.LENGTH_SHORT).show();

                        }

                        else
                        {
                            Toast.makeText(tab2medicalinfo.super.getContext(), "drug Unchecked!!", Toast.LENGTH_SHORT).show();

                        }
                        // Remove the meat

                    case R.id.checkbox_food:
                        if (checked){   Toast.makeText(tab2medicalinfo.super.getContext(), "food allergy checked!!", Toast.LENGTH_SHORT).show();
                        }
                        // Cheese me
                        else{   Toast.makeText(tab2medicalinfo.super.getContext(), "Unchecked!!", Toast.LENGTH_SHORT).show();}
                    case R.id.checkbox_skin:
                        if (checked){   Toast.makeText(tab2medicalinfo.super.getContext(), "skin allergy checked!!", Toast.LENGTH_SHORT).show();
                        }
                        // Cheese me
                        else{   Toast.makeText(tab2medicalinfo.super.getContext(), "skin Unchecked!!", Toast.LENGTH_SHORT).show();}
                    case R.id.checkbox_dust:
                        if (checked){   Toast.makeText(tab2medicalinfo.super.getContext(), "dust allergy checked!!", Toast.LENGTH_SHORT).show();
                        }
                        // Cheese me
                        else{   Toast.makeText(tab2medicalinfo.super.getContext(), "dust Unchecked!!", Toast.LENGTH_SHORT).show();}
                        break;

                }
                Toast.makeText(tab2medicalinfo.super.getContext(), "skin Unchecked!!", Toast.LENGTH_SHORT).show();
            }
        });
    */
    }

});

    /*    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Victim").child(userID);

        spinner = (Spinner)rootview.findViewById(R.id.spinnerid);
        allergiesSpinner =  (Spinner)rootview.findViewById(R.id.allergiesxd);
        medicalConditionSpinner =  (Spinner)rootview.findViewById(R.id.medicalCOnd);
        updateBtn = (Button)rootview.findViewById(R.id.Update);
        getUserInformation();
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                savaUserInformation();
              //  Toast.makeText(tab2medicalinfo.super.getContext(), "Information Updated!!", Toast.LENGTH_SHORT).show();

            }

        });

        ArrayAdapter aa = new ArrayAdapter(tab2medicalinfo.super.getContext(),R.layout.spinner_item,options);
        ArrayAdapter bb = new ArrayAdapter(tab2medicalinfo.super.getContext(),R.layout.spinner_item,allergiesOption);
        ArrayAdapter cc = new ArrayAdapter(tab2medicalinfo.super.getContext(),R.layout.spinner_item,conditionOption);


        aa.setDropDownViewResource(R.layout.spinner_item);
        bb.setDropDownViewResource(R.layout.spinner_item);
        cc.setDropDownViewResource(R.layout.spinner_item);

        spinner.getSelectedView();
        allergiesSpinner.getSelectedView();
        medicalConditionSpinner.getSelectedView();
        spinner.setAdapter(aa);
        allergiesSpinner.setAdapter(bb);
        medicalConditionSpinner.setAdapter(cc);

        medicalConditionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String[] conditionOption= {"Select Options","Alzeimers","Cancer","Cholesterol","Cold&Flu","Depression","Diabetes","Pregnant"};

                if(position==0){
                    selectedCondition = "x";
                }
                else if(position==1){

                    selectedCondition = "Alzeimers";
                }
                else if(position==2){

                    selectedCondition = "Cancer";

                }
                else if(position==3){

                    selectedCondition = "Cholesterol";

                }else if(position==4){

                    selectedCondition = "Cold&Flu";

                }
                else if(position==4){

                    selectedCondition = "Depression";

                }
                else if(position==4){

                    selectedCondition = "Diabetes";

                }
                else if(position==4){

                    selectedCondition = "Pregnant";

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
     allergiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
             //Drug Allergy","Food Allergy","Skin Allergy","Dust Allergy"
             if(position==0){
                 selectAllergy = "";
             }
             else if(position==1){

                 selectAllergy = " Drug Allergy";
             }
             else if(position==2){

                 selectAllergy = "Food Allergy";

             }
             else if(position==3){

                 selectAllergy = "Skin Allergy";

             }else if(position==4){

                 selectAllergy = "Dust Allergy";

             }


         }

         @Override
         public void onNothingSelected(AdapterView<?> adapterView) {

         }
     });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //String selectedItem = parent.getItemAtPosition(position).toString();
                if(position==0){
                    selectedSpinner = "";
                }
                else if(position==1){

                    selectedSpinner = "AB+";
                }
                else if(position==2){

                    selectedSpinner = "AB-";

                }
                else if(position==3){

                    selectedSpinner = "B+";

                }else if(position==4){

                    selectedSpinner = "B-";

                }
                else if(position==5){

                    selectedSpinner = "A+";

                }else if(position==6){

                    selectedSpinner = "A-";

                }
                else if(position==7){

                    selectedSpinner = "O+";

                }else if(position==8){

                    selectedSpinner = "O-";

                }
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
 */
        return rootview;

    }


    private void savaUserInformation() {
        String medcon = selectedCondition;
        String allergyTxt = selectAllergy;
        String bloodTxt = selectedSpinner;
        final Map userInfo = new HashMap();

        if(medcon.equals("x")){
      //      Toast.makeText(tab2medicalinfo.this, "", Toast.LENGTH_SHORT).show();
           // Toast.makeText(tab2medicalinfo.super.getContext(), "Medical Condition not selected!!", Toast.LENGTH_SHORT).show();
            Snackbar snackbarx = Snackbar
                    .make(mSnackLayout, "Medical Condition not selected", Snackbar.LENGTH_SHORT);

            snackbarx.show();
        }
        else{
            userInfo.put("medicalCondition",medcon);


        }
        if(allergyTxt.equals("")){
          //  Toast.makeText(tab2medicalinfo.super.getContext(), "Allergy not selected!!", Toast.LENGTH_SHORT).show();
            Snackbar snackbarx = Snackbar
                    .make(mSnackLayout, "Allergy not selected!!", Snackbar.LENGTH_SHORT);

            snackbarx.show();


        }else{
            userInfo.put("allergy",allergyTxt);

        }
        if(bloodTxt.equals("")){
       //     Toast.makeText(tab2medicalinfo.super.getContext(), "Blood type not selected!!", Toast.LENGTH_SHORT).show();

        }
        else{
            userInfo.put("bloodType",bloodTxt);
        }

        final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(tab2medicalinfo.super.getContext(),R.style.AlertDialog);

        dialog.setTitle("Confirmation");
        dialog.setCancelable(false);
        dialog.setMessage("Do you really want to Update Medical information ?");


        LayoutInflater inflater = LayoutInflater.from(tab2medicalinfo.super.getContext());
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                userDatabaseReference.updateChildren(userInfo);

                dialogInterface.dismiss();
            }
        });
        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                dialogInterface.dismiss();
            }
        });


        dialog.show();



    }
    String valMed;
    String valAll;
    String valBlood;


    private void getUserInformation() {
        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()&&dataSnapshot.getChildrenCount()>1)
                {
                    Map<String,Object> map = (Map<String, Object>)dataSnapshot.getValue();
                    if(map.get("medicalCondition")!=null){

                        mCondition.setText(map.get("medicalCondition").toString().replace("[", "").replace("]", ""));


                    }
                    else{
//                        Toast.makeText(tab2medicalinfo.super.getContext(), "Medical Condition not selected!!", Toast.LENGTH_SHORT).show();

                        mSelectedCondition.setText("No items Selected!");


                    }
                    if(map.get("allergy")!=null){

                      //  valAll = ;
                        mAllergyTxt.setText(map.get("allergy").toString().replace("[", "").replace("]", ""));
                        //medicalConditionSpinner
                    }
                    else{
                     //   Toast.makeText(tab2medicalinfo.super.getContext(), "Medical Condition not selected!!", Toast.LENGTH_SHORT).show();

                        mSelectedAllergy.setText("No items Selected!");


                    }

                    if(map.get("bloodType")!=null){

                       // valBlood = map.get("").toString();
                        mBloodTxt.setText(map.get("bloodType").toString().replace("[", "").replace("]", ""));


                       // int i = spinner.getSelectedItemPosition();
                       // spinner.setSelection(i);
                        //spinner.setEnabled(false);

                    }
                    else{
                       // Toast.makeText(tab2medicalinfo.super.getContext(), "Medical Condition not selected!!", Toast.LENGTH_SHORT).show();

                        mSelectedBloodTxt.setText("No items Selected!");
                    }



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void applyTexts(List<String> mSelectedItems) {
       String x ="";
       for(String xx : mSelectedItems){
           x = x+""+mSelectedItems;
       }

        Toast.makeText(tab2medicalinfo.super.getContext(),""+x,Toast.LENGTH_LONG).show();

    }
}
