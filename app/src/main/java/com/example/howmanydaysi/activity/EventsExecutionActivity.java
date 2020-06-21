package com.example.howmanydaysi.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.howmanydaysi.R;
import com.example.howmanydaysi.RecyclerItemClickListener;
import com.example.howmanydaysi.adapter.EventListAdapter;
import com.example.howmanydaysi.dataDase.DBHelper;
import com.example.howmanydaysi.model.Event;
import com.example.howmanydaysi.model.EventEntity;
import com.example.howmanydaysi.preferences.Preference;
import com.example.howmanydaysi.alarm.AlarmService;

import java.util.ArrayList;
import java.util.List;

public class EventsExecutionActivity extends AppCompatActivity {
    RecyclerView eventRecyclerView;
    EventListAdapter eventListAdapter;
    DBHelper dBHelper;
    SQLiteDatabase database;
    protected LinearLayoutManager layoutManager;
    private boolean isScrollDown = false;
    public List<Event> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_execution);



        eventRecyclerView = findViewById(R.id.events_view);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        layoutManager = (LinearLayoutManager) eventRecyclerView.getLayoutManager();

        TextView view = eventRecyclerView.findViewById(R.id.event_text);
        eventRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy != 0)
                    isScrollDown = layoutManager.findLastVisibleItemPosition() == eventRecyclerView.getAdapter().getItemCount() - 1;
            }
        });
        eventRecyclerView.addOnLayoutChangeListener(new RecyclerView.OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (isScrollDown && oldBottom > bottom) {
                    eventRecyclerView.post(() -> eventRecyclerView.scrollToPosition(eventListAdapter.eventEntityList.size() - 1));
                }
            }
        });
        eventRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), (v, position) -> {
            CheckBox checkBox = v.findViewById(R.id.checkBox);
            Event event = events.get(position);
            checkBox.setOnClickListener(v1 -> {
                event.setCheck(((CheckBox) v1).isChecked());
            });

        })
        );

    }

    @Override
    protected void onStart() {
        super.onStart();
        setListEvent();
        Preference.setContext(this);
    }

    public void setListEvent() {
        eventListAdapter = new EventListAdapter(false);
        dBHelper = new DBHelper(this);
        database = dBHelper.getWritableDatabase();

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
                Event event = new Event(eventEntity, true);
                events.add(event);
                eventListAdapter.eventEntityList.add(eventEntity);
            } while (cursor.moveToNext());

        }
        cursor.close();
        eventRecyclerView.setAdapter(eventListAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
//обработка нажатия на ОК, если пользователь отметил true,
// то количество текущих дней увеличим на 1, если осталось false, то обнуляем текущее количество дней для каждого события отдельно
    public void OKButtonOnClick(View view) {
        Button  OKButton = findViewById(R.id.OKButton);
        OKButton.setClickable(false);
        dBHelper = new DBHelper(this);
        database = dBHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_EVENTS, null, null, null, null, null, null);

        int currentQuantityIndex = cursor.getColumnIndex(DBHelper.FIELD_CURRENT_QUANTITY);
        int recordQuantityIndex = cursor.getColumnIndex(DBHelper.FIELD_RECORD_QUANTITY);

        for (int i = 0; i < events.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.FIELD_EVENT, eventListAdapter.eventEntityList.get(i).text);
            contentValues.put(DBHelper.FIELD_ICON, eventListAdapter.eventEntityList.get(i).getIconID());
            cursor.moveToPosition(i);
            if (events.get(i).isCheck()) {
                int current = cursor.getInt(currentQuantityIndex) + 1;
                int record = cursor.getInt(recordQuantityIndex);
                contentValues.put(DBHelper.FIELD_CURRENT_QUANTITY, current);
                if (current > record)//если текущее количество больше рекордног, то рекордное количество приравниваем к текущему
                    record = current;
                contentValues.put(DBHelper.FIELD_RECORD_QUANTITY, record);

            } else {
                contentValues.put(DBHelper.FIELD_CURRENT_QUANTITY, 0);
                contentValues.put(DBHelper.FIELD_RECORD_QUANTITY, eventListAdapter.eventEntityList.get(i).getRecord_quantity());
            }
            database.update(DBHelper.TABLE_EVENTS, contentValues, DBHelper.FIELD_ID + "=" + (i + 1), null);
        }
        cursor.close();
        Toast.makeText(getApplicationContext(),
                "Данные сохранены",
                Toast.LENGTH_SHORT).show();




        Preference.setAppPreference(Preference.APP_PREFERENCES_NAME_ALARM_ACTIVATED,false);
        Preference.setAppPreference(Preference.APP_PREFERENCES_NAME_VISITED, true);
        AlarmService.CloseAlarm();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        OKButton.setClickable(true);
        this.finish();
    }

    @Override
    public void onBackPressed() {

    }


}

