package com.infoteck.timewall.Gallery.Fragment;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.infoteck.timewall.Gallery.DetailActivity;
import com.infoteck.timewall.Gallery.Factory.AbstractItemFactory;
import com.infoteck.timewall.Gallery.Factory.Item;
import com.infoteck.timewall.Gallery.Services.alarmWeather;
import com.infoteck.timewall.Gallery.Services.serviceCalendar;
import com.infoteck.timewall.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

import static com.infoteck.timewall.Gallery.GalleryActivity.fabStart;

/**
 * Created by Pc on 31/12/2016.
 */

public class CalendarFragment extends Fragment  implements AdapterView.OnItemClickListener {
    private GridView mGridView;
    private GridAdapter mAdapter;
    private AbstractItemFactory factory;
    Boolean service_status;

    public CalendarFragment() {

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
        mGridView.setNestedScrollingEnabled(true);
        mGridView.setOnItemClickListener(this);

        //Setup the adapter
        SharedPreferences prefs = rootView.getContext().getSharedPreferences("calendarPreferences", Context.MODE_PRIVATE);
        List<Item> items= factory.getCalendarItem(prefs.getString("Mode","week"));
        if (items!=null){
            mAdapter = new GridAdapter(items,getActivity());
            mGridView.setAdapter(mAdapter);
        }

        if (isMyServiceRunning(serviceCalendar.class)){
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
                service_status=isMyServiceRunning(serviceCalendar.class);
                if (service_status) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), serviceCalendar.class);
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
                    intent.setClass(getActivity(), serviceCalendar.class);
                    getActivity().startService(intent);
                    Log.e("fab","Start calendar service");
                    fabStart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.onColor)));
                    fabStart.setImageDrawable(new IconicsDrawable(rootView.getContext())
                            .icon(GoogleMaterial.Icon.gmd_flash_on)
                            .color(Color.WHITE)
                            .sizeDp(24));
                    Toast.makeText(rootView.getContext(),R.string.switchCalendarActive,Toast.LENGTH_SHORT).show();
                }

            }
        });

        return rootView;

    }

    @Override
    public void onResume(){
        super.onResume();
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
        inflater.inflate(R.menu.calendar_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select_mode:
                // Do onlick on menu action here
                dialogSelectMode();
                return true;
        }
    return false;
    }

    public void dialogSelectMode() {
        String[] items = {getResources().getString(R.string.week),getResources().getString(R.string.month)};
        final Context context = this.getActivity();
        new MaterialDialog.Builder(context)
                .title(R.string.selectMode)
                .content(R.string.selectModeDescription)
                .items(items)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        String mode;
                        mode = (which == 0) ? "week" : "month";
                        SharedPreferences prefs = context.getSharedPreferences("calendarPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("Mode", mode);
                        editor.commit();
                        List<Item> items = factory.getCalendarItem(prefs.getString("Mode", "week"));
                        if (items != null) {
                            mAdapter = new GridAdapter(items, getActivity());
                            mGridView.setAdapter(mAdapter);
                        }
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