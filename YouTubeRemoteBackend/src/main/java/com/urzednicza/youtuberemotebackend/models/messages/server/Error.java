package com.urzednicza.youtuberemotebackend.models.messages.server;

import com.urzednicza.youtuberemotebackend.enums.MessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Error {

    private MessageType messageType = MessageType.ERROR;
    private String description;

    public Error(String description) {
        this.description = description;
    }
}
