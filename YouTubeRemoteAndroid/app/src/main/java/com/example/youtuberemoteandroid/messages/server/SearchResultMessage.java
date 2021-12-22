package com.example.youtuberemoteandroid.messages.server;

import com.example.youtuberemoteandroid.enums.MessageType;
import com.example.youtuberemoteandroid.enums.SearchType;

import java.util.List;

import lombok.Data;

@Data
public class SearchResultMessage {

    private MessageType messageType;
    private List<Section> content;

    @Data
    public static class Section{
        private String name;
        private List<SearchItem> items;
    }

    @Data
    public static class SearchItem {
        private String title;
        private SearchType type;

    }
}
