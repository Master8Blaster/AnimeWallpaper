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
import com.nuturaldot.animewallpaper.ImageViewActivity;
import com.nuturaldot.animewallpaper.R;
import com.nuturaldot.animewallpaper.Utils;
import com.nuturaldot.animewallpaper.model.ImagelistModel;

import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.Myview> {

    List<ImagelistModel> list;
    Activity activity;

    public ImageListAdapter(List<ImagelistModel> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public ImageListAdapter.Myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageListAdapter.Myview(LayoutInflater.from(activity).inflate(R.layout.image_list_design, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Myview holder, int position) {

        Glide
                .with(activity)
                .load(list.get(position).getUrl())
                .centerCrop()
                .into(holder.image_list);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utils.list = list;
                Intent intent = new Intent(activity, ImageViewActivity.class);
                intent.putExtra("position", holder.getAdapterPosition());
                activity.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Myview extends RecyclerView.ViewHolder{

        ImageView image_list;

        public Myview(@NonNull View itemView) {
            super(itemView);
            image_list = itemView.findViewById(R.id.img_list);

        }
    }
}

