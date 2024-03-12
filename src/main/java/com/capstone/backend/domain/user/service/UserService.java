package com.capstone.backend.domain.user.service;

import com.capstone.backend.domain.user.dto.UserDto;
import com.capstone.backend.domain.user.entity.Parent;
import com.capstone.backend.domain.user.entity.Role;
import com.capstone.backend.domain.user.entity.Teacher;
import com.capstone.backend.domain.user.entity.User;
import com.capstone.backend.domain.user.repository.ParentRepository;
import com.capstone.backend.domain.user.repository.TeacherRepository;
import com.capstone.backend.domain.user.repository.UserRepository;
import com.capstone.backend.global.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ParentRepository parentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void signUp(UserDto userDto) throws Exception {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .role(Role.GUEST)
                .build();

        user.passwordEncode(passwordEncoder);
        userRepository.save(user);
    }

    public void addInfo(UserDto userDto, String accessToken) throws Exception {
        try {
            User user = validateAccessTokenAndGetUser(accessToken);

            if (user.getRole() == Role.GUEST) {
                if (userDto.getRole() == Role.PARENT) { // role == PARENT
                    Parent parent = new Parent(user, userDto.getChildNum());
                    parentRepository.save(parent);

                    user.setRole(Role.PARENT);
                    userRepository.save(user);
                } else if (userDto.getRole() == Role.TEACHER) { // role == TEACHER
                    Teacher teacher = new Teacher(
                            user,
                            userDto.getTeacherSchool(),
                            userDto.getTeacherClass()
                    );
                    teacherRepository.save(teacher);

                    user.setRole(Role.TEACHER);
                    userRepository.save(user);
                } else {
                    throw new Exception("이미 유저 구분이 설정되었습니다.");
                }

            } else {
                throw new Exception("해당 이메일을 가진 사용자를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

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