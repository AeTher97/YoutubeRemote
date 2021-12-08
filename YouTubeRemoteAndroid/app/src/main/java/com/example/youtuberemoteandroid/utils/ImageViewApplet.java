package com.example.youtuberemoteandroid.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class ImageViewApplet {

    static final int hueRange = 360;


    public static int[] getColorsFromImage (Bitmap bitmap){
        int[] hues = new int[hueRange];
        float [] saturations = new float[hueRange];
        float [] brightnesses = new float[hueRange];

        int notSkipped=0;
        float border = 0.5f;
        while(notSkipped < 100) {
            notSkipped=0;
            for (int i = 0; i < bitmap.getWidth(); i++) {
                for (int j = 0; j < bitmap.getHeight(); j++) {
                    int pixel = bitmap.getPixel(i, j);
                    float[] hsv = new float[3];
                    Color.RGBToHSV(Color.red(pixel), Color.green(pixel), Color.blue(pixel), hsv);
                    if (hsv[1] < border || hsv[2]<border) {
                        continue;
                    }
                    notSkipped++;
                    int hue = (int) Math.floor(hsv[0]);
                    hues[hue]++;
                    saturations[hue] += hsv[1];
                    brightnesses[hue] += hsv[2];
                }
            }

            border-=0.05f;
        }

        // Find the most common hue.
        int hueCount = hues[0];
        int hue = 0;
        for (int i = 1; i < hues.length; i++) {
            if (hues[i] > hueCount) {
                hueCount = hues[i];
                hue = i;
            }
        }



        float[] hsvColor = new float[3];
        hsvColor[0] =hue;
        hsvColor[1] =saturations[hue]/hueCount;
        hsvColor[2] =brightnesses[hue]/hueCount;

        if(hsvColor[2]>0.5){
            hsvColor[2]=0.5f;
        }
        int [] result = new int[2];
        result[0]= Color.HSVToColor(hsvColor);
        hsvColor[2] = hsvColor[2]*0.5f;
        result[1] = Color.HSVToColor(hsvColor);
        return result;
    }
}
