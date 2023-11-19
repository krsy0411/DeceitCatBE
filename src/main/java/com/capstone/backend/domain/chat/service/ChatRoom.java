package com.capstone.backend.domain.chat.service;

import lombok.Data;
import java.util.HashMap;
import java.util.UUID;

@Data
public class ChatRoom {

    private String roomId; //채팅방 아이디
    private String roomName; //채팅방 이름
    private long userCount; // 채팅방 인원수

    private HashMap<String, String> userList = new HashMap<>();

    public ChatRoom create(String roomId, String RoomName){
        ChatRoom chatRoom = new ChatRoom();
        this.roomId = UUID.randomUUID().toString();
        this.roomName = roomName;

        return chatRoom;
    }
}