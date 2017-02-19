package com.infoteck.timewall.Gallery.Fragment;

/**
 * Created by Pc on 31/12/2016.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.infoteck.timewall.Gallery.Factory.Item;
import com.infoteck.timewall.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * {@link BaseAdapter} which displays items.
 */
class GridAdapter extends BaseAdapter {

    private List<Item> items;
    private Context context;
    GridAdapter(List<Item> list, Context contextReceived){
        items=list;
        context=contextReceived;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Item getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.grid_item, viewGroup, false);
        }

        //Log.e("getView",position+" "+getItem(position).getName());

        final Item item = getItem(position);
        final ImageView imageView;
        final TextView name;
        // Load the thumbnail image
        imageView = (ImageView) view.findViewById(R.id.imageview_item);
        name = (TextView) view.findViewById(R.id.textview_name);
        name.setText(item.getName());

        if(item.getLocalFileImage()==null){
            Log.i("GridAdapter","Load image from web");
            Picasso.with(view.getContext())
                    .load(item.getThumbnailUrl())
//                    .resize(500, 500)
//                    .centerInside()
                    .into(imageView,
                            PicassoPalette.with(item.getThumbnailUrl(), imageView)
                                    .use(PicassoPalette.Profile.VIBRANT)
                                    .intoBackground(name, PicassoPalette.Swatch.RGB)
                                    .intoTextColor(name, PicassoPalette.Swatch.BODY_TEXT_COLOR)
                    );
        }else{
            Log.i("GridAdapter","Load image from local storage");
            File file = new File(item.getLocalFileImage());
            Picasso.with(view.getContext())
                    .load(file)
                    .resize(500, 500)
                    .centerInside()
                    .into(imageView,
                            PicassoPalette.with(item.getThumbnailUrl(), imageView)
                                    .use(PicassoPalette.Profile.VIBRANT)
                                    .intoBackground(name, PicassoPalette.Swatch.RGB)
                                    .intoTextColor(name, PicassoPalette.Swatch.BODY_TEXT_COLOR)
                    );
        }



        return view;
    }
}
