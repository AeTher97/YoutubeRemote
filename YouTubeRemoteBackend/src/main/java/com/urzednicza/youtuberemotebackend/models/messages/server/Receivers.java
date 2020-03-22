package com.urzednicza.youtuberemotebackend.models.messages.server;

import com.urzednicza.youtuberemotebackend.enums.MessageType;
import com.urzednicza.youtuberemotebackend.models.Receiver;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Receivers {

    @Getter
    MessageType messageType;
    @Setter
    @Getter
    List<Receiver> receivers;

    public Receivers() {
        messageType = MessageType.RECEIVERS;
    }
}
