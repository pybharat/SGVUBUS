package com.bharatapp.sgvuBus.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bharatapp.sgvuBus.R;
import com.bharatapp.sgvuBus.model_class.SliderData;
import com.bumptech.glide.Glide;

import java.util.List;


public class SliderAdapter extends PagerAdapter {

    private Activity activity;

    private final List<SliderData> mSliderItems;
    public SliderAdapter(Activity activity,List<SliderData> mSliderItems) {

        this.activity = activity;
        this.mSliderItems = mSliderItems;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = ((Activity) activity).getLayoutInflater();

        View viewItem = inflater.inflate(R.layout.slider, container, false);
        ImageView imageView = (ImageView) viewItem.findViewById(R.id.noticeimage);
        final SliderData sliderItem = mSliderItems.get(position);
        Glide.with(activity)
                .load(sliderItem.getImgUrl())
                .centerCrop()
                .into(imageView);
        ((ViewPager) container).addView(viewItem);

        return viewItem;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mSliderItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stub
        return view == ((View) object);
    }

    @Override
    public void destroyItem( ViewGroup container, int position, Object object) {

        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}