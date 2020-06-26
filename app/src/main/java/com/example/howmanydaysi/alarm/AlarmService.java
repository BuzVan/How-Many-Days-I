package com.example.howmanydaysi.alarm;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.howmanydaysi.activity.EventsExecutionActivity;
import com.example.howmanydaysi.R;
import com.example.howmanydaysi.preferences.Preference;

public class AlarmService extends BroadcastReceiver {
// при уведомленни происходит переход на это окно
    // Идентификатор канала
    private static String CHANNEL_ID = "Channel";
    private boolean melody = false, vibrate = false;
    private static  NotificationManagerCompat notificationManager;
    public static int id = 1;
    public static void CloseAlarm(){
        if (null != notificationManager){
            notificationManager.cancel(id);
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences preferences = Preference.getInstance(context);
        Preference.setAppPreference(preferences,Preference.APP_PREFERENCES_NAME_REMIND_TODAY,true);

        if (Preference.getAppPreference(preferences,Preference.APP_PRECEFENCES_NAME_EVENT_COUNT,0)==0) return;

        Preference.setAppPreference(preferences,Preference.APP_PREFERENCES_NAME_ALARM_ACTIVATED, true);


        vibrate = Preference.getAppPreference(preferences,Preference.APP_PREFERENCES_NAME_VIBRATE, false);
        melody = Preference.getAppPreference(preferences,Preference.APP_PREFERENCES_NAME_MELODY, false);


        Intent notificationIntent = new Intent(context, EventsExecutionActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                1, notificationIntent,
                0);


        //создание уведомления
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.icon_calendar)
                        .setContentTitle(context.getString(R.string.alarm_title))
                        .setContentIntent(contentIntent)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.alarm_text)))
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_calendar))
                        .setColor(Color.GRAY)
                        .setOngoing(true)
                        .setAutoCancel(true);

        Notification notification = builder.build();
        if (melody)
            notification.defaults |= Notification.DEFAULT_SOUND;
        if (vibrate)
            notification.defaults |=  Notification.DEFAULT_VIBRATE;

        notificationManager =
                NotificationManagerCompat.from(context);

        notificationManager.notify(id, notification);


    }


}
