package com.infoteck.timewall.Gallery.Fragment;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import com.infoteck.timewall.Gallery.Services.*;
import com.infoteck.timewall.R;

import static com.infoteck.timewall.Gallery.GalleryActivity.collapsingToolbarLayout;
import static com.infoteck.timewall.Gallery.GalleryActivity.fabStart;
import static com.infoteck.timewall.Gallery.GalleryActivity.imageView;
import static com.infoteck.timewall.Gallery.GalleryActivity.result;
import static com.infoteck.timewall.Gallery.GalleryActivity.subTitle;

/**
 * Created by Pc on 31/12/2016.
 */

public class HomeFragment extends Fragment   {

	SwitchCompat switchCalendar,switchWeather,switchFavorite,switchAssistant;
    CardView cardCalendar,cardWeather,cardFavorite,cardAssistant;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        setHasOptionsMenu(true);
        fabStart.hide();
        cardCalendar=(CardView)rootView.findViewById(R.id.cardCalendar);
        cardWeather=(CardView)rootView.findViewById(R.id.cardWeather);
        cardFavorite=(CardView)rootView.findViewById(R.id.cardFavorite);
        cardAssistant=(CardView)rootView.findViewById(R.id.cardAssistant);

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
//        cardAssistant.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                result.setSelectionAtPosition(5);
//            }
//        });

        switchCalendar=(SwitchCompat)rootView.findViewById(R.id.switchCalendar);
        switchWeather=(SwitchCompat)rootView.findViewById(R.id.switchWeather);
        switchFavorite=(SwitchCompat)rootView.findViewById(R.id.switchFavorite);
        switchAssistant=(SwitchCompat)rootView.findViewById(R.id.switchAssistant);

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
        switchAssistant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchCalendar.setChecked(false);
                getActivity().stopService(new Intent(getActivity(),serviceCalendar.class));
                switchFavorite.setChecked(false);
                getActivity().stopService(new Intent(getActivity(),serviceFavorite.class));
                switchWeather.setChecked(false);
                getActivity().stopService(new Intent(getActivity(),serviceWeather.class));
                Context context = buttonView.getContext();
                if(isChecked){
                     Toast.makeText(context, R.string.switchAssistantActive, Toast.LENGTH_SHORT).show();
                    getActivity().startService(new Intent(getActivity(),serviceAssistant.class));
                }else{
                     Toast.makeText(context, R.string.switchAssistantNotActive, Toast.LENGTH_SHORT).show();
                    getActivity().stopService(new Intent(getActivity(),serviceAssistant.class));
                }
            }
        });


        return rootView;

    }


    private void loadCurrentServiceState() {
        if (isServiceRunning("com.infoteck.timewall.Gallery.Services.serviceCalendar")) switchCalendar.setChecked(true);  else  switchCalendar.setChecked(false);
        if (isServiceRunning("com.infoteck.timewall.Gallery.Services.serviceFavorite")) switchFavorite.setChecked(true);  else  switchFavorite.setChecked(false);
        if (isServiceRunning("com.infoteck.timewall.Gallery.Services.serviceAssistant")) switchAssistant.setChecked(true);  else  switchAssistant.setChecked(false);
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        menu.clear();
        inflater.inflate(R.menu.menu_main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i=0;
        switch (item.getItemId()) {
            case R.id.settings:
                // Do onlick on menu action herecollapsingToolbarLayout.setTitle(getResources().getString(R.string.settings));
                imageView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.settings_background));
                fabStart.setVisibility(View.GONE);
                collapsingToolbarLayout.setTitle(getResources().getString(R.string.settings));
                subTitle.setText("");
                getFragmentManager().beginTransaction().replace(R.id.container,new SettingsFragment()).commit();

                return true;
        }
        return false;
    }

}