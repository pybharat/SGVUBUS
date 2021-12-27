package com.bharatapp.sgvuBus.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bharatapp.sgvuBus.R;
import com.bharatapp.sgvuBus.activities.Update_notice;
import com.bharatapp.sgvuBus.activities.detail_notice;
import com.bharatapp.sgvuBus.model_class.notice_data;

import java.util.List;

public class myadaptar extends RecyclerView.Adapter<myadaptar.holder>
{
    List<notice_data> list1List;
    Context ct;
    String s1;
    public myadaptar(Context ct,List<notice_data> list1List,String s1)
    {
        this.list1List = list1List;
        this.ct=ct;
        this.s1=s1;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.update_item,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        holder.notice_title.setText(list1List.get(position).getNtitle());
        holder.short_des.setText(list1List.get(position).getNshort_des());
        holder.date1.setText(list1List.get(position).getDate1());

    }

    @Override
    public int getItemCount() {
        return list1List.size();
    }




    class holder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView notice_title,short_des,date1;
        public holder(@NonNull View itemView) {
            super(itemView);

            notice_title=(TextView)itemView.findViewById(R.id.notice_til);
            short_des=(TextView)itemView.findViewById(R.id.short_des);
            date1=(TextView)itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            String nid,ntitle,nfull_des,img_url,date1,nshort_des;
            nid=list1List.get(position).getNid();
            nshort_des=list1List.get(position).getNshort_des();
            ntitle=list1List.get(position).getNtitle();
            nfull_des=list1List.get(position).getNfull_des();
            img_url=list1List.get(position).getImg_url();
            date1=list1List.get(position).getDate1();
            if(s1=="normal") {
                Intent intent = new Intent(ct, detail_notice.class);
                intent.putExtra("nid", nid);
                intent.putExtra("ntitle", ntitle);
                intent.putExtra("nshort_des", nshort_des);
                intent.putExtra("nfull_des", nfull_des);
                intent.putExtra("img_url", img_url);
                intent.putExtra("date1", date1);
                ct.startActivity(intent);
            }
            else if(s1=="admin")
            {
                Intent intent = new Intent(ct, Update_notice.class);
                intent.putExtra("nid", nid);
                intent.putExtra("ntitle", ntitle);
                intent.putExtra("nshort_des", nshort_des);
                intent.putExtra("nfull_des", nfull_des);
                intent.putExtra("img_url", img_url);
                intent.putExtra("date1", date1);
                ct.startActivity(intent);
            }
        }
    }
}
