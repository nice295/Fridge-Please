package com.nice295.fridgeplease.model;


/**
 * Created by kyuholee on 2016. 9. 4..
 */
public class Food {

    public Food() {
    }

    public Food(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String name;
    public String imageUrl;
}