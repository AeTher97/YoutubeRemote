package com.example.youtuberemoteandroid.messages.server;

import com.example.youtuberemoteandroid.enums.MessageType;

import lombok.Data;

@Data
public class QueueLenghtMessage {

    private MessageType messageType;
    private int content;
}
