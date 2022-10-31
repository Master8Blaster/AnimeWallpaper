package com.nuturaldot.animewallpaper.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.nuturaldot.animewallpaper.R;
import com.nuturaldot.animewallpaper.model.ImagelistModel;

import java.util.List;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    List<ImagelistModel>list;

    public ViewPagerAdapter(Context context, List<ImagelistModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.full_image_view, null);

        ImageView imageView=view.findViewById(R.id.full_imageview);

        Glide.with(context)
                .load(list.get(position).getUrl())
                .into(imageView);

        Objects.requireNonNull(container).addView(view);
       /* ViewPager vp= (ViewPager) container;
        vp.addView(view,0);*/
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp= (ViewPager) container;
        View view= (View) object;
        vp.removeView(view);
    }
}
