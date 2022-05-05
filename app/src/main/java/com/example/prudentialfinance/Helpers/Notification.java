package com.example.prudentialfinance.Helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.prudentialfinance.HomeActivity;
import com.example.prudentialfinance.R;

public class Notification {
    private String title;
    private String content;

    public Notification() {
    }

    /***
     *
     */
    @SuppressLint("UnspecifiedImmutableFlag")
    public void showNotification(Activity activityDepartment, String title, String content)
    {
        /*Step 1*/
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activityDepartment)
                .setSmallIcon(R.drawable.ic_baseline_message_24)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        /*Step 2*/
        Intent intent = new Intent(activityDepartment, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        /*Step 3*/
         PendingIntent pendingIntent =
                PendingIntent.getActivity(activityDepartment, 0,intent,PendingIntent.FLAG_UPDATE_CURRENT );
         builder.setContentIntent(pendingIntent);

         /*Step 4*/
        NotificationManagerCompat manager = NotificationManagerCompat.from(activityDepartment);
        manager.notify(0, builder.build());
    }
}
