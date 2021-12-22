package com.example.youtuberemoteandroid.messages.server;

import com.example.youtuberemoteandroid.enums.MessageType;

import java.util.List;

import lombok.Data;

@Data
public class ReceiversMessage {

    private MessageType messageType;
    private List<Receiver> receivers;

    @Data
    public static class Receiver{
        private String deviceName;
        private String uuid;
    }
}
