package com.myyelp.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.myyelp.data.models.Business;

import java.util.List;

@Dao
public interface BusinessDao {
    @Query("Select * FROM Business")
    List<Business> getAll();

    @Insert
    void insert(Business business);
}
