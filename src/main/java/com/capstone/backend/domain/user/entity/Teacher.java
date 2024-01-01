package com.capstone.backend.domain.user.entity;

import com.capstone.backend.domain.user.entity.Child;
import com.capstone.backend.domain.user.entity.Manage;
import com.capstone.backend.domain.user.entity.Role;
import com.capstone.backend.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "TEACHERS")
@Entity
@Getter
@Setter
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

    public void setUser(User user) {
        this.user = user;
        user.setRole(Role.TEACHER);
    }

    public void addManageParent(Parent parent) {
        if (manageParents == null) {
            manageParents = new ArrayList<>();
        }
        manageParents.add(parent);
        parent.addManageTeacher(this);
    }
}
