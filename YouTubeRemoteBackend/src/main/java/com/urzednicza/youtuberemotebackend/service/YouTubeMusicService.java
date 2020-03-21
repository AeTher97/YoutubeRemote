package com.urzednicza.youtuberemotebackend.service;

import org.springframework.web.socket.WebSocketHandler;

public class YouTubeMusicService implements MusicPlayer {

    private WebSocketHandler webSocketHandler;

    public YouTubeMusicService(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void play(String sessionId) {
    }

    @Override
    public void pause(String sessionId) {

    }

    @Override
    public void next(String sessionId) {

    }

    @Override
    public void previous(String sessionId) {

    }
}
