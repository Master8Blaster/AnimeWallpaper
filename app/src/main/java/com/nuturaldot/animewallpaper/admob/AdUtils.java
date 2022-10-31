package com.nuturaldot.animewallpaper.admob;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.ArrayList;
import java.util.List;

public class AdUtils {

    static public AppOpenAd appOpenAd = null;
    public static String APPOPEN_AD_UNIT_ID = "";
    public static String NATIVE_AD_UNIT_ID = "";
    public static String BANNER_AD_UNIT_ID = "";
    public static List<AdModel> FULLLIST = new ArrayList<>();
    public static int FULLCOUNT = 1;
    public static int INITIAL_FULLCOUNT = 2;
    public static boolean AppOpenShowen = false;

    public static void ShowFull(Activity context) {
        if (AdUtils.FULLCOUNT % AdUtils.INITIAL_FULLCOUNT == 0 && AdUtils.FULLLIST.size() > 0) {
            if (AdUtils.FULLLIST.get(AdUtils.FULLCOUNT % AdUtils.FULLLIST.size()).getFull() != null) {
                AdUtils.FULLLIST.get(AdUtils.FULLCOUNT % AdUtils.FULLLIST.size()).getFull().show(context);
                AdUtils.FULLCOUNT++;
            }
        } else {
            AdUtils.FULLCOUNT++;
        }
    }

    public static void loadBanner(Activity context, LinearLayout adContainer) {
        AdView adView = new AdView(context);
        adView.setAdUnitId(AdUtils.BANNER_AD_UNIT_ID);
        adView.setAdSize(AdSize.BANNER);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                adContainer.removeAllViewsInLayout();
                adContainer.addView(adView);
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                adContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdClicked() {
            }

            @Override
            public void onAdClosed() {
            }
        });
        adView.loadAd(adRequest);
    }


}
