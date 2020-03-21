package com.urzednicza.youtuberemotebackend.service;

import com.urzednicza.youtuberemotebackend.enums.MemberType;
import com.urzednicza.youtuberemotebackend.models.MemberSession;
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

    public void registerSession(User user) {
        RemoteSession remoteSession = new RemoteSession(user);
        remoteSessions.put(user, remoteSession);
    }

    public void initializeSession(User user, WebSocketSession webSocketSession, String deviceId, MemberType memberType) {
        remoteSessions.get(user).addMemberSession(webSocketSession, deviceId, memberType);
    }

    public RemoteSession getRemoteSession(User user) {
        return remoteSessions.get(user);
    }

    public RemoteSession getRemoteSessionByMemberSession(WebSocketSession webSocketSession) {
        for (Map.Entry<User, RemoteSession> entry : remoteSessions.entrySet()) {
            for (Map.Entry<String, MemberSession> entry1 : entry.getValue().getMemberSessions().entrySet()) {
                if (entry1.getValue().getWebSocketSession().equals(webSocketSession)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }
}
