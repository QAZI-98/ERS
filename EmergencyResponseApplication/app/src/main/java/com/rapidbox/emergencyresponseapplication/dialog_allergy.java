package com.rapidbox.emergencyresponseapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class dialog_allergy extends AppCompatDialogFragment {

    View view;
    private List<String> mSelectedItems;
    CheckBox mCheckBox_drug,mCheckBox_dust,mCheckBox_skin,mCheckBox_food;
    public dialog_allergy_interface listner;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


    mSelectedItems = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();



        view = inflater.inflate(R.layout.dialog_allergy,null);

        mCheckBox_drug = (CheckBox) view.findViewById(R.id.checkbox_drug);
        mCheckBox_dust = (CheckBox) view.findViewById(R.id.checkbox_dust);
        mCheckBox_skin = (CheckBox) view.findViewById(R.id.checkbox_skin);
        mCheckBox_food = (CheckBox) view.findViewById(R.id.checkbox_food);







  /*      builder.setMultiChoiceItems(R.array.allergies, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                String[] items = getActivity().getResources().getStringArray(R.array.allergies);
                if(isChecked){

                    mSelectedItems.add(items[which]);
                }else if(mSelectedItems.contains(items[which])){
                    mSelectedItems.remove(items[which]);
                }

            }
        });*/

        builder.setView(view)
                .setTitle("")
                .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mCheckBox_drug.isChecked()){
                            mSelectedItems.add(mCheckBox_drug.getText().toString());

                        }else if (!mCheckBox_food.isChecked()){
                            mSelectedItems.remove("Drug");

                        }
                        if(mCheckBox_dust.isChecked()){
                            mSelectedItems.add("Dust");

                        }else{
                            mSelectedItems.remove("Dust");

                        }
                        if(mCheckBox_skin.isChecked()){
                            mSelectedItems.add("Skin");

                        }else{
                            mSelectedItems.remove("Skin");

                        }
                        if(mCheckBox_food.isChecked()){
                            mSelectedItems.add("Food");

                        }else{
                            mSelectedItems.remove("Food");

                        }

                        String mitems="";
                        for(String itemx:mSelectedItems){
                            mitems = mitems+""+itemx;

                        }

                        listner.applyTexts(mSelectedItems);
                        //Toast.makeText(dialog_allergy.super.getContext(), "!!!!"+mitems, Toast.LENGTH_SHORT).show();

                    }
                });
        return builder.create();


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listner = (dialog_allergy_interface) context;
        } catch (ClassCastException e) {
       //     Timber.d("%s did not implement ConfirmationDialogFragmentListener", activity);
        }

    }
    public void setDialogResult(dialog_allergy_interface dialogResult){
        listner = dialogResult;
    }
    public interface dialog_allergy_interface{
        void applyTexts(List<String> mSelectedItems);

    }
}

