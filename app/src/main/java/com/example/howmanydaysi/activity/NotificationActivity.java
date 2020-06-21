package com.example.howmanydaysi.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.example.howmanydaysi.R;
import com.example.howmanydaysi.dataDase.DBHelper;
import com.example.howmanydaysi.filters.FilterTime;
import com.example.howmanydaysi.key.TimeOnKeyListener;
import com.example.howmanydaysi.model.EventEntity;
import com.example.howmanydaysi.preferences.Preference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static android.app.AlarmManager.INTERVAL_DAY;

public class NotificationActivity extends AppCompatActivity {
EditText notification_time;

SwitchCompat vibrateSwitch, melodySwitch;
boolean notification, melody, vibrate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        notification_time= findViewById(R.id.time_text_view);
        notification_time.setFilters(new InputFilter[]{new FilterTime(notification_time)});
        Bundle intent = getIntent().getExtras();

        //стрелочка в оглавлении
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        notification_time.setOnKeyListener(new TimeOnKeyListener( notification_time,this));
        notification_time.setCursorVisible(false);
        vibrateSwitch= findViewById(R.id.switch_compat_vibration);
        melodySwitch= findViewById(R.id.switch_compat_melody);
        getValuesPreferences();//устанавливаем значения из SharedPreferences
        vibrateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // в зависимости от значения isChecked выводим нужное сообщение
                vibrate= isChecked;
                Preference.setAppPreference(Preference.APP_PREFERENCES_NAME_VIBRATE,vibrate);
            }
        });


        melodySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // в зависимости от значения isChecked выводим нужное сообщение
                melody= isChecked;
                Preference.setAppPreference(Preference.APP_PREFERENCES_NAME_MELODY, melody);
            }
        });


    }
    //метод обнуления текущего количества дней в базе данных, вызывается при отключении уведомлений и при игнорировании уведомлений
    static public void zeroingCurrentDays(Context context)
    {
        DBHelper dBHelper=new DBHelper(context);
        SQLiteDatabase database= dBHelper.getWritableDatabase();

        List<EventEntity> eventEntities=new ArrayList<>();
        Cursor cursor = database.query(DBHelper.TABLE_EVENTS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int eventIndex = cursor.getColumnIndex(DBHelper.FIELD_EVENT);
            int iconIndex = cursor.getColumnIndex(DBHelper.FIELD_ICON);
            int currentQuantityIndex = cursor.getColumnIndex(DBHelper.FIELD_CURRENT_QUANTITY);
            int recordQuantityIndex = cursor.getColumnIndex(DBHelper.FIELD_RECORD_QUANTITY);

            do {
                EventEntity eventEntity = new EventEntity(cursor.getString(eventIndex),
                        cursor.getInt(iconIndex), cursor.getInt(currentQuantityIndex),
                        cursor.getInt(recordQuantityIndex));
                eventEntities.add(eventEntity);
            } while (cursor.moveToNext());

        }
        cursor.close();
        for(int i=0; i<eventEntities.size();i++) {
            EventEntity eventEntity = eventEntities.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.FIELD_EVENT, eventEntity.text);
            contentValues.put(DBHelper.FIELD_ICON, eventEntity.getIconID());
            contentValues.put(DBHelper.FIELD_CURRENT_QUANTITY, 0);
            contentValues.put(DBHelper.FIELD_RECORD_QUANTITY, eventEntity.getRecord_quantity());
            database.update(DBHelper.TABLE_EVENTS, contentValues, DBHelper.FIELD_ID + "=" + (i + 1), null);
        }
    }
    //установка уведомлений
    static  public void setAlarmManager(Context context, PendingIntent pendingIntent,  PendingIntent pendingIntent1, AlarmManager alarmManager) {
        final SimpleDateFormat format = new SimpleDateFormat("DD-MM-yyyy hh:mm");


        String time = Preference.getAppPreference(Preference.APP_PREFERENCES_NAME_TIME, "21:00");
        Calendar nextNotification;
        Calendar now= Calendar.getInstance();
        String[] timeStr=time.split(":");
        int hour= Integer.parseInt(timeStr[0]);
        int minut= Integer.parseInt(timeStr[1]);

        nextNotification=new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), hour, minut,0);

        if(now.get(Calendar.HOUR_OF_DAY)>hour||(now.get(Calendar.HOUR_OF_DAY)==hour&&now.get(Calendar.MINUTE)>minut))
        {
            nextNotification.add(Calendar.DATE, 1);
        }
        else if(Preference.getAppPreference(Preference.APP_PREFERENCES_NAME_REMIND_TODAY, false))//если уведомление сегодня уже было
        {
            nextNotification.add(Calendar.DATE, 1);
            Toast.makeText(context,
                    "Сегодня уведомление уже было, следующее будет завтра в назначенное время",
                    Toast.LENGTH_SHORT).show();
        }
        //ночное событие:
        Calendar midnight=new GregorianCalendar(now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH),0,0,0);
        midnight.add(Calendar.DATE,1);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, midnight.getTimeInMillis(), INTERVAL_DAY, pendingIntent1);
        Log.i("ALARM_MIDNIGHT", format.format(midnight.getTime()));


        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, nextNotification.getTimeInMillis(), INTERVAL_DAY, pendingIntent);
        Log.i("ALARM_NEXT_NOTIF", format.format(nextNotification.getTime()));
    }
//устанавливаем значения при открытии activity
    private void getValuesPreferences() {

        vibrate= Preference.getAppPreference(Preference.APP_PREFERENCES_NAME_VIBRATE, true);
        vibrateSwitch.setChecked(vibrate);

        melody= Preference.getAppPreference(Preference.APP_PREFERENCES_NAME_MELODY, true);
        melodySwitch.setChecked(melody);


        notification_time.setText(Preference.getAppPreference(Preference.APP_PREFERENCES_NAME_TIME, getString(R.string.time_def)));
    }

    public void timeEditTextClick(View view) {
        notification_time.setCursorVisible(true);
        notification_time.setText("");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(Preference.getAppPreference(Preference.APP_PREFERENCES_NAME_ALARM_ACTIVATED,false)){
            Intent in = new Intent(getApplicationContext(), EventsExecutionActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
        }
    }
}