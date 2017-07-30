package com.psu.sweng500.team4.parkpal.Services;

/**
 * Created by brhoads on 7/13/2017.
 */

import com.microsoft.windowsazure.notifications.NotificationsHandler;
import com.psu.sweng500.team4.parkpal.MainActivity;
import com.psu.sweng500.team4.parkpal.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationService extends NotificationsHandler {

    public static final int NOTIFICATION_ID = 1;
    private static final String recommendations = "ParkPal just sent you new recommendations";

    @Override
    public void onRegistered(Context context,  final String gcmRegistrationId) {
        super.onRegistered(context, gcmRegistrationId);

        new AsyncTask<Void, Void, Void>() {

            protected Void doInBackground(Void... params) {
                try {
                    MainActivity.mClient.getPush().register(gcmRegistrationId);
                    return null;
                } catch (Exception e) {
                    // handle error
                }
                return null;
            }
        }.execute();
    }
    @Override
    public void onReceive(Context context, Bundle bundle) {
        String msg;

        Intent intent = new Intent(context, MainActivity.class);

        if (bundle.getString("message").equals("recommendations")){
            msg = recommendations;
            intent.putExtra("fragment", "recommendations");
        }
        else{
            msg = bundle.getString("message");
        }

        Log.d("NOTIFICATION", msg);

        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, // requestCode
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT); // flags

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.camp_icon)
                .setContentTitle("ParkPal Notifications")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setContentIntent(contentIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
