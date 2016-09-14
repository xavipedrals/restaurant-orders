package com.example.xavi.comandesidi.Utils;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.view.View;


public class DialogFragmentUtils extends DialogFragment {

    public View.OnClickListener getDismissClickLitener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
