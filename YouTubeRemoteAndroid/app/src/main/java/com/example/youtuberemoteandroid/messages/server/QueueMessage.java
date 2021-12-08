package com.example.youtuberemoteandroid.messages.server;

import com.example.youtuberemoteandroid.enums.MessageType;

import java.util.List;

import lombok.Data;

@Data
public class QueueMessage {

    private MessageType messageType;
    private List<QueueSong> content;

    @Data
    public static class QueueSong{
        private int index;
        private String title;
        private String performer;
        private String imgSrc;
        private String time;
        private boolean selected;


    }

}
