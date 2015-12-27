package com.example.xavi.comandesidi.EditarPlats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xavi.comandesidi.R;

/**
 * Created by xavi on 20/12/15.
 */
public class EditTextDialog extends DialogFragment {

    //TODO: Fer canvi en els TextViews des de l'activity, no des de Dialog

    TextView acceptTV, cancelTv, missatgeTv;
    EditText respostaEt;
    private OnEditTextDialogResultListener onEditTextDialogResultListener;

    public EditTextDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_text_dialog, container);
        respostaEt = (EditText) view.findViewById(R.id.editTextResposta);
        acceptTV = (TextView) view.findViewById(R.id.textViewAccept);
        cancelTv = (TextView) view.findViewById(R.id.textViewCancel);
        missatgeTv = (TextView) view.findViewById(R.id.textViewMissatge);

        boolean isEditName = getArguments().getBoolean("isEditName");
        if(isEditName){
            respostaEt.setInputType(InputType.TYPE_CLASS_TEXT);
            missatgeTv.setText("Introdueix un nou nom:");
        }

        respostaEt.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditTextDialogResultListener.onNegativeResult();
                dismiss();
            }
        });
        acceptTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = respostaEt.getText().toString();
                onEditTextDialogResultListener.onPositiveResult(result);
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

    public interface OnEditTextDialogResultListener {
        public abstract void onPositiveResult(String result);
        public abstract void onNegativeResult();
    }

    public void setOnEditTextDialogResultListener(OnEditTextDialogResultListener listener) {
        this.onEditTextDialogResultListener = listener;
    }
}
