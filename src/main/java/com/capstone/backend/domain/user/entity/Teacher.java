package com.capstone.backend.domain.user.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Table(name = "TEACHERS")
@AllArgsConstructor
@Entity
public class Teacher extends User {
    @Column
    private String teacherSchool; // 선생님 학교

    @Column
    private String teacherClass; // 선생님 반

    @Column
    private int manageNum; // 관리 그룹 학부모 수

    @OneToMany(mappedBy = "teacher")
    private List<Manage> manages;

    @OneToMany(mappedBy = "teacher")
    private List<Child> childs;
}
