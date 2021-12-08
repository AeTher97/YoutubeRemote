package com.example.youtuberemoteandroid.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.youtuberemoteandroid.utils.FetchImage;
import com.example.youtuberemoteandroid.utils.ImageViewApplet;

public class FetchImageView extends androidx.appcompat.widget.AppCompatImageView {
    String url;

    public FetchImageView(Context context) {
        super(context);
    }

    public FetchImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FetchImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageUlr(String url, AppCompatActivity context){
        if(url==null){
            setImageBitmap(Bitmap.createBitmap(20,20,Bitmap.Config.ARGB_8888));
            return;
        }

        new Thread(new FetchImage(url, (image) -> {
            context.runOnUiThread(() -> {
                setImageBitmap(image);
                setClipToOutline(true);
            });
        })).start();
    }


}
