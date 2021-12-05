package com.example.youtuberemoteandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class MainActivity extends AppCompatActivity {

    private CustomCoordinatorLayout container;
    private ConstraintLayout parent;
    private ConstraintLayout songControls;
    private ConstraintLayout bottomSheet;
    private boolean layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.container);
        container.setMainActivity(this);

        parent = findViewById(R.id.parent);
        bottomSheet = findViewById(R.id.bottomSheet);
        songControls = findViewById(R.id.song_controls);

        parent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!layout) {
                    layout=true;
                    Log.i("Layout", "layout");
                    container.getLayoutParams().height = parent.getHeight();
                    BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                    bottomSheetBehavior.setPeekHeight((int) (parent.getHeight()*0.1f));
                    songControls.getLayoutParams().height = (int) (parent.getHeight()*0.9f);
                    bottomSheet.getLayoutParams().height = (int) (parent.getHeight()*0.9f);
                    container.requestLayout();

                }

            }
        });
    }
}