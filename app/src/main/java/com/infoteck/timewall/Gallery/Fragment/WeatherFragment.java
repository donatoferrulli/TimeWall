package com.infoteck.timewall.Gallery.Fragment;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.app.Fragment;
import android.support.v4.util.Pair;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.infoteck.timewall.Gallery.DetailActivity;
import com.infoteck.timewall.Gallery.Factory.AbstractItemFactory;
import com.infoteck.timewall.Gallery.Factory.Item;
import com.infoteck.timewall.Gallery.Services.alarmWeather;
import com.infoteck.timewall.Gallery.Services.serviceCalendar;
import com.infoteck.timewall.Gallery.Services.serviceWeather;
import com.infoteck.timewall.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.client.okhttp.WeatherDefaultClient;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.exception.WeatherProviderInstantiationException;
import com.survivingwithandroid.weather.lib.model.City;
import com.survivingwithandroid.weather.lib.provider.openweathermap.OpenweathermapProviderType;

import java.util.List;
import java.util.Locale;

import static com.infoteck.timewall.Gallery.GalleryActivity.collapsingToolbarLayout;
import static com.infoteck.timewall.Gallery.GalleryActivity.fabStart;
import static com.infoteck.timewall.Gallery.GalleryActivity.subTitle;

/**
 * Created by Pc on 31/12/2016.
 */

public class WeatherFragment extends Fragment  implements AdapterView.OnItemClickListener {
    private GridView mGridView;
    private GridAdapter mAdapter;
    private AbstractItemFactory factory ;

    public WeatherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.grid, container, false);
        factory = AbstractItemFactory.getAbstractItemFactory("TimeWall",rootView.getContext());
        //enable menu control
        setHasOptionsMenu(true);
        //Setup the GridView and set the adapter
        mGridView = (GridView) rootView.findViewById(R.id.grid);
        mGridView.setBackgroundColor(getResources().getColor(R.color.weatherActivity));
        mGridView.setNestedScrollingEnabled(true);
        mGridView.setOnItemClickListener(this);

        SharedPreferences prefs = getActivity().getSharedPreferences("weatherPreferences", Context.MODE_PRIVATE);
        collapsingToolbarLayout.setTitle(prefs.getString("WeatherCity","Weather"));
        subTitle.setText(prefs.getString("WeatherConditions",""));

        //Setup the adapter
        List<Item> items= factory.getWeatherItem();
        if (items!=null){
            mAdapter = new GridAdapter(items,getActivity());
            mGridView.setAdapter(mAdapter);
        }


        if (isMyServiceRunning(serviceWeather.class)){
            fabStart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.onColor)));
            fabStart.setImageDrawable(new IconicsDrawable(rootView.getContext())
                    .icon(GoogleMaterial.Icon.gmd_flash_on)
                    .color(Color.WHITE)
                    .sizeDp(24));
        }else{
            fabStart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.offColor)));
            fabStart.setImageDrawable(new IconicsDrawable(rootView.getContext())
                    .icon(GoogleMaterial.Icon.gmd_flash_off)
                    .color(Color.WHITE)
                    .sizeDp(24));
        }
        fabStart.setVisibility(View.VISIBLE);

        fabStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isMyServiceRunning(serviceWeather.class)) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), serviceWeather.class);
                    getActivity().stopService(intent);
                    Log.e("fab","Stop calendar service");
                    fabStart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.offColor)));
                    fabStart.setImageDrawable(new IconicsDrawable(rootView.getContext())
                            .icon(GoogleMaterial.Icon.gmd_flash_off)
                            .color(Color.WHITE)
                            .sizeDp(24));
                    Toast.makeText(rootView.getContext(),R.string.switchCalendarNotActive,Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), serviceWeather.class);
                    getActivity().startService(intent);
                    Log.e("fab","Start calendar service");
                    fabStart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.onColor)));
                    fabStart.setImageDrawable(new IconicsDrawable(rootView.getContext())
                            .icon(GoogleMaterial.Icon.gmd_flash_on)
                            .color(Color.WHITE)
                            .sizeDp(24));
                    Toast.makeText(rootView.getContext(),R.string.switchWeatherActive,Toast.LENGTH_SHORT).show();
                }

            }
        });

        return rootView;

    }

    @Override
    public void onResume(){
        super.onResume();
        Log.e("weatherFragment","onresume");
        mAdapter.notifyDataSetChanged();

    }


    /**
     * Called when an item in the {@link GridView} is clicked. Here will launch the
     * {@link DetailActivity}, using the Scene Transition animation functionality.
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Item item = (Item) adapterView.getItemAtPosition(position);

        // Construct an Intent as normal
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_PARAM_ID, item.getId());

        // BEGIN_INCLUDE(start_activity)
        /**
         * Now create an {@link android.app.ActivityOptions} instance using the
         * {@link ActivityOptionsCompat#makeSceneTransitionAnimation(Activity, Pair[])} factory
         * method.
         */
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(),

                // Now we provide a list of Pair items which contain the view we can transitioning
                // from, and the name of the view it is transitioning to, in the launched activity
                new Pair<View, String>(view.findViewById(R.id.imageview_item),
                        DetailActivity.VIEW_NAME_HEADER_IMAGE),
                new Pair<View, String>(view.findViewById(R.id.textview_name),
                        DetailActivity.VIEW_NAME_HEADER_TITLE));

        // Now we can start the Activity, providing the activity options as a bundle
        ActivityCompat.startActivity(getActivity(), intent, activityOptions.toBundle());
        // END_INCLUDE(start_activity)
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        menu.clear();
        inflater.inflate(R.menu.weather_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changeLocation:
                // Do onlick on menu action here
                dialogChangeLocation(getActivity());
                return true;
            case R.id.updateWeather:
                // Do onlick on menu action here
                updateWeather(getActivity());
                return true;
            case R.id.changeInterval:
                // Do onlick on menu action here
                dialogChangeInterval(getActivity());
                return true;
            }
    return false;
    }

    private void updateWeather(final Context context) {
        final SharedPreferences prefs = context.getSharedPreferences("weatherPreferences", Context.MODE_PRIVATE);
        Log.e("Updating this city:",prefs.getString("CityLocationID",""));
        Intent alarmIntent = new Intent(context, alarmWeather.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);

    }


    public void dialogChangeLocation(final Context context) {
        new MaterialDialog.Builder(context)
                .title(R.string.changeLocationTitle)
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT)
                .input(R.string.changeLocationDescription, R.string.nullString, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                        // TODO: 30/01/2017 progress bar while loading
                        final MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                                .title(R.string.searchLocation)
                                .content(R.string.please_wait)
                                .progress(true, 0)
                                .show();
                       WeatherClient.ClientBuilder builder = new WeatherClient.ClientBuilder();
                       WeatherConfig config = new WeatherConfig();
                       config.ApiKey = getResources().getString(R.string.ApiKey);
                       config.unitSystem = WeatherConfig.UNIT_SYSTEM.M;
                       config.lang = Locale.getDefault().getCountry();
                       config.maxResult = 5; // Max number of cities retrieved
                       config.numDays = 6; // Max num of days in the forecast
                       WeatherClient weatherClient = null;
                       try {
                           weatherClient = builder.attach(context)
                                   .provider(new OpenweathermapProviderType())
                                   .httpClient(WeatherDefaultClient.class)
                                   .config(config)
                                   .build();
                       } catch (WeatherProviderInstantiationException e) {
                           e.printStackTrace();
                       }
                       weatherClient.searchCity(input.toString(), new WeatherClient.CityEventListener() {
                           @Override
                           public void onCityListRetrieved(final List cities) {
                               materialDialog.cancel();
                               //// TODO: 30/01/2017 remove progress bar
                              // The data is ready
                               new MaterialDialog.Builder(context)
                                       .title(R.string.locationFounded)
                                       .items(cities)
                                       .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                           @Override
                                           public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                            City item =(City)cities.get(which);
                                            SharedPreferences prefs = context.getSharedPreferences("weatherPreferences", Context.MODE_PRIVATE);
                                            collapsingToolbarLayout.setTitle(item.getName());
                                            SharedPreferences.Editor editor = prefs.edit();
                                            editor.putString("CityLocationID", item.getId());
                                            editor.putString("WeatherCity", item.getName());
                                            editor.commit();
                                            Log.e("CityLocation:",item.toString()+"-"+item.getId());
                                            Intent alarmIntent = new Intent(context, alarmWeather.class);
                                            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
                                            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                            manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
                                            return true;
                                           }
                                       })
                                       .positiveText(android.R.string.ok)
                                       .show();
                           }

                           @Override
                           public void onWeatherError(WeatherLibException e) {
                              // Error on geting data
                               materialDialog.cancel();
                               Toast.makeText(context, R.string.noCityFound, Toast.LENGTH_SHORT).show();
                           }

                           @Override
                           public void onConnectionError(Throwable throwable) {
                              // Connection error
                               materialDialog.cancel();
                               Toast.makeText(context, R.string.connectionProblem, Toast.LENGTH_SHORT).show();
                           }
                        });
                    }
                })
                .positiveText(R.string.find)
                .show();

    
    }

    public void dialogChangeInterval(final Context context) {
        String[] items = {"30 "+getResources().getString(R.string.minute),"60 "+getResources().getString(R.string.minute),"90 "+getResources().getString(R.string.minute)};

        new MaterialDialog.Builder(context)
                .title(R.string.changeIntervalTitle)
                .items(items)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        String stringText= text.toString();
                        SharedPreferences prefs = context.getSharedPreferences("weatherPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        String[] splited = stringText.split("\\s+");
                        editor.putLong("Interval", Integer.valueOf(splited[0])*1000*60);
                        editor.commit();
                        Log.e("Interval",String.valueOf(Integer.valueOf(splited[0])*1000*60));
                        Intent alarmIntent = new Intent(context, alarmWeather.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
                        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
                        return true;
                    }
                })
                .positiveText(android.R.string.ok)
                .show();
    
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



}