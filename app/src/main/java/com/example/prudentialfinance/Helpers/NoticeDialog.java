package com.example.prudentialfinance.Helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.widget.TextView;

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
            activity.finish();
        });
        dialog.show();
    }

    public void showDialogWithContent(Activity activity, String message)
    {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.notification_with_content);

        AppCompatButton buttonDismiss = dialog.findViewById(R.id.buttonDismiss);
        TextView content = dialog.findViewById(R.id.contentNotification);

        content.setText(message);


        buttonDismiss.setOnClickListener(view->{
            dialog.dismiss();
            activity.finish();
        });
        dialog.show();
    }
}
