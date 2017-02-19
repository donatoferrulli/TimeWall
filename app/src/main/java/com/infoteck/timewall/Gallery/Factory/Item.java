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

package com.infoteck.timewall.Gallery.Factory;

import com.infoteck.timewall.Gallery.GalleryActivity;

import java.io.File;

/**
 * Represents an Item in our application. Each item has a name, id, full size image url and
 * thumbnail url.
 */
public class Item {

    private String mName;
    private String mAuthor;
    private String mThumbnailUrl;
    private String mPhotoUrl;
    private String mLocalPath;
    private int mWeatherCode;

    public Item (String name, String author, String ThumbnailUrl, String PhotoUrl) {
        mName = name;
        mAuthor = author;
        mThumbnailUrl = ThumbnailUrl;
        mPhotoUrl = PhotoUrl;
    }
    public Item (String name, String author, String ThumbnailUrl, String PhotoUrl,String LocalPath) {
        mName = name;
        mAuthor = author;
        mThumbnailUrl = ThumbnailUrl;
        mPhotoUrl = PhotoUrl;
        mLocalPath = LocalPath;
    }
    public Item (String name, String author, String ThumbnailUrl, String PhotoUrl,int WeatherCode) {
        mName = name;
        mAuthor = author;
        mThumbnailUrl = ThumbnailUrl;
        mPhotoUrl = PhotoUrl;
        mWeatherCode = WeatherCode;
    }
    public Item (String name, String author, String url) {
        mName = name;
        mAuthor = author;
        mThumbnailUrl = url;
        mPhotoUrl = url;
    }

    public int getId() {
        int hashCode = (mPhotoUrl.equals(""))? Integer.parseInt(mName): Math.abs(mPhotoUrl.hashCode());

        return hashCode;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getName() {
        return mName;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public int getWeatherCode() {
        return mWeatherCode;
    }

    public String getLocalFileImage(){return mLocalPath;}

    public void setLocalPath(String LocalPath){
        mLocalPath = LocalPath;
    }

}
