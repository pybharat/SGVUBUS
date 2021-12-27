package com.bharatapp.sgvuBus.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.TextView;

import com.bharatapp.sgvuBus.R;
import com.bharatapp.sgvuBus.fragments.add_notice;
import com.bharatapp.sgvuBus.fragments.update_profile;

public class user_update extends AppCompatActivity {
    int position,userid;
    TextView heading;

    String email2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);
        heading=findViewById(R.id.heading);
        Toolbar toolbar=(Toolbar)findViewById(R.id.actionbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadfragment(new update_profile());
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            position=bundle.getInt("position");

        }

        if(position==0)
        {
            loadfragment(new add_notice());
            heading.setText("UPDATE PROFILE");
        }
        else if (position==1)
        {
            //loadfragment(new change_password());
            heading.setText("CHANGE PASSWORD");
        }

    }



    private void loadfragment(Fragment fragment) {
        FragmentManager fm=getFragmentManager();
        FragmentTransaction fragmentTransaction=fm.beginTransaction();
        fragmentTransaction.replace(R.id.add,fragment);
        fragmentTransaction.commit();
    }
}