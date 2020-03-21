package com.urzednicza.youtuberemotebackend.models;

import com.urzednicza.youtuberemotebackend.enums.MemberType;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

@Data
public class MemberSession {

    private WebSocketSession webSocketSession;
    private MemberType memberType;
}
