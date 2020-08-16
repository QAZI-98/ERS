package com.example.DispatcherModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.DispatcherModule.MainActivity.validate;

public class signup extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextInputEditText mPhone,mName,mEmail,mPassword;
    Button mSignBtn;
    String firstNameTxt,phoneTxt,emailTxt,passwordTxt;
    TextInputLayout mNameLayout,mEmailLayout,mPasswordLayout,mPhoneLayout;
    Boolean checkLength;
    private Spinner spinner;
    String item;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

       // spinner = findViewById(R.id.spinnerCountries);
        //spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countryData.countryNames));
        item = "";
        mName = (TextInputEditText)findViewById(R.id.firstname);
        mEmail = (TextInputEditText)findViewById(R.id.email);
        mPhone = (TextInputEditText) findViewById(R.id.phone);
        mPassword = (TextInputEditText)findViewById(R.id.password);
        firebaseAuth = FirebaseAuth.getInstance();
        mSignBtn = (Button)findViewById(R.id.signupBtn);
        checkLength = false;

        mNameLayout = (TextInputLayout) findViewById(R.id.firstNameLayout);
        mPhoneLayout = (TextInputLayout) findViewById(R.id.lastNameLayout);
        mEmailLayout = (TextInputLayout) findViewById(R.id.emailLayout);
        mPasswordLayout = (TextInputLayout) findViewById(R.id.passwordLayout);


       Spinner spinner = (Spinner) findViewById(R.id.dispatcher_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dispatcher_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //int x = mPhone.length();

            }

            @Override
            public void afterTextChanged(Editable s) {
                int x = mPhone.length();
                if(x!=14){
                   mPhone.setError("");
                    checkLength =true;
                }else{
                    checkLength =false;

                }
            }
        });
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        mSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailTxt = mEmail.getText().toString();
                passwordTxt = mPassword.getText().toString();
                phoneTxt = mPhone.getText().toString();
                firstNameTxt = mName.getText().toString();

                if (checkFirstName()==true||checkEmail()==true||checkPhone()==true||checkPassword()==true){
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

                                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                Toast.makeText(signup.this,"Please Check your Email",Toast.LENGTH_SHORT).show();
                                                mName.setText("");
                                                mEmail.setText("");
                                                mPhone.setText("");
                                                mPassword.setText("");



                                            user user = new user(firstNameTxt, emailTxt,phoneTxt, passwordTxt,item);
                                            String userId = firebaseAuth.getCurrentUser().getUid();
                                                DatabaseReference current_user_db = null;

                                              if(item.equals("Ambulance")) {
                                                 current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("MedicalTeam").child(userId);
                                            }else if(item.equals("Police")){
                                                    current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("PoliceTeam").child(userId);

                                                }else if(item.equals("Firefighter")){
                                                    current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("FirefighterTeam").child(userId);

                                                }
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











                                }
                            }
                        });
            }else{
                    Toast.makeText(signup.this, "Error!", Toast.LENGTH_SHORT).show();

                }
            }


                /*                phoneTxt = mPhone.getText().toString();

                //&& checkPhone() != true
                if (checkFirstName() != true  && checkEmail() != true && checkPassword() != true) {
                    //Toast.makeText(signup.this, "Successfull!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(signup.this, " "+phoneTxt, Toast.LENGTH_SHORT).show();


                    Intent i = new Intent(signup.this,verifyPhone.class);
                    i.putExtra("name",firstNameTxt);
                    i.putExtra("phone",phoneTxt);
                    i.putExtra("email",emailTxt);
                    i.putExtra("password",passwordTxt);
                    i.putExtra("type",item);
                    startActivity(i);

                }else{
                    Toast.makeText(signup.this, "Error!", Toast.LENGTH_SHORT).show();

                }

*/});
    }
    public boolean checkFirstName() {
        firstNameTxt = mName.getText().toString();
        if (TextUtils.isEmpty(firstNameTxt)) {
            mNameLayout.setErrorEnabled(true);
            mNameLayout.setError("*");

            return true;
        } else {
            mNameLayout.setErrorEnabled(false);
            return false;

        }

    }

    public boolean checkPhone() {
        phoneTxt = mPhone.getText().toString();
        if (TextUtils.isEmpty(phoneTxt) || checkLength==true) {
            mPhoneLayout.setErrorEnabled(true);
            mPhoneLayout.setError("*");

            return true;
        } else {
            mPhoneLayout.setErrorEnabled(false);
            return false;

        }

    }

    public boolean checkEmail() {
        emailTxt = mEmail.getText().toString();
        //validate method is imported from mainactivity see 'imports'
        boolean b = validate(emailTxt);

        if (b != true) {

            mEmailLayout.setErrorEnabled(true);
            mEmailLayout.setError("*");
            return true;
        } else {
            mEmailLayout.setErrorEnabled(false);
            return false;

        }


    }
    public boolean checkPassword() {
        passwordTxt = mPassword.getText().toString();
        if (TextUtils.isEmpty(passwordTxt)) {
            mPasswordLayout.setErrorEnabled(true);
            mPasswordLayout.setError("*");

            return true;
        } else {
            mPasswordLayout.setErrorEnabled(false);
            return false;

        }


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

         item = parent.getItemAtPosition(position).toString();
        Toast.makeText(signup.this, ""+ item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
