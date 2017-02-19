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

import com.github.florent37.picassopalette.PicassoPalette;
import com.infoteck.timewall.Gallery.Factory.AbstractItemFactory;
import com.infoteck.timewall.Gallery.Factory.Item;
import com.infoteck.timewall.Gallery.Fragment.HomeFragment;
import com.infoteck.timewall.R;
import com.infoteck.timewall.Utilities.changeWallpaper;
import com.konifar.fab_transformation.FabTransformation;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * Our secondary Activity which is launched from {@link GalleryActivity}. Has a simple detail UI
 * which has a large banner image, title and body text.
 */
public class DetailActivity extends AppCompatActivity {

    // Extra name for the ID parameter
    public static final String EXTRA_PARAM_ID = "detail:_id";

    // View name of the header image. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";

    // View name of the header title. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_TITLE = "detail:header:title";

    private AbstractItemFactory factory ;
    private ImageView mHeaderImageView;
    private Item mItem;
    Toolbar toolbar;
    private FloatingActionButton fab ;
    private Toolbar toolbarFooter;
    private ImageView modifyImageView;
    private ImageView defaultImageView;
    private ImageView addPhotoImageView;
    private ImageView favoriteImageView;
    private ImageView infoImageView;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase)); //wrap icon material for fab
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        factory = AbstractItemFactory.getAbstractItemFactory("TimeWall",this);
        // Retrieve the correct Item instance, using the ID provided in the Intent
        mItem = factory.getItem(getIntent().getIntExtra(EXTRA_PARAM_ID, 0));
        //get components view FOOTER
        fab = (FloatingActionButton) findViewById(R.id.fab);
        toolbarFooter = (Toolbar) findViewById(R.id.toolbar_footer);
        modifyImageView = (ImageView) findViewById(R.id.modifyImageView);
        defaultImageView = (ImageView) findViewById(R.id.defaultImageView);
        addPhotoImageView = (ImageView) findViewById(R.id.addPhotoImageView);
        favoriteImageView = (ImageView) findViewById(R.id.favoriteImageView);
        infoImageView = (ImageView) findViewById(R.id.infoImageView);

        //get components view
        mHeaderImageView = (ImageView) findViewById(R.id.imageview_header);
        toolbar = (Toolbar) findViewById(R.id.toolbarDetail);
        toolbar.setTitle(mItem.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        // BEGIN_INCLUDE(detail_set_view_name)
        /**
         * Set the name of the view's which will be transition to, using the static values above.
         * This could be done in the layout XML, but exposing it via static variables allows easy
         * querying from other Activities
         */
        ViewCompat.setTransitionName(mHeaderImageView, VIEW_NAME_HEADER_IMAGE);
        // END_INCLUDE(detail_set_view_name)

        //fab management
        fab.hide();
        Handler mHandler= new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fab.show();
            }
        }, 1000);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FabTransformation.with(fab)
                        //.duration(1000)
                        .transformTo(toolbarFooter);
            }
        });
        //Toolbar actions management
        modifyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FabTransformation.with(fab)
                        .transformFrom(toolbarFooter);
            }
        });
        defaultImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FabTransformation.with(fab)
                        .transformFrom(toolbarFooter);

            }
        });
        addPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FabTransformation.with(fab)
                        .transformFrom(toolbarFooter);

            }
        });
        favoriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FabTransformation.with(fab)
                        .transformFrom(toolbarFooter);
                Picasso.with(view.getContext())
                        .load(mItem.getPhotoUrl())
                        .into(getTarget(mItem.getId(),"Favorite/"));
                Toast.makeText(view.getContext(),R.string.favoriteCreated,Toast.LENGTH_SHORT).show();

            }
        });

        infoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FabTransformation.with(fab)
                        .transformFrom(toolbarFooter);

                AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                builder.setTitle(mItem.getName())
                        .setMessage(getResources().getString(R.string.createdBy)+": "+mItem.getAuthor())
                        .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder.show();

            }
        });

        //ONLY FOR TEST PURPOSE
        new changeWallpaper(getApplicationContext()).execute(mItem.getPhotoUrl());
        ///////////
        loadItem();
    }

    private void loadItem() {
        // Set the title TextView to the item's name and author
        //mHeaderTitle.setText(getString(R.string.image_header, mItem.getName(), mItem.getAuthor()));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && addTransitionListener()) {
            // If we're running on Lollipop and we have added a listener to the shared element
            // transition, load the thumbnail. The listener will load the full-size image when
            // the transition is complete.
            loadThumbnail();
        } else {
            // If all other cases we should just load the full-size image now
            loadFullSizeImage();
        }
    }

    /**
     * Load the item's thumbnail image into our {@link ImageView}.
     */
    private void loadThumbnail() {
        Picasso.with(mHeaderImageView.getContext()).load(mItem.getThumbnailUrl()).into(mHeaderImageView,
                PicassoPalette.with(mItem.getThumbnailUrl(), mHeaderImageView)
                        .use(PicassoPalette.Profile.VIBRANT)
                        .intoCallBack(
                                new PicassoPalette.CallBack() {
                                    @Override
                                    public void onPaletteLoaded(Palette palette) {
                                        //specific task
                                        fab.setBackgroundTintList(ColorStateList.valueOf(palette.getVibrantColor(0)));
                                        toolbarFooter.setBackgroundTintList(ColorStateList.valueOf(palette.getVibrantColor(0)));
                                        if(palette.getVibrantColor(0)==0){
                                            toolbarFooter.setAlpha(1);
                                        }

                                    }
                                })
        );
    }

    /**
     * Load the item's full-size image into our {@link ImageView}.
     */
    private void loadFullSizeImage() {
        Picasso.with(mHeaderImageView.getContext())
                .load(mItem.getPhotoUrl())
                .noFade()
                .noPlaceholder()
                .into(getTarget(mItem.getId(),""));
    }

    /**
     * Try and add a {@link Transition.TransitionListener} to the entering shared element
     * {@link Transition}. We do this so that we can load the full-size image after the transition
     * has completed.
     *
     * @return true if we were successful in adding a listener to the enter transition
     */
    private boolean addTransitionListener() {
        final Transition transition = getWindow().getSharedElementEnterTransition();

        if (transition != null) {
            // There is an entering shared element transition so add a listener to it
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    // As the transition has ended, we can now load the full-size image
                    loadFullSizeImage();

                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionStart(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionResume(Transition transition) {
                    // No-op
                }
            });
            return true;
        }

        // If we reach here then we have not added a listener
        return false;
    }

    //target to save
    private Target getTarget(final int id , final String dirPath){
        Target target = new Target(){

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                mHeaderImageView.setImageBitmap(bitmap);
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        String pathGlobal = Environment.getExternalStorageDirectory() + File.separator + "TimeWall";

                        try{
                                String path= pathGlobal+ "/"+dirPath+id+".jpg";
                                File file = new File(path);
                                file.createNewFile();
                                FileOutputStream ostream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                                ostream.flush();
                                ostream.close();

                                //add Item Path to ITEMS
                                factory.setItemPath(mItem.getId(),path);

                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());

                            File dirPath = new File(pathGlobal);
                            if (!dirPath.exists())
                                dirPath.mkdirs();
                            dirPath = new File(pathGlobal+ "/Favorite");
                            if (!dirPath.exists())
                                dirPath.mkdirs();
                        }
                    }
                }).start();

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


}
