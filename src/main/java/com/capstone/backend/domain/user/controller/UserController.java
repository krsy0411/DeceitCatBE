package com.capstone.backend.domain.user.controller;

import com.capstone.backend.domain.user.dto.UserSignUpDto;
import com.capstone.backend.domain.user.entity.Role;
import com.capstone.backend.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}