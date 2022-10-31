package com.nuturaldot.animewallpaper.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nuturaldot.animewallpaper.ImageListActivity;
import com.nuturaldot.animewallpaper.R;
import com.nuturaldot.animewallpaper.model.Model;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.Myview> {

    List<Model>list;
    Activity activity;

    public MyAdapter(List<Model> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Myview(LayoutInflater.from(activity).inflate(R.layout.category_design, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Myview holder, int position) {

        Glide
                .with(activity)
                .load(list.get(position).getUrl())
                .centerCrop()
                .into(holder.cat_img);
        holder.cat_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ImageListActivity.class);
                intent.putExtra("key", list.get(position).getKey());
                intent.putExtra("name", "T20 Highlights");
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Myview extends RecyclerView.ViewHolder{

        ImageView cat_img;

        public Myview(@NonNull View itemView) {
            super(itemView);
            cat_img = itemView.findViewById(R.id.cat_img);
        }
    }
}
