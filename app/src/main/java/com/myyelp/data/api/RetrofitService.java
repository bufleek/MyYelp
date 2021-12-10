package com.myyelp.data.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    public static final String BASE_URL = "https://api.yelp.com/";
    public final static String TOKEN = "dY2DyqOsxepJPO0sDI91vfp2bNmrMqNpx53IL7IS2NxTO9CD71pQim5tG47Qzzzf4VP2oAjn1kdiXuKKysHqnWqmLPUNreYZUhNqJGOk5ApGHGwfwXI-Bx-eKMKsYXYx";

    private static OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + TOKEN)
                    .build();
            return chain.proceed(newRequest);
        }
    }).build();

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
}
