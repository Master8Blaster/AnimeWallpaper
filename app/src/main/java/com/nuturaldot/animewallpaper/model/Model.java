package com.nuturaldot.animewallpaper.model;

public class Model {

    String name;
    String url;
    String key;

    public Model(String name, String url, String key) {
        this.name = name;
        this.url = url;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getKey() {
        return key;
    }
}