package com.example.xavi.comandesidi.StocProductes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xavi.comandesidi.R;

import java.text.DecimalFormat;

/**
 * Created by xavi on 22/12/15.
 */
public class StockDialog extends DialogFragment {

    private TextView acceptTV, cancelTv, totalTv, textTV;
    private LinearLayout quantitatLayout;
    private RadioButton radioButtonInc, radioButtonDec, radioButtonZero;
    private EditText editText;
    private OnStockDialogResultListener onStockDialogResultListener;


    public StockDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stoc_dialog, container);
        acceptTV = (TextView) view.findViewById(R.id.textViewAccept);
        cancelTv = (TextView) view.findViewById(R.id.textViewCancel);
        totalTv = (TextView) view.findViewById(R.id.textViewPriceDg);
        textTV = (TextView) view.findViewById(R.id.textViewPriceTx);
        quantitatLayout = (LinearLayout) view.findViewById(R.id.layoutQuantitat);
        editText = (EditText) view.findViewById(R.id.editText);

        radioButtonInc = (RadioButton) view.findViewById(R.id.radioButton);
        radioButtonDec = (RadioButton) view.findViewById(R.id.radioButton2);
        radioButtonZero = (RadioButton) view.findViewById(R.id.radioButton3);
        radioButtonInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantitatLayout.setVisibility(View.VISIBLE);
            }
        });
        radioButtonDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantitatLayout.setVisibility(View.VISIBLE);
            }
        });
        radioButtonZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantitatLayout.setVisibility(View.GONE);
            }
        });

        acceptTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                if(radioButtonZero.isChecked()){
                    bundle.putString("opcio", "zero");
                    onStockDialogResultListener.onPositiveResult(bundle);
                    dismiss();
                } else if (radioButtonInc.isChecked()){
                    if(editText.getText().toString().equals(""))
                        Toast.makeText(getActivity().getApplicationContext(), "Introdueix una quantitat", Toast.LENGTH_LONG).show();
                    else {
                        bundle.putString("opcio", "incrementar");
                        bundle.putInt("quantitat", Integer.parseInt(editText.getText().toString()));
                        onStockDialogResultListener.onPositiveResult(bundle);
                        dismiss();
                    }
                } else if (radioButtonDec.isChecked()){
                    if(editText.getText().toString().equals(""))
                        Toast.makeText(getActivity().getApplicationContext(), "Introdueix una quantitat", Toast.LENGTH_LONG).show();
                    else {
                        bundle.putString("opcio", "decrementar");
                        bundle.putInt("quantitat", Integer.parseInt(editText.getText().toString()));
                        onStockDialogResultListener.onPositiveResult(bundle);
                        dismiss();
                    }
                }
            }
        });

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public interface OnStockDialogResultListener {
        public abstract void onPositiveResult(Bundle bundle);
        public abstract void onNegativeResult();
    }

    public void setOnStockDialogResultListener(OnStockDialogResultListener listener) {
        this.onStockDialogResultListener = listener;
    }

}