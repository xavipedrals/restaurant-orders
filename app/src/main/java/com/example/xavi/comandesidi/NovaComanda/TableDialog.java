package com.example.xavi.comandesidi.NovaComanda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xavi.comandesidi.R;

import java.text.DecimalFormat;

/**
 * Created by xavi on 13/12/15.
 */
public class TableDialog extends DialogFragment {

    TextView acceptTV, cancelTv;
    EditText numeroTaulaEt;
    private OnTableDialogResultListener onTableDialogResultListener;

    public TableDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.table_dialog, container);
        numeroTaulaEt = (EditText) view.findViewById(R.id.editTextnumTaula);
        acceptTV = (TextView) view.findViewById(R.id.textViewAccept);
        cancelTv = (TextView) view.findViewById(R.id.textViewCancel);

        numeroTaulaEt.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTableDialogResultListener.onNegativeResult();
                dismiss();
            }
        });
        acceptTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numTaula = Integer.parseInt(numeroTaulaEt.getText().toString());
                if (numTaula <= 0 || numTaula > 20)
                    Toast.makeText(getActivity().getApplicationContext(), "El número de taula ha d'estar entre 1 i 20", Toast.LENGTH_LONG).show();
                else {
                    onTableDialogResultListener.onPositiveResult(numTaula);
                    dismiss();
                }
            }
        });

        getDialog().setCancelable(true);
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public interface OnTableDialogResultListener {
        public abstract void onPositiveResult(int numTaula);
        public abstract void onNegativeResult();
    }

    public void setOnTableDialogResultListener(OnTableDialogResultListener listener) {
        this.onTableDialogResultListener = listener;
    }

}

