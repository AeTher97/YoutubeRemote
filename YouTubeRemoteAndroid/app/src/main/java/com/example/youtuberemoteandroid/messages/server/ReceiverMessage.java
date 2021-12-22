package com.example.youtuberemoteandroid.messages.server;

import com.example.youtuberemoteandroid.enums.MessageType;

import lombok.Data;

@Data
public class ReceiverMessage {

    private MessageType messageType;
    private String deviceName;
}
