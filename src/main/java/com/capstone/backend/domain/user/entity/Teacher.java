package com.capstone.backend.domain.user.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "TEACHERS")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @Column
    private String teacherSchool; // 학교

    @Column
    private String teacherClass; // 학급

    @OneToMany(mappedBy = "teacher")
    private List<Child> children = new ArrayList<>(); // 소속 학생

    @ManyToMany
    private List<Parent> manageParents; // 학생 부모님 목록

    public Teacher(User user, String teacherSchool, String teacherClass) {
        this.user = user;
        this.user.setRole(Role.TEACHER);
        this.teacherSchool = teacherSchool;
        this.teacherClass = teacherClass;
    }

    public String getTeacherName() {
        return this.user.getName();
    }

    public void addManageParent(Parent parent) {
        if (manageParents == null) {
            manageParents = new ArrayList<>();
        }
        manageParents.add(parent);
        parent.addManageTeacher(this);
    }
}
