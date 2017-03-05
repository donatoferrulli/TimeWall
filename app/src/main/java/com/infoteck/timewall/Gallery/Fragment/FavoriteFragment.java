package com.infoteck.timewall.Gallery.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.infoteck.timewall.Gallery.DetailActivity;
import com.infoteck.timewall.Gallery.DetailActivityFavorite;
import com.infoteck.timewall.Gallery.Factory.AbstractItemFactory;
import com.infoteck.timewall.Gallery.Factory.Item;
import com.infoteck.timewall.R;

import java.util.List;

/**
 * Created by Pc on 31/12/2016.
 */

public class FavoriteFragment extends Fragment  implements AdapterView.OnItemClickListener {
    private GridView mGridView;
    private GridAdapter mAdapter;
    private AbstractItemFactory factory ;

    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.grid, container, false);
        factory = AbstractItemFactory.getAbstractItemFactory("TimeWall",rootView.getContext());
        //Setup the GridView and set the adapter
        mGridView = (GridView) rootView.findViewById(R.id.grid);
        mGridView.setNestedScrollingEnabled(true);
        mGridView.setOnItemClickListener(this);

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


}