package com.example.youtuberemoteandroid.messages.server;

import com.example.youtuberemoteandroid.enums.MessageType;

import lombok.Data;

@Data
public class ControlTimeMessage {

    private MessageType messageType;
    private ControlContent content;

    @Data
    public static class ControlContent {
        boolean playing;
        int time;
        int maxTime;
    }
}
