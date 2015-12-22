package com.example.xavi.comandesidi.StocProductes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.xavi.comandesidi.R;

import java.text.DecimalFormat;

/**
 * Created by xavi on 22/12/15.
 */
public class StockDialog extends DialogFragment {

    TextView acceptTV, cancelTv, totalTv, textTV;
    LinearLayout quantitatLayout;
    RadioButton radioButtonInc, radioButtonDec, radioButtonZero;


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
                if(radioButtonZero.isChecked()){

                } else if (radioButtonInc.isChecked()){

                } else if (radioButtonDec.isChecked()){

                }
            }
        });

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        getDialog().setCancelable(true);
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}