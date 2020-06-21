package com.example.howmanydaysi.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {
    public static final String APP_PREFERENCES = "mysetеings";
    public static final String APP_PREFERENCES_NAME_VIBRATE = "vibrate";
    public static final String APP_PREFERENCES_NAME_MELODY = "melody";
    public static final String APP_PREFERENCES_NAME_TIME = "time";
    public static final String APP_PREFERENCES_NAME_REMIND_TODAY = "today";
    public static final String APP_PREFERENCES_NAME_VISITED = "visited";
    public static final String APP_PREFERENCES_NAME_ALARM_ACTIVATED = "alarm";
    public static final String APP_PRECEFENCES_NAME_EVENT_COUNT = "event_count";
    private static Context mainContext;
    private static  SharedPreferences mSettings;
    public static void setContext(Context context){
        mainContext = context;
        mSettings = mainContext.getSharedPreferences(APP_PREFERENCES,
                Context.MODE_PRIVATE);
    }
    public static void setAppPreference(String preference, boolean value){
        SharedPreferences.Editor e=mSettings.edit();
        e.putBoolean(preference, value);
        e.apply();

    }
    public static  boolean getAppPreference(String preference, boolean defVal){
       return   mSettings.getBoolean(preference, defVal);
    }
    public static void setAppPreference(String preference, String value){
        SharedPreferences.Editor e=mSettings.edit();
        e.putString(preference, value);
        e.apply();
    }
    public static  String getAppPreference(String preference, String defVal){
        return   mSettings.getString(preference, defVal);
    }
    public static void setAppPreference(String preference, int value){
        SharedPreferences.Editor e=mSettings.edit();
        e.putInt(preference, value);
        e.apply();
    }
    public static  int getAppPreference(String preference, int defVal){
        return   mSettings.getInt(preference, defVal);
    }

    public static boolean isFirstOpen(){
        return ! mSettings.contains(APP_PREFERENCES_NAME_TIME);
    }

    public static void setDefaultPreference() {
        SharedPreferences.Editor e=mSettings.edit();
        e.putBoolean(APP_PREFERENCES_NAME_VIBRATE, true);
        e.putBoolean(APP_PREFERENCES_NAME_MELODY, true);
        e.putBoolean(APP_PREFERENCES_NAME_REMIND_TODAY, false);

        e.putBoolean(APP_PREFERENCES_NAME_VISITED, true);
        e.putBoolean(APP_PREFERENCES_NAME_ALARM_ACTIVATED, false);
        e.putString(APP_PREFERENCES_NAME_TIME, "21:00");
        e.putInt(APP_PRECEFENCES_NAME_EVENT_COUNT, 0);
        e.apply();
    }
}
