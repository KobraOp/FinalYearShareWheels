package com.android.sharewheelsnewui.apicallers;

import android.os.ParcelUuid;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherData {

    @SerializedName("weather")
    public List<Weather> weather;

    @SerializedName("main")
    public Main main;

    public static class Weather{
        @SerializedName("main")
        public String main;
        @SerializedName("description")
        public String description;
        @SerializedName("icon")
        public String icon;
    }

    public static class Main{
        @SerializedName("temp")
        public double temp;

        @SerializedName("feels_like")
        public double feel_like;

        @SerializedName("humidity")
        public int humidity;
    }

}
