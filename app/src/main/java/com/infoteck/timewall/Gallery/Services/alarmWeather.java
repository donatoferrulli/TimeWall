package com.infoteck.timewall.Gallery.Services;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

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

public class alarmWeather extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.e("alarmWeather","Update wallpaper");
        SharedPreferences prefs = context.getSharedPreferences("weatherPreferences", Context.MODE_PRIVATE);
        WeatherClient.ClientBuilder builder = new WeatherClient.ClientBuilder();
        WeatherConfig config = new WeatherConfig();
        config.ApiKey = context.getResources().getString(R.string.ApiKey);
        config.unitSystem = WeatherConfig.UNIT_SYSTEM.M;
        config.lang = Locale.getDefault().getCountry();
        config.maxResult = 5; // Max number of cities retrieved
        config.numDays = 6; // Max num of days in the forecast
        try {
            WeatherClient weatherClient = builder.attach(context)
                    .provider(new OpenweathermapProviderType())
                    .httpClient(com.survivingwithandroid.weather.lib.client.okhttp.WeatherDefaultClient.class)
                    .config(config)
                    .build();
            WeatherRequest req = new WeatherRequest(prefs.getString("CityLocationID","3183178"));
            weatherClient.getCurrentCondition(req, new WeatherClient.WeatherEventListener() {
                @Override
                public void onWeatherRetrieved(CurrentWeather cWeather) {
                    Weather weather = cWeather.weather;
                    // The weather condition object is ready and we can use it
                    AbstractItemFactory factory = AbstractItemFactory.getAbstractItemFactory("TimeWall",context);
                    List<Item> items= factory.getWeatherItem();
                    int weatherID=weather.currentCondition.getWeatherId();
                    //correct WeatherID
                    weatherID=correctWeatherID(weatherID);
                    
                    
                    if (items!=null){
                            int i =0;
                            while (items.get(i).getWeatherCode()!=weatherID && i<items.size()-1){
                                i++;
                            }
                            if(items.get(i).getWeatherCode()==weatherID){
                                if(items.get(i).getLocalFileImage()==null){
                                    new changeWallpaper(context).execute(items.get(i).getPhotoUrl());
                                }else{
                                    new changeWallpaper(context).execute(items.get(i).getLocalFileImage());
                                }

                            }

                            //TODO check if notifications are enabled
                            int notificationID= 12;
                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle(weather.location.getCity()+" - "+Math.floor(weather.temperature.getTemp()*10)/10+" °C")
                                    .setContentText(weather.currentCondition.getDescr());
                            /*// Creates an explicit intent for an Activity in your app
                            Intent resultIntent = new Intent(this, ResultActivity.class);

                            // The stack builder object will contain an artificial back stack for the
                            // started Activity.
                            // This ensures that navigating backward from the Activity leads out of
                            // your application to the Home screen.
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                            // Adds the back stack for the Intent (but not the Intent itself)
                            stackBuilder.addParentStack(ResultActivity.class);
                            // Adds the Intent that starts the Activity to the top of the stack
                            stackBuilder.addNextIntent(resultIntent);
                            PendingIntent resultPendingIntent =
                                    stackBuilder.getPendingIntent(
                                        0,
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                    );
                            mBuilder.setContentIntent(resultPendingIntent);*/
                            NotificationManager mNotificationManager =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                            // mId allows you to update the notification later on.
                            mNotificationManager.notify(notificationID, mBuilder.build());

                    }
                }

                @Override
                public void onWeatherError(WeatherLibException t) {
                    // Something went wrong maybe we should inform the user
                }

                @Override
                public void onConnectionError(Throwable t) {
                    // There was a connection error
                }
            });

        } catch (WeatherProviderInstantiationException e) {
            e.printStackTrace();
        }
    }

    private int correctWeatherID(int weatherID) {
        if(weatherID>801 && weatherID<805){
            weatherID=801;
        }else if(weatherID>201 && weatherID<232){
            weatherID=201;
        }else if(weatherID>301 && weatherID<321){
            weatherID=301;
        }else if(weatherID>501 && weatherID<532){
            weatherID=501;
        }else if(weatherID>601 && weatherID<623){
            weatherID=601;
        }else if(weatherID>701 && weatherID<782){
            weatherID=701;
        }else if(weatherID>901 && weatherID<963){
            weatherID=900;
        }
        // TODO: 29/01/2017 OTHER CASES (most specific)
        
        return weatherID;
    }
}
