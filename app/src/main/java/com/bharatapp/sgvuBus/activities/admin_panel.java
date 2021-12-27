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
import com.bharatapp.sgvuBus.fragments.add_poster;
import com.bharatapp.sgvuBus.fragments.updates;

public class admin_panel extends AppCompatActivity {
    String nid,ntitle,aclass;
    int position;
    TextView heading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        heading=findViewById(R.id.heading);
        Toolbar toolbar=(Toolbar)findViewById(R.id.actionbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadfragment(new add_notice());
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            nid = bundle.getString("id");
            ntitle = bundle.getString("title");
            aclass = bundle.getString("class");
            position=bundle.getInt("position");
        }

        if(position==0)
        {
            loadfragment(new add_notice());
            heading.setText("ADD NOTICE");
        }
        else if (position==1)
        {
            updates update=new updates();
            Bundle bundle1=new Bundle();
            bundle1.putString("admin","admin");
            update.setArguments(bundle1);
            loadfragment(update);
            heading.setText("UPDATE NOTICE");
        }
        else if (position==2)
        {
            loadfragment(new add_poster());
            heading.setText("ADD POSTER");
        }
    }
    private void loadfragment(Fragment fragment) {
        FragmentManager fm=getFragmentManager();
        FragmentTransaction fragmentTransaction=fm.beginTransaction();
        fragmentTransaction.replace(R.id.add,fragment);
        fragmentTransaction.commit();
    }
}