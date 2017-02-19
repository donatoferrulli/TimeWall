package com.infoteck.timewall.Gallery.Fragment;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import com.infoteck.timewall.Gallery.Services.*;
import com.infoteck.timewall.R;

import static com.infoteck.timewall.Gallery.GalleryActivity.fabStart;
import static com.infoteck.timewall.Gallery.GalleryActivity.result;

/**
 * Created by Pc on 31/12/2016.
 */

public class HomeFragment extends Fragment   {

	SwitchCompat switchCalendar,switchWeather,switchFavorite,switchRandom;
    CardView cardCalendar,cardWeather,cardFavorite,cardRandom;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);

        fabStart.hide();
        cardCalendar=(CardView)rootView.findViewById(R.id.cardCalendar);
        cardWeather=(CardView)rootView.findViewById(R.id.cardWeather);
        cardFavorite=(CardView)rootView.findViewById(R.id.cardFavorite);
        cardRandom=(CardView)rootView.findViewById(R.id.cardRandom);

        cardCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setSelectionAtPosition(2);
            }
        });
        cardWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setSelectionAtPosition(3);
            }
        });
        cardFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setSelectionAtPosition(4);
            }
        });
        cardRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setSelectionAtPosition(5);
            }
        });

        switchCalendar=(SwitchCompat)rootView.findViewById(R.id.switchCalendar);
        switchWeather=(SwitchCompat)rootView.findViewById(R.id.switchWeather);
        switchFavorite=(SwitchCompat)rootView.findViewById(R.id.switchFavorite);
        switchRandom=(SwitchCompat)rootView.findViewById(R.id.switchRandom);

        loadCurrentServiceState();

 
        switchCalendar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Context context = buttonView.getContext();
                if(isChecked){
                    Toast.makeText(context, R.string.switchCalendarActive, Toast.LENGTH_SHORT).show();
                    getActivity().startService(new Intent(getActivity(),serviceCalendar.class));
                }else{
                    Toast.makeText(context, R.string.switchCalendarActive, Toast.LENGTH_SHORT).show();
                    getActivity().stopService(new Intent(getActivity(),serviceCalendar.class));
                }
            }
        });
        switchWeather.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Context context = buttonView.getContext();
                if(isChecked){
                     Toast.makeText(context, R.string.switchWeatherActive, Toast.LENGTH_SHORT).show();
                    getActivity().startService(new Intent(getActivity(),serviceWeather.class));
                }else{
                     Toast.makeText(context, R.string.switchWeatherNotActive, Toast.LENGTH_SHORT).show();
                    getActivity().stopService(new Intent(getActivity(),serviceWeather.class));
                }
                
            }
        });
        switchFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Context context = buttonView.getContext();
                if(isChecked){
                     Toast.makeText(context, R.string.switchFavoriteActive, Toast.LENGTH_SHORT).show();
                    getActivity().startService(new Intent(getActivity(),serviceFavorite.class));
                }else{
                     Toast.makeText(context, R.string.switchFavoriteNotActive, Toast.LENGTH_SHORT).show();
                    getActivity().stopService(new Intent(getActivity(),serviceFavorite.class));
                }
            }
        });
        switchRandom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Context context = buttonView.getContext();
                if(isChecked){
                     Toast.makeText(context, R.string.switchRandomActive, Toast.LENGTH_SHORT).show();
                    getActivity().startService(new Intent(getActivity(),serviceRandom.class));
                }else{
                     Toast.makeText(context, R.string.switchRandomNotActive, Toast.LENGTH_SHORT).show();
                    getActivity().stopService(new Intent(getActivity(),serviceRandom.class));
                }
            }
        });


        return rootView;

    }


    private void loadCurrentServiceState() {
        if (isServiceRunning("com.infoteck.timewall.Gallery.Services.serviceCalendar")) switchCalendar.setChecked(true);  else  switchCalendar.setChecked(false);
        if (isServiceRunning("com.infoteck.timewall.Gallery.Services.serviceFavorite")) switchFavorite.setChecked(true);  else  switchFavorite.setChecked(false);
        if (isServiceRunning("com.infoteck.timewall.Gallery.Services.serviceRandom")) switchRandom.setChecked(true);  else  switchRandom.setChecked(false);
        if (isServiceRunning("com.infoteck.timewall.Gallery.Services.serviceWeather")) switchWeather.setChecked(true);  else  switchWeather.setChecked(false);
        //// TODO: 29/01/2017 other services 
    }

    private boolean isServiceRunning(String nameService) {
        ActivityManager manager = (ActivityManager)getActivity().getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (nameService.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



}