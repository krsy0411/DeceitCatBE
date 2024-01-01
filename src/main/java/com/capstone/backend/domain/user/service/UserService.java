package com.capstone.backend.domain.user.service;

import com.capstone.backend.domain.user.entity.User;
import com.capstone.backend.domain.user.repository.UserRepository;
import com.capstone.backend.global.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

//    public void signUp(UserSignUpDto userSignUpDto) throws Exception {
//        if (userRepository.findByEmail(userSignUpDto.getEmail()).isPresent()) {
//            throw new Exception("이미 존재하는 이메일입니다.");
//        }
//
//        User user = User.builder()
//                .name(userSignUpDto.getName())
//                .email(userSignUpDto.getEmail())
//                .password(userSignUpDto.getPassword())
//                .role(Role.GUEST)
//                .build();
//
//        user.passwordEncode(passwordEncoder);
//        userRepository.save(user);
//    }

//    public void addInfo(UserAddInfoDto userAddInfoDto, String accessToken) throws Exception {
//        User user = validateAccessTokenAndGetUser(accessToken);
//
//        if (user.getRole() == Role.GUEST) {
//            if (userAddInfoDto.getRole() == Role.PARENT) { // role == PARENT
//                Parent parent = Parent.builder()
//                        .childNum(userAddInfoDto.getChildNum())
//                        .build();
//                userRepository.save(parent);
//
//                user.setRole(Role.PARENT);
//                userRepository.save(user);
//            } else if (userAddInfoDto.getRole() == Role.TEACHER) { // role == TEACHER
//                Teacher teacher = Teacher.builder()
//                        .teacherSchool(userAddInfoDto.getTeacherSchool())
//                        .teacherClass(userAddInfoDto.getTeacherClass())
//                        .manageNum(0)
//                        .build();
//                userRepository.save(teacher);
//
//                user.setRole(Role.TEACHER);
//                userRepository.save(user);
//            } else {
//                throw new Exception("이미 유저 구분이 설정되었습니다.");
//            }
//
//        } else {
//            throw new Exception("해당 이메일을 가진 사용자를 찾을 수 없습니다.");
//        }
//    }

    public User validateAccessTokenAndGetUser(String accessToken) throws Exception {
        if (jwtService.isTokenValid(accessToken)) {
            Optional<String> extractedEmail = jwtService.extractEmail(accessToken);
            if (extractedEmail.isPresent()) {
                // 여기서 userRepository.findByEmail()을 통해 사용자를 찾고 반환합니다.
                return userRepository.findByEmail(extractedEmail.get())
                        .orElseThrow(() -> new Exception("해당 이메일을 가진 사용자를 찾을 수 없습니다."));
            } else {
                throw new Exception("토큰에서 이메일을 추출할 수 없습니다.");
            }
        } else {
            throw new Exception("유효하지 않은 토큰입니다.");
        }
    }
}