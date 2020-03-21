package com.urzednicza.youtuberemotebackend.websocket;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urzednicza.youtuberemotebackend.enums.Action;
import com.urzednicza.youtuberemotebackend.enums.MessageType;
import com.urzednicza.youtuberemotebackend.models.RemoteSession;
import com.urzednicza.youtuberemotebackend.models.User;
import com.urzednicza.youtuberemotebackend.models.messages.BasicMessage;
import com.urzednicza.youtuberemotebackend.models.messages.MediaControl;
import com.urzednicza.youtuberemotebackend.models.messages.Start;
import com.urzednicza.youtuberemotebackend.models.messages.Stop;
import com.urzednicza.youtuberemotebackend.service.WebSocketSessionManager;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Log4j
public class YouTubeHandler implements WebSocketHandler, SubProtocolCapable {


    private ObjectMapper objectMapper;
    private WebSocketSessionManager webSocketSessionManager;

    public YouTubeHandler(WebSocketSessionManager webSocketSessionManager) {
        this.objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        this.webSocketSessionManager = webSocketSessionManager;
    }

    @Override
    public List<String> getSubProtocols() {
        List<String> subProtocols = new ArrayList<>();
        subProtocols.add("remote");
        return subProtocols;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        User user = new User();
        user.setUsername("Marek");
        if(webSocketSessionManager.getRemoteSession(user) == null){
            webSocketSessionManager.registerSession(user);
        }
        log.debug("Established new connection with session for user: " + user.getUsername());
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> message) throws Exception {
        System.out.println(message.getPayload());
        webSocketSession.sendMessage(message);

        User user= new User();
        user.setUsername("Marek");

        BasicMessage basicMessage = objectMapper.readValue(message.getPayload().toString(), BasicMessage.class);

        MessageType messageType = basicMessage.getMessageType();

        if(messageType==null){
            log.debug("Message type is null no action performed");
            return;
        }

        if(messageType.equals(MessageType.START)){
            handleStart(message,webSocketSession,user);
        } else if(messageType.equals(MessageType.MEDIA_CONTROL)){
            handleMediaControl(message,user);
        } else if(messageType.equals(MessageType.STOP)){
        }

    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        log.error("Transport error");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        RemoteSession remoteSession = webSocketSessionManager.getRemoteSessionByMemberSession(webSocketSession);
        log.debug("Closed session with: " + webSocketSession.getId() + " for user: " + remoteSession.getUser().getUsername());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private void handleStart(WebSocketMessage<?> message,WebSocketSession webSocketSession,User user) throws IOException {
        Start start = objectMapper.readValue(message.getPayload().toString(),Start.class);
        webSocketSessionManager.initializeSession(user,webSocketSession,start.getDeviceId());
    }

    private void handleMediaControl(WebSocketMessage<?> message,User user) throws IOException {
        MediaControl mediaControl = objectMapper.readValue(message.getPayload().toString(),MediaControl.class);
        Action action = mediaControl.getAction();

        switch (action){
            case NEXT:
                log.debug("next");
                break;
            case PLAY:
                log.debug("play");
                break;
            case PAUSE:
                log.debug("pause");
                break;
            case PREVIOUS:
                log.debug("previous");
                break;
        }

    }

    private void handleStop(WebSocketMessage<?> message,User user) throws IOException {
        Stop stop = objectMapper.readValue(message.getPayload().toString(),Stop.class);
        webSocketSessionManager.getRemoteSession(user).removeMemberSession(stop.getDeviceId());
        log.debug("Removed member session of user: " + user.getUsername() + " device: " + stop.getDeviceId());
    }
}
