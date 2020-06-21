package com.example.howmanydaysi.key;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.howmanydaysi.activity.NotificationActivity;
import com.example.howmanydaysi.preferences.Preference;
import com.example.howmanydaysi.alarm.AlarmService;
import com.example.howmanydaysi.alarm.AlarmServiceVisited;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.KeyEvent.KEYCODE_ENTER;

public class TimeOnKeyListener implements View.OnKeyListener{
    private EditText editText;
    private Context context;
    private boolean isFirstPress;

    public TimeOnKeyListener(EditText editText, Context context) {
        this.editText =editText;
        this.context=context;

    }
    public boolean onKey(View v, int keyCode, KeyEvent event) {
    //обработка нажатия на enter при вводе  времени
        boolean consumed = false;
        if (keyCode == KEYCODE_ENTER) {


            if (editText.getText().toString().equals("")) {//если время не введено
                Toast.makeText(context,
                        "Введите значение",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                if (editText.getText().toString().length() != 5) {
                    Toast.makeText(context,
                            "Неверное значение",
                            Toast.LENGTH_SHORT).show();

                } else {

                    try {
                        Date date = new SimpleDateFormat("HH:mm").parse(editText.getText().toString());

                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        editText.setCursorVisible(false);
                        Preference.setAppPreference(Preference.APP_PREFERENCES_NAME_TIME, editText.getText().toString());

                        //если время уведомления введено правильно
                        if (isFirstPress){
                            isFirstPress= false;
                            return true;
                        }
                        isFirstPress = true;

                        Intent intent = new Intent(context.getApplicationContext(), AlarmService.class);//устанавливаем уведомления на это время
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

                        Intent intent1 = new Intent(context, AlarmServiceVisited.class);//устанавливаем на полночь проверку, отметился ли пользователь
                        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0, intent1, 0);
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                        NotificationActivity.setAlarmManager(context, pendingIntent, pendingIntent1, alarmManager);//переходим к методу установки

                        consumed = true;

                    } catch (ParseException e) {

                        Toast.makeText(context,
                                "Неверное значение",
                                Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            }


        }
        return consumed;
    }
}
