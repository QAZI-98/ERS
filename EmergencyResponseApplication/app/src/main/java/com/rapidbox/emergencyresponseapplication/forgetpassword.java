package com.rapidbox.emergencyresponseapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class forgetpassword extends AppCompatActivity {
    TextInputEditText email;
    Button reset;
    String emailTxt;
     ImageView imageviewx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);

        email = (TextInputEditText)findViewById(R.id.emailEdit);
        reset = (Button)findViewById(R.id.resetbtn);
        imageviewx = (ImageView)findViewById(R.id.iv);
        imageviewx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(forgetpassword.this, MainActivity.class);
                //i.putExtra("reportType","medical");
                startActivity(i);
                finish();

            }
        });

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        emailTxt = email.getText().toString();


        if(!TextUtils.isEmpty(emailTxt)){
        final ProgressDialog progressDialog = new ProgressDialog(forgetpassword.this);
        progressDialog.setMessage("Sending Reset Instructions");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        mAuth.sendPasswordResetEmail(emailTxt).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();

                    Toast.makeText(forgetpassword.this, "Instruction sent to email", Toast.LENGTH_SHORT).show();

                }
                else{
                    progressDialog.dismiss();

                    Toast.makeText(forgetpassword.this, "Failed sending instruction email!!", Toast.LENGTH_SHORT).show();


                }
            }
        });
    }else{
            Toast.makeText(forgetpassword.this, "Please make sure Email is Written!!", Toast.LENGTH_SHORT).show();

        }

    }

});




    }
    public void backClick(){

        Intent i = new Intent(forgetpassword.this, MainActivity.class);
        //i.putExtra("reportType","medical");
        startActivity(i);
        finish();
    }


}
