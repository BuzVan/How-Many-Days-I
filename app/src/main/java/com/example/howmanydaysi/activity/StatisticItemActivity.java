package com.example.howmanydaysi.activity;

import android.content.DialogInterface;
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

import java.util.Calendar;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.vo.DateData;

public class StatisticItemActivity extends AppCompatActivity {
    DBHelper dBHelper;
    SQLiteDatabase database;
    TextView  quantityTextView, eventTextView;
   MCalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_statistic);
        //стрелочка в оглавлении
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        quantityTextView= findViewById(R.id.quantity_days_text);
        eventTextView= findViewById(R.id.event_text_view);
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
        int currentDays=cursor.getInt(currentQuantityIndex);

        eventTextView.setText(cursor.getString(eventIndex));
        String count_str = recordCount + " " +getDayWordForm(recordCount);
        quantityTextView.setText(count_str);
        cursor.close();

if(currentDays%7==0 && currentDays!=0){
    AlertDialog alertDialog =
    new AlertDialog.Builder(this)
            .setTitle("Поздравляю!")
            .setIcon(R.drawable.cup)
            .setPositiveButton("ОК", (dialog, id) -> {
                // Закрываем окно
                dialog.cancel();
            }).create();
    if (currentDays/7==1)
        alertDialog.setMessage("Вы выполняете событие уже целую неделю подряд. Молодец!");
    else
        alertDialog.setMessage(String.format("%dая неделя прошла! А вы продолжайте дальше :)", currentDays / 7));
    alertDialog.show();
}

        Calendar now=Calendar.getInstance();
        if( Preference.getAppPreference(Preference.APP_PREFERENCES_NAME_REMIND_TODAY,false))//проверяем было сегодня уведомление или нет
            //я использовал сторонний календарь , так как в CalendarView нельзя программно отмечать дни
            //в gradle я его включил, еще ссылка на этот календарь для информации https://github.com/SpongeBobSun/mCalendarView?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=2420
            //сегодняшний день отмечается другим цветом, в отличии от цепочки дней когда отмечался пользователь
            now.add(Calendar.DAY_OF_MONTH,-(currentDays-1));
        else
            now.add(Calendar.DAY_OF_MONTH,-currentDays);

        for(int i=0;i<currentDays; i++)
        {
            //отмечаем цепочку дней, когда пользователь отметил выполнение события
            calendarView.markDate(new DateData(now.get(Calendar.YEAR),now.get(Calendar.MONTH)+1,now.get(Calendar.DAY_OF_MONTH)));
            now.add(Calendar.DAY_OF_MONTH,1);
        }
    }

public String getDayWordForm(int quantityDays)//чисто для согласованности числа и слова день))
{
    if(quantityDays%10==1&&quantityDays!=11)
        return "день";
    else if(quantityDays%10>1&&quantityDays%10<5&&(quantityDays<10||quantityDays>20))
        return "дня";
    else
        return "дней";
}
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
