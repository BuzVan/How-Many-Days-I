package com.example.howmanydaysi.alarm;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.howmanydaysi.R;
import com.example.howmanydaysi.activity.NotificationActivity;
import com.example.howmanydaysi.preferences.Preference;

public class AlarmServiceVisited extends BroadcastReceiver {

//Если включено уведомление, и пользователь не отметился после 00:00, то текущие дни обнулятся
    @Override
    public void onReceive(Context context, Intent intent) {
        Preference.setContext(context);

        if (!Preference.getAppPreference(Preference.APP_PREFERENCES_NAME_VISITED, false)) {
            AlarmService.CloseAlarm();
            NotificationActivity.zeroingCurrentDays(context);
        }
        Preference.setAppPreference(Preference.APP_PREFERENCES_NAME_ALARM_ACTIVATED, false);
        Preference.setAppPreference(Preference.APP_PREFERENCES_NAME_VISITED, false);//отметился ли пользователь делаем false
        Preference.setAppPreference(Preference.APP_PREFERENCES_NAME_REMIND_TODAY,false);//было сегодня уведомление делаем false
    }
}

