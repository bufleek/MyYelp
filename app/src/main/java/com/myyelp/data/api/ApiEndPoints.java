package com.myyelp.data.api;

import com.myyelp.data.models.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiEndPoints {

    @GET("v3/businesses/search")
    public Call<SearchResponse> search(
            @Query("term") String term,
            @Query("latitude") double latitude,
            @Query("longitude") double longitude
    );
}
