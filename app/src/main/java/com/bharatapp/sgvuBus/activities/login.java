package com.bharatapp.sgvuBus.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bharatapp.sgvuBus.R;
import com.bharatapp.sgvuBus.fragments.emailLogin;
import com.bharatapp.sgvuBus.fragments.mobileLogin;
import com.bharatapp.sgvuBus.fragments.usersignup;

public class login extends AppCompatActivity {
    TextView mobile,signup,heading;
    FrameLayout frameLayout;
   int count=1,count2=1;
   SharedPreferences sharedPreferences;
    private  static  final String SHARED_PREF_NAME="sgvu";
    private  static  final String KEY_USERID="userid";
    private  static  final String KEY_TOKEN="token";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences=getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        int userid=sharedPreferences.getInt(KEY_USERID,0);
        if(userid!=0)
        {
            Intent i=new Intent(login.this,dashboard.class);
            startActivity(i);
        }
        mobile=(TextView) findViewById(R.id.mobile);
        heading=(TextView) findViewById(R.id.heading);
        signup=(TextView) findViewById(R.id.signup);
        frameLayout=(FrameLayout) findViewById(R.id.login1);
        loadfragment(new emailLogin());
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==1) {
                    loadfragment(new mobileLogin());
                    mobile.setText("Login by Password");
                    count=2;
                }
                else if(count==2)
                {
                    loadfragment(new emailLogin());
                    mobile.setText("Login by OTP");

                    count=1;
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count2==1) {
                    loadfragment(new usersignup());
                    heading.setText("SignUp");
                    signup.setText("Exits User!Login");
                    mobile.setText("");
                    count2=2;
                }
                else if(count2==2)
                {
                    loadfragment(new emailLogin());
                    heading.setText("Login");
                    signup.setText("New User!SignUp");
                    mobile.setText("Login by OTP");
                    count2=1;
                }
            }
        });


    }

    public void loadfragment(Fragment fragment) {
        FragmentManager fm=getFragmentManager();
        FragmentTransaction fragmentTransaction=fm.beginTransaction();
        fragmentTransaction.replace(R.id.login1,fragment);
        fragmentTransaction.commit();
    }
}