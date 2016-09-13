package com.example.xavi.comandesidi.Utils;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.Utils.ConstantValues;

import java.text.DecimalFormat;

/**
 * Created by xavi on 13/12/15.
 */

/**Serveix per mostrar info en general**/
public class InfoDialog extends DialogFragment {

    //TODO: Move to Utils package
    private TextView acceptTV, totalTv, textTV, cancelTv;
    private OnInfoDialogDialogResultListener onInfoDialogDialogResultListener;

    public InfoDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_dialog, container);
        initVisualItems(view);
        switch (getDialogType()) {
            case "No products selected":
                configNoProductsSelectedDialog();
                break;
            case "See price":
                configShowPriceDialog();
                break;
            case "See total":
                configSeeTotalDialog();
                break;
            case "Deleting confirmation":
                configDeletingConfirmationDialog();
                break;
            case "Reset table plats":
                configResetDishesTableDialog();
                break;
            case "Reset table comandes":
                configResetOrdersTableDialog();
                break;
        }
        getDialog().setCancelable(true);
        return view;
    }

    private String getDialogType() {
        Bundle b = getArguments();
        return b.getString("Type", null);
    }

    private double getPrice() {
        Bundle b = getArguments();
        return b.getDouble("price", 0);
    }

    private void initVisualItems(View view) {
        acceptTV = (TextView) view.findViewById(R.id.textViewAccept);
        totalTv = (TextView) view.findViewById(R.id.textViewPriceDg);
        textTV = (TextView) view.findViewById(R.id.textViewPriceTx);
        cancelTv = (TextView) view.findViewById(R.id.textViewCancel);
    }

    private void configNoProductsSelectedDialog() {
        textTV.setText("Add any dish to the order");
        textTV.setTextColor(getResources().getColor(R.color.primary_text));
        totalTv.setVisibility(View.GONE);
        cancelTv.setVisibility(View.GONE);
        acceptTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void configSeeTotalDialog() {
        textTV.setText("The earnings ascend to");
        configShowPriceDialog();
    }

    private void configShowPriceDialog() {
        String roundedPrice = getRoundedPrice(getPrice());
        cancelTv.setVisibility(View.GONE);
        textTV.setTextColor(Color.argb(ConstantValues.alpha, 0, 0, 0));
        totalTv.setText(String.valueOf(roundedPrice + " €"));
        acceptTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private String getRoundedPrice(double price) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(price);
    }

    private void configDeletingConfirmationDialog() {
        textTV.setText("Do you really want to delete the dish?");
        configAcceptCancelDialog();
    }

    private void configResetDishesTableDialog() {
        textTV.setText("Do you really want to delete all the dishes?");
        configAcceptCancelDialog();
    }

    private void configResetOrdersTableDialog() {
        textTV.setText("Do you really want to delete all the orders?");
        configAcceptCancelDialog();
    }

    private void configAcceptCancelDialog() {
        textTV.setTextColor(Color.argb(ConstantValues.alpha, 0, 0, 0));
        totalTv.setVisibility(View.GONE);
        acceptTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInfoDialogDialogResultListener.onPositiveResult();
                dismiss();
            }
        });
        setCancelTvClickListener();
    }

    private void setCancelTvClickListener() {
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public interface OnInfoDialogDialogResultListener {
        public abstract void onPositiveResult();
        public abstract void onNegativeResult();
    }

    public void setOnInfoDialogDialogResultListener(OnInfoDialogDialogResultListener onInfoDialogDialogResultListener) {
        this.onInfoDialogDialogResultListener = onInfoDialogDialogResultListener;
    }
}
