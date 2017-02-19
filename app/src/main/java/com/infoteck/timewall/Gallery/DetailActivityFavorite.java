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

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.florent37.picassopalette.PicassoPalette;
import com.infoteck.timewall.Gallery.Factory.AbstractItemFactory;
import com.infoteck.timewall.Gallery.Factory.Item;
import com.infoteck.timewall.R;
import com.infoteck.timewall.Utilities.changeWallpaper;
import com.konifar.fab_transformation.FabTransformation;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Our secondary Activity which is launched from {@link GalleryActivity}. Has a simple detail UI
 * which has a large banner image, title and body text.
 */
public class DetailActivityFavorite extends AppCompatActivity {

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
    private ImageView setWallpaperImageView;
    private ImageView deleteImageView;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase)); //wrap icon material for fab
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_favorite);
        // Retrieve the correct Item instance, using the ID provided in the Intent
        String path=Environment.getExternalStorageDirectory() + File.separator + "TimeWall"+ File.separator + "Favorite";
        mItem = new Item(String.valueOf(getIntent().getIntExtra(EXTRA_PARAM_ID,0)), "","" ,"",path+File.separator+String.valueOf(getIntent().getIntExtra(EXTRA_PARAM_ID,0))+".jpg");
        //get components view FOOTER
        fab = (FloatingActionButton) findViewById(R.id.fab);
        toolbarFooter = (Toolbar) findViewById(R.id.toolbar_footer);
        setWallpaperImageView = (ImageView) findViewById(R.id.setWallpaperImageView);
        deleteImageView = (ImageView) findViewById(R.id.deleteImageView);

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
        setWallpaperImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FabTransformation.with(fab)
                        .transformFrom(toolbarFooter);
                new changeWallpaper(getApplicationContext()).execute(mItem.getLocalFileImage());
                Toast.makeText(view.getContext(),R.string.setWallpaperFavorite,Toast.LENGTH_SHORT).show();
            }
        });
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FabTransformation.with(fab)
                        .transformFrom(toolbarFooter);
                File file = new File(mItem.getLocalFileImage());
                boolean deleted = file.delete();
                onBackPressed();
                Toast.makeText(view.getContext(),R.string.deleteImageFavorite,Toast.LENGTH_SHORT).show();

            }
        });



        ///////////
        loadItem();
    }

    private void loadItem() {
        // Set the title TextView to the item's name and author
        //mHeaderTitle.setText(getString(R.string.image_header, mItem.getName(), mItem.getAuthor()));
        File file = new File(mItem.getLocalFileImage());
        Picasso.with(mHeaderImageView.getContext())
                .load(file)
                .noFade()
                .noPlaceholder()
                .into(mHeaderImageView);
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
                    //loadFullSizeImage();

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



}
