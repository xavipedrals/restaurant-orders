package com.example.xavi.comandesidi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by xavi on 13/12/15.
 */
public class IntrQuantDialog extends DialogFragment {

    TextView acceptTV, cancelTv;
    EditText intrQuantEt;
    OnDialogResultListener onDialogResultListener;

    public IntrQuantDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intr_quant_dialog, container);
        intrQuantEt = (EditText) view.findViewById(R.id.editTextQuant);
        acceptTV = (TextView) view.findViewById(R.id.textViewAccept);
        cancelTv = (TextView) view.findViewById(R.id.textViewCancel);

        intrQuantEt.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDialogResultListener.onNegativeResult();
                dismiss();
            }
        });
        acceptTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = intrQuantEt.getText().toString();
                if(!string.equals("")){
                    int i = Integer.parseInt(intrQuantEt.getText().toString());
                    if(i>-1){
                        onDialogResultListener.onPositiveResult(i);
                    }
                }
                dismiss();
            }
        });

        getDialog().setCancelable(true);
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public interface OnDialogResultListener {
        public abstract void onPositiveResult(int value);
        public abstract void onNegativeResult();
    }

    public void setOnDialogResultListener(OnDialogResultListener listener) {
        this.onDialogResultListener = listener;
    }

}