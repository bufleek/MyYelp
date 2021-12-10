package com.myyelp.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

@Entity
public class Business {
    @PrimaryKey(autoGenerate = true)
    public int uuid;
    @Ignore
    public String id;
    @Ignore
    public String name;
    @Ignore
    public String image_url;
    @Ignore
    public float rating;
    @Ignore
    public String phone;
    @Ignore
    public String price;
    @Ignore
    public BusinessLocation location;
    @Ignore
    public ArrayList<Category> categories;

    @ColumnInfo()
    public String json;
}

