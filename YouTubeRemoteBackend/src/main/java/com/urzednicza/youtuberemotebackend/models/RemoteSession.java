package com.urzednicza.youtuberemotebackend.models;

import com.urzednicza.youtuberemotebackend.enums.MemberType;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

public class RemoteSession {

    private Map<String, MemberSession> memberSessions;
    private MemberSession mediaPlayer;
    private User user;

    public RemoteSession(User user) {
        this.memberSessions = new HashMap<>();
        this.user = user;
    }

    public void addMemberSession(WebSocketSession webSocketSession, String deviceName, MemberType memberType){
        MemberSession memberSession = new MemberSession();
        memberSession.setMemberType(memberType);
        memberSession.setWebSocketSession(webSocketSession);

        if(memberType.equals(MemberType.RECEIVER) && mediaPlayer == null){
            mediaPlayer = memberSession;
        }
        memberSessions.put(deviceName,memberSession);
    }

    public void removeMemberSession(String deviceName){
        memberSessions.remove(deviceName);
    }

    public MemberSession getMemberSession(String deviceName){
        return memberSessions.get(deviceName);
    }

    public Map<String, MemberSession> getMemberSessions(){
        return memberSessions;
    }

    public User getUser(){
        return user;
    }

    public MemberSession getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MemberSession mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }
}
