package com.bharatapp.sgvuBus.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bharatapp.sgvuBus.R;
import com.bharatapp.sgvuBus.forground.ExampleService;
import com.bharatapp.sgvuBus.retrofit.RetrofitClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class driver extends AppCompatActivity {
Button share,send,stopshare;
EditText msg;
TextView loaction1,locaTitle;
SharedPreferences sharedPreferences;
Boolean stops=true;
private static final String SHARED_PREF_NAME = "sgvu";
private static final String KEY_USERID = "userid";
private static final String KEY_TOKEN = "token";
FusedLocationProviderClient fusedLocationProviderClient;
RetrofitClient retrofitClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        share=findViewById(R.id.share);
        stopshare=findViewById(R.id.stopshare);
        send=findViewById(R.id.send);
        msg=findViewById(R.id.msg);
        loaction1=findViewById(R.id.locationView);
        locaTitle=findViewById(R.id.locationtitle);
        retrofitClient=new RetrofitClient();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                driver.this
        );

        View rootView = getWindow().getDecorView().getRootView();


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (ActivityCompat.checkSelfPermission(driver.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(driver.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }else
                {
                    ActivityCompat.requestPermissions(driver.this
                            ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                                    ,Manifest.permission.ACCESS_COARSE_LOCATION}
                            ,100);
                }

*/              startService(rootView);
                stops=true;
                refresh(1000);
                stopshare.setVisibility(View.VISIBLE);
                share.setVisibility(View.GONE);
                locaTitle.setText("Current Location");
            }

        });
        stopshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stops=false;
                stopshare.setVisibility(View.GONE);
                share.setVisibility(View.VISIBLE);
                locaTitle.setText("Last Location");
            }
        });
    }

    public void startService(View v) {
        Intent serviceIntent = new Intent(this, ExampleService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService(View v) {
        Intent serviceIntent = new Intent(this, ExampleService.class);
        stopService(serviceIntent);
    }
    private void content() {
        if (ActivityCompat.checkSelfPermission(driver.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(driver.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        }else
        {
            ActivityCompat.requestPermissions(driver.this
                    ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                            ,Manifest.permission.ACCESS_COARSE_LOCATION}
                    ,100);
        }
        // Toast.makeText(MainActivity.this,"refreshed",Toast.LENGTH_SHORT).show();

        if(stops==true)
        refresh(1000);

    }

    private void refresh(int i) {
        final Handler handler=new Handler();
        final Runnable runnable=new Runnable() {
            @Override
            public void run() {
                if(stops==true)
                content();
                else {
                    handler.removeCallbacks(this::run);
                    Log.d("bharat","stopped");
                }
            }
        };
        if(stops==true)
        handler.postDelayed(runnable,i);

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1]
                == PackageManager.PERMISSION_GRANTED)) {
            getCurrentLocation();
        } else {
            Toast.makeText(getApplicationContext(), "permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationManager locationManager=(LocationManager) getSystemService(
                Context.LOCATION_SERVICE
        );
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location=task.getResult();
                    if(location!=null)
                    {

                        try {
                            Geocoder geocoder=new Geocoder(driver.this, Locale.getDefault());
                            List<Address>addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                            String address=addresses.get(0).getAddressLine(0);
                            loaction1.setText(address);
                            upload(address);
                        }catch (Exception e)
                        {

                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        LocationRequest locationRequest=new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback=new LocationCallback()
                        {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location1=locationResult.getLastLocation();

                                try {
                                    Geocoder geocoder=new Geocoder(driver.this, Locale.getDefault());
                                    List<Address>addresses=geocoder.getFromLocation(location1.getLatitude(),location1.getLongitude(),1);
                                    String address=addresses.get(0).getAddressLine(0);
                                    loaction1.setText(address);
                                    upload(address);
                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
                    }
                }
            });
        }else
        {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void upload(String address) {
   /*     sharedPreferences= getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        int userid=sharedPreferences.getInt(KEY_USERID,0);
        String token=sharedPreferences.getString(KEY_TOKEN,null);
        JsonObject auth=new JsonObject();

        if(userid != 0 || token!=null)
        {
            auth.addProperty("id","2");
            auth.addProperty("token","7znjjx319q6wMGHh7VJe654jceLPEe");
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Login Again", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(getApplicationContext(), login.class);
            startActivity(i);
        }*/
        JsonObject auth=new JsonObject();
        auth.addProperty("id","2");
        auth.addProperty("token","7znjjx319q6wMGHh7VJe654jceLPEe");
        JsonObject locationdata=new JsonObject();
        locationdata.addProperty("userid","2");
        if(address!=null)
            locationdata.addProperty("location",address);
        locationdata.add("auth",auth);
        Log.d("bharat",locationdata.toString());
        retrofitClient.getWebService().insertlocation(locationdata).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {

                    try {
                        JSONObject obj = new JSONObject(response.body());
                        if(Integer.parseInt(obj.get("code").toString())==200)
                        {

                            Log.d("bharat","updated");

                        }
                        else if(Integer.parseInt(obj.get("code").toString())==400) {
                            Toast.makeText(getApplicationContext(),obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("bharat",t.toString());

            }
        });

    }



}