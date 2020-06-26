package com.example.howmanydaysi.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.howmanydaysi.R;
import com.example.howmanydaysi.dataDase.DBHelper;
import com.example.howmanydaysi.key.StringOnKeyListener;
import com.example.howmanydaysi.model.EventEntity;
import com.example.howmanydaysi.preferences.Preference;

public class NewEventActivity extends AppCompatActivity {
    int imageID;
    ImageView previousView;
    EditText eventText;

    DBHelper dBHelper;
    SQLiteDatabase database;

    int recordDays=0, currentDays=0;
    int position=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        eventText = findViewById(R.id.event_text_view);
        eventText.setCursorVisible(false);

        eventText.setOnKeyListener(new StringOnKeyListener( eventText,this));
        //стрелочка в оглавлении
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView titleTextView = findViewById(R.id.heading_textView);
        Bundle intent = getIntent().getExtras();
        if(intent!=null)
            position=intent.getInt("position");
         if(position!=-1) {
             //изменеие события
             dBHelper = new DBHelper(getApplicationContext());
             database = dBHelper.getWritableDatabase();
             Cursor cursor = database.query(DBHelper.TABLE_EVENTS, null, null, null, null, null, null);

             int eventIndex = cursor.getColumnIndex(DBHelper.FIELD_EVENT);
             int iconIndex = cursor.getColumnIndex(DBHelper.FIELD_ICON);
             int currentQuantityIndex = cursor.getColumnIndex(DBHelper.FIELD_CURRENT_QUANTITY);
             int recordQuantityIndex = cursor.getColumnIndex(DBHelper.FIELD_RECORD_QUANTITY);
             cursor.moveToPosition(position);
             previousView= findViewById(getID(cursor.getInt(iconIndex)));
             previousView.setBackground(getResources().getDrawable(R.drawable.image_style));
             imageID = cursor.getInt(iconIndex);
             eventText.setText(cursor.getString(eventIndex));
             currentDays=cursor.getInt(currentQuantityIndex);
             recordDays=cursor.getInt(recordQuantityIndex);
             cursor.close();
             titleTextView.setText(getResources().getText(R.string.change_event_name));
         }
         else
         {
             //новое событие
             //изображение по умолчанию
             previousView = findViewById(R.id.study);
             previousView.setBackground(getResources().getDrawable(R.drawable.image_style));
             imageID = R.drawable.study;

             eventText.setText(getResources().getText(R.string.new_event_name));
             titleTextView.setText(getResources().getText(R.string.new_event_name));
         }
    }
//обработка нажатия на стрелку
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void imageClick(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(eventText.getWindowToken(), 0);
        eventText.setCursorVisible(false);
        if (previousView != view) {//если предыдущее изображение не равно новому, то новому изображению устанавливаем фон,
            // а предыдущему отменяем фон
            previousView.setBackground(null);
            view.setBackground(getResources().getDrawable(R.drawable.image_style));
            imageID = getImage(view.getId());//устанавливаем значение картинки
            previousView = (ImageView)view;
        }

    }
    public int getID(int id) {//получение id по изображению
        int imgId = 0;
        switch (id) {
            case R.drawable.food: {
                imgId = R.id.food;
                break;
            }
            case R.drawable.sport: {
                imgId = R.id.sport;
                break;
            }
            case R.drawable.feel: {
                imgId = R.id.feel;
                break;
            }
            case R.drawable.study: {
                imgId = R.id.study;
                break;

            }
            case R.drawable.job: {
                imgId = R.id.job;
                break;

            }
            case R.drawable.phone: {
                imgId = R.id.phone;
                break;

            }
            case R.drawable.home: {
                imgId = R.id.home;
                break;

            }
            case R.drawable.game: {
                imgId = R.id.game;
                break;

            }
            case R.drawable.relax: {
                imgId = R.id.relax;
                break;

            }

        }
        return imgId;

    }

    public int getImage(int id) {//получение изображения по id
        int imgId = 0;
        switch (id) {
            case R.id.food: {
                imgId = R.drawable.food;
                break;
            }
            case R.id.sport: {
                imgId = R.drawable.sport;
                break;
            }
            case R.id.feel: {
                imgId = R.drawable.feel;
                break;
            }
            case R.id.study: {
                imgId = R.drawable.study;
                break;

            }
            case R.id.job: {
                imgId = R.drawable.job;
                break;

            }
            case R.id.phone: {
                imgId = R.drawable.phone;
                break;

            }
            case R.id.home: {
                imgId = R.drawable.home;
                break;

            }
            case R.id.game: {
                imgId = R.drawable.game;
                break;

            }
            case R.id.relax: {
                imgId = R.drawable.relax;
                break;

            }

        }
        return imgId;

    }
    //Добавление события
    public void addValue() {
        dBHelper = new DBHelper(getApplicationContext());
        database = dBHelper.getWritableDatabase();

        EventEntity eventEntity = new EventEntity(eventText.getText().toString(), imageID,  currentDays, recordDays);
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.FIELD_EVENT, eventEntity.text);
        contentValues.put(DBHelper.FIELD_ICON, eventEntity.getIconID());
        contentValues.put(DBHelper.FIELD_CURRENT_QUANTITY, eventEntity.getCurrent_quantity());
        contentValues.put(DBHelper.FIELD_RECORD_QUANTITY, eventEntity.getRecord_quantity());
        if(position==-1) {//добавление данных
            database.insert(DBHelper.TABLE_EVENTS, null, contentValues);
            Toast.makeText(getApplicationContext(),
                    "Событие создано",
                    Toast.LENGTH_SHORT).show();
            SharedPreferences preferences = Preference.getInstance(this);
            Preference.setAppPreference( preferences,
                    Preference.APP_PRECEFENCES_NAME_EVENT_COUNT,
                    Preference.getAppPreference(preferences, Preference.APP_PRECEFENCES_NAME_EVENT_COUNT,0) +1
                    );
        }
        else {//изменение данных
            database.update(DBHelper.TABLE_EVENTS, contentValues, DBHelper.FIELD_ID + "=" + (position+1), null);
            Toast.makeText(getApplicationContext(),
                    "Событие изменено",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void OKClick(View view) {
//если событие не пустое значение то добавим или изменим данные
        if (eventText.getText().toString().equals("")) {
            eventText.setCursorVisible(true);
            Toast.makeText(getApplicationContext(),
                    "Введите значение",
                    Toast.LENGTH_SHORT).show();
        } else {
            addValue();
            finish();
        }

    }



    public void textViewClick(View view) {
        eventText.setCursorVisible(true);
    }
}
