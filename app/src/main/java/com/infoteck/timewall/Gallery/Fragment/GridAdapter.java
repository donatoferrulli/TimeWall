package com.infoteck.timewall.Gallery.Fragment;

/**
 * Created by Pc on 31/12/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.infoteck.timewall.Gallery.Factory.AbstractItemFactory;
import com.infoteck.timewall.Gallery.Factory.Item;
import com.infoteck.timewall.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * {@link BaseAdapter} which displays items.
 */
class GridAdapter extends BaseAdapter {

    private List<Item> items;
    private Context context;
    private AbstractItemFactory factory ;
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
        factory = AbstractItemFactory.getAbstractItemFactory("TimeWall",view.getContext());

        //Log.e("getView",position+" "+getItem(position).getName());

        final Item item = getItem(position);
        final ImageView imageView;
        final TextView name;
        // Load the thumbnail image
        imageView = (ImageView) view.findViewById(R.id.imageview_item);
        name = (TextView) view.findViewById(R.id.textview_name);
        name.setText(item.getName());

        if(item.getLocalFileImage()==null){
            Log.i("GridAdapter","Load image from web "+item.getName());
            Picasso.with(view.getContext())
                    .load(item.getPhotoUrl())
//                    .resize(500, 500)
//                    .centerInside()
                    .into(imageView,
                            PicassoPalette.with(item.getPhotoUrl(), imageView)
                                    .use(PicassoPalette.Profile.VIBRANT)
                                    .intoBackground(name, PicassoPalette.Swatch.RGB)
                                    .intoTextColor(name, PicassoPalette.Swatch.BODY_TEXT_COLOR)
                    );
            //save image
            Picasso.with(view.getContext())
                    .load(item.getPhotoUrl())
                    .into(getTarget(item,""));
        }else{
            Log.i("GridAdapter","Load image from local storage "+item.getName()+" "+item.getLocalFileImage().replace(".jpg","_thumb.jpg"));

            File file = new File(item.getLocalFileImage().replace(".jpg","_thumb.jpg"));
            if (file.exists()){
                Picasso.with(view.getContext())
                        .load(file)
                        .into(imageView,
                                PicassoPalette.with(item.getPhotoUrl(), imageView)
                                        .use(PicassoPalette.Profile.VIBRANT)
                                        .intoBackground(name, PicassoPalette.Swatch.RGB)
                                        .intoTextColor(name, PicassoPalette.Swatch.BODY_TEXT_COLOR)
                        );
            }else{
                factory.setItemPath(item,null);
            }


        }



        return view;
    }




    //target to save
    private Target getTarget(final Item item , final String dirPath){
        Target target = new Target(){

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                        String pathGlobal = Environment.getExternalStorageDirectory() + File.separator + "TimeWall";
                        String path= pathGlobal+ "/"+dirPath+item.getId()+".jpg";
                        try{

                            File file = new File(path);
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                            ostream.flush();
                            ostream.close();
                            //create thumbnail
                            try {
                                createThumbFileFromBitmap(Bitmap.createScaledBitmap(bitmap,225,400,false),item);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //add Item Path to ITEMS
                            factory.setItemPath(item,path);
                            Log.e("GridAdapter.getTarget","Path created:"+path);




                        } catch (Exception e) {
                            Log.e("Exception GridAdapter", e.getMessage());
                            Log.e("Exception GridAdapter2", item.getName()+" "+path);

                            File dirPath = new File(pathGlobal);
                            if (!dirPath.exists())
                                dirPath.mkdirs();
                            dirPath = new File(pathGlobal+ "/Favorite");
                            if (!dirPath.exists())
                                dirPath.mkdirs();
                            dirPath = new File(pathGlobal+ "/User_photos");
                            if (!dirPath.exists())
                                dirPath.mkdirs();
                        }

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }


    private void createThumbFileFromBitmap(final Bitmap bitmap, final Item item) throws IOException {
        Log.e("GridAdapter.createThumb","Create thumb: "+item.getName());
        new Thread(new Runnable() {

            @Override
            public void run() {
                String pathGlobal = Environment.getExternalStorageDirectory() + File.separator + "TimeWall";

                try{
                    String path= pathGlobal+ "/"+""+item.getId()+"_thumb.jpg";
                    File file = new File(path);
                    file.createNewFile();
                    FileOutputStream ostream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                    ostream.flush();
                    ostream.close();

                } catch (IOException e) {
                    Log.e("IOException", e.getLocalizedMessage());

                    File dirPath = new File(pathGlobal);
                    if (!dirPath.exists())
                        dirPath.mkdirs();
                    dirPath = new File(pathGlobal+ "/Favorite");
                    if (!dirPath.exists())
                        dirPath.mkdirs();
                    dirPath = new File(pathGlobal+ "/User_photos");
                    if (!dirPath.exists())
                        dirPath.mkdirs();
                }
            }
        }).start();

    }

}
