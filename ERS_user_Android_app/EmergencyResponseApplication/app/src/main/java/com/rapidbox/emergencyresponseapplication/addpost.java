package com.rapidbox.emergencyresponseapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class addpost extends AppCompatActivity {
    private static final int choose_image = 1;//to check when user comes back from gallery activity

    private Button b1, b3;
    private ImageView iv1;
    private Uri url;
    private EditText  et2;
    private String email;
    private FirebaseAuth auth;
    private StorageReference sref;
    private StorageTask stask;
    private DatabaseReference dref, root;
    private String name;
    private ProgressBar pb;
    Uri uploaduri;
    StorageReference ref;
    FusedLocationProviderClient client;
    SimpleDateFormat sdf;
    String date;
    String time;

    Spinner spinner;
    String selectedOption;
    LinearLayout mLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpost);
        String[] options = {"Select Emergency","Accident","Crime","Hazards","Others"};
        mLinear = (LinearLayout)findViewById(R.id.linearMain);
        spinner = (Spinner)findViewById(R.id.spinner1);
        ArrayAdapter aa = new ArrayAdapter(addpost.this,R.layout.spinner_item,options);



        aa.setDropDownViewResource(R.layout.spinner_item);
        spinner.getSelectedView();

        spinner.setAdapter(aa);


        pb = findViewById(R.id.progressBar1);

       // et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        b1 = findViewById(R.id.take_photo);
        b3 = findViewById(R.id.upload);
        iv1 = findViewById(R.id.iv1);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if(position==0){
                    selectedOption = "";
                }
                else if(position==1){

                    selectedOption = "accident";
                }
                else if(position==2){
                    selectedOption = "crime";


                }
                else if(position==3){
                    selectedOption = "hazard";


                }
                else if(position==4){
                    selectedOption = "others";


                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        client = LocationServices.getFusedLocationProviderClient(this);
        url = Uri.parse("android.resource://com.example.emergencyresponseapplicatio/" + R.drawable.document);
        auth = FirebaseAuth.getInstance();
        email = auth.getCurrentUser().getEmail();
        root = FirebaseDatabase.getInstance().getReference();
        Query query = root.child("Users").child("Victim");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String _email = ds.child("email").getValue(String.class);
                    if (email.equals(_email)) {
                        name = ds.child("firstname").getValue(String.class);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
          //      Toast.makeText(addpost.this, "error", Toast.LENGTH_SHORT).show();
                Snackbar snackbarx = Snackbar
                        .make(mLinear, "Something is wrong", Snackbar.LENGTH_SHORT);

                snackbarx.show();
            }
        };
        query.addListenerForSingleValueEvent(eventListener);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose();
            }
        });
        dref = FirebaseDatabase.getInstance().getReference().child("incident");
        sref = FirebaseStorage.getInstance().getReference().child("incident");
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pb.setVisibility(View.VISIBLE);

                if(selectedOption.equals("")){
//                    Toast.makeText(addpost.this, "Emergency Type not specified!!! ", Toast.LENGTH_SHORT).show();

                    Snackbar snackbarx = Snackbar
                            .make(mLinear, "Emergency Type not specified!", Snackbar.LENGTH_SHORT);

                    snackbarx.show();
                }else{
                    pb.setVisibility(View.VISIBLE);
                upload(selectedOption.toString(), et2.getText().toString());}
            }
        });


    }

    private void choose() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, choose_image);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == choose_image && resultCode == RESULT_OK && data.getData() != null) {
            url = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), url);
                iv1.setImageBitmap(bitmap);
            } catch (Exception e) {
            //    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Snackbar snackbarx = Snackbar
                        .make(mLinear, ""+e.getMessage(), Snackbar.LENGTH_SHORT);

                snackbarx.show();
            }
        }
    }

    private String getfileextension(Uri url) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mtm = MimeTypeMap.getSingleton();
        return mtm.getExtensionFromMimeType(cr.getType(url));

    }

    private void upload(final String type, final String text) {


        if (et2.getText().toString().trim().isEmpty()) {
            if (et2.getText().toString().isEmpty()) {
                et2.requestFocus();
                et2.setError("required");
            }

        } else {
            if (url != null) {
                ref = sref.child(System.currentTimeMillis() + "." + getfileextension(url));

                stask = ref.putFile(url)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                pb.setVisibility(View.GONE);

                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        uploaduri = uri;
                                        if (ActivityCompat.checkSelfPermission(addpost.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                            return;
                                        }
                                        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                            @Override
                                            public void onSuccess(Location location) {
                                                if (location != null) {
                                                    String key = dref.push().getKey();

                                                    String lat = Double.toString(location.getLatitude());
                                                    String longe = Double.toString(location.getLongitude());
                                                    Geocoder geocoder;
                                                    List<Address> addressList;
                                                    String title=type;
                                                   String fullAddress;
                                                   // geocoder = new Geocoder(addpost.this, Locale.getDefault());
                                                    try {
                                                        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                                                        fullAddress = reverseGeocoding(latlng);
                                                       // addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                                        title+=" "+fullAddress;

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    sdf=new SimpleDateFormat("d/MM/yy");
                                                    date= sdf.format(new Date());
                                                    sdf=new SimpleDateFormat("HH:mm");
                                                    time= sdf.format(new Date());


                                                    incident pic = new incident(text, uploaduri.toString(), name, title, key, email, lat, longe,time,date,type);
                                                    dref.child(key).setValue(pic);
                                                  //  Toast.makeText(addpost.this, "uploaded", Toast.LENGTH_SHORT).show();
                                                    Snackbar snackbarx = Snackbar
                                                            .make(mLinear, "Post has been Uploaded Successfully", Snackbar.LENGTH_SHORT);
                                                  snackbarx.show();

                                                    // et1.setText("");
                                                    spinner.setSelection(0);

                                                    et2.setText("");
                                                    iv1.setImageResource(R.drawable.photoframe);
                                                }
                                                else{
                                             //       Toast.makeText(addpost.this, "Turn on location to post", Toast.LENGTH_SHORT).show();

                                                    Snackbar snackbarx = Snackbar
                                                            .make(mLinear, "Turn on location to post", Snackbar.LENGTH_SHORT);

                                                    snackbarx.show();
                                                }
                                            }
                                        });

                                    }
                                });


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Toast.makeText(addpost.this, "Please Select Image First!!", Toast.LENGTH_SHORT).show();
                                Snackbar snackbarx = Snackbar
                                        .make(mLinear, "Image is not Selected", Snackbar.LENGTH_SHORT);

                                snackbarx.show();

                                pb.setVisibility(View.GONE);
                            }
                        });


            } else {
           //     Toast.makeText(this, "no file selected", Toast.LENGTH_SHORT).show();
                Snackbar snackbarx = Snackbar
                        .make(mLinear, "No file Selected!", Snackbar.LENGTH_SHORT);

                snackbarx.show();
            }

        }


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
           // Toast.makeText(this, ""+ioException.getMessage(), Toast.LENGTH_SHORT).show();
            Snackbar snackbarx = Snackbar
                    .make(mLinear, ""+ioException.getMessage(), Snackbar.LENGTH_SHORT);

            snackbarx.show();
            // Catch network or other I/O problems.
            //  Log.e(TAG, errorMessage, ioException);
        }



        return strAddress;


    }


}
