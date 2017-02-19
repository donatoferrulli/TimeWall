package com.infoteck.timewall.Utilities;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

/**
 * Created by Pc on 29/12/2016.
 */

public class changeWallpaper extends AsyncTask<String, String, Void> {

    private Context mContext;

    public changeWallpaper(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... args) {
        try {
            //getDisplaySize
            int widthPixels;
            int heightPixels;
            WindowManager w = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display d = w.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            d.getMetrics(metrics);
// since SDK_INT = 1;
            widthPixels = metrics.widthPixels;
            heightPixels = metrics.heightPixels;
// includes window decorations (statusbar bar/menu bar)
            if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
                try {
                    widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                    heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
                } catch (Exception ignored) {
                }
// includes window decorations (statusbar bar/menu bar)
            if (Build.VERSION.SDK_INT >= 17)
                try {
                    Point realSize = new Point();
                    Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                    widthPixels = realSize.x;
                    heightPixels = realSize.y;
                } catch (Exception ignored) {
                }
            Bitmap result;
            //Load and set wallpaper background
            if (args[0].contains("http")){
                result= Picasso.with(mContext)
                        .load(args[0])
                        .resize(widthPixels,heightPixels)
                        .get();
            }else{
                File file = new File(args[0]);
                result= Picasso.with(mContext)
                        .load(file)
                        .resize(widthPixels,heightPixels)
                        .get();
            }

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(mContext);
            wallpaperManager.setBitmap(result);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }




}