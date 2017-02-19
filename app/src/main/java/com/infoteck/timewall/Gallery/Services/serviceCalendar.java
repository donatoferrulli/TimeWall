package com.infoteck.timewall.Gallery.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class serviceCalendar extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        SharedPreferences prefs = getSharedPreferences("calendarPreferences", Context.MODE_PRIVATE);
        long interval =prefs.getLong("Interval",3600000);
        Intent alarmIntent = new Intent(getApplicationContext(), alarmCalendar.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

        Log.e("serviceCalendar","setted alarm");

    }
}
