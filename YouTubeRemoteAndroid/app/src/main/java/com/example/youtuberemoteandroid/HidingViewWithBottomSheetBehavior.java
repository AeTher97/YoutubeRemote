package com.example.youtuberemoteandroid;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class HidingViewWithBottomSheetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    private static final float UNDEFINED = Float.MAX_VALUE;

    private float yStart = 0f;
    private float motionStart = 0f;
    private int peekHeight = 0;
    private boolean peekHeightCached;
    private ValueAnimator animator;
    private boolean animating;
    private int parentHeight;
    private int childHeight = 1;
    private int endPosition;
    private int fillerPosition;

    private float childStartY = UNDEFINED;
    private boolean holding;
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
        if (bottomSheetBehavior != null) {
            if (!animating && !holding && !closed) {
                if (childHeight == 1) {
                    childHeight = child.getHeight();
                }
                float slideOffset = getSlideOffset(parent, dependency, bottomSheetBehavior);
                View filler = parent.findViewById(R.id.filler);
                if (fillerPosition == 0) {
                    fillerPosition = (int) filler.getY();
                }

                filler.setY(fillerPosition - getAbsoluteOffset(parent, dependency, bottomSheetBehavior));

                if (childStartY == UNDEFINED) {
                    childStartY = child.getLayoutParams().height;
                }


                ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
                layoutParams.height = (int) (parent.getHeight() * 0.1f + (parent.getHeight() * 0.8f * (1 - slideOffset)));
                child.setLayoutParams(layoutParams);
                setChildHeight(child, (int) ((int) (parent.getHeight() * 0.1f + (parent.getHeight() * 0.8f * (1 - slideOffset)))));
            }

        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull MotionEvent ev) {
        return ev.getAction() == MotionEvent.ACTION_DOWN && parent.isPointInChildBounds(child, (int) ev.getX(), (int) ev.getY())
                && ev.getAction() != MotionEvent.ACTION_MOVE;
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

            if (!peekHeightCached) {
                parentHeight = parent.getHeight();
                peekHeight = bottomSheetBehavior.getPeekHeight();
                peekHeightCached = true;
                childHeight = child.getHeight();
                endPosition = (int) (parentHeight - customCoordinatorLayout.getBottomBarHeight() - parentHeight * 0.1f);
            }
            return true;
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            int motion = (int) (ev.getRawY() - yStart);
            int position = (int) (motionStart + motion);
            if (position < 0) {
                return true;
            }
            if (position > endPosition) {
                return true;
            }

            parent.setY(position);

            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
            float bottomBarFraction = position / (float) endPosition;
            int drawerPeek = peekHeight - motion;
            if (drawerPeek > peekHeight) {
                drawerPeek = peekHeight;
            }
            int menuPeek = (int) (bottomBarFraction * customCoordinatorLayout.getBottomBarHeight());
            int height = (int) (parentHeight - (Math.max(drawerPeek, menuPeek)) - (position));
            layoutParams.height = height;
            setChildHeight(child, height);
            child.setLayoutParams(layoutParams);


            customCoordinatorLayout.setBottomBarVisible((motionStart + ev.getRawY() - yStart) / (float) endPosition);
            return true;
        } else if (ev.getAction() == MotionEvent.ACTION_CANCEL) {
            holding = false;
            if (parent.getY() > parentHeight / 3f) {
                closed = true;
                animatePosition((int) parent.getY(), endPosition, bottomSheetBehavior, customCoordinatorLayout, child);
            } else {
                closed = false;
                animatePosition((int) parent.getY(), 0, bottomSheetBehavior, customCoordinatorLayout, child);
            }
            return true;
        } else {
            return super.onTouchEvent(parent, child, ev);
        }

    }

    private void animatePosition(int start, int end, BottomSheetBehavior bottomSheetBehavior, CustomCoordinatorLayout parent, View child) {
        int span = Math.abs(end - start);

        animating = true;
        animator = ValueAnimator.ofInt(start, end);
        animator.setDuration((long) ((float) span / childHeight * 500));
        animator.addUpdateListener((animation) -> {
            int currentValue = (int) animation.getAnimatedValue();
            parent.setY((float) (int) animation.getAnimatedValue());
            if (endPosition != 0) {
                parent.setBottomBarVisible((float) (int) animation.getAnimatedValue() / endPosition);
            }

            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
            float bottomBarFraction = currentValue / (float) endPosition;
            int drawerPeek = peekHeight - currentValue;
            if (drawerPeek > peekHeight) {
                drawerPeek = peekHeight;
            }
            int menuPeek = (int) (bottomBarFraction * parent.getBottomBarHeight());
            int height = (int) (parentHeight - (Math.max(drawerPeek, menuPeek)) - (currentValue));
            layoutParams.height = height;
            setChildHeight(child, height);
            child.setLayoutParams(layoutParams);

            if ((int) animation.getAnimatedValue() == end) {
                animating = false;
            }
        });
        animator.start();
    }

    private void setChildHeight(View child, int height) {
        if (childHeight == 1) {
            return;
        }
        float fraction = height / (float) childHeight;
        ImageView imageView = child.findViewById(R.id.song_image);
        imageView.setScaleX(fraction);
        imageView.setScaleY(fraction);
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