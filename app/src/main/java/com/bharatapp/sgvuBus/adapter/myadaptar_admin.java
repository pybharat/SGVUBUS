package com.bharatapp.sgvuBus.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bharatapp.sgvuBus.R;
import com.bharatapp.sgvuBus.activities.admin_panel;
import com.bharatapp.sgvuBus.model_class.admin_url_data;
import com.bumptech.glide.Glide;

import java.util.List;

public class myadaptar_admin extends RecyclerView.Adapter<myadaptar_admin.holder> {
    List<admin_url_data> list1List;
    Context ct;
    public myadaptar_admin(Context ct,List<admin_url_data> list1List)
    {
        this.list1List = list1List;
        this.ct=ct;
    }

    @NonNull
    @Override
    public myadaptar_admin.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.img_url,parent,false);
        return new myadaptar_admin.holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myadaptar_admin.holder holder, int position) {
        holder.notice_title.setText(list1List.get(position).getTitle());

        Glide.with(ct)
                .load(list1List.get(position).getImage_url())
                .fitCenter()
                .into(holder.img_url);
    }

    @Override
    public int getItemCount() {
        return list1List.size();
    }




    class holder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView notice_title;
        ImageView img_url;
        public holder(@NonNull View itemView) {
            super(itemView);

            notice_title=(TextView)itemView.findViewById(R.id.notice_til);

            img_url=(ImageView)itemView.findViewById(R.id.img_url);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            String nid,title,aclass;
            nid=list1List.get(position).getId();
            title=list1List.get(position).getTitle();
            aclass=list1List.get(position).getAclass();
            Intent intent=new Intent(ct, admin_panel.class);
            intent.putExtra("position",position);
                intent.putExtra("id",nid);
                intent.putExtra("title",title);
                intent.putExtra("class",aclass);
                ct.startActivity(intent);
        }
    }
}
