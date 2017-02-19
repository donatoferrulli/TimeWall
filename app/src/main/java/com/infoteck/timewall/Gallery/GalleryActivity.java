/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.infoteck.timewall.Gallery;

import com.infoteck.timewall.Gallery.Factory.Item;
import com.infoteck.timewall.Gallery.Fragment.CalendarFragment;
import com.infoteck.timewall.Gallery.Fragment.FavoriteFragment;
import com.infoteck.timewall.Gallery.Fragment.HomeFragment;
import com.infoteck.timewall.Gallery.Fragment.RandomFragment;
import com.infoteck.timewall.Gallery.Fragment.SettingsFragment;
import com.infoteck.timewall.Gallery.Fragment.WeatherFragment;
import com.infoteck.timewall.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Our main Activity in this sample. Displays a grid of items which an image and title. When the
 * user clicks on an item, {@link DetailActivity} is launched, using the Activity Scene Transitions
 * framework to animatedly do so.
 */
public class GalleryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static Drawer result;
    public static FloatingActionButton fabStart;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);

        fabStart = (FloatingActionButton) findViewById(R.id.fabStart);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        setSupportActionBar(toolbar);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.TimeWall));



        getFragmentManager().beginTransaction()
                .add(R.id.container, new HomeFragment()).commit();



        //NAVIGATION DRAWER
        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.header)
                .build();
        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem home = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.home).withIcon(GoogleMaterial.Icon.gmd_home);
        PrimaryDrawerItem calendar = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.calendar).withIcon(GoogleMaterial.Icon.gmd_event);
        PrimaryDrawerItem weather = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.weather).withIcon(GoogleMaterial.Icon.gmd_wb_sunny);
        PrimaryDrawerItem favorite = new PrimaryDrawerItem().withIdentifier(4).withName(R.string.favorite).withIcon(GoogleMaterial.Icon.gmd_favorite);
        PrimaryDrawerItem random = new PrimaryDrawerItem().withIdentifier(5).withName(R.string.random).withIcon(GoogleMaterial.Icon.gmd_shuffle);
        PrimaryDrawerItem settings = new PrimaryDrawerItem().withIdentifier(6).withName(R.string.settings).withIcon(GoogleMaterial.Icon.gmd_settings);

        //create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(home,calendar,weather,favorite,random,new DividerDrawerItem(),settings)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        int id = (int)drawerItem.getIdentifier();
                        switch (id) {
                            case 1:
                                collapsingToolbarLayout.setTitle(getResources().getString(R.string.home));
                                getFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
                                imageView.setBackground(ContextCompat.getDrawable(getApplication(), R.drawable.header));
                                fabStart.setVisibility(View.GONE);
                                break;
                            case 2:
                                collapsingToolbarLayout.setTitle(getResources().getString(R.string.calendar));
                                //get type of visualization from settings TODO
                                getFragmentManager().beginTransaction().replace(R.id.container,new CalendarFragment()).commit();
                                imageView.setBackground(ContextCompat.getDrawable(getApplication(), R.drawable.calendar_background));
                                break;
                            case 3:
                                collapsingToolbarLayout.setTitle(getResources().getString(R.string.weather));
                                getFragmentManager().beginTransaction().replace(R.id.container,new WeatherFragment()).commit();
                                imageView.setBackground(ContextCompat.getDrawable(getApplication(), R.drawable.weather_background));
                                break;
                            case 4:
                                collapsingToolbarLayout.setTitle(getResources().getString(R.string.favorite));
                                getFragmentManager().beginTransaction().replace(R.id.container,new FavoriteFragment()).commit();
                                break;
                            case 5:
                                collapsingToolbarLayout.setTitle(getResources().getString(R.string.random));
                                getFragmentManager().beginTransaction().replace(R.id.container,new RandomFragment()).commit();
                                break;
                            case 6:
                                collapsingToolbarLayout.setTitle(getResources().getString(R.string.settings));
                                getFragmentManager().beginTransaction().replace(R.id.container,new SettingsFragment()).commit();
                                break;
                            default:
                                break;
                        }
//                        mAdapter.notifyDataSetChanged();
                        return false;
                    }
                })
                .build();


    }

    /**
     * Called when an item in the {@link GridView} is clicked. Here will launch the
     * {@link DetailActivity}, using the Scene Transition animation functionality.
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Item item = (Item) adapterView.getItemAtPosition(position);

        // Construct an Intent as normal
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_PARAM_ID, item.getId());

        // BEGIN_INCLUDE(start_activity)
        /**
         * Now create an {@link android.app.ActivityOptions} instance using the
         * {@link ActivityOptionsCompat#makeSceneTransitionAnimation(Activity, Pair[])} factory
         * method.
         */
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,

                // Now we provide a list of Pair items which contain the view we can transitioning
                // from, and the name of the view it is transitioning to, in the launched activity
                new Pair<View, String>(view.findViewById(R.id.imageview_item),
                        DetailActivity.VIEW_NAME_HEADER_IMAGE),
                new Pair<View, String>(view.findViewById(R.id.textview_name),
                        DetailActivity.VIEW_NAME_HEADER_TITLE));

        // Now we can start the Activity, providing the activity options as a bundle
        ActivityCompat.startActivity(this, intent, activityOptions.toBundle());
        // END_INCLUDE(start_activity)
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
