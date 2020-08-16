package com.example.DispatcherModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    TextInputEditText email,password;
    TextView forgetPass;
    CheckBox remember;
    Button signin;
    TextView signup;
    String emailTxt,passwordTxt;
    TextInputLayout til,passwordLayout;
    private FirebaseAuth mAuth;
    String uid;



//    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
      //  FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.etPassword);
        remember = (CheckBox)findViewById(R.id.checkbxx);
        signin = (Button)findViewById(R.id.signinBtn);
        signup = (TextView) findViewById(R.id.signupBtn);
        forgetPass = (TextView)findViewById(R.id.forgetTxt);

        til = (TextInputLayout)findViewById(R.id.emailLayout);
        passwordLayout = (TextInputLayout)findViewById(R.id.etPasswordLayout);
        mAuth = FirebaseAuth.getInstance();
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //   Intent i =  new Intent(MainActivity.this, forgetpassword.class);
               // startActivity(i);

            }
        });


    }
    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    public boolean checkEmail(){
        emailTxt = email.getText().toString();

        boolean b= validate(emailTxt);

        if (b!=true){

            til.setErrorEnabled(true);
            til.setError("Wrong email!!!");
            return true;
        }
        else{
            til.setErrorEnabled(false);
            return false;

        }

    }
    public boolean checkPassword(){
        passwordTxt = password.getText().toString();


        if(TextUtils.isEmpty(passwordTxt)){
            passwordLayout.setErrorEnabled(true);
            passwordLayout.setError("Enter password!!!");

            return true;
        }
        else{
            passwordLayout.setErrorEnabled(false);

            return false;

        }
    }
    public void login(){
        final ProgressDialog progressBar = new ProgressDialog(MainActivity.this);
        progressBar.setMessage("Wait..");
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();

        try {
            if (checkEmail() != true && checkPassword() != true) {

                // Toast.makeText(MainActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                mAuth.signInWithEmailAndPassword(emailTxt, passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            if(mAuth.getCurrentUser().isEmailVerified()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                // if(mAuth.getCurrentUser().isEmailVerified()) {


                                uid = user.getUid();
                               // Toast.makeText(MainActivity.this, "" + uid, Toast.LENGTH_LONG).show();
                                Query userQueryFireFighter = FirebaseDatabase.getInstance().getReference()
                                        .child("Users").child("Dispatcher").child("FirefighterTeam").orderByKey().equalTo(uid);
                                Query userQueryAmbulance = FirebaseDatabase.getInstance().getReference()
                                        .child("Users").child("Dispatcher").child("MedicalTeam").orderByKey().equalTo(uid);
                                Query userQueryPolice = FirebaseDatabase.getInstance().getReference()
                                        .child("Users").child("Dispatcher").child("PoliceTeam").orderByKey().equalTo(uid);


                                userQueryFireFighter.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        //String user = dataSnapshot.getKey().toString();
                                        if (dataSnapshot.exists()) {

                                            progressBar.dismiss();
//                                        Toast.makeText(MainActivity.this, "signed in firefighter", Toast.LENGTH_SHORT).show();

                                            mAuth = FirebaseAuth.getInstance();
                                            db = FirebaseDatabase.getInstance();
                                            users = db.getReference("Users").child("Dispatcher").child("FirefighterTeam");

                                            Intent i = new Intent(MainActivity.this, FireFighterDispatcher.class);

                                            startActivity(i);
                                            finish();


                                        } else {
                                            progressBar.dismiss();
                                            //    Toast.makeText(MainActivity.this, "signed error", Toast.LENGTH_SHORT).show();
//                                        FirebaseAuth.getInstance().signOut();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }

                                });
                                userQueryAmbulance.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        //String user = dataSnapshot.getKey().toString();
                                        if (dataSnapshot.exists()) {
                                            progressBar.dismiss();

                                            //Toast.makeText(MainActivity.this, "signed in as ambulance", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(MainActivity.this, MedicalTeamDispatcher.class);
                                            startActivity(i);
                                            finish();


                                        } else {
                                            progressBar.dismiss();
                                            //  Toast.makeText(MainActivity.this, "signed error", Toast.LENGTH_SHORT).show();
                                            //                                      FirebaseAuth.getInstance().signOut();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }

                                });
                                userQueryPolice.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        //String user = dataSnapshot.getKey().toString();
                                        if (dataSnapshot.exists()) {
                                            progressBar.dismiss();
                                            //Toast.makeText(MainActivity.this, "signed in as police", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(MainActivity.this, PoliceTeamDispatcher.class);
                                            startActivity(i);
                                            finish();


                                        } else {
                                            progressBar.dismiss();
                                            //Toast.makeText(MainActivity.this, "signed error", Toast.LENGTH_SHORT).show();
                                            //                                    FirebaseAuth.getInstance().signOut();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }

                                });

                            }else{
                                progressBar.dismiss();
                                Toast.makeText(MainActivity.this, "Please Verify Email! ", Toast.LENGTH_SHORT).show();

                            }
                        }
                        else{
                            progressBar.dismiss();

                            Toast.makeText(MainActivity.this, "sign in error ", Toast.LENGTH_SHORT).show();

                        }
                    } //else {
                    //progressBar.dismiss();
                    //Toast.makeText(MainActivity.this, "not successfull in", Toast.LENGTH_SHORT).show();

                    //Intent i = new Intent(MainActivity.this,dashboardActivity.class);
                    //startActivity(i);

                    //}
                    // }
                });


            } else {
                progressBar.dismiss();

                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();


            }
        }
        catch (Exception x)
        {
            Toast.makeText(MainActivity.this, "error"+x, Toast.LENGTH_SHORT).show();

        }
    }
    public void signupcall(View view){
        Intent i = new Intent(MainActivity.this,signup.class);
        startActivity(i);

    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
