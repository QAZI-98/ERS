package com.rapidbox.emergencyresponseapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.rapidbox.emergencyresponseapplication.historyRecyclerView.historyObject;
import com.rapidbox.emergencyresponseapplication.historyRecyclerView.history_adapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class historyActivity extends AppCompatActivity {


    private List<historyObject> listitems;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView mHistoryRecyclerView;

    private RecyclerView.Adapter mHistoryAdapter;
    private androidx.recyclerview.widget.RecyclerView.LayoutManager mHistoryLayoutManager;
    String dispatcherID = "";

    String typeofEmergency = "";
    String userID;
    private FirebaseRecyclerAdapter adapter;

    private String teamType;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String KEY = "Key";
    SharedPreferences sharedpreferences;
    ProgressDialog showLoad;
    TextView noHist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history2);
        try {
            //     Toast.makeText(this, "dasdas", Toast.LENGTH_SHORT).show();
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            showLoad = new ProgressDialog(historyActivity.this, R.style.AppCompatAlertDialogStyle);
            showLoad.setMessage("Wait...");
            showLoad.show();
            showLoad.setCanceledOnTouchOutside(false);
            mHistoryRecyclerView = (RecyclerView) findViewById(R.id.historyRecyclerView);
            mHistoryRecyclerView.setHasFixedSize(true);
            noHist = (TextView) findViewById(R.id.noHistory);
            mHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            listitems = new ArrayList<>();
       /* for (int i =0;i<10;i++){

            historyObject ho = new historyObject(
                    "hello"+i
            );
            listitems.add(ho);
        }*/
            mHistoryAdapter = new history_adapter(listitems, this);
            mHistoryRecyclerView.setAdapter(mHistoryAdapter);


            // LinearLayoutManager llm = new LinearLayoutManager(historyActivity.this);
            // mHistoryRecyclerView.setLayoutManager(llm);

            // mHistoryRecyclerView.setNestedScrollingEnabled(false);
            //mHistoryRecyclerView.setHasFixedSize(true);
            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            //linearLayoutManager = new LinearLayoutManager(this);
            //linearLayoutManager.setReverseLayout(true);
            // linearLayoutManager.setStackFromEnd(true);
            //   mHistoryRecyclerView.setLayoutManager(linearLayoutManager);
            //    mHistoryRecyclerView.setHasFixedSize(true);

            //mHistoryLayoutManager = new LinearLayoutManager(historyActivity.this);

            // mHistoryRevcyclerView.setLayoutManager(mHistoryLayoutManager);
            //mHistoryAdapter = new history_adapter(getDataSetHistory(),historyActivity.this);
            //  mHistoryRecyclerView.setAdapter(mHistoryAdapter);

            //  historyObject obj = new historyObject(Integer.toString(i));
            //    historyObject obj = new historyObject("1234");
            //  resultHistory.add(obj);
            getUserHistoryId();

        /*
        for (int i =0;i<100;i++){
           historyObject obj = new historyObject(Integer.toString(i));
            resultHistory.add(obj);
     //       Toast.makeText(this,Integer.toString(i), Toast.LENGTH_SHORT).show();

        }
  */      //mHistoryAdapter.notifyDataSetChanged();
        }catch (Exception e){
                Toast.makeText(historyActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    public void getUserHistoryId(){
        DatabaseReference userhistoryDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Victim").child(userID).child("history");
        userhistoryDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot history : dataSnapshot.getChildren()){
                        fetchRideInformation(history.getKey());
                    }

                }
                else{
                    showLoad.hide();
                    noHist.setText("Empty History !!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
 //   ArrayList<String> names = new ArrayList<String>();

    private ArrayList resultHistory = new ArrayList<historyObject>();
    private ArrayList<historyObject> getDataSetHistory() {
        return resultHistory;
    }
   public String phoneUser = "";
    public String nameUser = "";

    private String nameSetM;
    public void setName(String group){
        this.nameSetM = group;
    }
    private String phoneSetM;
    public void setPhone(String group){
        this.phoneSetM = group;
    }


    private void fetchRideInformation(String ridekey) {
        DatabaseReference historyDatabase = FirebaseDatabase.getInstance().getReference().child("history").child(ridekey);

       // historyDatabase.keepSynced(true);
        historyDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String rideId = dataSnapshot.getKey();
                    Long timeStamp = 0L;
                    String reportT = "";
                    String fromLocation = "";
                    String toLocation = "";

                    //  String userIDVal = "";
                  //  String dispatcherID = "";

                    for(DataSnapshot child: dataSnapshot.getChildren()){
                        if(child.getKey().equals("timestamp")){

                        timeStamp = Long.valueOf(child.getValue().toString());
                        }
                        if(child.getKey().equals("reportType")){

                            reportT = child.getValue().toString();
                        }
                        if(child.getKey().equals("pickup")){
                            fromLocation = child.getValue().toString();
                        }
                        if(child.getKey().equals("drop")){
                            toLocation = child.getValue().toString();
                        }
                        if(child.getKey().equals("medicalTeam")){
                            dispatcherID = child.getValue().toString();
                            typeofEmergency = "MedicalTeam";

                         //   nameUser = getUserName(dispatcherID,"MedicalTeam");
                          // phoneUser = getUserPhone(dispatcherID,"MedicalTeam");
                        }
                        if(child.getKey().equals("crimeTeam")){
                            dispatcherID = child.getValue().toString();
                            typeofEmergency = "PoliceTeam";
                          //nameUser=  getUserName(dispatcherID,"PoliceTeam");
                          //phoneUser=  getUserPhone(dispatcherID,"PoliceTeam");

                        }

                        if(child.getKey().equals("firefighterTeam")){
                            dispatcherID = child.getValue().toString();
                            typeofEmergency = "FirefighterTeam";

                            //nameUser= getUserName(dispatcherID,"FirefighterTeam");
                            //phoneUser = getUserPhone(dispatcherID,"FirefighterTeam");
                        }




                    }
                    if(reportT.equals("Crime")){
                        teamType = "Crime Team";
                    }
                    else if(reportT.equals("Accident")){
                        teamType = "Accident Team";

                    }
                    else if(reportT.equals("Fire")){
                        teamType =  "Firefighter Team";

                    }
                    /*
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child(typeofEmergency).child(dispatcherID);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){

                                /*
                                for(DataSnapshot child: dataSnapshot.getChildren()) {
                                    if (child.getKey().equals("name")) {
                                        nameUser  = child.getValue().toString();
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString("name", nameUser);
                                        editor.commit();
                                        //timeStamp = Long.valueOf(child.getValue().toString());
                                    }
                                    if (child.getKey().equals("phone")) {
                                        phoneUser  = child.getValue().toString();
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString("phone", phoneUser);
                                        editor.commit();
                                        //timeStamp = Long.valueOf(child.getValue().toString());
                                    }

                                }*/
                       /*         Map<String,Object> map =(Map<String,Object>) dataSnapshot.getValue();
                                if(map.get("name")!=null){
                                   String x = map.get("name").toString();
                                   //    Toast.makeText(historyActivity.this, ""+nameUser, Toast.LENGTH_SHORT).show();
                                    //SharedPreferences.Editor editor = sharedpreferences.edit();
                                    //editor.putString("name", x);
                                        //editor.commit();
                                    names.add(x);
                                }
                                if(map.get("phone")!=null){
                                    String y= map.get("phone").toString();
                                    names.add(y);
                                 //    Toast.makeText(historyActivity.this, ""+phoneUser, Toast.LENGTH_SHORT).show();
                               //     SharedPreferences.Editor editor = sharedpreferences.edit();
                                 //   editor.putString("name", y);
                                  //  editor.commit();

                                }

                    //        cv = new checkVal(nameUser,phoneUser);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                          */
                  //  Toast.makeText(historyActivity.this, ""+dispatcherID, Toast.LENGTH_SHORT).show();
         //           getUserName();
                 //   Toast.makeText(historyActivity.this, ""+nameUser+phoneUser, Toast.LENGTH_SHORT).show();
                    //    Toast.makeText(historyActivity.this, ""+rideId, Toast.LENGTH_SHORT).show();
                    historyObject ho = new historyObject(rideId,getDate(timeStamp),reportT,fromLocation,toLocation,teamType);
                    listitems.add(ho);

                    //historyObject obj = new historyObject(rideId);
                    //resultHistory.add(obj);
                    mHistoryAdapter.notifyDataSetChanged();
                    showLoad.hide();

                }
                else{
                    showLoad.hide();
                    noHist.setText("Empty History !!");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private String valUserName ="";
    private String valUserPhone ="";

    private String getUserPhone(String userIDVal,String type) {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child(type).child(userIDVal);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map<String,Object> map =(Map<String,Object>) dataSnapshot.getValue();
                    if(map.get("phone")!=null){
                        valUserPhone = map.get("phone").toString();
                       // Toast.makeText(historyActivity.this, ""+valUserPhone, Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return valUserPhone;
    }
        private void getUserName() {


        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child(typeofEmergency).child(dispatcherID);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map<String,Object> map =(Map<String,Object>) dataSnapshot.getValue();
                    if(map.get("name")!=null){
                        nameUser = map.get("name").toString();
                     //   Toast.makeText(historyActivity.this, ""+valUserName, Toast.LENGTH_SHORT).show();

                    }
                    if(map.get("phone")!=null){
                        phoneUser = map.get("phone").toString();
                        // Toast.makeText(historyActivity.this, ""+valUserPhone, Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDispatcherName(String dispatcherID) {
    }

    private String getDate(Long timeStamp) {

        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timeStamp*1000);
        String date = android.text.format.DateFormat.format("dd-MM-yyyy hh:mm",cal).toString();
        return  date;
    }

   /* private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("incident");
        final FirebaseRecyclerOptions<incident> options =
                new FirebaseRecyclerOptions.Builder<incident>()
                        .setQuery(query, new SnapshotParser<incident>() {
                            @NonNull
                            @Override
                            public incident parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new incident
                                        (snapshot.child("text").getValue().toString(),
                                                snapshot.child("name").getValue().toString(),
                                                snapshot.child("type").getValue().toString(),
                                                snapshot.child("imguri").getValue().toString(),
                                                snapshot.child("latitude").getValue().toString(),
                                                snapshot.child("longitude").getValue().toString()
                                        );

                            }
                        })
                        .build();


    }*/





}

