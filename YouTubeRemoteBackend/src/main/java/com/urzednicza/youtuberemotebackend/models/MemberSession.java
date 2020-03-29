package com.urzednicza.youtuberemotebackend.models;

import com.urzednicza.youtuberemotebackend.enums.MemberType;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

import java.util.Date;

@Data
public class MemberSession {

    private String deviceName;
    private WebSocketSession webSocketSession;
    private MemberType memberType;
    private Date lastActive;

}
