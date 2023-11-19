package com.capstone.backend.controller;

import dto.AuthRequestDTO;
import com.capstone.backend.service.FirebaseAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final FirebaseAuthService firebaseAuthService;

    @Autowired
    public AuthController(FirebaseAuthService firebaseAuthService) {
        this.firebaseAuthService = firebaseAuthService;
    }


    //회원가입
    @PostMapping("/https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=AIzaSyC_L0oUkbuBtgvlclg1HpLySF7I2oB13Og")
    public String signUp(@RequestBody AuthRequestDTO authRequest) {
        try {
            firebaseAuthService.createUserWithEmailAndPassword(authRequest.getEmail(), authRequest.getPassword());
            // 회원 가입 후의 처리 로직 추가
            return "redirect:/login"; // 회원 가입 후 로그인 페이지로 리다이렉트
        } catch (Exception e) {
            // 회원 가입 실패 시 예외 처리 로직 추가
            return "redirect:/signup?error"; // 실패 시 에러를 URL 추가하여 회원 가입 페이지로 리다이렉트
        }
    }
    //로그인
    @PostMapping("/https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyC_L0oUkbuBtgvlclg1HpLySF7I2oB13Og")
    public String login(@RequestBody AuthRequestDTO authRequest) {
        try {
            firebaseAuthService.signInWithEmailAndPassword(authRequest.getEmail(), authRequest.getPassword());
            // 로그인 후의 처리 로직 추가
            return "redirect:/dashboard"; // 로그인 후 페이지로 리다이렉트
        } catch (Exception e) {
            // 로그인 실패 시 예외 처리 로직 추가
            return "redirect:/login?error"; // 실패 시 에러를 URL에 추가하여 로그인 페이지로 리다이렉트
        }
    }
}