package com.rapidbox.emergencyresponseapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
    ConstraintLayout constLayout;
    private FirebaseAuth.AuthStateListener mAuthListner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.etPassword);
        remember = (CheckBox)findViewById(R.id.checkbxx);
        signin = (Button)findViewById(R.id.signinBtn);
        signup = (TextView) findViewById(R.id.signupBtn);
        forgetPass = (TextView)findViewById(R.id.forgetTxt);
        constLayout = (ConstraintLayout)findViewById(R.id.containtMain);
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

                Intent i =  new Intent(MainActivity.this, forgetpassword.class);
                startActivity(i);
                finish();

            }
        });


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                til.setError(null);
                til.setErrorEnabled(false);
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passwordLayout.setError(null);
                passwordLayout.setErrorEnabled(false);
            }
        });


    }
    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    public boolean checkEmail(){
        emailTxt = email.getText().toString().replace(" ","");

        boolean b= validate(emailTxt);

        if (b!=true){

            til.setErrorEnabled(true);
            til.setError("Email is not correct");
            return true;
        }
        else{
            til.setError(null);
            til.setErrorEnabled(false);
            return false;

        }

    }
    public boolean checkPassword(){
        passwordTxt = password.getText().toString();


        if(TextUtils.isEmpty(passwordTxt) ){
            passwordLayout.setErrorEnabled(true);
            passwordLayout.setError("Password is Required!");

            return true;
        }
        else{
            passwordLayout.setError(null);

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

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                   // if(mAuth.getCurrentUser().isEmailVerified()) {


                        uid = user.getUid();

                        Query userQuery = FirebaseDatabase.getInstance().getReference()
                                .child("Users").child("Victim").orderByKey().equalTo(uid);
                        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //String user = dataSnapshot.getKey().toString();
                                if (dataSnapshot.exists()) {
                                    progressBar.dismiss();
                                    Intent i = new Intent(MainActivity.this, dashboard.class);
                                    startActivity(i);
                                    finish();


                                } else {
                                    progressBar.dismiss();
                                    Snackbar snackbar = Snackbar
                                            .make(constLayout, "Wrong Credentials", Snackbar.LENGTH_SHORT);

                                    snackbar.show();

                                    FirebaseAuth.getInstance().signOut();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });
                    }
                    else{
                    progressBar.dismiss();

                    //    Toast.makeText(MainActivity.this, "sign in error ", Toast.LENGTH_SHORT).show();

                    try {
                        throw task.getException();
                    } catch(FirebaseAuthWeakPasswordException e) {
                        //       mTxtPassword.setError(getString(R.string.error_weak_password));
                        password.requestFocus();
                        Snackbar snackbar = Snackbar
                                .make(constLayout, "Error Password is Weak", Snackbar.LENGTH_SHORT);

                        snackbar.show();
                    } catch(FirebaseAuthInvalidCredentialsException e) {
                        //  mTxtEmail.setError(getString(R.string.error_invalid_email));
                        email.requestFocus();

                        Snackbar snackbar = Snackbar
                                .make(constLayout, "Invalid Email", Snackbar.LENGTH_SHORT);

                        snackbar.show();
                    } catch(FirebaseAuthUserCollisionException e) {
                        //      mTxtEmail.setError(getString(R.string.error_user_exists));
                        email.requestFocus();
                        Snackbar snackbar = Snackbar
                                .make(constLayout, "User Already Exits!", Snackbar.LENGTH_SHORT);

                        snackbar.show();


                    } catch(Exception e) {
                        //    Log.e(TAG, e.getMessage());
                        Snackbar snackbar = Snackbar
                                .make(constLayout, "Wrong Credentials!", Snackbar.LENGTH_SHORT);

                        snackbar.show();
                    }

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

       // Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
        Snackbar snackbar = Snackbar
                .make(constLayout, "Error", Snackbar.LENGTH_SHORT);

        snackbar.show();

    }
}
    catch (Exception x)
    {
        Toast.makeText(MainActivity.this, "error"+x, Toast.LENGTH_SHORT).show();
        Snackbar snackbar = Snackbar
                .make(constLayout, "Error "+x, Snackbar.LENGTH_SHORT);

        snackbar.show();
    }
    }
    public void signupcall(View view){
        Intent i = new Intent(MainActivity.this,signup.class);
        startActivity(i);

    }
}
