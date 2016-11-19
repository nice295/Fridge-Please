package com.nice295.fridgeplease.model;

/**
 * Created by kyuholee on 2016. 9. 4..
 */
public class Item {

    public Item() {
    }

    public Item(String mName, String mImageUrl) {
        this.name = mName;
        this.imageUrl = mImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String mName) {
        this.name = mName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.imageUrl = mImageUrl;
    }

    public String name;
    public String imageUrl;
}