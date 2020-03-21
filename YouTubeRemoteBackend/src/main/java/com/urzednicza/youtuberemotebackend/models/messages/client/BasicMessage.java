package com.urzednicza.youtuberemotebackend.models.messages.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.urzednicza.youtuberemotebackend.enums.MessageType;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BasicMessage {

    private MessageType messageType;
}
