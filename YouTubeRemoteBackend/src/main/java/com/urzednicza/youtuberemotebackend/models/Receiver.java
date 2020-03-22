package com.urzednicza.youtuberemotebackend.models;

import lombok.Data;

import java.util.UUID;

@Data
public class Receiver {
    private String deviceName;
    private UUID uuid;
}
