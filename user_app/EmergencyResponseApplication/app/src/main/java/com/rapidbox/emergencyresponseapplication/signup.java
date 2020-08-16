package com.rapidbox.emergencyresponseapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

import static com.rapidbox.emergencyresponseapplication.MainActivity.validate;

public class signup extends AppCompatActivity {
    TextInputEditText firstName, lastName, email, password, repeatPassword;

    CheckBox termCondition;
    Button signup;
    String firstNameTxt, lastNameTxt, emailTxt, passwordTxt, repeatPasswordTxt;

    TextInputLayout firstnamelayout, lastnamelayout, emaillayout, passwordlayout, repeatpasswordlayout;

    TextView textViewx;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        //to set up back arrow button
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        textViewx = (TextView) findViewById(R.id.textView9);
        textViewx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(signup.this, MainActivity.class);
                //i.putExtra("reportType","medical");
                startActivity(i);
                finish();
            }
        });
        // components initialization
        firstName = (TextInputEditText) findViewById(R.id.firstname);
        lastName = (TextInputEditText) findViewById(R.id.lastname);
        email = (TextInputEditText) findViewById(R.id.email);
        password = (TextInputEditText) findViewById(R.id.password);
        repeatPassword = (TextInputEditText) findViewById(R.id.againPassword);
        termCondition = (CheckBox) findViewById(R.id.termsconditioncheckbox);
        signup = (Button) findViewById(R.id.signupBtn);

        // layouts initialization
        firstnamelayout = (TextInputLayout) findViewById(R.id.firstNameLayout);
        lastnamelayout = (TextInputLayout) findViewById(R.id.lastNameLayout);
        emaillayout = (TextInputLayout) findViewById(R.id.emailLayout);
        passwordlayout = (TextInputLayout) findViewById(R.id.passwordLayout);
        repeatpasswordlayout = (TextInputLayout) findViewById(R.id.passwordAgainLayout);

        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                firstnamelayout.setError(null);

                firstnamelayout.setErrorEnabled(false);
            }
        });
        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                lastnamelayout.setError(null);

                lastnamelayout.setErrorEnabled(false);
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
                emaillayout.setError(null);

                emaillayout.setErrorEnabled(false);
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
                passwordlayout.setError(null);

                passwordlayout.setErrorEnabled(false);
            }
        });
        repeatPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                repeatpasswordlayout.setError(null);

                repeatpasswordlayout.setErrorEnabled(false);
            }
        });






    }

    public boolean checkFirstName() {
        firstNameTxt = firstName.getText().toString();
        if (TextUtils.isEmpty(firstNameTxt)) {
            firstnamelayout.setErrorEnabled(true);
            firstnamelayout.setError("First Name cannot be Empty");

            return true;
        } else {
            firstnamelayout.setError(null);
            firstnamelayout.setErrorEnabled(false);
            return false;

        }

    }

    public boolean checkLastName() {
        lastNameTxt = lastName.getText().toString();
        if (TextUtils.isEmpty(lastNameTxt)) {
            lastnamelayout.setErrorEnabled(true);
            lastnamelayout.setError("Last Name cannot be Empty");

            return true;
        } else {
            lastnamelayout.setError(null);
            lastnamelayout.setErrorEnabled(false);
            return false;

        }

    }

    public boolean checkEmail() {
        emailTxt = email.getText().toString();
        //validate method is imported from mainactivity see 'imports'
        boolean b = validate(emailTxt);

        if (b != true) {

            emaillayout.setErrorEnabled(true);
            emaillayout.setError("Email cannot be Empty");
            return true;
        } else {
            emaillayout.setError(null);
            emaillayout.setErrorEnabled(false);
            return false;

        }


    }

    public boolean checkPassword() {
        passwordTxt = password.getText().toString();
        if (TextUtils.isEmpty(passwordTxt) ||passwordTxt.length()<6) {
            passwordlayout.setErrorEnabled(true);
            passwordlayout.setError("Write atleast 6 Letters");

            return true;
        } else {
            passwordlayout.setError(null);
            passwordlayout.setErrorEnabled(false);
            return false;

        }

    }

    public boolean checkRepearPassword() {
        repeatPasswordTxt = repeatPassword.getText().toString();
        if (TextUtils.isEmpty(repeatPasswordTxt)||repeatPasswordTxt.length()<6) {
            repeatpasswordlayout.setErrorEnabled(true);
            repeatpasswordlayout.setError("Write atleast 6 Letters");

            return true;
        } else {
            repeatpasswordlayout.setError(null);
            repeatpasswordlayout.setErrorEnabled(false);
            return false;

        }

    }

    public boolean matchRepeatPassword() {
        passwordTxt = password.getText().toString().trim();
        repeatPasswordTxt = repeatPassword.getText().toString().trim();
        if (passwordTxt.equals(repeatPasswordTxt)) {

            passwordlayout.setErrorEnabled(false);
            repeatpasswordlayout.setErrorEnabled(false);
            return true;

        } else {
            repeatpasswordlayout.setErrorEnabled(true);

            repeatpasswordlayout.setError("Password do not match");
            return false;

        }


    }

    public void signupCompleteion(View view) {

        if (checkFirstName() != true && checkLastName() != true && checkEmail() != true && checkPassword() != true && checkRepearPassword() != true) {
            if (matchRepeatPassword() == true) {


                if (termCondition.isChecked()) {
                    //Toast.makeText(signup.this, "Successfull", Toast.LENGTH_SHORT).show();
                    final ProgressDialog progressBar = new ProgressDialog(signup.this);
                    progressBar.setMessage("Registering..");
                    progressBar.setCancelable(false);
                    progressBar.setCanceledOnTouchOutside(false);
                    progressBar.show();
                    firebaseAuth.createUserWithEmailAndPassword(emailTxt, passwordTxt)
                            .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {
                                        progressBar.dismiss();
                                        Toast.makeText(signup.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {


                                        user user = new user(firstNameTxt, lastNameTxt, emailTxt);


                                        String userId = firebaseAuth.getCurrentUser().getUid();
                                        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Victim").child(userId);

                                        current_user_db.setValue(true);

                                                current_user_db
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {



                                                            progressBar.dismiss();
                                                            Toast.makeText(signup.this, "Successfully Registered!.",
                                                                    Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(signup.this, MainActivity.class));

                                                        } else {
                                                            progressBar.dismiss();
                                                            Toast.makeText(signup.this, "failed!!." + task.getException(),
                                                                    Toast.LENGTH_SHORT).show();


                                                        }
                                                    }
                                                });











                                    }
                                }
                            });
                } else {
                    Toast.makeText(signup.this, "Please check term and conditions", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }


}