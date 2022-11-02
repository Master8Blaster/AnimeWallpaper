package com.nuturaldot.animewallpaper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.nuturaldot.animewallpaper.Adapter.ViewPagerAdapter;
import com.nuturaldot.animewallpaper.admob.AdUtils;

import java.io.File;

public class ImageViewActivity extends AppCompatActivity {

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    LinearLayout btn_download;
    boolean b = false;
    int position;
    Window window;


    private FrameLayout adContainerView;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_FullView);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);


        adContainerView = findViewById(R.id.ad_view_container);
        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
            }
        });

        AdUtils.ShowFull(ImageViewActivity.this);

        btn_download = findViewById(R.id.btn_download);

        viewPager = findViewById(R.id.viewPagerMain);
        position = getIntent().getIntExtra("position", 0);
        viewPagerAdapter = new ViewPagerAdapter(this, Utils.list);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(position);
        viewPagerAdapter.notifyDataSetChanged();

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int pos) {
                position = pos;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadWallpaper();
            }
        });


    }


    void downloadWallpaper() {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/Download/" + getResources().getString(R.string.app_name));
            if (!file.exists()) {
                file.mkdirs();
                Toast.makeText(ImageViewActivity.this, "" + file.exists(), Toast.LENGTH_SHORT).show();

            }
            file = new File(file + "/" + getResources().getString(R.string.app_name) + "_" + Utils.list.get(position).getKey() + ".png");
            DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(Utils.list.get(position).getUrl());//Uri.fromFile(file);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(getResources().getString(R.string.app_name) + "_" + Utils.list.get(position).getKey());
            request.setDescription("Downloading");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, getResources().getString(R.string.app_name) + "/" + file.getName());
            downloadmanager.enqueue(request);

            Toast.makeText(this, "Downloaded started successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception c) {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }

    }


    private void loadBanner() {
        // Create an ad request.

        adView = new AdView(this);
        adView.setAdUnitId(AdUtils.BANNER_AD_UNIT_ID);
        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);


    }

    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }


}