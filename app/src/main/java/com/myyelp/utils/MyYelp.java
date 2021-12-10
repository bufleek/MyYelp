package com.myyelp.utils;

import android.app.Application;

import com.myyelp.data.database.RoomDb;
import com.myyelp.data.repos.MainRepo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyYelp extends Application {
    public MainRepo mainRepo;
    public RoomDb db;
    private ExecutorService executor = Executors.newFixedThreadPool(10);

    @Override
    public void onCreate() {
        super.onCreate();
        db = RoomDb.getInstance(this);
        mainRepo = new MainRepo(executor, db);
    }
}
