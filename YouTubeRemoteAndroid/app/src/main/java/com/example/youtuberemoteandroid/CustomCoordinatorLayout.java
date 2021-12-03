package com.example.youtuberemoteandroid;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import lombok.Setter;

public class CustomCoordinatorLayout extends CoordinatorLayout {
    public CustomCoordinatorLayout(@NonNull Context context) {
        super(context);
    }

    public CustomCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Setter
    private MainActivity mainActivity;


    public int getBottomBarHeight(){
        return mainActivity.findViewById(R.id.linearLayout).getHeight();
    }

    public void setBottomBarVisible(float fraction){
        LinearLayout bottomBar = mainActivity.findViewById(R.id.linearLayout);
        bottomBar.setY(mainActivity.findViewById(R.id.parent).getHeight()- bottomBar.getHeight() * (fraction));

    }


}
