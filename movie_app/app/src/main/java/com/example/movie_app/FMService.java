package com.example.movie_app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FMService extends FirebaseMessagingService {

    private static int NOTIFY_TAG = 123;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        PendingIntent pendingIntent = PendingIntent.
                getActivity(getBaseContext(), NOTIFY_TAG,
                        new Intent(getApplicationContext(), MainActivity.class),
                        PendingIntent.FLAG_CANCEL_CURRENT);


//Criamos a notificacao e capturamos os dados
        Notification notification = new NotificationCompat.Builder(this,"qui")
                .setSmallIcon(R.drawable.bluelogo)
                .setContentTitle(getString(R.string.app_name) + " - "+remoteMessage.getNotification().getTitle())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(remoteMessage.getNotification().getBody()))
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 2000})
                .setContentText(remoteMessage.getNotification().getBody())
                .setContentIntent(pendingIntent).build();

        Context context = getBaseContext();
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        manager.notify(NOTIFY_TAG, notification);
    }
}
