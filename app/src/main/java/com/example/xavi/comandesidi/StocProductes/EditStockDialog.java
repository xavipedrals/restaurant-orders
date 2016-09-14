package com.example.xavi.comandesidi.StocProductes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xavi.comandesidi.R;
import com.example.xavi.comandesidi.Utils.DialogFragmentUtils;


public class EditStockDialog extends DialogFragmentUtils {

    private TextView acceptTV, cancelTv, totalTv, textTV;
    private LinearLayout quantitatLayout;
    private RadioButton radioButtonInc, radioButtonDec, radioButtonZero;
    private EditText editText;
    private OnStockDialogResultListener onStockDialogResultListener;

    public EditStockDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.stoc_dialog, container);
        initVisuals(view);
        setIncrementRadioButtonClickListener();
        setDecrementRadioButtonClickListener();
        setZeroRadioButtonClickListener();
        setAcceptTVClickListener();
        setCancelTvClickListener();
        getDialog().setCancelable(true);
        return view;
    }

    private void initVisuals(View view) {
        acceptTV = (TextView) view.findViewById(R.id.textViewAccept);
        cancelTv = (TextView) view.findViewById(R.id.textViewCancel);
        totalTv = (TextView) view.findViewById(R.id.textViewPriceDg);
        textTV = (TextView) view.findViewById(R.id.textViewPriceTx);
        quantitatLayout = (LinearLayout) view.findViewById(R.id.layoutQuantitat);
        editText = (EditText) view.findViewById(R.id.editText);
        radioButtonInc = (RadioButton) view.findViewById(R.id.radioButton);
        radioButtonDec = (RadioButton) view.findViewById(R.id.radioButton2);
        radioButtonZero = (RadioButton) view.findViewById(R.id.radioButton3);
    }

    private void setIncrementRadioButtonClickListener() {
        radioButtonInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantitatLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setDecrementRadioButtonClickListener() {
        radioButtonDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantitatLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setZeroRadioButtonClickListener() {
        radioButtonZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantitatLayout.setVisibility(View.GONE);
            }
        });
    }

    private void setAcceptTVClickListener() {
        acceptTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioButtonZero.isChecked()){
                    manageZeroOption();
                }
                else if (radioButtonInc.isChecked()){
                    manageIncrementOption();
                }
                else if (radioButtonDec.isChecked()){
                    manageDecrementOption();
                }
            }
        });
    }

    private void manageZeroOption() {
        Bundle bundle = new Bundle();
        bundle.putString("opcio", "zero");
        onStockDialogResultListener.onPositiveResult(bundle);
        dismiss();
    }

    private void manageIncrementOption() {
        if(checkQuantityNotDefined()) {
            showInsertQuantityToast();
        }
        else {
            makeStockIncrement();
        }
    }

    private void manageDecrementOption() {
        if(checkQuantityNotDefined()) {
            showInsertQuantityToast();
        }
        else {
            makeStockDecrement();
        }
    }

    private boolean checkQuantityNotDefined() {
        return editText.getText().toString().equals("");
    }

    private void showInsertQuantityToast() {
        Toast.makeText(getActivity().getApplicationContext(), "Insert a quantity", Toast.LENGTH_LONG).show();
    }

    private void makeStockIncrement() {
        Bundle bundle = new Bundle();
        bundle.putString("opcio", "incrementar");
        bundle.putInt("quantitat", Integer.parseInt(editText.getText().toString()));
        onStockDialogResultListener.onPositiveResult(bundle);
        dismiss();
    }

    private void makeStockDecrement() {
        Bundle bundle = new Bundle();
        bundle.putString("opcio", "decrementar");
        bundle.putInt("quantitat", Integer.parseInt(editText.getText().toString()));
        onStockDialogResultListener.onPositiveResult(bundle);
        dismiss();
    }

    private void setCancelTvClickListener() {
        cancelTv.setOnClickListener(super.getDismissClickLitener());
    }

    public interface OnStockDialogResultListener {
        public abstract void onPositiveResult(Bundle bundle);
        public abstract void onNegativeResult();
    }

    public void setOnStockDialogResultListener(OnStockDialogResultListener listener) {
        this.onStockDialogResultListener = listener;
    }

}