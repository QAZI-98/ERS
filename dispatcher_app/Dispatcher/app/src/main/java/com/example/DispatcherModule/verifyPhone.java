package com.example.DispatcherModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class verifyPhone extends AppCompatActivity {
private String VerificationId;
private FirebaseAuth firebaseAuth;
EditText edt ;
    String mPhone,mName,mEmail,mPassword,mType;
    Boolean check = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
         mPhone = getIntent().getStringExtra("phone");
        mName = getIntent().getStringExtra("name");
        mEmail = getIntent().getStringExtra("email");
    mPassword = getIntent().getStringExtra("password");
    mType = getIntent().getStringExtra("type");

Toast.makeText(this,mName+" "+mEmail+" "+mPhone+" "+mPassword+" "+mType,Toast.LENGTH_LONG).show();

    edt = (EditText)findViewById(R.id.editText);
        firebaseAuth = firebaseAuth.getInstance();
        sendVerificationCode(mPhone);
        Button btn = (Button)findViewById(R.id.verifybtn);
      btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = edt.getText().toString().trim();
                if(code.isEmpty()||code.length()<6){
                    return;
                }
                verifyCode(code);
                if(check==true){
                    emailAuth();
                }

            }
        });
    }
    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificationId,code);
        SigninWithCredential(credential);
        Toast.makeText(this, "Verifying...", Toast.LENGTH_SHORT).show();
    }

    private void SigninWithCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                   // Intent i = new Intent()
                   // Intent i = new Intent(verifyPhone.this, AmbulanceDispatcher.class);
                    //startActivity(i);
                    Toast.makeText(verifyPhone.this, "Successful!", Toast.LENGTH_SHORT).show();
                    emailAuth();
                    check =true;


                }else{
                    Toast.makeText(verifyPhone.this, "error "+task.getResult(), Toast.LENGTH_SHORT).show();
                    check = false;

                }            }
        });

    }
    private void emailAuth(){
        firebaseAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(verifyPhone.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                            Toast.makeText(verifyPhone.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            user user = new user(mName, mEmail, mPhone, mPassword, mType);
                            String userId = firebaseAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference();
                            if (mType == "Ambulance") {
                                current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("MedicalTeam").child(userId);
                            } else if (mType == "Police") {
                                current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("PoliceTeam").child(userId);

                            } else if (mType == "Firefighter") {
                                current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Dispatcher").child("FirefighterTeam").child(userId);


                            }
                            current_user_db.setValue(true);

                            current_user_db
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {


                                        Toast.makeText(verifyPhone.this, "Successfully Registered!.",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(verifyPhone.this, MainActivity.class));

                                    } else {
                                        Toast.makeText(verifyPhone.this, "failed!!." + task.getException(),
                                                Toast.LENGTH_SHORT).show();


                                    }
                                }
                            });


                        }

                    }

                });

    }
    private void sendVerificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            VerificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            //Toast.makeText(verifyPhone.this, "Successful!", Toast.LENGTH_SHORT).show();
            String code = phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(verifyPhone.this, "Error!"+e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };
}
