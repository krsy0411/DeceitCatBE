package com.capstone.backend.domain.user.service;

import com.capstone.backend.domain.user.dto.UserDto;
import com.capstone.backend.domain.user.entity.*;
import com.capstone.backend.domain.user.repository.ParentRepository;
import com.capstone.backend.domain.user.repository.TeacherRepository;
import com.capstone.backend.domain.user.repository.UserLoginCountRepository;
import com.capstone.backend.domain.user.repository.UserRepository;
import com.capstone.backend.global.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ParentRepository parentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final UserLoginCountRepository userLoginCountRepository;

    private static final String USER_NOT_FOUND_MESSAGE = "해당 사용자를 찾을 수 없습니다: ";

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


    public Map<String, Object> loginUser(String email, String password) {

        User user = validateUserExistsByEmail(email);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 틀립니다.");
        }

        updateLoginCount(user);
        return generateTokens(user);
    }


    private Map<String, Object> generateTokens(User user) {

        Map<String, Object> tokens = new HashMap<>();
        tokens.put("accessToken", jwtService.createAccessToken(user.getEmail(), user.getName(), user.getRole()));
        tokens.put("refreshToken", jwtService.createRefreshToken());
        return tokens;
    }


    public void updateLoginCount(User user) {
        LocalDate today = getToday();
        int weekOfYear = today.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int year = today.getYear();

        UserLoginCount userLoginCount = userLoginCountRepository
                .findByUserAndLoginYearAndLoginWeek(user, year, weekOfYear)
                .orElseGet(() -> createUserLoginCount(user, today, year, weekOfYear));

        userLoginCount.incrementCount();
        userLoginCountRepository.save(userLoginCount);
    }

    private UserLoginCount createUserLoginCount(User user, LocalDate loginDate, int loginYear, int loginWeek) {
        return UserLoginCount.builder()
                .user(user)
                .loginDate(loginDate)
                .loginYear(loginYear)
                .loginWeek(loginWeek)
                .build();
    }

    // 사용자별 주간 로그인 횟수 조회
    public long getUserWeeklyLoginCount(long userId) {
        LocalDate today = getToday();
        isValidUser(userId);
        return userLoginCountRepository.sumLoginCountsByUserAndYearAndWeek(userId, today.getYear(), today.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR))
                .orElse(0);
    }

    // 전체 사용자의 주간 로그인 횟수 조회
    public long getTotalWeeklyLoginCount() {
        LocalDate today = getToday();
        return userLoginCountRepository.sumTotalLoginCountsByYearAndWeek(today.getYear(), today.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR))
                .orElse(0);
    }

    // 전체 사용자 수 조회
    public long getTotalUserCount() {
        return userRepository.count();
    }

    private void isValidUser(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException((USER_NOT_FOUND_MESSAGE + userId)));
    }

    private User validateUserExistsByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException((USER_NOT_FOUND_MESSAGE + email)));
    }

    private LocalDate getToday() {
        return LocalDate.now();
    }

}