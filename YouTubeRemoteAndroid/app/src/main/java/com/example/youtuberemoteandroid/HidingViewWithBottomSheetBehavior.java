package com.example.youtuberemoteandroid;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import lombok.Getter;
import lombok.Setter;

public class HidingViewWithBottomSheetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    private static final float UNDEFINED = Float.MAX_VALUE;

    private float yStart = 0f;
    private float motionStart = 0f;
    private boolean animating;
    private boolean holding;
    @Getter
    @Setter
    private boolean closed;

    public HidingViewWithBottomSheetBehavior() {
    }

    public HidingViewWithBottomSheetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return getBottomSheetBehavior(dependency) != null;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        BottomSheetBehavior bottomSheetBehavior = getBottomSheetBehavior(dependency);
        CustomCoordinatorLayout customCoordinatorLayout = (CustomCoordinatorLayout) parent;
        if (bottomSheetBehavior != null) {
            if (!animating && !holding && !closed) {

                float slideOffset = getSlideOffset(parent, dependency, bottomSheetBehavior);
                View filler = parent.findViewById(R.id.filler);
                filler.setY(customCoordinatorLayout.getFillerPosition() - getAbsoluteOffset(parent, dependency, bottomSheetBehavior));

                ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
                int height =  (int) (parent.getHeight() * ((CustomCoordinatorLayout) parent).getClosedSongControlsPercentage()
                        + (parent.getHeight() * (customCoordinatorLayout.getSongControlsFraction() - ((CustomCoordinatorLayout) parent).getClosedSongControlsPercentage()) * (1 - slideOffset)));
                layoutParams.height =height;
                child.setLayoutParams(layoutParams);
                setChildHeight(customCoordinatorLayout, child, height,true);
            }

        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull MotionEvent ev) {
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(parent.findViewById(R.id.bottomSheet));
        CustomCoordinatorLayout customCoordinatorLayout = (CustomCoordinatorLayout) parent;
        bottomSheetBehavior.setPeekHeight(bottomSheetBehavior.getPeekHeight());

        if (!holding && !parent.isPointInChildBounds(child, (int) ev.getX(), (int) ev.getY())) {
            return false;
        }

        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED || animating) {
            return false;
        }


        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            holding = true;
            yStart = ev.getRawY();
            motionStart = parent.getY();
            return true;
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            int motion = (int) (ev.getRawY() - yStart);
            int position = (int) (motionStart + motion);

            if (position < 0) {
                return true;
            }
            if (position > customCoordinatorLayout.getEndPosition()) {
                return true;
            }

            parent.setY(position);

            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
            float bottomBarFraction = position / customCoordinatorLayout.getEndPosition();
            int drawerPeek = customCoordinatorLayout.getPeekHeight() - position;
            if (drawerPeek > customCoordinatorLayout.getPeekHeight()) {
                drawerPeek = customCoordinatorLayout.getPeekHeight();
            }
            int menuPeek = (int) (bottomBarFraction * customCoordinatorLayout.getBottomBarHeight());
            int height = customCoordinatorLayout.getParentHeight() - (Math.max(drawerPeek, menuPeek)) - (position);
            layoutParams.height = height;
            setChildHeight(customCoordinatorLayout, child, height,false);
            child.setLayoutParams(layoutParams);


            customCoordinatorLayout.setBottomBarVisible((motionStart + ev.getRawY() - yStart) / (float) ((CustomCoordinatorLayout) parent).getEndPosition());
            return true;

        } else if (ev.getAction() == MotionEvent.ACTION_CANCEL && holding) {
            holding = false;

            if ((!closed && parent.getY() > customCoordinatorLayout.getParentHeight() / 10f) || (closed &&
                    parent.getY() > (customCoordinatorLayout.getParentHeight()- customCoordinatorLayout.getClosedSongControlsPercentage()* customCoordinatorLayout.getParentHeight()) * 0.9f)) {
                closed = true;
                animatePosition((int) parent.getY(), (int) customCoordinatorLayout.getEndPosition(), customCoordinatorLayout, child);
            } else {
                closed = false;
                animatePosition((int) parent.getY(), 0, customCoordinatorLayout, child);
            }
            return true;
        } else {
            return super.onTouchEvent(parent, child, ev);
        }

    }

    public void animatePosition(int start, int end, CustomCoordinatorLayout parent, View child) {
        int span = Math.abs(end - start);

        animating = true;
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration((long) ((float) span / parent.getChildHeight() * 200));
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener((animation) -> {
            int currentValue = (int) animation.getAnimatedValue();
            parent.setY((float) (int) animation.getAnimatedValue());
            parent.setBottomBarVisible((float) (int) animation.getAnimatedValue() / parent.getEndPosition());


            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
            float bottomBarFraction = currentValue / (float) parent.getEndPosition();
            int drawerPeek = parent.getPeekHeight() - currentValue;
            if (drawerPeek > parent.getPeekHeight()) {
                drawerPeek = parent.getPeekHeight();
            }
            int menuPeek = (int) (bottomBarFraction * parent.getBottomBarHeight());
            int height = (int) (parent.getParentHeight() - (Math.max(drawerPeek, menuPeek)) - (currentValue));
            layoutParams.height = height;
            setChildHeight(parent, child, height,false);
            child.setLayoutParams(layoutParams);

            if ((int) animation.getAnimatedValue() == end) {
                animating = false;
            }
        });
        animator.start();
    }

    public void closeSongControls(CustomCoordinatorLayout parent,View child){
        parent.setY(parent.getEndPosition());
        parent.setBottomBarVisible(1f);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(parent.findViewById(R.id.bottomSheet));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        ViewGroup.LayoutParams layoutParams = child.getLayoutParams();

        int menuPeek = (int) (parent.getBottomBarHeight());
        int height = (int) (parent.getParentHeight() - menuPeek - (parent.getEndPosition()));
        layoutParams.height = height;
        setChildHeight(parent, child, height,false);
        closed=true;
    }

    public void layoutChild(CustomCoordinatorLayout parent,View child){
        setChildHeight(parent,child,parent.getTargetChildHeight(),false);
    }

    private void setChildHeight(CustomCoordinatorLayout parent, View child, int height,boolean drawer) {

        ImageView imageView = child.findViewById(R.id.song_image);

        float fraction = (parent.getChildHeight() - (float) height) / (parent.getChildHeight() - (float) parent.getTargetChildHeight());
        float imageScaling = 1 - fraction * (1 - parent.getTargetImageScale());
        float imageX = parent.getStartImageX() - fraction * (parent.getStartImageX() - parent.getTargetImageX());
        float imageY = parent.getStartImageY() - fraction * (parent.getStartImageY() - parent.getTargetImageY());
        TextView title = child.findViewById(R.id.song_title);
        TextView artist = child.findViewById(R.id.song_artist);
        TextView time = child.findViewById(R.id.song_current_time);
        TextView maxTime = child.findViewById(R.id.song_total_time);
        SeekBar seekBar = child.findViewById(R.id.song_bar);
        ImageButton play = child.findViewById(R.id.song_play);
        ImageButton forward = child.findViewById(R.id.song_forward);
        ImageButton backward = child.findViewById(R.id.song_back);
        ImageButton repeat = child.findViewById(R.id.song_repeat);
        ImageButton random = child.findViewById(R.id.song_randomize);
        LinearLayout songAdditionalControls = child.findViewById(R.id.song_additional_controls);
        ConstraintLayout songTinyControls = child.findViewById(R.id.song_closed_controls);

        if (fraction > 0.15) {
            title.setAlpha(1-((fraction - 0.15f) / 0.15f));
            artist.setAlpha(1-((fraction - 0.15f) / 0.15f));
            time.setAlpha(1-((fraction - 0.15f) / 0.15f));
            maxTime.setAlpha(1-((fraction - 0.15f) / 0.15f));
            seekBar.setAlpha(1-((fraction - 0.15f) / 0.15f));
            play.setAlpha(1-((fraction - 0.15f) / 0.15f));
            forward.setAlpha(1-((fraction - 0.15f) / 0.15f));
            backward.setAlpha(1-((fraction - 0.15f) / 0.15f));
            repeat.setAlpha(1-((fraction - 0.15f) / 0.15f));
            random.setAlpha(1-((fraction - 0.15f) / 0.15f));
            songAdditionalControls.setAlpha(1-((fraction - 0.15f) / 0.15f));
        } else {
            title.setAlpha(1f);
            artist.setAlpha(1f);
            time.setAlpha(1f);
            maxTime.setAlpha(1f);
            seekBar.setAlpha(1f);
            play.setAlpha(1f);
            forward.setAlpha(1f);
            backward.setAlpha(1f);
            repeat.setAlpha(1f);
            random.setAlpha(1f);
            songAdditionalControls.setAlpha(1f);
        }


        songTinyControls.setAlpha(((fraction - 0.8f) / 0.2f));
        if(fraction<0.9f){
            child.findViewById(R.id.song_closed_next).setClickable(false);
            child.findViewById(R.id.song_closed_play).setClickable(false);
            child.findViewById(R.id.song_additional_close).setClickable(true);
        } else {
            child.findViewById(R.id.song_closed_next).setClickable(true);
            child.findViewById(R.id.song_closed_play).setClickable(true);
            child.findViewById(R.id.song_additional_close).setClickable(false);
        }
        View barBackground= child.findViewById(R.id.song_closed_bar_background);
        View barFill= child.findViewById(R.id.song_closed_bar_fill);
        if(drawer){
            barBackground.setAlpha(0f);
            barFill.setAlpha(0f);
        } else if(fraction > 0.998f){
            barBackground.setAlpha(1f);
            barFill.setAlpha(1f);
        } else {
            barBackground.setAlpha(0f);
            barFill.setAlpha(0f);
        }

        if(!drawer){
            int intermediateColor=(int)new ArgbEvaluator().evaluate(fraction,parent.getMainActivity().getCurrentBackgroundColor(), Color.parseColor("#1B1B1B"));
            int intermediateBar=(int)new ArgbEvaluator().evaluate(fraction,parent.getMainActivity().getCurrentBackgroundColor(), Color.BLACK);
            child.setBackgroundColor(intermediateColor);
            parent.findViewById(R.id.filler).setBackgroundColor(intermediateColor);
            parent.getMainActivity().getWindow().setStatusBarColor(intermediateBar);
        }


        try {
            imageView.setScaleX(imageScaling);
            imageView.setScaleY(imageScaling);
            imageView.setX(imageX);
            imageView.setY(imageY);

        } catch (Exception e){

        }


    }


    private float getSlideOffset(CoordinatorLayout parent, View dependency, BottomSheetBehavior bottomSheetBehavior) {
        int parentHeight = parent.getMeasuredHeight();
        float sheetY = dependency.getY();
        int peekHeight = bottomSheetBehavior.getPeekHeight();
        int sheetHeight = dependency.getHeight();
        float collapseY = parentHeight - peekHeight;
        float expandY = parentHeight - sheetHeight;
        float deltaY = collapseY - expandY;

        return (parentHeight - peekHeight - sheetY) / deltaY;
    }

    private float getAbsoluteOffset(CoordinatorLayout parent, View dependency, BottomSheetBehavior bottomSheetBehavior) {
        int parentHeight = parent.getMeasuredHeight();
        float sheetY = dependency.getY();
        int peekHeight = bottomSheetBehavior.getPeekHeight();
        return parentHeight - peekHeight - sheetY;
    }

    @Nullable
    private BottomSheetBehavior getBottomSheetBehavior(@NonNull View view) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        if (behavior instanceof BottomSheetBehavior) {
            return (BottomSheetBehavior) behavior;
        }
        return null;
    }
}