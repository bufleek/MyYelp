package com.myyelp.data.models;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BusinessTypeConverter {
    @TypeConverter
    public String fromBusiness(Business business) {
        return new Gson().toJson(business);
    }

    @TypeConverter
    public Business toBusiness(String json) {
        return new Gson().fromJson(json, new TypeToken<Business>() {
        }.getType());
    }
}
