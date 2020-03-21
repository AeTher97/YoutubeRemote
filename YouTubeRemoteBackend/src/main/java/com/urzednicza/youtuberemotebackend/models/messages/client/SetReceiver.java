package com.urzednicza.youtuberemotebackend.models.messages.client;

import com.urzednicza.youtuberemotebackend.enums.MessageType;
import lombok.Data;

@Data
public class SetReceiver {

    MessageType messageType;
    String deviceName;

}
