package com.rapidbox.emergencyresponseapplication;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String CHANNEL_1_ID = "AmbulanceArrived";
    public static final String CHANNEL_2_ID = "PoliceArrived";


    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();

    }
  public void createNotificationChannels(){

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Ambulance Arrived",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Ambulance has arrieved at your location");
            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Police Arrived",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel2.setDescription("Police has arrived at your location");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);

        }
    }
}
