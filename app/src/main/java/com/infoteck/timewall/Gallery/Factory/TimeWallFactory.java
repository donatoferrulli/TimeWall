package com.infoteck.timewall.Gallery.Factory;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.infoteck.timewall.Utilities.jsonFromUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Pc on 29/12/2016.
 */

public class TimeWallFactory extends AbstractItemFactory{

    public TimeWallFactory(Context context){
        //get default images json from web
        try {
            SharedPreferences sharedPref = context.getSharedPreferences( "appData", Context.MODE_PRIVATE);
            webDefaultJson= new JSONObject(sharedPref.getString("webDefaultJson","{}"));
            if(webDefaultJson.length()==0){
                webDefaultJson  = new jsonFromUrl().execute("https://api.myjson.com/bins/1e6ybf").get();
                String str = webDefaultJson.toString();
                SharedPreferences.Editor prefEditor = context.getSharedPreferences( "appData", Context.MODE_PRIVATE).edit();
                prefEditor.putString( "webDefaultJson", str );
                prefEditor.commit();
            }

            //TODO check version and update local storage
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<Item> findItem(String query) {
        return items;
    }


    @Override
    public List<Item> getCalendarItem(String type) {
        List<Item> tempList = null;
        if (type.equals("calendar")){
            tempList=getDailyElements(type);
        }else if(type.equals("week") || type.equals("month")){
            tempList=getCalendarElements(type);
        }
        return tempList;
    }

    @Override
    public List<Item> getWeatherItem() {
        items = new ArrayList<Item>();
        try {
            JSONObject jObject = webDefaultJson.getJSONObject("weather");
            Iterator<?> keys = jObject.keys();

            while( keys.hasNext() ) {
                String key = (String)keys.next();
                if ( jObject.get(key) instanceof JSONObject ) {
                    JSONObject tempJObject = (JSONObject)jObject.get(key);
                    items.add(new Item(tempJObject.getString("type"), tempJObject.getString("author"),tempJObject.getString("ThumbnailUrl") ,tempJObject.getString("PhotoUrl"),Integer.parseInt(key)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return items;
    }

    @Override
    public List<Item> getItems() {
        return items;
    }

    @Override
    public void clearItems() {
        items.clear();
    }

    @Override
    public Item getItem(int id) {
        for (Item item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void setItemPath(int id, String path) {
        for (Item item : items) {
            if (item.getId() == id) {
                item.setLocalPath(path);
                //TODO update webDefaultJson
            }
        }
    }

    @Override
    public int getFactoryVersion() {
        return factoryVersion;
    }

    public List<Item> getDailyElements(String type){
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int tempMonth = calendar.get(Calendar.MONTH);
        Calendar calendarPlus7Days = Calendar.getInstance();
        calendarPlus7Days.add(Calendar.DATE, 7);
        items = new ArrayList<Item>();
        try {
            JSONObject jObject = webDefaultJson.getJSONObject(type).getJSONObject(String.valueOf(currentMonth));
            int currentDay=calendar.get(Calendar.DAY_OF_MONTH);
            while(calendar.getTimeInMillis() < calendarPlus7Days.getTimeInMillis()){
                tempMonth = calendar.get(Calendar.MONTH);
                if (tempMonth>currentMonth){
                    jObject = webDefaultJson.getJSONObject(type).getJSONObject(String.valueOf(tempMonth));
                }
                JSONObject dayObject = (JSONObject)jObject.get(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                items.add(new Item(new SimpleDateFormat("dd/MM").format(calendar.getTime()), dayObject.getString("author"),dayObject.getString("ThumbnailUrl") ,dayObject.getString("PhotoUrl")));
                calendar.add(Calendar.DATE, 1);
            }



        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return items;
    }


    public List<Item> getCalendarElements(String type){
        items = new ArrayList<Item>();
        try {
            JSONObject jObject = webDefaultJson.getJSONObject(type);
            Iterator<?> keys = jObject.keys();

            while( keys.hasNext() ) {
                String key = (String)keys.next();
                if ( jObject.get(key) instanceof JSONObject ) {
                    JSONObject tempJObject = (JSONObject)jObject.get(key);
                    //TODO names
                    items.add(new Item(tempJObject.getString("name"), tempJObject.getString("author"),tempJObject.getString("ThumbnailUrl") ,tempJObject.getString("PhotoUrl")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return items;
    }



}
