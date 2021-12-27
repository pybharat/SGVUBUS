package com.bharatapp.sgvuBus.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bharatapp.sgvuBus.R;
import com.bumptech.glide.Glide;

public class detail_notice extends AppCompatActivity {
    String nid,ntitle,nfull_des,img_url,date2;
    TextView title,date1,full_des;
    ImageView imageView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_notice);
       title=(TextView)findViewById(R.id.notice_title);
       date1=(TextView)findViewById(R.id.date2);
       full_des=(TextView)findViewById(R.id.full_des);
       imageView=(ImageView)findViewById(R.id.noticeimage);
       toolbar=(Toolbar)findViewById(R.id.actionbar1);
       setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            nid = bundle.getString("nid");
            ntitle = bundle.getString("ntitle");
            nfull_des = bundle.getString("nfull_des");
            img_url = bundle.getString("img_url");
            date2 = bundle.getString("date1");
        }
        title.setText(ntitle);
        date1.setText(date2);
        full_des.setText(nfull_des);

            Glide.with(this)
                    .load(img_url)
                    .centerCrop()
                    .into(imageView);

    }
}