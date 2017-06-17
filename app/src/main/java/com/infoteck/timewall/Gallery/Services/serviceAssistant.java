package com.infoteck.timewall.Gallery.Services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Pc on 28/01/2017.
 * The service will start an serviceAssistant every 30 min. (default)
 */

public class serviceAssistant extends Service {
    private AlarmManager manager;
    private PendingIntent pendingIntent;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        SharedPreferences prefs = getSharedPreferences("randomPreferences", Context.MODE_PRIVATE);
        long intervalAssistant =1800000;
        long intervalWeather =1800000;
        long intervalCalendar =1800000;
        //START ASSISTANT AFTER 0 SEC
        Intent alarmIntent = new Intent(getApplicationContext(), alarmAssistant.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);
        manager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), intervalAssistant, pendingIntent);
        //START WEATHER AFTER 15MIN
        alarmIntent = new Intent(getApplicationContext(), alarmWeather.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);
        manager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+900000, intervalWeather, pendingIntent);
        //START CALENDAR AFTER 7MIN
        alarmIntent = new Intent(getApplicationContext(), alarmCalendar.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);
        manager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+450000, intervalCalendar, pendingIntent);

        Log.e("serviceAssistant","setted alarm");

    }

    public void onDestroy()
    {
        super.onDestroy();
        //destroy alarm
        manager.cancel(pendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();

    }
}
