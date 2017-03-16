package com.infoteck.timewall.Gallery.Services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.infoteck.timewall.R;
import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.exception.WeatherProviderInstantiationException;
import com.survivingwithandroid.weather.lib.model.CurrentWeather;
import com.survivingwithandroid.weather.lib.model.Weather;
import com.survivingwithandroid.weather.lib.provider.openweathermap.OpenweathermapProviderType;
import com.survivingwithandroid.weather.lib.request.WeatherRequest;

import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Created by Pc on 28/01/2017.
 * The service will start an serviceRandom every 30 min. (default)
 */

public class serviceRandom extends Service {
    private AlarmManager manager;
    private PendingIntent pendingIntent;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        SharedPreferences prefs = getSharedPreferences("randomPreferences", Context.MODE_PRIVATE);
        long interval =prefs.getLong("Interval",1800000);
        Intent alarmIntent = new Intent(getApplicationContext(), alarmRandom.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);

        manager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

        Log.e("serviceRandom","setted alarm");

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
