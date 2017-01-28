package com.guanzhuli.zestate.buyer.adapters;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.guanzhuli.zestate.R;
import com.guanzhuli.zestate.buyer.BuyerNavigation;


public class AppointmentReminder extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        createNotification(context,bundle.getString("agentName"),bundle.getString("agentPhone"));
    }

    private void createNotification(Context context, String agentName, String agentPhone) {
        PendingIntent notifyIntent =PendingIntent.getActivity(context,101,new Intent(context, BuyerNavigation.class),0);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("Zestate");
        inboxStyle.addLine("you have scheduled Appointment with "+agentName);
        inboxStyle.addLine("today phone Number is " + agentPhone);

        NotificationCompat.Builder mBuiler = new NotificationCompat.Builder(context);
        mBuiler.setSmallIcon(R.mipmap.zestate);
        mBuiler.setContentTitle("Zestate");
        mBuiler.setContentText("");
        mBuiler.setStyle(inboxStyle);
        mBuiler.setTicker("Zestate appointment");
        mBuiler.setContentIntent(notifyIntent);
        mBuiler.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuiler.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(101,mBuiler.build());
    }
}
