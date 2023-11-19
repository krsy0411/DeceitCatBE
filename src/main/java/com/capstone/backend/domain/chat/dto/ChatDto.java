package com.capstone.backend.domain.chat.dto;

import lombok.Builder;
import lombok.Data;

import java.lang.invoke.CallSite;

@Data
@Builder
public class ChatDto {
    public enum MessageType {
        ENTER, CHAT, LEAVE
    }
    private MessageType type; // 메세지 타입
    private String roomId; // 방 번호
    private String sender; // 채팅을 보낸 사람
    private String message; // 채팅 메세지
    private String time; // 채팅 발송 시간
}
