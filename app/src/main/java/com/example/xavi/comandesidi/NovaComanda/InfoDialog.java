package com.example.xavi.comandesidi.NovaComanda;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xavi.comandesidi.R;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by xavi on 13/12/15.
 */

/**Serveix per mostrar info en general**/
public class InfoDialog extends DialogFragment {

    TextView acceptTV, totalTv, textTV;

    public InfoDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_dialog, container);
        acceptTV = (TextView) view.findViewById(R.id.textViewAccept);
        totalTv = (TextView) view.findViewById(R.id.textViewPriceDg);
        textTV = (TextView) view.findViewById(R.id.textViewPriceTx);
        Bundle b = getArguments();
        String type = b.getString("Type", null);

        switch (type) {
            case "No products selected":
                textTV.setText("Encara no has afegit cap producte a la comanda");
                textTV.setTextColor(getResources().getColor(R.color.primary_text));
                totalTv.setVisibility(View.GONE);
                acceptTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });

                break;

            case "See price":
                double price = b.getDouble("price", 0);
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String roundedPrice = decimalFormat.format(price);

                int alpha = 54 * 255 / 100; //54% de opacitat, secondary text

                textTV.setTextColor(Color.argb(alpha, 0, 0, 0));
                totalTv.setText(String.valueOf(roundedPrice + " €"));

                acceptTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
                break;

            case "Deleting confirmation":

                break;
        }

        getDialog().setCancelable(true);
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
