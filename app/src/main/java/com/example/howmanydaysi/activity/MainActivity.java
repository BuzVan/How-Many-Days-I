package com.example.howmanydaysi.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.howmanydaysi.R;
import com.example.howmanydaysi.preferences.Preference;
import com.example.howmanydaysi.alarm.AlarmService;
import com.example.howmanydaysi.alarm.AlarmServiceVisited;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Preference.setContext(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //приветствие
        if (Preference.isFirstOpen()){

            //данные по умолчанию
            Preference.setDefaultPreference();

            //запуск уведомлений
            Intent intent = new Intent(this.getApplicationContext(), AlarmService.class);//устанавливаем уведомления на это время
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            Intent intent1 = new Intent(this, AlarmServiceVisited.class);//устанавливаем на полночь проверку, отметился ли пользователь
            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, 0, intent1, 0);
            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            NotificationActivity.setAlarmManager(this, pendingIntent, pendingIntent1, alarmManager);//переходим к методу установки

            AlertDialog alertDialog =
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.hello_title)
                            .setPositiveButton("ОК", (dialog, id) -> {
                                // Закрываем окно
                                dialog.cancel();
                            }).create();
                alertDialog.setMessage(getString(R.string.hello_message));
            alertDialog.show();
        }


    }
    @Override
    protected void onStart() {
        super.onStart();
        Preference.setContext(this);
        if(Preference.getAppPreference(Preference.APP_PREFERENCES_NAME_ALARM_ACTIVATED,false)){
            Intent in = new Intent(getApplicationContext(), EventsExecutionActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {//выбор эелемента из шторки

        int id = item.getItemId();

        if (id == R.id.statistic) {
            {
                Intent intent = new Intent(getApplicationContext(), AllEventsActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.notification) {
            Intent intent = new Intent(getApplicationContext(),NotificationActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        //закрытие шторки при нажатии назад
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }



}
