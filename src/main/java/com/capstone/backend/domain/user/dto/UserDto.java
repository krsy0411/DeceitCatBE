package com.capstone.backend.domain.user.dto;

import com.capstone.backend.domain.user.entity.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class UserDto {
    private String name;
    private String email;
    private String password;

    private Role role; // GUEST, TEACHER, PARNET

    private String teacherSchool;
    private String teacherClass;

    private int childNum;
    private List<ChildDto> children;

    /* 자녀 수 만큼의 ChildDto 객체를 생성하고 리스트에 추가 */
    public void setChildNum(int childNum) {
        this.childNum = childNum;
        this.children = new ArrayList<>();
        for (int i=0; i<childNum; i++) {
            this.children.add(new ChildDto());
        }
    }
}
