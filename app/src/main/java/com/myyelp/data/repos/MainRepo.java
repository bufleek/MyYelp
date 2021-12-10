package com.myyelp.data.repos;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.myyelp.data.api.ApiEndPoints;
import com.myyelp.data.api.RetrofitService;
import com.myyelp.data.database.RoomDb;
import com.myyelp.data.models.Business;
import com.myyelp.data.models.SearchResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepo {
    private final double latitude = 37.786882;
    private final double longitude = -122.399972;
    private final ApiEndPoints apiService = RetrofitService.retrofit.create(ApiEndPoints.class);
    public MutableLiveData<Boolean> isSearchLoading = new MutableLiveData<>();
    public MutableLiveData<SearchResponse> searchResponse = new MutableLiveData<>();
    public MutableLiveData<String> searchError = new MutableLiveData<>();
    public MutableLiveData<List<Business>> favourites = new MutableLiveData<List<Business>>();
    private ExecutorService executor;
    private RoomDb db;

    public MainRepo(ExecutorService executor, RoomDb db) {
        this.executor = executor;
        this.db = db;
    }

    public void search(String term) {
        searchError.postValue(null);
        isSearchLoading.postValue(true);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                apiService.search(term, latitude, longitude).enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                        isSearchLoading.setValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            searchResponse.postValue(response.body());
                        } else {
                            searchError.postValue("Failed to load results, Something went wrong");
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        isSearchLoading.postValue(false);
                        searchError.postValue(t.getLocalizedMessage());
                    }
                });
            }
        });
    }

    public void addFavourite(Business business) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                business.json = new Gson().toJson(business);
                db.businessDao().insert(business);
                getFavourites();
            }
        });
    }

    public void getFavourites() {
        executor.execute(new Runnable() {

            @Override
            public void run() {
                List<Business> businesses = db.businessDao().getAll();
                ArrayList<Business> businessesFinal = new ArrayList<Business>();
                for(Business business: businesses){
                    businessesFinal.add(new Gson().fromJson(business.json, new TypeToken<Business>(){}.getType()));
                }
                favourites.postValue((List<Business>) businessesFinal);
            }
        });
    }
}
