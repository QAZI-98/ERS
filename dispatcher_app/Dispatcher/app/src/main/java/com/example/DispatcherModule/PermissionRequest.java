package com.example.DispatcherModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PermissionRequest extends AppCompatActivity {

    int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    //locationReceiver mLocationReceiver;
    LocationManager lm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_request);
        Button prompt = (Button) findViewById(R.id.button2);
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        prompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(PromptLocationEnable.this, "Clicked!!", Toast.LENGTH_SHORT).show();
                //   requestPermission();
                ActivityCompat.requestPermissions(PermissionRequest.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            }


        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MY_PERMISSIONS_REQUEST_LOCATION){
            if(grantResults.length>0 &&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent i = new Intent(PermissionRequest.this, MainActivity.class);

                startActivity(i);
                finish();
                //   statusCheck();
            }
            else{
                Toast.makeText(PermissionRequest.this, "Permission Denied!!", Toast.LENGTH_SHORT).show();

            }
        }

    }


}
