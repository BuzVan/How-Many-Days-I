package com.example.howmanydaysi.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.howmanydaysi.R;
import com.example.howmanydaysi.RecyclerItemClickListener;
import com.example.howmanydaysi.adapter.EventListAdapter;
import com.example.howmanydaysi.dataDase.DBHelper;
import com.example.howmanydaysi.model.EventEntity;
import com.example.howmanydaysi.preferences.Preference;

public class AllEventsActivity extends AppCompatActivity {
    RecyclerView eventRecyclerView;
    EventListAdapter eventListAdapter;
    DBHelper dBHelper;
    SQLiteDatabase database;
    protected LinearLayoutManager layoutManager;
    private boolean isScrollDown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);
        //стрелочка в оглавлении
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        eventRecyclerView = findViewById(R.id.events_recycler_view);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        layoutManager = (LinearLayoutManager) eventRecyclerView.getLayoutManager();

        registerForContextMenu(eventRecyclerView);//устанавливаем контекстное меню
        //обработка нажатия на элемент из RecycleView, получаем позицию и элемент
        eventRecyclerView.addOnItemTouchListener( new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        Intent intent = new Intent(getApplicationContext(), EventItemActivity.class);
                        intent.putExtra("position",position);
                        startActivity(intent);
                    }
                })
        );

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListEvent();
    }
public void setListEvent()//установление значений в RecycleView
{
    eventListAdapter = new EventListAdapter(true);
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
            eventListAdapter.eventEntityList.add(eventEntity);
        } while (cursor.moveToNext());
        //eventListAdapter.SortList();
    }
    cursor.close();

    eventRecyclerView.setAdapter(eventListAdapter);
    //добавление в таблицу количество элементов
}
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void addClick(View view) {
        Intent intent = new Intent(getApplicationContext(), NewEventActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)//контекстное меню для удаления и изменения
    {
        int position = -1;
        try {
            position = ((EventListAdapter)eventRecyclerView.getAdapter()).getPosition() ;
        } catch (Exception e) {

            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.change:
                Intent intent = new Intent(getApplicationContext(), NewEventActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
                break;
            case R.id.delete:
                dBHelper = new DBHelper(this);
                database = dBHelper.getWritableDatabase();
                long y=database.delete(DBHelper.TABLE_EVENTS, DBHelper.FIELD_ID + "=" + (position+1), null);
                //уменьшение сохранённого количества элементов на 1
                Preference.setAppPreference(
                        Preference.APP_PRECEFENCES_NAME_EVENT_COUNT,
                        Preference.getAppPreference(Preference.APP_PRECEFENCES_NAME_EVENT_COUNT,0) -1
                );
                setListEvent();
                rewritingDataBase();
                break;
        }
        return super.onContextItemSelected(item);

    }
    //после удаления элемента перезаписываем данные в базу данных, чтобы номера шли по порядку
    public void rewritingDataBase(){
        dBHelper = new DBHelper(this);
        database = dBHelper.getWritableDatabase();
        database.delete(DBHelper.TABLE_EVENTS, null, null);
        for (int i = 0; i<eventListAdapter.eventEntityList.size(); i++)
        {
            EventEntity eventEntity =eventListAdapter.eventEntityList.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.FIELD_EVENT, eventEntity.text);
            contentValues.put(DBHelper.FIELD_ICON, eventEntity.getIconID());
            contentValues.put(DBHelper.FIELD_CURRENT_QUANTITY, eventEntity.getCurrent_quantity());
            contentValues.put(DBHelper.FIELD_RECORD_QUANTITY, eventEntity.getRecord_quantity());
                database.insert(DBHelper.TABLE_EVENTS, null, contentValues);
        }

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
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
