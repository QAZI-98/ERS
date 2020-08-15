package com.rapidbox.emergencyresponseapplication;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class route_service extends Service {

    public void show_loc() {

        sql q=new sql(this);
        Cursor cursor = q.fetch("select email from routes");
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext())
            {

            }

        } else {
            Toast.makeText(this, "db empty", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    FusedLocationProviderClient client;
    String tag="service";
    String email;
    String iid;
    int channel_id;
    FirebaseAuth auth;
    int i=0;
    sql sql_query;
    int counter_for_notification_channel_id;
    SimpleDateFormat sdf;
    long diffindays;
    Date firstDate;
    Date secondDate;


    private NotificationManagerCompat notificationmanagercompat;
    @Override
    public void onCreate() {
        client= LocationServices.getFusedLocationProviderClient(this);
        sql_query=new sql(this);
        //   Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        auth=FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!=null)
            read();
        return super.onStartCommand(intent, flags, startId);
    }

    public void read()
    {



        sdf=new SimpleDateFormat("d/MM/yy");
        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("incident");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    email=auth.getCurrentUser().getEmail();

                    if (email!=null) {
//
                        String date_toaday = sdf.format(new Date());
                        try {

                            firstDate = sdf.parse(ds.child("date").getValue(String.class));
                            secondDate = sdf.parse(date_toaday);
                            diffindays = secondDate.getTime() - firstDate.getTime();
                            diffindays = diffindays / (24 * 60 * 60 * 1000);
                            Log.i("mycheck",Long.toString(diffindays));

                        } catch (Exception e) {
                        }


                        if (diffindays<3) {

                            String _email = ds.child("email").getValue(String.class);
                            if (!(email.equals(_email))) {

                                Cursor notify_cursor = sql_query.fetch("select id,nid from notifications where email='" + email + "'");
                                boolean notify_saved = false;
                                iid = ds.child("iid").getValue(String.class);
                                if (notify_cursor.getCount() != 0) {
                                    while (notify_cursor.moveToNext() && !(notify_saved)) {
                                        counter_for_notification_channel_id = Integer.parseInt(notify_cursor.getString(0));
                                        String nid = notify_cursor.getString(1);
                                        if (nid.equals(iid)) {
                                            notify_saved = true;
                                            break;

                                        }
                                    }

                                }


                                if (!notify_saved) {
//                                    Log.i("inside",Long.toString(diffindays));

                                    Cursor cursor = sql_query.fetch("select latitude,longitude from routes where email= '" + email + "'");
                                    boolean check = false;
                                    if (cursor.getCount() != 0 && !(check)) {
                                        while (cursor.moveToNext() && !(check)) {
                                            String lat = cursor.getString(0);
                                            String longe = cursor.getString(1);
                                            String slat = ds.child("latitude").getValue(String.class);
                                            String slong = ds.child("longitude").getValue(String.class);
                                            LatLng current = new LatLng(Double.parseDouble(lat), Double.parseDouble(longe));
                                            LatLng destination = new LatLng(Double.parseDouble(slat), Double.parseDouble(slong));

                                            Double distance = SphericalUtil.computeDistanceBetween(current, destination);
                                            if (distance < 100) {

                                                sql_query.insert(iid, email);
                                                notificationmanagercompat = NotificationManagerCompat.from(route_service.this);
                                                Notification notification = new NotificationCompat
                                                        .Builder(route_service.this, notifications.channel_id)
                                                        .setSmallIcon(R.drawable.document)
                                                        .setColor(Color.RED)
                                                        .setContentText(ds.child("type").getValue(String.class) + " near your route check news feed")
                                                        .setContentTitle("ERS: Alert!!")
                                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                                        .build();

                                                notificationmanagercompat.notify(counter_for_notification_channel_id, notification);
                                                check = true;

                                                break;
                                            }

                                        }

                                    }
                                }


                            }//
                        }
                        else
                        {Log.i("acheck","morethan 2");}
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Toast.makeText(addpost.this, "error", Toast.LENGTH_SHORT).show();
            }
        };
        reference.addValueEventListener(eventListener);

    }


}