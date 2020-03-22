package com.urzednicza.youtuberemotebackend.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urzednicza.youtuberemotebackend.enums.MemberType;
import com.urzednicza.youtuberemotebackend.enums.MessageType;
import com.urzednicza.youtuberemotebackend.models.messages.client.SetReceiver;
import javassist.NotFoundException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RemoteSession {

    private Map<String, MemberSession> memberSessions;
    private MemberSession mediaPlayer;
    private final User user;
    private final ObjectMapper objectMapper;

    public RemoteSession(User user, ObjectMapper objectMapper) {
        this.memberSessions = new HashMap<>();
        this.user = user;
        this.objectMapper = objectMapper;
    }

    public void addMemberSession(WebSocketSession webSocketSession, String deviceName, MemberType memberType) throws IOException {
        MemberSession memberSession = new MemberSession();
        memberSession.setMemberType(memberType);
        memberSession.setWebSocketSession(webSocketSession);

        memberSessions.put(deviceName, memberSession);

        if (memberType.equals(MemberType.RECEIVER) && mediaPlayer == null) {
            mediaPlayer = memberSession;
            notifyReceivers();

        }

        notifyReceivers();
    }

    public void removeMemberSession(String deviceName) throws IOException {
        if (memberSessions.get(deviceName).equals(mediaPlayer)) {
            mediaPlayer = null;
        }
        memberSessions.remove(deviceName);
        if (!getReceiversNames().isEmpty() && mediaPlayer == null) {
            mediaPlayer = memberSessions.get(getReceiversNames().get(0));
            notifyReceivers();

        }
    }

    public MemberSession getMemberSession(String deviceName) {
        return memberSessions.get(deviceName);
    }

    public String getMemberSessionDeviceName(WebSocketSession webSocketSession) {
        for (Map.Entry<String, MemberSession> entry : memberSessions.entrySet()) {
            if (entry.getValue().getWebSocketSession().equals(webSocketSession)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public Map<String, MemberSession> getMemberSessions() {
        return memberSessions;
    }

    public User getUser() {
        return user;
    }

    public MemberSession getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MemberSession mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public void setMediaPlayer(String deviceName) throws NotFoundException, IOException {
        if (getMemberSession(deviceName) != null) {
            this.mediaPlayer = getMemberSession(deviceName);
            notifyReceivers();
        } else {
            throw new NotFoundException("Receiver not found exception");
        }
    }

    public List<Receiver> getReceivers() {
        return getReceiversNames().stream().map(receiverName -> {
            Receiver receiver = new Receiver();
            receiver.setDeviceName(receiverName);
            return receiver;
        }).collect(Collectors.toList());
    }

    public List<String> getReceiversNames() {
        List<String> receiversList = new ArrayList<>();
        for (Map.Entry<String, MemberSession> entry : memberSessions.entrySet()) {
            if (entry.getValue().getMemberType().equals(MemberType.RECEIVER)) {
                receiversList.add(entry.getKey());
            }
        }
        return receiversList;
    }

    public List<MemberSession> getControllers() {
        return memberSessions.values().stream().filter(memberSession -> memberSession.getMemberType().equals(MemberType.CONTROLLER)).collect(Collectors.toList());
    }

    private void notifyReceivers() throws JsonProcessingException {
        SetReceiver setReceiver = new SetReceiver();
        setReceiver.setMessageType(MessageType.SET_RECEIVER);
        if (mediaPlayer != null) {
            setReceiver.setDeviceName(getMemberSessionDeviceName(mediaPlayer.getWebSocketSession()));
        } else {
            setReceiver.setDeviceName(null);
        }

        TextMessage message = new TextMessage(objectMapper.writeValueAsString(setReceiver));
        getReceiversNames().forEach(receiver -> {
            try {
                getMemberSession(receiver).getWebSocketSession().sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        getControllers().forEach(controller -> {
            try {
                controller.getWebSocketSession().sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
