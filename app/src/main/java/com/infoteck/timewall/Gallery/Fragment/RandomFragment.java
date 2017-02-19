package com.infoteck.timewall.Gallery.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.infoteck.timewall.Gallery.DetailActivity;
import com.infoteck.timewall.Gallery.Factory.AbstractItemFactory;
import com.infoteck.timewall.Gallery.Factory.Item;
import com.infoteck.timewall.R;

import java.util.List;

/**
 * Created by Pc on 31/12/2016.
 */

public class RandomFragment extends Fragment implements AdapterView.OnItemClickListener{
	private GridView mGridView;
    private GridAdapter mAdapter;
    private AbstractItemFactory factory;

    public RandomFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.grid, container, false);
        factory = AbstractItemFactory.getAbstractItemFactory("TimeWall",rootView.getContext());

        mGridView = (GridView) rootView.findViewById(R.id.grid);
        mGridView.setNestedScrollingEnabled(true);
        mGridView.setOnItemClickListener(this);

        //Setup the adapter
        SharedPreferences prefs = rootView.getContext().getSharedPreferences("randomPreferences", Context.MODE_PRIVATE);
        if(prefs.getString("paths","").equals("")){

        }else{
            List<Item> items= factory.getRandomLocalItem(prefs.getString("paths",""));
            if (items!=null){
                mAdapter = new GridAdapter(items,getActivity());
                mGridView.setAdapter(mAdapter);
            }
        }



        return rootView;

    }

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
}