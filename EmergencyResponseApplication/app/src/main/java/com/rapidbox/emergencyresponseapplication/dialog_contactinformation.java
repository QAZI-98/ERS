package com.rapidbox.emergencyresponseapplication;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class dialog_contactinformation  extends AppCompatDialogFragment {
    View view;
    EditText mName,mPhone;
    Button mSave,mDiscard;
    public dialog_contactinformation.dialog_contactinformation_interface listner;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.dialog_emergency_contact,null);

        mSave = (Button)view.findViewById(R.id.btnsave);
        mDiscard = (Button)view.findViewById(R.id.btndiscard);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nametxt = mName.getText().toString();
                String phonetxt = mPhone.getText().toString();

                    boolean b1=false;
                    boolean b2=false;
                if(nametxt.isEmpty() ){
                        mName.setError("Please Enter Name");
                    b1 = false;
                }
                    else{
                        b1 = true;
                    }
                if(phonetxt.isEmpty() ){
                    mPhone.setError("Please Enter Phone");
                    b2 = false;
                }
                else{
                    b2 = true;
                }
                if(b1 ==true &&b2==true) {
                    listner.applyTexts(nametxt, phonetxt);
                    dismiss();
                }
            }
        });
        mDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
dismiss();
            }
        });


        mName = (EditText) view.findViewById(R.id.person);
        mPhone = (EditText) view.findViewById(R.id.phone);

        builder.setView(view);

        return builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listner = (dialog_contactinformation_interface) context;
        } catch (ClassCastException e) {
            //     Timber.d("%s did not implement ConfirmationDialogFragmentListener", activity);
        }

    }
    public void setDialogResult(dialog_contactinformation_interface dialogResult){
        listner =  dialogResult;
    }
    public interface dialog_contactinformation_interface{
        void applyTexts(String pname,String pphone);

    }
}
