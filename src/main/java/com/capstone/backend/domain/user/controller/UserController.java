package com.capstone.backend.domain.user.controller;

import com.capstone.backend.domain.user.dto.UserSignUpDto;
import com.capstone.backend.domain.user.entity.Role;
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
    public String singUp(@RequestBody UserSignUpDto userSignUpDto) throws Exception {
        userService.signUp(userSignUpDto);
        return "회원가입 성공";
    }

    @Operation(summary = "추가정보 입력")
    @PostMapping("/auth/add-info")
    public ResponseEntity<String> setUserRole(@RequestParam(required = false) String email, @RequestParam(required = false) String socialId, @RequestParam Role role) {
        try {
            userService.addInfo(email, socialId, role);
            return ResponseEntity.ok("사용자 역할이 성공적으로 업데이트되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Autowired
    private JwtService jwtService;
    @Operation(summary = "유저 정보 요청")
    @GetMapping("/entry")
    public ResponseEntity<?> getUserInfo(@RequestHeader(name="Authorization") String token) {
        Optional<String> extractedEmail = jwtService.extractEmail(token);
        Optional<String> extractedUsername = jwtService.extractUsername(token);

        if (extractedEmail.isPresent() && extractedUsername.isPresent()) {
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("email", extractedEmail.get());
            userInfo.put("username", extractedUsername.get());
            return ResponseEntity.ok(userInfo);
        } else {
            // 이메일 또는 사용자 이름이 없거나 토큰이 유효하지 않은 경우에 대한 예외 처리
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰, 이메일 또는 사용자 이름이 없습니다.");
        }
    }
}