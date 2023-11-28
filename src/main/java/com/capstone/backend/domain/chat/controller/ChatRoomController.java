package com.capstone.backend.domain.chat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.capstone.backend.domain.chat.service.ChatService;
import com.capstone.backend.domain.chat.dto.ChatRoom;

import java.util.List;
import java.util.Map;

@Controller
@RestController
@Slf4j
@Tag(name = "채팅방", description = "생성, 조회, 입장")
@RequestMapping("/room")
public class ChatRoomController {
    @Autowired
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    @Operation(summary = "채팅방 생성")
    @PostMapping("/create")
    public ResponseEntity<String> createRoom(@RequestBody Map<String, String> requestBody, RedirectAttributes rttr) {
        String name = requestBody.get("name");
        ChatRoom room = chatService.createChatRoom(name);
        log.info("CREATE ROOM {}", room);
        rttr.addFlashAttribute("roomName", room);

        return ResponseEntity.ok(room.getRoomId());
    }

    @Operation(summary = "채팅방 조회")
    @GetMapping("/list")
    public ResponseEntity<List<ChatRoom>> roomList(Model model) {
        List<ChatRoom> rooms = chatService.findAllRoom();
        log.info("SHOW ALL RoomList {}", rooms);
        return ResponseEntity.ok().body(rooms);
    }

    // 채팅방 입장 화면
    // 파라미터로 넘어오는 roomId 를 확인후 해당 roomId 를 기준으로
    // 채팅방을 찾아서 클라이언트를 chatroom 으로 보낸다.
    @Operation(summary = "채팅방 입장", description = "room Id와 일치하는 채팅방에 클라이언트를 입장시킴")
    @GetMapping("/{roomId}")
    public ResponseEntity<String> enterRoom(@PathVariable String roomId){
        ChatRoom room = chatService.findByRoomId(roomId);
        if (room == null) {
            log.error("Room ID {} does not exist", roomId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found");
        }

        try {
            String roomInfoJson = objectMapper.writeValueAsString(room);
            log.info("ENTER ROOM {}", room.getRoomName());
            return ResponseEntity.ok(roomInfoJson);
        } catch (JsonProcessingException e) {
            log.error("Error converting room info to JSON", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error converting room info to JSON");
        }
    }
}