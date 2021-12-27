package com.bharatapp.sgvuBus.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bharatapp.sgvuBus.R;
import com.bharatapp.sgvuBus.model_class.userinfo_data;
import com.bharatapp.sgvuBus.retrofit.RetrofitClient;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class profile extends AppCompatActivity {
    ImageView img;
    TextView name,contact1,email1;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    Button profile,password;
    int userid;
    public userinfo_data userinfo_data;
    SharedPreferences sharedPreferences;
    String u_name,contact,email,img_u;
    private static final String SHARED_PREF_NAME = "sgvu";
    private static final String KEY_USERID = "userid";
    private static final String KEY_TOKEN = "token";

    RetrofitClient retrofitClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        img=findViewById(R.id.img);
        contact1=findViewById(R.id.contact1);
        email1=findViewById(R.id.email1);
        name=findViewById(R.id.name);
        retrofitClient=new RetrofitClient();

        toolbar=(Toolbar)findViewById(R.id.actionbar1);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        Intent i2=new Intent(profile.this, dashboard.class);
                        i2.putExtra("poster",1);
                        startActivity(i2);
                        return true;
                    case R.id.profile:
                        Intent i1=new Intent(profile.this,profile.class);
                        startActivity(i1);
                        return true;
                    case R.id.lms:
                        Intent i=new Intent(profile.this, webview.class);
                        i.putExtra("url","https://mygyanvihar.com/2020");
                        startActivity(i);
                        return true;
                    default:return false;
                }

            }

        });
        profile=findViewById(R.id.uprofile);
        password=findViewById(R.id.changepassword);

profile.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i=new Intent(profile.this,update_profile.class);
        i.putExtra("name",u_name);
        i.putExtra("contact",contact);
        i.putExtra("email",email);
        i.putExtra("img_url",img_u);
        startActivity(i);
    }
});
password.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i=new Intent(profile.this,change_password.class);
        i.putExtra("base","profile");
        startActivity(i);
    }
});
        getuserinfo();
    }

    private void getuserinfo() {
        sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        int userid = sharedPreferences.getInt(KEY_USERID, 0);
        String token = sharedPreferences.getString(KEY_TOKEN, null);
        JsonObject auth = new JsonObject();
        userinfo_data = new userinfo_data();
        if (userid != 0 || token != null) {
            auth.addProperty("id", userid);
            auth.addProperty("token", token);
        } else {
            Toast.makeText(getApplicationContext(), "Login Again", Toast.LENGTH_SHORT).show();
        }
        JsonObject user = new JsonObject();
        user.addProperty("userid", userid);
        user.add("auth", auth);

       retrofitClient.getWebService().getuserinfo(user).enqueue(new Callback<String>() {
           @Override
           public void onResponse(Call<String> call, Response<String> response) {

               if (response.isSuccessful()) {
                   try {
                       JSONObject obj = new JSONObject(response.body());
                       if (Integer.parseInt(obj.get("code").toString()) == 200) {
                           JSONArray jsonArray=obj.getJSONArray("message");
                           JSONObject jsonObject=jsonArray.getJSONObject(0);
                           u_name=jsonObject.getString("user_firstname");
                           contact=jsonObject.getString("user_mobile");
                           email=jsonObject.getString("user_email");
                           img_u=jsonObject.getString("profile_image");
                           if(img_u.isEmpty())
                           {
                               img_u="https://www.seekho.live/bharat-sir/sgvuapi/assets/profile/default.jpeg";
                           }
                           else
                           {
                               img_u="https://www.seekho.live/bharat-sir/sgvuapi/assets/profile/"+img_u;
                           }
                           Glide.with(profile.this)
                                   .load(img_u)
                                   .centerCrop()
                                   .into(img);
                           name.setText(u_name);
                           contact1.setText(contact);
                           email1.setText(email);
                       } else if (Integer.parseInt(obj.get("code").toString()) == 400) {
                           Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                       }
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

               } else {
                   Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

               }
           }

           @Override
           public void onFailure(Call<String> call, Throwable t) {
               Log.d("bharat123",t.toString());
           }
       });
    }



}