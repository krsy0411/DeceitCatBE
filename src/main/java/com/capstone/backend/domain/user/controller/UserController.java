package com.capstone.backend.domain.user.controller;

import com.capstone.backend.domain.user.dto.UserDto;
import com.capstone.backend.domain.user.service.UserService;
import com.capstone.backend.global.jwt.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "유저 관리", description = "회원가입, 추가정보 입력")
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입")
    @PostMapping("/auth/sign-up")
    public String singUp(@RequestBody UserDto userSignUpDto) throws Exception {
//        userService.signUp(userSignUpDto);
        return "회원가입 성공";
    }

//    @Operation(summary = "추가정보 입력")
//    @PostMapping("/auth/add-info")
//    public ResponseEntity<String> addInfo(@RequestBody UserAddInfoDto userAddInfoDto, @RequestHeader("Authorization") String token) {
//        try {
//            userService.addInfo(userAddInfoDto,token);
//            return ResponseEntity.ok("사용자의 추가정보 입력 완료!");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }

//    @PostMapping("/friend-request")
//    public ResponseEntity<String> sendFriendRequest(@RequestParam Long childId, @RequestParam Long teacherId) {
//        try {
//            userService.sendFriendRequest(childId, teacherId);
//            return ResponseEntity.ok("Friend request sent!");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }
//
//    @PostMapping("/approve-friend-request")
//    public ResponseEntity<String> approveFriendRequest(@RequestParam Long teacherId, @RequestParam Long childId, @RequestParam boolean approved) {
//        try {
//            userService.approveFriendRequest(teacherId, childId, approved);
//            return ResponseEntity.ok("Friend request approved!");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }

    @Autowired
    private JwtService jwtService;
    @Operation(summary = "유저 정보 요청")
    @GetMapping("/entry")
    public ResponseEntity<?> getUserInfo(@RequestHeader(name="Authorization") String token) {
        Optional<String> extractedEmail = jwtService.extractEmail(token);
        Optional<String> extractedUsername = jwtService.extractUsername(token);
        Optional<String> extractedRole = jwtService.extractRole(token);
        if (extractedEmail.isPresent() && extractedUsername.isPresent()) {
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("email", extractedEmail.get());
            userInfo.put("username", extractedUsername.get());
            userInfo.put("role", extractedRole.get());
            return ResponseEntity.ok(userInfo);
        } else {
            // 이메일 또는 사용자 이름이 없거나 토큰이 유효하지 않은 경우에 대한 예외 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰, 이메일 또는 사용자 이름이 없습니다.");
        }
    }
}