package com.urzednicza.youtuberemotebackend.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urzednicza.youtuberemotebackend.enums.MemberType;
import com.urzednicza.youtuberemotebackend.models.messages.server.CurrentReceiver;
import com.urzednicza.youtuberemotebackend.models.messages.server.Receivers;
import javassist.NotFoundException;
import lombok.extern.log4j.Log4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Log4j
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
        }

        if (memberType.equals(MemberType.CONTROLLER)) {
            emitReceivers();
        }
        notifyReceivers();
    }

    public void removeMemberSession(String deviceName) throws IOException {

        boolean isReceiver = memberSessions.get(deviceName).getMemberType().equals(MemberType.RECEIVER);
        if (memberSessions.get(deviceName).equals(mediaPlayer)) {
            mediaPlayer = null;
        }
        memberSessions.remove(deviceName);
        if (!getReceiversNames().isEmpty() && mediaPlayer == null) {
            mediaPlayer = memberSessions.get(getReceiversNames().get(0));
            if (isReceiver) {
                emitReceivers();
            }
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

    private List<String> getReceiversNames() {
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
        CurrentReceiver currentReceiver = new CurrentReceiver();
        if (mediaPlayer != null) {
            currentReceiver.setDeviceName(getMemberSessionDeviceName(mediaPlayer.getWebSocketSession()));
        } else {
            currentReceiver.setDeviceName(null);
        }

        TextMessage message = new TextMessage(objectMapper.writeValueAsString(currentReceiver));
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

    private void emitReceivers() throws JsonProcessingException {
        Receivers receivers = new Receivers();
        receivers.setReceivers(getReceivers());
        TextMessage receiversMessage = new TextMessage(objectMapper.writeValueAsString(receivers));

        getControllers().forEach(controller -> {
            try {
                controller.getWebSocketSession().sendMessage(receiversMessage);
            } catch (IOException e) {
                log.error("Failed to emit receivers");
            }
        });
    }
}
