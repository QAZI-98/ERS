package com.rapidbox.emergencyresponseapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class notificationAlert extends AppCompatActivity {
    Switch mSwitch;
    SharedPreferences sharedPreferences;
    public static final String ex = "Switch";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_alert);
        mSwitch = (Switch)findViewById(R.id.switch1);

        sharedPreferences = getSharedPreferences(" ",MODE_PRIVATE);


       final SharedPreferences.Editor editor = sharedPreferences.edit();

       mSwitch.setChecked(sharedPreferences.getBoolean(ex,false));


 //       Boolean showNotification = sharedPreferences.getBoolean("switchValue",false);
   //     Toast.makeText(this, ""+showNotification, Toast.LENGTH_SHORT).show();
       /* if(mSwitch.isChecked()==showNotification){

            mSwitch.setChecked(false);
        }
        else{
            mSwitch.setChecked(true);
        }*/
mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
        if(ischecked){
            editor.putBoolean(ex,true);
        }else{
            editor.putBoolean(ex,false);
        }
        editor.commit();
    }
});

        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSwitch.isChecked()){

                    final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(notificationAlert.this,R.style.AlertDialog);

                        dialog.setTitle("ALERT");
                        dialog.setCancelable(false);
                        dialog.setMessage("Do you really want to Turn on Notification Alerts ?");
                        LayoutInflater inflater = LayoutInflater.from(notificationAlert.this);
                        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
    //                            editor.putBoolean(ex,true);

            //                    editor.putBoolean("switchValue",mSwitch.isChecked());
              //                  editor.apply();
                                Toast.makeText(notificationAlert.this, "Notification Turned on", Toast.LENGTH_SHORT).show();
                                startService(new Intent(notificationAlert.this, route_service.class));

                                dialogInterface.dismiss();
                                mSwitch.setChecked(true);
                            }
                        });
                        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editor.putBoolean(ex,false);


                                stopService(new Intent(notificationAlert.this, route_service.class));
                                dialogInterface.dismiss();
                                mSwitch.setChecked(false);
                            }
                        });


                        dialog.show();


                }
                else{

                    editor.putBoolean(ex,false);
                    final androidx.appcompat.app.AlertDialog.Builder dialog = new androidx.appcompat.app.AlertDialog.Builder(notificationAlert.this,R.style.AlertDialog);

                    dialog.setTitle("ALERT");
                    dialog.setCancelable(false);
                    dialog.setMessage("Do you really want to Turned off Notification Alerts ?");
                    LayoutInflater inflater = LayoutInflater.from(notificationAlert.this);
                    dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            stopService(new Intent(notificationAlert.this, route_service.class));
                            mSwitch.setChecked(false);
                            dialogInterface.dismiss();
                            editor.putBoolean(ex,false);

                            Toast.makeText(notificationAlert.this, "You won't be able to get Notification Alerts!!", Toast.LENGTH_SHORT).show();

                        }
                    });
                    dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            editor.putBoolean(ex,true);


                            startService(new Intent(notificationAlert.this, route_service.class));

                            dialogInterface.dismiss();
                            mSwitch.setChecked(true);
                        }
                    });

editor.commit();
                    dialog.show();


                }
            }
        });
    }
}
