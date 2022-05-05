package com.example.prudentialfinance.Helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;

import androidx.appcompat.widget.AppCompatButton;

import com.example.prudentialfinance.R;

public class NoticeDialog {

    private Activity activity;

    @SuppressLint("ResourceType")
    public void showDialog(Activity activity, int layout)
    {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(layout);

        AppCompatButton buttonDismiss = dialog.findViewById(R.id.buttonDismiss);

        buttonDismiss.setOnClickListener(view->{
            dialog.dismiss();
        });
        dialog.show();
    }
}