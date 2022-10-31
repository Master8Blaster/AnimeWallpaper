package com.nuturaldot.animewallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nuturaldot.animewallpaper.admob.AdModel;
import com.nuturaldot.animewallpaper.admob.AdUtils;

public class SplashActivity extends AppCompatActivity {

    Handler handler = new Handler();
    public static BottomSheetDialog nointernet;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isNetworkConnected()) {
                nointernet.dismiss();
                go();
                handler.removeCallbacks(runnable);
            } else {
                handler.postDelayed(runnable, 100);
            }
        }
    };


    static AppOpenAd appOpenAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 300);
            } else {
                if (isNetworkConnected()) {
                    loadAdIds();
                } else {
                    nointernet = new BottomSheetDialog(SplashActivity.this);
                    nointernet.setContentView(R.layout.no_internet);
                    nointernet.setCancelable(false);
                    nointernet.show();
                    handler.postDelayed(runnable, 100);
                }
            }
        }
        else {
            if (isNetworkConnected()) {
                loadAdIds();
            } else {
                nointernet = new BottomSheetDialog(SplashActivity.this);
                nointernet.setContentView(R.layout.no_internet);
                nointernet.setCancelable(false);
                nointernet.show();
                handler.postDelayed(runnable, 100);
            }
        }
    }

    void loadAdIds() {
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    AdUtils.INITIAL_FULLCOUNT = snapshot.child("FullCount").getValue(Integer.class);
                    boolean ad = snapshot.child("AdOn").getValue(Boolean.class);
                    if (ad) {
                        for (DataSnapshot data : snapshot.child("admob").getChildren()) {
                            if (data.child("adType").getValue(String.class).toLowerCase().equals("appopen") && data.child("b").getValue(Boolean.class)) {
                                AdUtils.APPOPEN_AD_UNIT_ID = data.child("id").getValue(String.class);
                            } else if (data.child("adType").getValue(String.class).toLowerCase().equals("native") && data.child("b").getValue(Boolean.class)) {
                                AdUtils.NATIVE_AD_UNIT_ID = data.child("id").getValue(String.class);
                            } else if (data.child("adType").getValue(String.class).toLowerCase().equals("banner") && data.child("b").getValue(Boolean.class)) {
                                AdUtils.BANNER_AD_UNIT_ID = data.child("id").getValue(String.class);
                            } else if (data.child("adType").getValue(String.class).toLowerCase().equals("full") && data.child("b").getValue(Boolean.class)) {
                                AdUtils.FULLLIST.add(new AdModel(data.child("id").getValue(String.class)));
                            }
                        }
                        googleOpenAd();
                        loadFull();
                    } else {
                        go();
                    }
                } else {
                    go();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                go();
            }
        });
    }

    public void googleOpenAd() {

        AdRequest adRequest = new AdRequest.Builder().build();
        AppOpenAd.load(
                SplashActivity.this,
                AdUtils.APPOPEN_AD_UNIT_ID,
                adRequest,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                new AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        appOpenAd = ad;
                        go();
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        Log.e("onAdFailedToLoad", loadAdError.getMessage());
                        go();
                    }
                });
    }

    void loadFull() {
        for (int i = 0; i < AdUtils.FULLLIST.size(); i++) {
            loadFullByNum(i);
            Log.e("load", "load inter");
        }
    }

    void loadFullByNum(int i) {

        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, AdUtils.FULLLIST.get(i).getId(), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        AdUtils.FULLLIST.get(i).setFull(interstitialAd);
                        if (AdUtils.FULLLIST.get(i).getFull() != null)
                            AdUtils.FULLLIST.get(i).getFull().setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    loadFullByNum(i);
                                    Log.d("TAG", "The ad was dismissed.");
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    Log.d("TAG", "The ad failed to show.");
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    Log.d("TAG", "The ad was shown.");
                                }
                            });
                        Log.i("TAG", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.i("TAG", loadAdError.getMessage());
                        loadFullByNum(i);
                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 300) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    if (isNetworkConnected()) {
                        go();
                    } else {
                        nointernet = new BottomSheetDialog(SplashActivity.this);
                        nointernet.setContentView(R.layout.no_internet);
                        nointernet.setCancelable(false);
                        nointernet.show();
                        handler.postDelayed(runnable, 100);
                    }
                } else {
                    finish();
                    Toast.makeText(SplashActivity.this, "We Need Permission without permission We can not move Forward!", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    void go() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        },3000);

    }
    private boolean isNetworkConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

}