package com.infoteck.timewall.Gallery.Factory;

import android.content.Context;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Pc on 29/12/2016.
 */

public abstract class AbstractItemFactory {
    static List<Item> items = new ArrayList<Item>();
    static String currentJsonView="";
    int factoryVersion;
    JSONObject webDefaultJson;

    public static AbstractItemFactory getAbstractItemFactory(String type, Context context){
        if(type.equals("TimeWall")){
            return new TimeWallFactory(context);
        }else{
            return null;
        }
    };
    public abstract List<Item> findItem(String query);
    public abstract List<Item> getCalendarItem(String type);
    public abstract List<Item> getWeatherItem();
    public abstract List<Item> getItems();
    public abstract void clearItems();
    public abstract Item getItem(int id);
    public abstract void setItemPath(Item itemToEdit, String path);
    public abstract int getFactoryVersion();

    public List<Item> getFavoriteItem(){
            currentJsonView="favorite";
            List<Item> items = new ArrayList<Item>();
            String path=Environment.getExternalStorageDirectory() + File.separator + "TimeWall"+ File.separator + "Favorite";

            File file = new File(path);
            String [] imagesPath=file.list();
            for (String fileName : imagesPath) {
                if (fileName.toLowerCase().endsWith(".jpg")) {
                    items.add(new Item(fileName.replace(".jpg",""), "" ,"",path+File.separator+fileName));
                }
            }

            return items;

    }

        public List<Item> getRandomLocalItem(String path){
            currentJsonView="random";
            List<Item> items = new ArrayList<Item>();
            File file = new File(path);
            String [] imagesPath=file.list();
            for (String fileName : imagesPath) {
                if (fileName.toLowerCase().endsWith(".jpg")) {
                    items.add(new Item(fileName.replace(".jpg",""), "" ,"",path+File.separator+fileName));
                }
            }
            long seed = System.nanoTime();
            Collections.shuffle(items, new Random(seed));

            return items;

    }

}
