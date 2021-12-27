package com.bharatapp.sgvuBus.fragments;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bharatapp.sgvuBus.R;
import com.bharatapp.sgvuBus.model_class.links_url;
import com.bharatapp.sgvuBus.adapter.myadapeter_url;

import java.util.ArrayList;
import java.util.List;


public class important_link extends Fragment {
View view;
    RecyclerView rcv;
    List<links_url> list1s;
    int i;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_important_link, container, false);
        list1s = new ArrayList<>();

        rcv = (RecyclerView)view.findViewById(R.id.rc2);
        rcv.setLayoutManager(new GridLayoutManager(getActivity(),2));

            links_url l = new links_url();
            l.setId("1");
            l.setUrl_title("Gyan Vihar");
            l.setUrl("https://www.gyanvihar.org");
            l.setImage_url("https://seekho.live/bharat-sir/slider/gyanviharnewlogo.png");
            list1s.add(l);

        links_url l2 = new links_url();
        l2.setId("3");
        l2.setUrl_title("Gmail");
        l2.setUrl("https://mygyanvihar.com");
        l2.setImage_url("https://seekho.live/bharat-sir/slider/gyanviharnewlogo.png");
        list1s.add(l2);
        links_url l3 = new links_url();
        l3.setId("4");
        l3.setUrl_title("Seekho_Live");
        l3.setUrl("https://seekho.live");
        l3.setImage_url("https://seekho.live/bharat-sir/slider/seekho.PNG");
        list1s.add(l3);
        links_url l4 = new links_url();
        l4.setId("5");
        l4.setUrl_title("Results");
        l4.setUrl("http://results.gyanvihar.org/");
        l4.setImage_url("https://seekho.live/bharat-sir/slider/gyanviharnewlogo.png");
        list1s.add(l4);
        rcv.setAdapter(new myadapeter_url(getActivity(), list1s));
        return view;
    }
}