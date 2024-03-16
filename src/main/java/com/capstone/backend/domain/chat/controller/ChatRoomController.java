package com.capstone.backend.domain.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.capstone.backend.domain.chat.service.ChatService;
import com.capstone.backend.domain.chat.dto.ChatRoom;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "채팅방", description = "생성, 조회, 입장")
@RequestMapping("/room")
public class ChatRoomController {
    private final ChatService chatService;

    @Operation(summary = "채팅방 생성")
    @PostMapping("/create")
    public ResponseEntity<Object> createRoom(@RequestBody Map<String, String> requestBody) {
        String name = requestBody.get("name");
        ChatRoom room = chatService.createRoom(name);
        log.debug("채팅방 생성 중... ");

        if (room != null) {
            log.debug("채팅방 생성 성공! {}", room);
            return ResponseEntity.ok().body(Map.of("roomId", room.getRoomId()));
        } else {
            log.error("채팅방 생성 실패 :(");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "채팅방 생성에 실패했습니다. 요청 데이터를 확인해주세요."));
        }
    }

    @Operation(summary = "채팅방 조회")
    @GetMapping("/list")
    public List<ChatRoom> roomList() {
        try {
            List<ChatRoom> rooms = chatService.findAllRoom();
            log.debug("모든 채팅방 조회 {}", rooms);
            return rooms;
        } catch (Exception e) {
            log.error("채팅방 조회 중 에러 발생 : {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "채팅방 조회에 실패했습니다.");
        }
    }
}