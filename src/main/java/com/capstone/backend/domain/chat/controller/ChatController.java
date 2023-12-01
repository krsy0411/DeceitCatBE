package com.capstone.backend.domain.chat.controller;

import com.capstone.backend.domain.chat.dto.ChatDto;
import com.capstone.backend.domain.chat.service.ChatService;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "채팅", description = "유저 리스트, 중복 유저")
@Controller
public class ChatController {
    private final SimpMessageSendingOperations template;

    @Autowired
    ChatService service;

    // MessageMapping 을 통해 webSocket 로 들어오는 메시지를 발신
    // 클라이언트에서는 /pub/chat/message 로 요청하게 되고 이것을 controller 가 받아서 처리한다.
    // 처리가 완료되면 구독한 방인 /sub/chat/{roomId} 로 메시지가 전송된다.
    @MessageMapping("/chat/enterUser")
    public void enterUser(@Payload ChatDto chat, SimpMessageHeaderAccessor headerAccessor) {
        // 채팅방 유저 +1
        service.addUser(chat.getRoomId(), chat.getSender());

        // 채팅방에 유저 추가 및 UserUUID 반환
        String userUUID = service.addUser(chat.getRoomId(), chat.getSender());

        // 반환 결과를 socket session 에 userUUID 로 저장
        headerAccessor.getSessionAttributes().put("userUUID", userUUID);
        headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());
        chat.setMessage(chat.getSender() + " 님 입장!");
        template.convertAndSend("/sub/chat/" + chat.getRoomId(), chat);
    }

    @MessageMapping("/chat/message")
    public void sendMessage(@Payload ChatDto chat) {
        log.info("CHAT {}", chat);
        String message = chat.getMessage();

        boolean isHidden = checkMessage(message);
        chat.setHidden(new int[]{isHidden ? 1 : 0}); // hidden 값 설정

        template.convertAndSend("/sub/chat/" + chat.getRoomId(), chat);
    }

    @MessageMapping("/chat/checkMessage")
    public boolean checkMessage(@Payload String message) {
        String baseUrl = "http://43.202.161.139:8888/";
        String requestUrl = baseUrl + message;

        HttpHeaders header = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(header);

        ResponseEntity<ChatDto> responseEntity = new RestTemplate().exchange(
                requestUrl, HttpMethod.GET, entity, ChatDto.class
        );

        // Response에서 modelResult 가져오기
        ChatDto responseBody = responseEntity.getBody();
        int[] modelResult = responseBody.getHidden();

        // ChatDto에 hidden 필드 설정
        chat.setHidden(modelResult);

        return modelResult != null && modelResult.length > 0 && modelResult[0] == 1;
    }

    // 유저 퇴장 시에는 EventListener 을 통해서 유저 퇴장을 확인
    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("DisConnEvent {}", event);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // stomp 세션에 있던 uuid 와 roomId 를 확인해서 채팅방 유저 리스트와 room 에서 해당 유저를 삭제
        String userUUID = (String) headerAccessor.getSessionAttributes().get("userUUID");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");

        log.info("headAccessor {}", headerAccessor);

        // 채팅방 유저 -1
        service.decreaseUser(roomId);

        // 채팅방 유저 리스트에서 UUID 유저 닉네임 조회 및 리스트에서 유저 삭제
        String username = service.getUserName(roomId, userUUID);
        service.deleteUser(roomId, userUUID);

        if (username != null) {
            log.info("User Disconnected : " + username);

            // builder 어노테이션 활용
            ChatDto chat = ChatDto.builder()
                    .type(ChatDto.MessageType.LEAVE)
                    .sender(username)
                    .message(username + " 님 퇴장!")
                    .build();

            template.convertAndSend("/sub/chat/" + chat.getRoomId(), chat);
        }
    }

    // 채팅에 참여한 유저 리스트 반환
    @Operation(summary = "채팅에 참여한 유저 리스트 반환")
    @GetMapping("/chat/userList")
    @ResponseBody
    public List<String> userList(String roomId) {
        return service.getUserList(roomId);
    }

    // 유저 닉네임 중복 확인
    @Operation(summary = "유저 닉네임 중복 확인")
    @GetMapping("/chat/duplicateName")
    @ResponseBody
    public String isDuplicateName(@RequestParam("roomId") String roomId, @RequestParam("username") String username) {
        // 유저 이름 확인
        String userName = service.isDuplicateName(roomId, username);
        log.info("DuplicateName : {}", userName);

        return userName;
    }
}

