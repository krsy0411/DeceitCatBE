package com.capstone.backend.domain.user.entity;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "USERS")
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true) // email 은 중복되지 않아야 하기 때문에 unique 하도록 설정
    private String email; // 이메일

    @Column
    private String password; // 비밀번호

    @Column
    private String nickname; // 닉네임

    @Column
    private String imgUrl; // 프로필 이미지

    @Column
    @Enumerated(EnumType.STRING)
    private Role role; // 유저 역할

    @Column
    @Enumerated(EnumType.STRING)
    private SocialType socialType; // GOOGLE, NAVER, KAKAO

    private String socialId; // 로그인 한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

    private String refreshToken; // 리프레시 토큰

    public void authorizeUser() {
        this.role = Role.USER;
    }

    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }
}
