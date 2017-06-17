package com.infoteck.timewall.Gallery.Services;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.infoteck.timewall.Gallery.Factory.AbstractItemFactory;
import com.infoteck.timewall.Gallery.Factory.Item;
import com.infoteck.timewall.R;
import com.infoteck.timewall.Utilities.changeWallpaper;
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

/**
 * Created by Pc on 29/01/2017.
 * This BroadcastReceiver receive intent from WeatherService and update the weather and the wallpaper.
 */

public class alarmAssistant extends BroadcastReceiver {
    static int currentItem=0;
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.e("alarmAssistant","Update wallpaper");
        SharedPreferences prefs = context.getSharedPreferences("randomPreferences", Context.MODE_PRIVATE);    
        SharedPreferences.Editor editor = prefs.edit();
        AbstractItemFactory factory = AbstractItemFactory.getAbstractItemFactory("TimeWall",context);
//        List<Item> items= factory.getRandomLocalItem("");
//        //// TODO: 01/02/2017 get path for getRandomLocalItem
//        if (items.size()>currentItem) {
//            currentItem=0;
//        }else {
//            currentItem++;
//        }
//        new changeWallpaper(context).execute(items.get(currentItem).getLocalFileImage());
    }
}
