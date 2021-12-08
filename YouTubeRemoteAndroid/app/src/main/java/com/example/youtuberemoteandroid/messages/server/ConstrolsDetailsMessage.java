package com.example.youtuberemoteandroid.messages.server;

import com.example.youtuberemoteandroid.enums.MessageType;
import com.example.youtuberemoteandroid.enums.RepeatType;

import lombok.Data;

@Data
public class ConstrolsDetailsMessage {

    private MessageType messageType;
    private DetailsInfo content;

    @Data
    public static class DetailsInfo{
        private boolean muted;
        private int volume;
        private RepeatType repeatType;
    }
}
