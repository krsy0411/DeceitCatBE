package com.capstone.backend.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class ChatDto {
    public enum MessageType {
        ENTER, CHAT, LEAVE
    }
    private MessageType type; // 메세지 타입
    private int[] hidden; // 메세지 히든 값 0 == view | 1 == hidden
    private String roomId; // 방 번호
    private String sender; // 채팅을 보낸 사람
    private String message; // 채팅 메세지
    private String time; // 채팅 발송 시간

    @JsonCreator
    public ChatDto(@JsonProperty("type") MessageType type,
                   @JsonProperty("hidden") int[] hidden,
                   @JsonProperty("roomId") String roomId,
                   @JsonProperty("sender") String sender,
                   @JsonProperty("message") String message,
                   @JsonProperty("time") String time) {
        this.type = type;
        this.hidden = hidden;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.time = time;
    }
}
