package com.example.youtuberemoteandroid.adapters;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtuberemoteandroid.MainActivity;
import com.example.youtuberemoteandroid.R;
import com.example.youtuberemoteandroid.enums.SearchAction;
import com.example.youtuberemoteandroid.messages.client.ClientMessage;

import java.util.List;

import lombok.Data;
import lombok.Getter;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<SearchRecyclerItem> queueItems;
    private final Typeface face;
    private final MainActivity mainActivity;

    public SearchAdapter(List<SearchRecyclerItem> searchRecyclerItems, MainActivity mainActivity) {
        queueItems = searchRecyclerItems;
        this.mainActivity = mainActivity;
        face = Typeface.createFromAsset(mainActivity.getAssets(),
                "YouTubeSansBold.otf");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        if (viewType == R.layout.search_list_header) {
            return new HeaderViewHolder(view);
        } else if (viewType == R.layout.search_list_item) {
            return new ItemViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).getHeading().setTypeface(face);
            ((HeaderViewHolder) holder).getHeading().setText(queueItems.get(position).getName());

        } else if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).getTitle().setText(queueItems.get(position).getName());
            ((ItemViewHolder) holder).getView().setOnClickListener(l -> {
                mainActivity.getMediaWebSocketClient().send(ClientMessage.searchControlMessage(SearchAction.PLAY_SONG,queueItems.get(position).getSectionIndex(),queueItems.get(position).getItemIndex()));
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        SearchRecyclerItem.Type type = queueItems.get(position).getType();
        if (type.equals(SearchRecyclerItem.Type.HEADER)) {
            return R.layout.search_list_header;
        } else if (type.equals(SearchRecyclerItem.Type.ENTRY)) {
            return R.layout.search_list_item;
        } else {
            return 0;
        }
    }


    @Override
    public int getItemCount() {
        return queueItems.size();
    }

    @Getter
    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView heading;
        private View view;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.search_heading);
            this.view = itemView;
        }
    }

    @Getter
    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private View view;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.search_item_title);
            this.view = itemView;
        }
    }
}
