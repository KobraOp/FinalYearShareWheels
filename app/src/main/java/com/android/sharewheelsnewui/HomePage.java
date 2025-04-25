package com.android.sharewheelsnewui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.sharewheelsnewui.apicallers.QueryCaller;
import com.android.sharewheelsnewui.apicallers.WeatherClient;
import com.android.sharewheelsnewui.apicallers.WeatherData;
import com.android.sharewheelsnewui.permissioncaller.PermissionCaller;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePage extends AppCompatActivity implements PermissionCaller.PermissionResultCallback {

    LinearLayout l1,l2,l3,l4;
    private TextView t1,t2,t3;
    private ImageView i1,i2;
    private static final int REQUEST_CODE = 2001;
    private Intent intent;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        String[] permission = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        PermissionCaller.requestPermissions(this, permission, REQUEST_CODE);
        intent = getIntent();

        int bannerDuration = 2000;
        int profileDuration = 3000;
        int descDuration = 4000;

        l1 = findViewById(R.id.bannerLook);
        l2 = findViewById(R.id.profileLook);
        l3 = findViewById(R.id.weatherLook);
        l4 = findViewById(R.id.descLook);
        t1 = findViewById(R.id.name);
        t2 = findViewById(R.id.temp);
        i1 = findViewById(R.id.profilePhoto);
        i2 = findViewById(R.id.weatherPhoto);
        t3 = findViewById(R.id.desc);

        displayProfile();
        new Handler().postDelayed(() -> animateAndSwitchView(l1,l2), bannerDuration);
        new Handler().postDelayed(() -> animateAndSwitchView(l2,l3), bannerDuration+profileDuration);
        new Handler().postDelayed(() -> animateAndSwitchView(l3,l4), bannerDuration+profileDuration+descDuration);
        new Handler().postDelayed(() -> animateAndSwitchView(l4,l3), bannerDuration + profileDuration + descDuration * 2);

    }

    private void animateAndSwitchView(LinearLayout fromView, LinearLayout toView) {
        // Fade out the current view
        if (fromView.getVisibility() == View.VISIBLE) {
            fromView.animate()
                    .translationY(50)
                    .alpha(0f)
                    .setDuration(400)
                    .withEndAction(() -> {
                        fromView.setVisibility(View.GONE);
                        fromView.setAlpha(1f); // reset for reuse
                        fromView.setTranslationY(0);

                        // Fade in the next view
                        toView.setAlpha(0f);
                        toView.setTranslationY(-50);
                        toView.setVisibility(View.VISIBLE);
                        toView.animate()
                                .translationY(0)
                                .alpha(1f)
                                .setDuration(500)
                                .setInterpolator(new android.view.animation.DecelerateInterpolator())
                                .start();
                    }).start();
        }
    }


    private void displayProfile(){
        String name = intent.getStringExtra("name");
        String image = intent.getStringExtra("photo");

        t1.setText(name);
        Glide.with(this)
                .load(image)
                .circleCrop()
                .into(i1);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionCaller.handlePermissionsResult(this, requestCode, REQUEST_CODE, permissions, grantResults);
    }

    @Override
    public void onPermissionGranted() {
        getCurrentLocation();
    }

    @Override
    public void onPermissionDenied(List<String> deniedPermissions) {

    }

    private void getCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if(location != null){
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        getWeatherData(latitude, longitude);
                    } else{
                        Toast.makeText(this, "Error in fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getWeatherData(double lat, double lan){

        QueryCaller caller = WeatherClient.getInstance();
        Call<WeatherData> call = caller.getWeatherByCords(
                lat,lan,
                BuildConfig.WEATHER_API,
                "metric"
        );
        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                if(response.isSuccessful() && response.body() != null){
                    WeatherData weather = response.body();

                    String iconCode = weather.weather.get(0).icon;
                    String desc = weather.weather.get(0).description;
                    String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

                    Glide.with(HomePage.this)
                            .load(iconUrl)
                            .circleCrop()
                            .into(i2);

                    t2.setText(weather.main.temp+"°C");
                    String capitalizedDesc = desc.substring(0, 1).toUpperCase() + desc.substring(1);
                    t3.setText(capitalizedDesc);
                }
                else {
                    Toast.makeText(HomePage.this, "Fetch Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Toast.makeText(HomePage.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}