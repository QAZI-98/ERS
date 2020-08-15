package com.rapidbox.emergencyresponseapplication;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.widget.Toast;

public class notifications extends Application {

    public static final String channel_id="c1";
    @Override
    public void onCreate() {
        super.onCreate();
        create_notification();

    }

    /////////////////////////////
    /////////   check here if any issue revisit
    /////////////////////////////////
    public  void create_notification( ) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel nc = new NotificationChannel(channel_id, "channel1", NotificationManager.IMPORTANCE_HIGH);
            nc.setDescription("this is c1");
            NotificationManager mgr = getSystemService(NotificationManager.class);
            if (mgr != null) {
                mgr.createNotificationChannel(nc);
            }
        }
        else{
           // Toast.makeText(this, "abcd", Toast.LENGTH_SHORT).show();
        }


    }

}

