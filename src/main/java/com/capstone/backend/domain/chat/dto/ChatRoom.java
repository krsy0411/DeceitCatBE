package com.capstone.backend.domain.chat.dto;

import lombok.Data;
import java.util.HashMap;

@Data
public class ChatRoom {
    private String roomId;
    private String roomName;
    private HashMap<String, String> userList = new HashMap<>();
    public int getUserCount() {
        return userList.size();
    }
}