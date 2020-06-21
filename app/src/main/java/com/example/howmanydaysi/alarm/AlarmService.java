package com.example.howmanydaysi.alarm;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.howmanydaysi.activity.EventExecutionActivity;
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

        Preference.setContext(context);
        Preference.setAppPreference(Preference.APP_PREFERENCES_NAME_REMIND_TODAY,true);

        if (Preference.getAppPreference(Preference.APP_PRECEFENCES_NAME_EVENT_COUNT,0)==0) return;

        Preference.setAppPreference(Preference.APP_PREFERENCES_NAME_ALARM_ACTIVATED, true);


        vibrate = Preference.getAppPreference(Preference.APP_PREFERENCES_NAME_VIBRATE, false);
        melody = Preference.getAppPreference(Preference.APP_PREFERENCES_NAME_MELODY, false);


        Intent notificationIntent = new Intent(context, EventExecutionActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                1, notificationIntent,
                0);


        //создание уведомления
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.icon_calendar)
                        .setContentTitle("Отметьте успешные события до полуночи")
                        .setContentIntent(contentIntent)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Если вы не успеете отметиться до полуночи, то это засчитается как пропуск дня"))
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
