package com.example.youtuberemoteandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.youtuberemoteandroid.adapters.QueueAdapter;
import com.example.youtuberemoteandroid.enums.Action;
import com.example.youtuberemoteandroid.messages.client.ClientMessage;
import com.example.youtuberemoteandroid.messages.server.ConstrolsDetailsMessage;
import com.example.youtuberemoteandroid.messages.server.ControlSongMessage;
import com.example.youtuberemoteandroid.messages.server.ControlTimeMessage;
import com.example.youtuberemoteandroid.messages.server.QueueMessage;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import lombok.Getter;

public class MainActivity extends AppCompatActivity {

    private CustomCoordinatorLayout container;
    private ConstraintLayout parent;
    private ConstraintLayout songControls;
    private ConstraintLayout bottomSheet;
    private float compactBarStartingX;
    private boolean layout;
    private boolean secondLayout;
    private RecyclerView queueRecycler;
    private QueueAdapter queueAdapter;
    private boolean songPlaying = false;
    private final Timer reconnectTimer = new Timer();

    private float songControlsFraction = 0.92f;
    private MediaWebSocketClient mediaWebSocketClient;

    @Getter
    private int currentBackgroundColor;
    @Getter
    private int currentForegroundColor;


    public void connectWithReconnection() {
        try {
            mediaWebSocketClient = new MediaWebSocketClient(new URI("wss://globalcapsleague.com/remote"), this);
            reconnectTimer.schedule(new ConnectTask(), 0);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private class ConnectTask extends TimerTask {

        @Override
        public void run() {
            boolean connected;
            try {
                connected = mediaWebSocketClient.connectBlocking();
            } catch (InterruptedException e) {
                e.printStackTrace();
                connected = false;
            }

            if (!connected) {
                reconnectTimer.schedule(new ConnectTask(), 0);
            }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = findViewById(R.id.container);
        container.setMainActivity(this);

        parent = findViewById(R.id.parent);
        bottomSheet = findViewById(R.id.bottomSheet);
        songControls = findViewById(R.id.song_controls);
        queueRecycler = findViewById(R.id.queue_recycler);


        parent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (secondLayout) {
                    secondLayout = false;
                    container.setVariables(songControlsFraction);
                    compactBarStartingX = findViewById(R.id.song_closed_bar_fill).getX();
                    container.setVisibility(View.GONE);
                }
                if (!layout) {
                    layout = true;
                    Log.i("Layout", "layout");
                    container.getLayoutParams().height = parent.getHeight();
                    songControlsFraction = 1f - findViewById(R.id.drawer_header).getHeight() / (float) parent.getHeight();
                    BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                    bottomSheetBehavior.setPeekHeight(findViewById(R.id.drawer_header).getHeight());
                    songControls.getLayoutParams().height = (int) (parent.getHeight() * songControlsFraction);
                    bottomSheet.getLayoutParams().height = (int) (parent.getHeight() * (1f - container.getClosedSongControlsPercentage()));
                    container.requestLayout();

                    secondLayout = true;
                }

            }
        });

        connectWithReconnection();

        findViewById(R.id.song_play).setOnClickListener(l -> {
            if (mediaWebSocketClient.getCurrentTimeState().getContent().isPlaying()) {
                mediaWebSocketClient.send(ClientMessage.mediaControlMessage(Action.PAUSE));
            } else {
                mediaWebSocketClient.send(ClientMessage.mediaControlMessage(Action.PLAY));
            }
        });

        findViewById(R.id.song_closed_play).setOnClickListener(l -> {
            if (mediaWebSocketClient.getCurrentTimeState().getContent().isPlaying()) {
                mediaWebSocketClient.send(ClientMessage.mediaControlMessage(Action.PAUSE));
            } else {
                mediaWebSocketClient.send(ClientMessage.mediaControlMessage(Action.PLAY));
            }
        });

        findViewById(R.id.song_forward).setOnClickListener(l -> {
            mediaWebSocketClient.send(ClientMessage.mediaControlMessage(Action.NEXT));

        });

        findViewById(R.id.song_closed_next).setOnClickListener(l -> {
            mediaWebSocketClient.send(ClientMessage.mediaControlMessage(Action.NEXT));

        });

        findViewById(R.id.song_back).setOnClickListener(l -> {
            mediaWebSocketClient.send(ClientMessage.mediaControlMessage(Action.PREVIOUS));

        });


        ((SeekBar) findViewById(R.id.song_bar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int timeSet = (int) (progress / 100f * mediaWebSocketClient.getCurrentTimeState().getContent().getMaxTime());
                    mediaWebSocketClient.send(ClientMessage.mediaControlMessage(Action.SET_TIME, timeSet));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.song_additional_close).setOnClickListener(l -> {
            container.closeSongControls();
        });


        queueRecycler.setLayoutManager(new LinearLayoutManager(this));

        queueAdapter = new QueueAdapter(new ArrayList<>(), this, mediaWebSocketClient);
        queueRecycler.setAdapter(queueAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            private int currentIndex = -1;

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                if (currentIndex == -1) {
                    Log.i("update", "xd");
                    currentIndex = viewHolder.getAdapterPosition();
                }
                Collections.swap(((QueueAdapter) recyclerView.getAdapter()).getSongs(), viewHolder.getAdapterPosition(), target.getAdapterPosition());
                recyclerView.getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                queueAdapter.getSongs().remove(viewHolder.getAdapterPosition());
                queueAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                mediaWebSocketClient.send(ClientMessage.queueControlMessage(Action.REMOVE, position));
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                if (!((QueueAdapter.SongViewHolder) viewHolder).isSelected()) {
                    ((QueueAdapter.SongViewHolder) viewHolder).getBackground().setBackgroundColor(Color.TRANSPARENT);
                }
                if (currentIndex != -1) {
                    int targetIndex = viewHolder.getAdapterPosition();
                    mediaWebSocketClient.send(ClientMessage.queueControlMessage(Action.MOVE, targetIndex, currentIndex));
                    currentIndex = -1;
                }
                super.clearView(recyclerView, viewHolder);
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

        });

        queueAdapter.setItemTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(queueRecycler);

    }


    public void setQueue(List<QueueMessage.QueueSong> newSongs) {
        List<QueueMessage.QueueSong> oldSongs = queueAdapter.getSongs();
        newSongs.forEach(song -> {
            if (song.getIndex() >= oldSongs.size()) {
                oldSongs.add(song);
                queueAdapter.notifyItemInserted(song.getIndex());
            } else {
                QueueMessage.QueueSong oldSong = oldSongs.get(song.getIndex());
                if (oldSong.getTitle().equals(song.getTitle())) {
                    if (oldSong.getImgSrc() == null && song.getImgSrc() != null) {
                        oldSong.setImgSrc(song.getImgSrc());
                        queueAdapter.notifyItemChanged(song.getIndex());
                    }
                    if (oldSong.isSelected() != song.isSelected()) {
                        oldSong.setSelected(song.isSelected());
                        queueAdapter.notifyItemChanged(song.getIndex());
                    }
                    return;
                }
                oldSong.setTitle(song.getTitle());
                oldSong.setPerformer(song.getPerformer());
                oldSong.setSelected(song.isSelected());
                oldSong.setImgSrc(song.getImgSrc());
                queueAdapter.notifyItemChanged(song.getIndex());
            }
        });
    }

    public void setQueueLength(int length) {
        List<QueueMessage.QueueSong> oldSongs = queueAdapter.getSongs();

        if (oldSongs.size() > length) {
            oldSongs.subList(length - 1, oldSongs.size()).clear();
            queueAdapter.notifyItemRangeRemoved(length - 1, oldSongs.size() - 1);

        }
    }


    public void setColors(int primarySongColor, int secondarySongColor) {
        ValueAnimator primaryColorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), ((GradientDrawable) bottomSheet.getBackground()).getColor().getDefaultColor(), primarySongColor);
        primaryColorAnimation.setDuration(250);
        primaryColorAnimation.addUpdateListener((animator) -> {
            currentForegroundColor = (int) animator.getAnimatedValue();
            ((GradientDrawable) bottomSheet.getBackground()).setColor((int) animator.getAnimatedValue());
            ((GradientDrawable) ((RippleDrawable) songControls.findViewById(R.id.song_play).getBackground()).getDrawable(1)).setColor((int) animator.getAnimatedValue());


            Optional<QueueMessage.QueueSong> song = queueAdapter.getSongs().stream().filter(QueueMessage.QueueSong::isSelected).findAny();
            song.ifPresent(queueSong -> queueAdapter.notifyItemChanged(queueSong.getIndex()));

        });

        ValueAnimator secondaryColorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), ((ColorDrawable) songControls.getBackground()).getColor(), secondarySongColor);
        secondaryColorAnimation.setDuration(250);
        secondaryColorAnimation.addUpdateListener((animator) -> {
            currentBackgroundColor = (int) animator.getAnimatedValue();
            if (!container.getBehavior().isClosed()) {
                songControls.setBackgroundColor((int) animator.getAnimatedValue());
                container.findViewById(R.id.filler).setBackgroundColor((int) animator.getAnimatedValue());
                getWindow().setStatusBarColor((int) animator.getAnimatedValue());
            }
        });

        primaryColorAnimation.start();
        secondaryColorAnimation.start();
    }

    public void setImage(Bitmap bitmap) {
        ImageView imageView = findViewById(R.id.song_image);
        imageView.setImageBitmap(bitmap);
        imageView.setClipToOutline(true);
        if (container.getBehavior().isClosed()) {
            new Handler().postDelayed(() -> {
                container.layoutChild();
            }, 50);
        }

    }

    public void setSongInfo(ControlSongMessage.SongContent info) {
        Typeface face = Typeface.createFromAsset(getAssets(),
                "YouTubeSansBold.otf");
        TextView songTitle = findViewById(R.id.song_title);
        songTitle.setTypeface(face);
        songTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        songTitle.setSingleLine(true);
        songTitle.setText(info.getTitle());
        songTitle.setSelected(true);

        TextView songPerformer = findViewById(R.id.song_artist);
        songPerformer.setSingleLine(true);
        songPerformer.setEllipsize(TextUtils.TruncateAt.END);
        songPerformer.setText(info.getPerformer());

        TextView songCompactTitle = findViewById(R.id.song_closed_title);
        songCompactTitle.setSelected(true);
        songCompactTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        songCompactTitle.setSingleLine(true);
        songCompactTitle.setText(info.getTitle());

        TextView songCompactPerformer = findViewById(R.id.song_closed_artist);
        songCompactPerformer.setSingleLine(true);
        songCompactPerformer.setEllipsize(TextUtils.TruncateAt.END);
        songCompactPerformer.setText(info.getPerformer());

        if (!songPlaying) {
            container.setVisibility(View.VISIBLE);
            container.closeSongControlsInstantly();
            songPlaying = true;
        }
    }

    public void setTimeInfo(ControlTimeMessage.ControlContent info) {
        ((TextView) findViewById(R.id.song_current_time)).setText(MediaWebSocketClient.convertToHourString(info.getTime()));
        ((TextView) findViewById(R.id.song_total_time)).setText(MediaWebSocketClient.convertToHourString(info.getMaxTime()));
        View closedBarFill = findViewById(R.id.song_closed_bar_fill);
        SeekBar seekBar = findViewById(R.id.song_bar);
        seekBar.setMax(100);
        seekBar.setProgress((int) (info.getTime() / (float) info.getMaxTime() * 100), true);
        closedBarFill.setX(compactBarStartingX + (int) (info.getTime() / (float) info.getMaxTime() * container.getWidth()));
        ImageButton imageButton = findViewById(R.id.song_play);
        ImageButton compactPlayButton = findViewById(R.id.song_closed_play);
        if (info.isPlaying()) {
            imageButton.setImageResource(R.drawable.pause);
            compactPlayButton.setImageResource(R.drawable.pause);
        } else {
            imageButton.setImageResource(R.drawable.play);
            compactPlayButton.setImageResource(R.drawable.play);
        }
    }

    public void setDetailedInfo(ConstrolsDetailsMessage.DetailsInfo info) {
        ImageButton repeatButton = findViewById(R.id.song_repeat);
        switch (info.getRepeatType()) {
            case REPEAT_ALL:
                repeatButton.setImageResource(R.drawable.repeat_all);
                break;
            case REPEAT_OFF:
                repeatButton.setImageResource(R.drawable.no_repeat);
                break;
            case REPEAT_ONE:
                repeatButton.setImageResource(R.drawable.repeat_one);
                break;
        }
    }
}