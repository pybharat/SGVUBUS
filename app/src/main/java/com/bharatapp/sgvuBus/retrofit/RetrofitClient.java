package com.bharatapp.sgvuBus.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static String BASE_URL="https://seekho.live/bharat-sir/sgvubusapi/";
    private static RetrofitClient retrofitClient;
    private static Retrofit retrofit;
    private OkHttpClient.Builder builder=new OkHttpClient.Builder()
            .readTimeout(60L, TimeUnit.SECONDS)
            .connectTimeout(60L, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true);

    public Api getWebService()
    {
        Gson gson=new GsonBuilder()
                .setLenient()
                .create();
        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(builder.build())
                .build();
        return retrofit.create(Api.class);
    }
}
