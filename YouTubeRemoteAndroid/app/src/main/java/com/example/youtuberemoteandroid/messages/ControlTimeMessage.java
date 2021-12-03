package com.example.youtuberemoteandroid.messages;

import com.example.youtuberemoteandroid.MessageType;

import lombok.Data;
import lombok.Getter;

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
