package com.infoteck.timewall.Intro;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.infoteck.timewall.Gallery.GalleryActivity;
import com.infoteck.timewall.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Pc on 27/12/2016.
 */

public class Intro extends AppIntro2 {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  Initialize SharedPreferences
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        //  Create a new boolean and preference and set it to true
        boolean isFirstStart = getPrefs.getBoolean("firstStart", true);
        //  If the activity has never started before...
        if (!isFirstStart) {
            //  Launch app intro
            Intent i = new Intent(this, GalleryActivity.class);
            startActivity(i);
            finish();
        }
        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.intro1), "", R.mipmap.ic_launcher, Color.parseColor("#004272")));
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.intro2), getResources().getString(R.string.descriptionCalendar), R.drawable.intro_calendar, Color.parseColor("#EA782C")));
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.intro3), getResources().getString(R.string.descriptionWeather), R.drawable.intro_temperature, Color.parseColor("#005C9E")));
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.intro4), getResources().getString(R.string.descriptionFavorite), R.drawable.intro_like, Color.parseColor("#006775")));
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.intro5), getResources().getString(R.string.descriptionAssistant), R.drawable.intro_robot, Color.parseColor("#004272")));
        // SHOW or HIDE the statusbar
        showStatusBar(false);
        showSkipButton(false);

    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment,@Nullable Fragment newFragment){
        // Do something when users tap on Next button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        // Do something when users tap on Done button.
        finish();
        Intent i = new Intent(this, GalleryActivity.class);
        startActivity(i);
        //create local dir and files
        String path = Environment.getExternalStorageDirectory() + File.separator + "TimeWall";
        String pathFavorite = Environment.getExternalStorageDirectory() + File.separator + "TimeWall"+ File.separator + "Favorite";

        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(pathFavorite);
        if (!file.exists()) {
            file.mkdirs();
        }

        //  Initialize SharedPreferences
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        //  Make a new preferences editor
        SharedPreferences.Editor e = getPrefs.edit();
        //  Edit preference to make it false because we don't want this to run again
        e.putBoolean("firstStart", false);
        //  Apply changes
        e.apply();

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent i = new Intent(this, GalleryActivity.class);
        startActivity(i);
    }
}