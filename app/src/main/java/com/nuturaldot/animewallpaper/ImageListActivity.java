package com.nuturaldot.animewallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nuturaldot.animewallpaper.Adapter.ImageListAdapter;
import com.nuturaldot.animewallpaper.admob.AdUtils;
import com.nuturaldot.animewallpaper.model.ImagelistModel;

import java.util.ArrayList;
import java.util.List;

public class ImageListActivity extends AppCompatActivity {

    RecyclerView rv;
    List<ImagelistModel> dataList;
    LinearLayout nodata, progresslayout;
    TextView cat_name;
    Dialog dialog;
    CardView cdYes, cdNo;


    private FrameLayout adContainerView;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        AdUtils.ShowFull(ImageListActivity.this);

        adContainerView = findViewById(R.id.ad_view_container);
        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
            }
        });

        rv = findViewById(R.id.recyler_image_list);
        nodata = findViewById(R.id.noData);
        progresslayout = findViewById(R.id.progressbar);
        cat_name = findViewById(R.id.cat_name);
        Intent intent = getIntent();
        cat_name.setText(intent.getStringExtra("name"));
        rv.setLayoutManager(new GridLayoutManager(this, 2));

        getData();
    }

    void getData() {
        progresslayout.setVisibility(View.VISIBLE);
        nodata.setVisibility(View.GONE);
        rv.setVisibility(View.GONE);

        FirebaseDatabase.getInstance().getReference("categories").child(getIntent().getStringExtra("key")).child("images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    dataList = new ArrayList<>();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        dataList.add(new ImagelistModel(snap.child("key").getValue(String.class), snap.child("url").getValue(String.class)));
                    }

                    if (dataList != null && !dataList.isEmpty()) {
                        rv.setHasFixedSize(true);
                        rv.setAdapter(new ImageListAdapter(dataList, ImageListActivity.this));

                        //set Visibilities
                        progresslayout.setVisibility(View.GONE);
                        nodata.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);

                    }else{
                        progresslayout.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);
                    }
                } else {
                    progresslayout.setVisibility(View.GONE);
                    nodata.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ImageListActivity.this, "Ooh!!Something went wrong.", Toast.LENGTH_SHORT).show();
                Log.d("Master", "onCancelled: " + error.getMessage());
                progresslayout.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
            }
        });

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


    public void ic_back(View view) {
        onBackPressed();
    }


}