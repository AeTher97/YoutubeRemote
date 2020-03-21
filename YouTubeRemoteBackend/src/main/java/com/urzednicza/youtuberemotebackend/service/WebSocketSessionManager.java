package com.urzednicza.youtuberemotebackend.service;

import com.urzednicza.youtuberemotebackend.models.RemoteSession;
import com.urzednicza.youtuberemotebackend.models.User;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class WebSocketSessionManager {

    private Map<User, RemoteSession> remoteSessions;

    public WebSocketSessionManager() {
        this.remoteSessions = new HashMap<>();
    }

    public void registerSession(User user){
        RemoteSession remoteSession = new RemoteSession(user);
        remoteSessions.put(user,remoteSession);
    }

    public void initializeSession(User user,WebSocketSession webSocketSession, String deviceId){
        remoteSessions.get(user).addMemberSession(webSocketSession,deviceId);
    }

    public RemoteSession getRemoteSession(User user){
        return remoteSessions.get(user);
    }

    public RemoteSession getRemoteSessionByMemberSession(WebSocketSession webSocketSession){
        return (RemoteSession)remoteSessions.entrySet().stream().filter(remoteSession ->
                remoteSession.getValue().getMemberSessions().entrySet().stream()
                        .filter(memberSession -> memberSession.getValue().equals(webSocketSession)).map(Map.Entry::getKey).count()>0)
                .map(Map.Entry::getValue).collect(Collectors.toSet()).toArray()[0];
    }
}
