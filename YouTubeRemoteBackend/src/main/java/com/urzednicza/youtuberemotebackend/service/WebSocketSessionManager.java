package com.urzednicza.youtuberemotebackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urzednicza.youtuberemotebackend.enums.MemberType;
import com.urzednicza.youtuberemotebackend.models.MemberSession;
import com.urzednicza.youtuberemotebackend.models.RemoteSession;
import com.urzednicza.youtuberemotebackend.models.User;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j
public class WebSocketSessionManager {

    private Map<User, RemoteSession> remoteSessions;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WebSocketSessionManager() {
        this.remoteSessions = new HashMap<>();
    }

    public void registerSession(User user) {
        RemoteSession remoteSession = new RemoteSession(user, objectMapper);
        remoteSessions.put(user, remoteSession);
    }

    public void initializeSession(User user, WebSocketSession webSocketSession, String deviceId, MemberType memberType) {
        try {
            remoteSessions.get(user).addMemberSession(webSocketSession, deviceId, memberType);
        } catch (IOException e) {
            log.error("Failed to notify receiver" + e.getMessage());
        }
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
