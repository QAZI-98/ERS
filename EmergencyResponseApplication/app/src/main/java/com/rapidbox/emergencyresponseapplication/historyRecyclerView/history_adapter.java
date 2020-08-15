package com.rapidbox.emergencyresponseapplication.historyRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rapidbox.emergencyresponseapplication.R;

import java.util.List;

public class history_adapter extends RecyclerView.Adapter<history_adapter.ViewHolder> {


    private List<historyObject> itemlist;
    private Context context;

    public history_adapter(List<historyObject> itemlist ,Context context){
        this.itemlist = itemlist;
        this.context = context;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);


        return new ViewHolder(layoutView);


    }

    /*
        @Override
        public historyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,null,false);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutView.setLayoutParams(lp);
            historyViewHolder rcv = new historyViewHolder(layoutView);
            return rcv;
        }
    */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    historyObject histObj = itemlist.get(position);


        holder.rideId.setText(histObj.getRideId());
        holder.timeTxt.setText(histObj.getTime());
        holder.reportTxt.setText(histObj.getReportType());
        holder.fromTxt.setText(histObj.getFromLocation());
        holder.toTxt.setText(histObj.getToLocation());
        holder.teamTypeTxt.setText(histObj.getTeamType());
     //   holder.teamNameTxt.setText(histObj.getTeamName());
       // holder.teamPhoneTxt.setText(histObj.getTeamPhone());



    }
/*
    @Override
    public void onBindViewHolder(historyViewHolder holder, int position) {

        holder.rideId.setText(itemlist.get(position).getRideId());
    }
*/
    @Override
    public int getItemCount() {
        return itemlist.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView rideId;
        public TextView reportTxt;
        public TextView timeTxt;
        public TextView fromTxt;
        public TextView toTxt;

        public TextView teamTypeTxt;
       // public TextView teamNameTxt;
       // public TextView teamPhoneTxt;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rideId = (TextView) itemView.findViewById(R.id.rideId);

            reportTxt = (TextView) itemView.findViewById(R.id.reportypeshow);
            timeTxt = (TextView) itemView.findViewById(R.id.time);
            fromTxt = (TextView) itemView.findViewById(R.id.fromAddress);
            toTxt = (TextView) itemView.findViewById(R.id.toAddress);

            teamTypeTxt = (TextView) itemView.findViewById(R.id.teamTypeID);
         //   teamNameTxt = (TextView) itemView.findViewById(R.id.teamname);
           // teamPhoneTxt = (TextView) itemView.findViewById(R.id.phoneNo);

        }
    }
}
