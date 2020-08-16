package com.example.DispatcherModule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class locationReceiver extends BroadcastReceiver {


        private final LocationCallBack locationCallBack;

        public locationReceiver(LocationCallBack locationCallBack) {
            this.locationCallBack = locationCallBack;
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                locationCallBack.onLocationTriggered();
            }
        }
    }


