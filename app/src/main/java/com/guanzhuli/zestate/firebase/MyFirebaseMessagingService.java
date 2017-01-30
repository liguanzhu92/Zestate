package com.guanzhuli.zestate.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.buyer.BuyerNavigation;

/**
 * Created by shashank reddy on 1/24/2017.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        createNotification(remoteMessage.getNotification().getBody());

    }
    private void createNotification( String pushMessage) {
        PendingIntent notifyIntent = PendingIntent.getActivity(this, 101, new Intent(this, BuyerNavigation.class), 0);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("Zestate");
        inboxStyle.addLine(pushMessage);

        NotificationCompat.Builder mBuiler = new NotificationCompat.Builder(this);
        mBuiler.setSmallIcon(R.mipmap.zestate);
        mBuiler.setContentTitle("Zestate");
        mBuiler.setContentText(pushMessage);
        mBuiler.setStyle(inboxStyle);
        mBuiler.setTicker("Zestate appointment");
        mBuiler.setContentIntent(notifyIntent);
        mBuiler.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuiler.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(102, mBuiler.build());
    }
}
