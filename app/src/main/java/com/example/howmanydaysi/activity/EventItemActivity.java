package com.example.howmanydaysi.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.howmanydaysi.R;
import com.example.howmanydaysi.dataDase.DBHelper;
import com.example.howmanydaysi.preferences.Preference;
import com.example.howmanydaysi.service.WordsForm;

import java.util.Calendar;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.vo.DateData;

public class EventItemActivity extends AppCompatActivity {
    DBHelper dBHelper;
    SQLiteDatabase database;
    TextView  quantityTextView, eventTextView, recordTextView;
   MCalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_event);
        //стрелочка в оглавлении
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        quantityTextView= findViewById(R.id.quantity_days_text);
        eventTextView= findViewById(R.id.event_text_view);
        recordTextView = findViewById(R.id.record_text);
        calendarView= findViewById(R.id.calendar);
        calendarView.getMarkedDates().getAll().clear();

        dBHelper = new DBHelper(getApplicationContext());
        database = dBHelper.getWritableDatabase();
        Bundle intent = getIntent().getExtras();
        int position=intent.getInt("position");
        Cursor cursor = database.query(DBHelper.TABLE_EVENTS, null, null, null, null, null, null);

        int eventIndex = cursor.getColumnIndex(DBHelper.FIELD_EVENT);
        int currentQuantityIndex = cursor.getColumnIndex(DBHelper.FIELD_CURRENT_QUANTITY);
        int recordQuantityIndex = cursor.getColumnIndex(DBHelper.FIELD_RECORD_QUANTITY);
        cursor.moveToPosition(position);
        int recordCount=cursor.getInt(recordQuantityIndex);
        int currentCount=cursor.getInt(currentQuantityIndex);
        String event_text = cursor.getString(eventIndex);
        //ограничение на число теста в событии
        if (event_text.length()>100){
           event_text = event_text.substring(0,100) + "...";
        }
        eventTextView.setText(event_text);

        String rec_count_str =
                String.format("%s %d %s", getString(R.string.record_name), recordCount, WordsForm.getDayWordForm(recordCount));
        recordTextView.setText(rec_count_str);

        String curr_str =
                String.format("%s %d %s", getString(R.string.days_now_name), currentCount, WordsForm.getDayWordForm(currentCount));
        quantityTextView.setText(curr_str);
        cursor.close();

if(currentCount%7==0 && currentCount!=0){
    AlertDialog alertDialog =
    new AlertDialog.Builder(this)
            .setTitle("Поздравляю!")
            .setIcon(R.drawable.cup)
            .setPositiveButton("ОК", (dialog, id) -> {
                // Закрываем окно
                dialog.cancel();
            }).create();
    if (currentCount/7==1)
        alertDialog.setMessage("Вы выполняете событие уже целую неделю подряд. Молодец!");
    else
        alertDialog.setMessage(String.format("%dая неделя прошла! А вы продолжайте дальше :)", currentCount / 7));
    alertDialog.show();
}

        Calendar now=Calendar.getInstance();
        if( Preference.getAppPreference(Preference.APP_PREFERENCES_NAME_REMIND_TODAY,false))//проверяем было сегодня уведомление или нет
            //я использовал сторонний календарь , так как в CalendarView нельзя программно отмечать дни
            //в gradle я его включил, еще ссылка на этот календарь для информации https://github.com/SpongeBobSun/mCalendarView?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=2420
            //сегодняшний день отмечается другим цветом, в отличии от цепочки дней когда отмечался пользователь
            now.add(Calendar.DAY_OF_MONTH,-(currentCount-1));
        else
            now.add(Calendar.DAY_OF_MONTH,-currentCount);

        for(int i=0;i<currentCount; i++)
        {
            //отмечаем цепочку дней, когда пользователь отметил выполнение события
            calendarView.markDate(new DateData(now.get(Calendar.YEAR),now.get(Calendar.MONTH)+1,now.get(Calendar.DAY_OF_MONTH)));
            now.add(Calendar.DAY_OF_MONTH,1);
        }
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
