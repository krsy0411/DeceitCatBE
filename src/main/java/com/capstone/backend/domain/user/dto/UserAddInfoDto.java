package com.capstone.backend.domain.user.dto;

import com.capstone.backend.domain.user.entity.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserAddInfoDto {
    private Role role; // PARNET, TEACHER
    private String teacherSchool;
    private String teacherClass;
    private int childNum;
    private String childName;
    private String childSchool;
    private String childClass;
}
