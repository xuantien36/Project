package com.t3h.project.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.t3h.project.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder>{
    private List<News> data;
    private LayoutInflater inflater;
    private FaceItemListener listener;


    public void setOnItemClickListener(FaceItemListener itemClickListener) {
        this.listener = itemClickListener;
    }
    public List<News> getData() {
        return data;
    }

    public NewsAdapter(Context context){
        inflater= LayoutInflater.from(context);
    }
    public void setData(List<News> data){
            this.data=data;
            notifyDataSetChanged();
    }


    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_news,parent,false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, final int position) {
        News news=data.get(position);
        holder.binData(news);
        if(listener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   News n = (News) view.getTag();
                    listener.onClick(position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                   listener.onLongClick(position);
                   return true;

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return data==null? 0:data.size();
    }

    public class NewsHolder extends RecyclerView.ViewHolder{
        private ImageView imavt;
        private TextView txtTitle;
        private TextView txtDesc;
        private TextView txtPubDate;

        public NewsHolder(@NonNull View itemView) {
            super(itemView);
            imavt=itemView.findViewById(R.id.img_news);
            txtTitle=itemView.findViewById(R.id.txt_title);
            txtDesc=itemView.findViewById(R.id.txt_desc);
            txtPubDate=itemView.findViewById(R.id.txt_pubDate);
        }
        public void binData(News item){
            txtTitle.setText(item.getTitle());
            txtDesc.setText(item.getDesc());
            txtPubDate.setText(item.getDate());
            if (item.getImg() == null){
                Glide.with(imavt).load(R.drawable.ic_launcher_foreground).into(imavt);
            }
            else {
                Glide.with(imavt).load(item.getImg()).into(imavt);
            }
        }
    }
    public interface FaceItemListener{
        void onClick(int position);
        void onLongClick(int position);
    }


}
