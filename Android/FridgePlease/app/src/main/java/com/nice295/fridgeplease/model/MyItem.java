package com.nice295.fridgeplease.model;

/**
 * Created by kyuholee on 2016. 9. 4..
 */

public class MyItem {

    public MyItem() {
    }

    public MyItem(String name, int time, String imageUrl) {
        this.name = name;
        this.time = time;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String name;
    public int time;
    public String imageUrl;

}