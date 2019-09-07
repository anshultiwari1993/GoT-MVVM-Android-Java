package com.anshultiwari.androidassignment.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Celebrity {
    @PrimaryKey
    @NonNull
    private String name;

    private String height;
    private String age;
    private String popularity;
    private String imageUrl;

    public Celebrity(String name, String height, String age, String popularity, String imageUrl) {
        this.name = name;
        this.height = height;
        this.age = age;
        this.popularity = popularity;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getHeight() {
        return height;
    }

    public String getAge() {
        return age;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
