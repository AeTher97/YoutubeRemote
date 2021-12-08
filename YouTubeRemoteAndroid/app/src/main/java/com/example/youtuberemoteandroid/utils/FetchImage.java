package com.example.youtuberemoteandroid.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;

public class FetchImage implements Runnable {

    private String source;
    private Success success;

    public FetchImage(String source, Success success) {
        this.source = source;
        this.success = success;
    }

    @Override
    public void run() {
        Bitmap image = null;
        try {
            InputStream inputStream = new URL(source).openStream();
            image = BitmapFactory.decodeStream(inputStream);
            success.run(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static interface Success {
        void run(Bitmap bitmap);
    }
}
