package com.bharatapp.sgvuBus.adapter;

        import android.content.Context;
        import android.content.Intent;
        import android.net.Uri;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.browser.customtabs.CustomTabsIntent;
        import androidx.recyclerview.widget.RecyclerView;


        import com.bharatapp.sgvuBus.R;
        import com.bharatapp.sgvuBus.model_class.links_url;
        import com.bharatapp.sgvuBus.activities.webview;
        import com.bumptech.glide.Glide;

        import java.util.List;

public class myadapeter_url extends RecyclerView.Adapter<myadapeter_url.holder>
{
    List<links_url> list1List;
    Context ct;
    public myadapeter_url(Context ct,List<links_url> list1List)
    {
        this.list1List = list1List;
        this.ct=ct;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.img_url,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        holder.notice_title.setText(list1List.get(position).getUrl_title());

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
            String nid,title,url,img_url;
            nid=list1List.get(position).getId();
            title=list1List.get(position).getUrl_title();
            url=list1List.get(position).getUrl();
            img_url=list1List.get(position).getImage_url();
if(url=="https://mygyanvihar.com" || url=="https://seekho.live") {
    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
    CustomTabsIntent customTabsIntent = builder.build();
    customTabsIntent.launchUrl(ct, Uri.parse(url));
}
else {
          Intent intent=new Intent(ct, webview.class);
            intent.putExtra("id",nid);
            intent.putExtra("title",title);
            intent.putExtra("url",url);
            intent.putExtra("img_url",img_url);
            ct.startActivity(intent);
}

        }
    }
}
