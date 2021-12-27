package com.bharatapp.sgvuBus.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.bharatapp.sgvuBus.R;
import com.bharatapp.sgvuBus.activities.dashboard;

public class mobileLogin extends Fragment {

View view;
Button button,dbutton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view=inflater.inflate(R.layout.fragment_mobile_login, container, false);
       button=(Button)view.findViewById(R.id.login2);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.otpverification, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        dbutton=dialogView.findViewById(R.id.login3);
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               alertDialog.show();
           }
       });
       dbutton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i=new Intent(getActivity().getApplicationContext(), dashboard.class);
               startActivity(i);
           }
       });
        return view;
    }
}