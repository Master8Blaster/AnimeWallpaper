package com.nuturaldot.animewallpaper.admob;

import com.google.android.gms.ads.interstitial.InterstitialAd;

public class AdModel {

    InterstitialAd full;
    String id;


    public AdModel(String id) {
        this.id = id;

    }

    public InterstitialAd getFull() {
        return full;
    }

    public void setFull(InterstitialAd full) {
        this.full = full;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
