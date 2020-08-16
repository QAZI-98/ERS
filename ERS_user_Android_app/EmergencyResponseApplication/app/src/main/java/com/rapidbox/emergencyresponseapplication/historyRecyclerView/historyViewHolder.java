package com.rapidbox.emergencyresponseapplication.historyRecyclerView;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rapidbox.emergencyresponseapplication.R;

public class historyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView rideId;




    public historyViewHolder( View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        rideId = (TextView) itemView.findViewById(R.id.rideId);

    }

    @Override
    public void onClick(View view) {

    }
}
