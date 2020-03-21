package com.urzednicza.youtuberemotebackend.models.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.urzednicza.youtuberemotebackend.enums.MessageType;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Start {

    private MessageType messageType;
    private String deviceId;
}
