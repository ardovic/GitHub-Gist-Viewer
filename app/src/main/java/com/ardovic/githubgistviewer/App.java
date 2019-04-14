package com.ardovic.githubgistviewer;

import android.app.Application;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE))
                .build();
        picasso.setLoggingEnabled(true);
        Picasso.setSingletonInstance(picasso);

    }

    public static App getInstance() {
        return instance;
    }
}
