package com.urzednicza.youtuberemotebackend.models;

import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

public class RemoteSession {

    private Map<String, WebSocketSession> memberSessions;
    private User user;

    public RemoteSession(User user) {
        this.memberSessions = new HashMap<>();
    }

    public void addMemberSession(WebSocketSession webSocketSession, String deviceName){
        memberSessions.put(deviceName,webSocketSession);
    }

    public void removeMemberSession(String deviceName){
        memberSessions.remove(deviceName);
    }

    public WebSocketSession getMemberSession(String deviceId){
        return memberSessions.get(deviceId);
    }

    public Map<String, WebSocketSession> getMemberSessions(){
        return memberSessions;
    }

    public User getUser(){
        return user;
    }
}
