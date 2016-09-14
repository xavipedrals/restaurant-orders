package com.example.xavi.comandesidi.NewOrder;

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
import com.example.xavi.comandesidi.Utils.DialogFragmentUtils;

/**
 * Created by xavi on 13/12/15.
 */
public class TableDialog extends DialogFragmentUtils {

    TextView acceptTV, cancelTv;
    EditText tableNumberEt;
    private OnTableDialogResultListener onTableDialogResultListener;

    public TableDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.table_dialog, container);
        initVisuals(view);
        tableNumberEt.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        setCancelTvClickListener();
        setAcceptTVClickListener();

        getDialog().setCancelable(true);
        return view;
    }

    private void initVisuals(View view) {
        tableNumberEt = (EditText) view.findViewById(R.id.editTextnumTaula);
        acceptTV = (TextView) view.findViewById(R.id.textViewAccept);
        cancelTv = (TextView) view.findViewById(R.id.textViewCancel);
    }

    private void setCancelTvClickListener() {
        cancelTv.setOnClickListener(super.getDismissClickLitener());
    }

    private void setAcceptTVClickListener() {
        acceptTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tableNumber = Integer.parseInt(tableNumberEt.getText().toString());
                if (tableNumber <= 0 || tableNumber > 20)
                    Toast.makeText(getActivity().getApplicationContext(), "The table number must be between 1 and 20", Toast.LENGTH_LONG).show();
                else {
                    onTableDialogResultListener.onPositiveResult(tableNumber);
                    dismiss();
                }
            }
        });
    }

    public interface OnTableDialogResultListener {
        public abstract void onPositiveResult(int numTaula);
        public abstract void onNegativeResult();
    }

    public void setOnTableDialogResultListener(OnTableDialogResultListener listener) {
        this.onTableDialogResultListener = listener;
    }

}

