package com.bharatapp.sgvuBus;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.annotation.NonNull;

public class process extends Dialog {
ImageView img;
int i=1;
    public process(@NonNull Context context) {
        super(context);
        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.gravity= Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(params);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view= LayoutInflater.from(context).inflate(R.layout.process_bar,null);
        img=view.findViewById(R.id.pro);
       /* Animation animZoomOut = AnimationUtils.loadAnimation(context, R.anim.zoom_out);
        img.startAnimation(animZoomOut);*/
        setContentView(view);
    }
}
