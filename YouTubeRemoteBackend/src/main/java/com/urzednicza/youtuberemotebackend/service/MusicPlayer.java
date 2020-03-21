package com.urzednicza.youtuberemotebackend.service;

public interface MusicPlayer {

    void play(String sessionId);

    void pause(String sessionId);

    void next(String sessionId);

    void previous(String sessionId);
}
