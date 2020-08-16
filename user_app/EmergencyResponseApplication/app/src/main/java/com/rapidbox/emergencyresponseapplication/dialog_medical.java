package com.rapidbox.emergencyresponseapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class dialog_medical extends AppCompatDialogFragment{

    View view;
    private List<String> mSelectedItems;
    CheckBox mCheckbox_alzeimers,mCheckBox_cancer,mCheckBox_cholesterol,mCheckBox_cold,mCheckBox_depression,mCheckBox_diabetes,mCheckBox_pregnant;
    public dialog_medical.dialog_medical_interface listner;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        mSelectedItems = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();



        view = inflater.inflate(R.layout.dialog_medical_condition,null);

        mCheckbox_alzeimers = (CheckBox) view.findViewById(R.id.checkbox_alzeimers);
        mCheckBox_cancer = (CheckBox) view.findViewById(R.id.checkbox_cancer);
        mCheckBox_cholesterol = (CheckBox) view.findViewById(R.id.checkbox_cholesterol);
        mCheckBox_cold= (CheckBox) view.findViewById(R.id.checkbox_cold);
        mCheckBox_depression= (CheckBox) view.findViewById(R.id.checkbox_depression);
        mCheckBox_diabetes= (CheckBox) view.findViewById(R.id.checkbox_diabetes);
        mCheckBox_pregnant= (CheckBox) view.findViewById(R.id.checkbox_pregnant);


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
                        if(mCheckbox_alzeimers.isChecked()){
                            mSelectedItems.add(mCheckbox_alzeimers.getText().toString());

                        }else if (!mCheckbox_alzeimers.isChecked()){
                            mSelectedItems.remove(mCheckbox_alzeimers.getText().toString());

                        }
                        if(mCheckBox_cancer.isChecked()){
                            mSelectedItems.add(mCheckBox_cancer.getText().toString());

                        }else{
                            mSelectedItems.remove(mCheckBox_cancer.getText().toString());

                        }
                        if(mCheckBox_cholesterol.isChecked()){
                            mSelectedItems.add(mCheckBox_cholesterol.getText().toString());

                        }else{
                            mSelectedItems.remove(mCheckBox_cholesterol.getText().toString());

                        }
                        if(mCheckBox_cold.isChecked()){
                            mSelectedItems.add(mCheckBox_cold.getText().toString());

                        }else{
                            mSelectedItems.remove(mCheckBox_cold.getText().toString());

                        }
                        if(mCheckBox_depression.isChecked()){
                            mSelectedItems.add(mCheckBox_depression.getText().toString());

                        }else{
                            mSelectedItems.remove(mCheckBox_depression.getText().toString());

                        }
                        if(mCheckBox_diabetes.isChecked()){
                            mSelectedItems.add(mCheckBox_diabetes.getText().toString());

                        }else{
                            mSelectedItems.remove(mCheckBox_diabetes.getText().toString());

                        }
                        if(mCheckBox_pregnant.isChecked()){
                            mSelectedItems.add(mCheckBox_pregnant.getText().toString());

                        }else{
                            mSelectedItems.remove(mCheckBox_pregnant.getText().toString());

                        }



                        String mitems="";
                        for(String itemx:mSelectedItems){
                            mitems = mitems+""+itemx;

                        }

                        listner.applyTexts(mSelectedItems);
                       // Toast.makeText(dialog_medical.super.getContext(), "!!!!"+mitems, Toast.LENGTH_SHORT).show();

                    }
                });
        return builder.create();



    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listner = (dialog_medical.dialog_medical_interface) context;
        } catch (ClassCastException e) {
            //     Timber.d("%s did not implement ConfirmationDialogFragmentListener", activity);
        }

    }
    public void setDialogResult(dialog_medical.dialog_medical_interface dialogResult){
        listner =  dialogResult;
    }
    public interface dialog_medical_interface{
        void applyTexts(List<String> mSelectedItems);

    }

}
