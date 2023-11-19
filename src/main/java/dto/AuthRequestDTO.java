package dto;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String email;
    private String password;

    // 생성자, 게터, 세터 등 필요한 메서드 추가
}