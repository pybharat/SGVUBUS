package com.bharatapp.sgvuBus.fragments;

import android.app.Fragment;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bharatapp.sgvuBus.R;
import com.bharatapp.sgvuBus.model_class.admin_url_data;
import com.bharatapp.sgvuBus.adapter.myadaptar_admin;

import java.util.ArrayList;
import java.util.List;

public class admin_link extends Fragment {

    View view;
    RecyclerView rcv;
    List<admin_url_data> list1s;
    int i;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_admin_link, container, false);
        list1s = new ArrayList<>();

        rcv = (RecyclerView)view.findViewById(R.id.rc2);
        rcv.setLayoutManager(new GridLayoutManager(getActivity(),2));

        admin_url_data l = new admin_url_data();
        l.setId("1");
        l.setTitle("ADD NOTICE");
        l.setAclass("add_notice");
        l.setImage_url("https://seekho.live/bharat-sir/slider/gyanviharnewlogo.png");
        list1s.add(l);
        admin_url_data l1 = new admin_url_data();
        l1.setId("2");
        l1.setTitle("UPDATE NOTICE");
        l1.setAclass("update_notice");
        l1.setImage_url("https://seekho.live/bharat-sir/slider/gyanviharnewlogo.png");
        list1s.add(l1);
        admin_url_data l2 = new admin_url_data();
        l2.setId("3");
        l2.setTitle("ADD POSTER");
        l2.setAclass("add_poster");
        l2.setImage_url("https://seekho.live/bharat-sir/slider/gyanviharnewlogo.png");
        list1s.add(l2);
        rcv.setAdapter(new myadaptar_admin(getActivity(), list1s));
        return view;
    }
}