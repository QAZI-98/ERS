package com.rapidbox.emergencyresponseapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class dialog_blood extends AppCompatDialogFragment {

    View view;
    private List<String> mSelectedItems;
    RadioButton mCheckbox_AB_plus,mCheckBox_AB_minus,mCheckBox_B_plus,mCheckBox_B_minus,mCheckBox_A_plus,mCheckBox_A_minus,mCheckBox_O_plus,mCheckbox_O_minus;
    public dialog_blood.dialog_blood_interface listner;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        mSelectedItems = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();



        view = inflater.inflate(R.layout.dialog_blood,null);

        mCheckbox_AB_plus = (RadioButton) view.findViewById(R.id.checkbox_AB_plus);
        mCheckBox_AB_minus = (RadioButton) view.findViewById(R.id.checkbox_AB_minus);
        mCheckBox_B_plus = (RadioButton) view.findViewById(R.id.checkbox_B_plus);
        mCheckBox_B_minus= (RadioButton) view.findViewById(R.id.checkbox_B_minus);
        mCheckBox_A_plus= (RadioButton) view.findViewById(R.id.checkbox_A_plus);
        mCheckBox_A_minus= (RadioButton) view.findViewById(R.id.checkbox_A_minus);
       mCheckBox_O_plus = (RadioButton) view.findViewById(R.id.checkbox_O_plus);
        mCheckbox_O_minus = (RadioButton) view.findViewById(R.id.checkbox_O_minus);


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
                        if(mCheckbox_AB_plus.isChecked()){
                            mSelectedItems.add(mCheckbox_AB_plus.getText().toString());

                        }else if (!mCheckbox_AB_plus.isChecked()){
                            mSelectedItems.remove(mCheckbox_AB_plus.getText().toString());

                        }
                        if(mCheckBox_AB_minus.isChecked()){
                            mSelectedItems.add(mCheckBox_AB_minus.getText().toString());

                        }else{
                            mSelectedItems.remove(mCheckBox_AB_minus.getText().toString());

                        }
                        if(mCheckBox_A_plus.isChecked()){
                            mSelectedItems.add(mCheckBox_A_plus.getText().toString());

                        }else{
                            mSelectedItems.remove(mCheckBox_A_plus.getText().toString());

                        }
                        if(mCheckBox_A_minus.isChecked()){
                            mSelectedItems.add(mCheckBox_A_minus.getText().toString());

                        }else{
                            mSelectedItems.remove(mCheckBox_A_minus.getText().toString());

                        }

                        if(mCheckBox_B_plus.isChecked()){
                            mSelectedItems.add(mCheckBox_B_plus.getText().toString());

                        }else{
                            mSelectedItems.remove(mCheckBox_B_plus.getText().toString());

                        }
                        if(mCheckBox_B_minus.isChecked()){
                            mSelectedItems.add(mCheckBox_B_minus.getText().toString());

                        }else{
                            mSelectedItems.remove(mCheckBox_B_minus.getText().toString());

                        }
                        if(mCheckBox_O_plus.isChecked()){
                            mSelectedItems.add(mCheckBox_O_plus.getText().toString());

                        }else{
                            mSelectedItems.remove(mCheckBox_O_plus.getText().toString());

                        }
                        if(mCheckbox_O_minus.isChecked()){
                            mSelectedItems.add(mCheckbox_O_minus.getText().toString());

                        }else{
                            mSelectedItems.remove(mCheckbox_O_minus.getText().toString());

                        }



                        String mitems="";
                        for(String itemx:mSelectedItems){
                            mitems = mitems+""+itemx;

                        }

                        listner.applyTexts(mSelectedItems);
                     //   Toast.makeText(dialog_blood.super.getContext(), "!!!!"+mitems, Toast.LENGTH_SHORT).show();

                    }
                });
        return builder.create();



    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listner = (dialog_blood.dialog_blood_interface) context;
        } catch (ClassCastException e) {
            //     Timber.d("%s did not implement ConfirmationDialogFragmentListener", activity);
        }

    }
    public void setDialogResult(dialog_blood.dialog_blood_interface dialogResult){
        listner =  dialogResult;
    }
    public interface dialog_blood_interface{
        void applyTexts(List<String> mSelectedItems);

    }


}
