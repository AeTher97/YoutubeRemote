package com.example.youtuberemoteandroid.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtuberemoteandroid.MainActivity;
import com.example.youtuberemoteandroid.MediaWebSocketClient;
import com.example.youtuberemoteandroid.R;
import com.example.youtuberemoteandroid.enums.Action;
import com.example.youtuberemoteandroid.messages.client.ClientMessage;
import com.example.youtuberemoteandroid.messages.server.QueueMessage;
import com.example.youtuberemoteandroid.views.FetchImageView;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.SongViewHolder> {


    private MediaWebSocketClient mediaWebSocketClient;
    @Setter
    private ItemTouchHelper itemTouchHelper;
    @Getter
    private final List<QueueMessage.QueueSong> songs;
    private MainActivity mainActivity;

    public QueueAdapter(List<QueueMessage.QueueSong> songs, MainActivity mainActivity, MediaWebSocketClient mediaWebSocketClient) {
        this.songs = songs;
        this.mainActivity = mainActivity;
        this.mediaWebSocketClient = mediaWebSocketClient;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_item, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        QueueMessage.QueueSong song = songs.get(position);
        holder.getTitle().setText(song.getTitle());
        if(!song.getPerformer().isEmpty()) {
            holder.getPerformer().setText(song.getPerformer().replace("\n", "") + " • " + song.getTime());
        } else {
            holder.getPerformer().setText("");
        }
        if (song.isSelected()) {
            holder.setSelected(true);
            holder.getBackground().setBackgroundColor(ColorUtils.blendARGB(Color.parseColor("#ffffff"), mainActivity.getCurrentForegroundColor(), 0.9f));
        } else {
            holder.getBackground().setBackgroundColor(Color.TRANSPARENT);
        }

        holder.getFetchImageView().setImageUlr(song.getImgSrc(), mainActivity);


        holder.getDragHandle().setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                itemTouchHelper.startDrag(holder);
                holder.getBackground().setBackgroundColor(ColorUtils.blendARGB(Color.parseColor("#ffffff"), mainActivity.getCurrentForegroundColor(), 0.9f));
            }
            return true;
        });

        holder.getBackground().setOnClickListener(l -> {
            mediaWebSocketClient.send(ClientMessage.queueControlMessage(Action.PLAY,position));
        });


    }


    @Override
    public int getItemCount() {
        return songs.size();
    }

    @Getter
    public static class SongViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView performer;
        private final FetchImageView fetchImageView;
        private final ConstraintLayout background;
        private final ImageView dragHandle;
        @Setter
        private boolean selected;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.queue_item_title);
            performer = itemView.findViewById(R.id.queue_item_artist);
            fetchImageView = itemView.findViewById(R.id.queue_item_image);
            background = itemView.findViewById(R.id.queue_item_background);
            dragHandle = itemView.findViewById(R.id.queue_item_drag_handle);
        }
    }
}
