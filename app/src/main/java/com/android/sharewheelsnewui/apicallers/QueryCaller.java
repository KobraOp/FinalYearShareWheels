package com.android.sharewheelsnewui.apicallers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QueryCaller {

    @GET("weather")
    Call<WeatherData> getWeatherByCords(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("appid") String apiKey,
            @Query("units") String units
    );
}
