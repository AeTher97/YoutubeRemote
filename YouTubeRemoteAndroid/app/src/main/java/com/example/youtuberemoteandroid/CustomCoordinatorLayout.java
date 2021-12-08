package com.example.youtuberemoteandroid;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CustomCoordinatorLayout extends CoordinatorLayout {

    private int peekHeight;
    private int parentHeight;
    private int childHeight;
    private int targetChildHeight;
    private float endPosition;
    private float fillerPosition;
    private final float closedSongControlsPercentage = 0.09f;
    private final float closedImagePercentage = 0.065f;
    private  float  targetImageScale;
    private float targetImageX;
    private float targetImageY;
    private float startImageX;
    private float startImageY;
    private float songControlsFraction;


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

    public void setVariables(float songControlsFraction) {
        this.songControlsFraction = songControlsFraction;
        parentHeight = getHeight();
        childHeight = (int) (parentHeight*songControlsFraction);
        fillerPosition = findViewById(R.id.filler).getY();
        endPosition = (int) (parentHeight - getBottomBarHeight() - parentHeight * closedSongControlsPercentage);
        peekHeight = BottomSheetBehavior.from(findViewById(R.id.bottomSheet)).getPeekHeight();
        targetChildHeight = (int) (closedSongControlsPercentage * parentHeight);
        ImageView imageView = findViewById(R.id.song_image);
        targetImageScale =getClosedImagePercentage() * getParentHeight()/imageView.getHeight();
        targetImageX = 20;
        targetImageY = targetChildHeight/2f - closedImagePercentage * parentHeight/2f;
        startImageX = imageView.getX();
        startImageY = imageView.getY();
        findViewById(R.id.song_bar).getLayoutParams().width = (int) (getWidth() * 0.85f);
        findViewById(R.id.song_closed_controls).getLayoutParams().height = (int) (parentHeight * closedSongControlsPercentage);
        findViewById(R.id.song_closed_title).setX(targetImageX + 30 + closedImagePercentage * getHeight());
        findViewById(R.id.song_closed_artist).setX(targetImageX + 30 + closedImagePercentage * getHeight());
        int tinyTitleY = (int) (targetChildHeight/2f - (findViewById(R.id.song_closed_title).getHeight() + findViewById(R.id.song_closed_artist).getHeight()/2));

        findViewById(R.id.song_closed_title).setY(tinyTitleY);
        findViewById(R.id.song_closed_artist).setY(tinyTitleY + findViewById(R.id.song_closed_title).getHeight());


    }

    public void closeSongControls(){
        getBehavior().animatePosition(0, (int) endPosition,this,findViewById(R.id.song_controls));
        getBehavior().setClosed(true);
    }

    public void openSongControls(){
        getBehavior().animatePosition((int) endPosition,0,this,findViewById(R.id.song_controls));
        getBehavior().setClosed(false);
    }

    public void closeSongControlsInstantly(){
        getBehavior().closeSongControls(this,findViewById(R.id.song_controls));
    }

    public void layoutChild(){
       getBehavior().layoutChild(this,findViewById(R.id.song_controls));
    }

    public HidingViewWithBottomSheetBehavior getBehavior(){
        return ((HidingViewWithBottomSheetBehavior)((CoordinatorLayout.LayoutParams)findViewById(R.id.song_controls).getLayoutParams())
                .getBehavior());
    }


}
