package com.rapidbox.emergencyresponseapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class Custom_window_adapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public Custom_window_adapter(Context mContext) {
        this.mContext = mContext;
        mWindow = LayoutInflater.from(mContext).inflate(R.layout.custom_window_info,null);

    }
    private void rendowWindowText(Marker marker,View view){
        String title = marker.getTitle();
        TextView tvTitle = (TextView)view.findViewById(R.id.title);
        if(!title.equals("")){
            tvTitle.setText("PICKUP LOCATION");

        }
        String snippet = marker.getSnippet();
        TextView tvSnippet = (TextView)view.findViewById(R.id.snippet);
        if(!snippet.equals("")){
            tvSnippet.setText("You should stand nearby pickup location");

        }

    }
    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker,mWindow);
        return mWindow;


    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker,mWindow);
        return mWindow;
    }
}
