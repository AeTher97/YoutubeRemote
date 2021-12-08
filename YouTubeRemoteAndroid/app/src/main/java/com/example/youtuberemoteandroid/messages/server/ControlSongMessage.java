package com.example.youtuberemoteandroid.messages.server;

import com.example.youtuberemoteandroid.enums.MessageType;

import lombok.Data;

@Data
public class ControlSongMessage {

    private MessageType messageType;
    private SongContent content;

    @Data
    public static class SongContent {
        private String title;
        private String performer;
        private String imgSrc;
        private String imgSrcLarge;
    }
}
