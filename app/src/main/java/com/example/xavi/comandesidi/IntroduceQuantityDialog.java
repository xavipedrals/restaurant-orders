package com.example.xavi.comandesidi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class IntroduceQuantityDialog extends DialogFragment {

    TextView acceptTV, cancelTv;
    EditText quantityEt;
    OnDialogResultListener onDialogResultListener;

    public IntroduceQuantityDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intr_quant_dialog, container);
        initVisuals(view);
        configQuantityEt();
        setCancelClickListener();
        setAcceptClickListener();
        getDialog().setCancelable(true);
        return view;
    }

    private void initVisuals(View view) {
        quantityEt = (EditText) view.findViewById(R.id.editTextQuant);
        acceptTV = (TextView) view.findViewById(R.id.textViewAccept);
        cancelTv = (TextView) view.findViewById(R.id.textViewCancel);
    }

    private void configQuantityEt() {
        quantityEt.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void setCancelClickListener() {
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDialogResultListener.onNegativeResult();
                dismiss();
            }
        });
    }

    private void setAcceptClickListener() {
        acceptTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = quantityEt.getText().toString();
                if(!string.equals("")){
                    int i = Integer.parseInt(quantityEt.getText().toString());
                    if(i>-1){
                        onDialogResultListener.onPositiveResult(i);
                    }
                }
                dismiss();
            }
        });
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