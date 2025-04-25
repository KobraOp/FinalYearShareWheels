package com.android.sharewheelsnewui.apicallers;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherClient {

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static QueryCaller queryCaller;

    public static QueryCaller getInstance() {
        if(queryCaller == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            queryCaller = retrofit.create(QueryCaller.class);
        }
        return queryCaller;
    }
}
