package com.infoteck.timewall.Gallery.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.infoteck.timewall.Gallery.DetailActivity;
import com.infoteck.timewall.Gallery.DetailActivityFavorite;
import com.infoteck.timewall.Gallery.Factory.AbstractItemFactory;
import com.infoteck.timewall.Gallery.Factory.Item;
import com.infoteck.timewall.Gallery.Services.serviceFavorite;
import com.infoteck.timewall.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

import static com.infoteck.timewall.Gallery.GalleryActivity.fabStart;

/**
 * Created by Pc on 31/12/2016.
 */

public class FavoriteFragment extends Fragment  implements AdapterView.OnItemClickListener {
    private GridView mGridView;
    private GridAdapter mAdapter;
    private AbstractItemFactory factory ;
    Boolean service_status;

    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.grid, container, false);
        factory = AbstractItemFactory.getAbstractItemFactory("TimeWall",rootView.getContext());
        //Setup the GridView and set the adapter
        mGridView = (GridView) rootView.findViewById(R.id.grid);
        mGridView.setBackgroundColor(getResources().getColor(R.color.homeActivity));
        mGridView.setNestedScrollingEnabled(true);
        mGridView.setOnItemClickListener(this);

        if (isMyServiceRunning(serviceFavorite.class)){
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
                service_status=isMyServiceRunning(serviceFavorite.class);
                if (service_status) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), serviceFavorite.class);
                    getActivity().stopService(intent);
                    Log.e("fab","Stop calendar service");
                    fabStart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.offColor)));
                    fabStart.setImageDrawable(new IconicsDrawable(rootView.getContext())
                            .icon(GoogleMaterial.Icon.gmd_flash_off)
                            .color(Color.WHITE)
                            .sizeDp(24));
                    Toast.makeText(rootView.getContext(),R.string.switchFavoriteNotActive,Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), serviceFavorite.class);
                    getActivity().startService(intent);
                    Log.e("fab","Start calendar service");
                    fabStart.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.onColor)));
                    fabStart.setImageDrawable(new IconicsDrawable(rootView.getContext())
                            .icon(GoogleMaterial.Icon.gmd_flash_on)
                            .color(Color.WHITE)
                            .sizeDp(24));
                    Toast.makeText(rootView.getContext(),R.string.switchFavoriteActive,Toast.LENGTH_SHORT).show();
                }

            }
        });

        return rootView;

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(getActivity(), permissions[0]) == PackageManager.PERMISSION_GRANTED) {

            switch (requestCode) {
                case 1:
                    List<Item> items = factory.getFavoriteItem();
                    if (items != null) {
                        mAdapter = new GridAdapter(items, getActivity());
                        mGridView.setAdapter(mAdapter);
                    }
                    break;
            }
        }else{
            Log.e("onRequestPermissions","no");
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter!=null){
            mAdapter.notifyDataSetChanged();
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(getActivity(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle(R.string.permissionTitle);
                alertBuilder.setMessage(R.string.permissionStorage);
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                });

                AlertDialog alert = alertBuilder.create();
                alert.show();
            }else{
                List<Item> items= factory.getFavoriteItem();
                if (items!=null){
                    mAdapter = new GridAdapter(items,getActivity());
                    mGridView.setAdapter(mAdapter);
                }
            }
        }
    }

    /**
     * Called when an item in the {@link GridView} is clicked. Here will launch the
     * {@link DetailActivity}, using the Scene Transition animation functionality.
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Item item = (Item) adapterView.getItemAtPosition(position);

        // Construct an Intent as normal
        Intent intent = new Intent(getActivity(), DetailActivityFavorite.class);
        int sadsa= item.getId();
        intent.putExtra(DetailActivityFavorite.EXTRA_PARAM_ID, item.getId());

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
                        DetailActivityFavorite.VIEW_NAME_HEADER_IMAGE),
                new Pair<View, String>(view.findViewById(R.id.textview_name),
                        DetailActivityFavorite.VIEW_NAME_HEADER_TITLE));

        // Now we can start the Activity, providing the activity options as a bundle
        ActivityCompat.startActivity(getActivity(), intent, activityOptions.toBundle());
        // END_INCLUDE(start_activity)
    }

    //TODO: IMPLEMENT IN UTILS
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