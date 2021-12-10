package com.myyelp.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.myyelp.data.models.Business;
import com.myyelp.data.models.BusinessTypeConverter;

@Database(entities = {Business.class}, version = 1)
@TypeConverters(BusinessTypeConverter.class)
public abstract class RoomDb extends RoomDatabase {
    public static RoomDb getInstance(Context context) {
        RoomDb roomDb = Room.databaseBuilder(context, RoomDb.class, "myyelp").build();
        return roomDb;
    }

    public abstract BusinessDao businessDao();
}
