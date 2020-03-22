package com.urzednicza.youtuberemotebackend.models.messages.server;

import com.urzednicza.youtuberemotebackend.enums.MessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CurrentReceiver {

    private MessageType messageType = MessageType.CURRENT_RECEIVER;
    private String deviceName;

    public CurrentReceiver(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
