package com.example.howmanydaysi.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {
    static public final String APP_PREFERENCES = "mysetеings";
    static public final String APP_PREFERENCES_NAME_VIBRATE = "vibrate";
    static public final String APP_PREFERENCES_NAME_MELODY = "melody";
    static public final String APP_PREFERENCES_NAME_TIME = "time";
    static public final String APP_PREFERENCES_NAME_REMIND_TODAY = "today";
    static public final String APP_PREFERENCES_NAME_VISITED = "visited";
    static public final String APP_PREFERENCES_NAME_ALARM_ACTIVATED = "alarm";
    static public final String APP_PRECEFENCES_NAME_EVENT_COUNT = "event_count";

    static private SharedPreferences instance;
    //Паттерн одиночка
    static public SharedPreferences getInstance(Context context){
        if (instance == null){
            instance = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        }
        return instance;
    }
    static public void setAppPreference(SharedPreferences sharedPref, String preference, boolean value){
        SharedPreferences.Editor e=sharedPref.edit();
        e.putBoolean(preference, value);
        e.apply();
    }
    static public boolean getAppPreference(SharedPreferences sharedPref, String preference, boolean defVal){
       return   sharedPref.getBoolean(preference, defVal);
    }
    static public void setAppPreference(SharedPreferences sharedPref, String preference, String value){
        SharedPreferences.Editor e=sharedPref.edit();
        e.putString(preference, value);
        e.apply();
    }
    static public String getAppPreference(SharedPreferences sharedPref, String preference, String defVal){
        return   sharedPref.getString(preference, defVal);
    }
    static public void setAppPreference(SharedPreferences sharedPref, String preference, int value){
        SharedPreferences.Editor e=sharedPref.edit();
        e.putInt(preference, value);
        e.apply();
    }
    static public int getAppPreference(SharedPreferences sharedPref, String preference, int defVal){
        return   sharedPref.getInt(preference, defVal);
    }

    static public boolean isFirstOpen(SharedPreferences sharedPref){
        return ! sharedPref.contains(APP_PREFERENCES_NAME_TIME);
    }

    static public void setDefaultPreference(SharedPreferences sharedPref) {
        SharedPreferences.Editor e=sharedPref.edit();
        e.putBoolean(APP_PREFERENCES_NAME_VIBRATE, true);
        e.putBoolean(APP_PREFERENCES_NAME_MELODY, true);
        e.putBoolean(APP_PREFERENCES_NAME_REMIND_TODAY, false);

        e.putBoolean(APP_PREFERENCES_NAME_VISITED, true);
        e.putBoolean(APP_PREFERENCES_NAME_ALARM_ACTIVATED, false);
        e.putString(APP_PREFERENCES_NAME_TIME, "20:00");
        e.putInt(APP_PRECEFENCES_NAME_EVENT_COUNT, 0);
        e.apply();
    }
}
