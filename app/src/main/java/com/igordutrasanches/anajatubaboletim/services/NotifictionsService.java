package com.igordutrasanches.anajatubaboletim.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.igordutrasanches.anajatubaboletim.MainActivity;

public class NotifictionsService extends FirebaseMessagingService {

    public NotifictionsService() {
        super();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage.getNotification() != null){
            mostrarNotificacao(remoteMessage.getNotification());
        }
    }

    private void mostrarNotificacao(RemoteMessage.Notification notification) {
        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pending = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String title =notification.getTitle();
        String body = notification.getBody();
        Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder _builder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(som)
                .setContentIntent(pending);

        NotificationManager _NotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        _NotificationManager.notify(0, _builder.build());

    }

}