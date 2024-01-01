package com.capstone.backend.domain.user.entity;

import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.A;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "PARENTS")
@Entity
@Getter
@Setter
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @OneToMany(mappedBy = "parent")
    private List<Child> children = new ArrayList<>(); // 본인 자녀

    @ManyToMany(mappedBy = "manageParents")
    private List<Teacher> manageTeachers; // 자녀의 선생님 목록

    public void setUser(User user) {
        this.user = user;
        user.setRole(Role.PARENT);
    }

    public void addChild(Child child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
        child.setParent(this);
    }

    public void addManageTeacher(Teacher teacher) {
        if (manageTeachers == null) {
            manageTeachers = new ArrayList<>();
        }
        manageTeachers.add(teacher);
        teacher.addManageParent(this);
    }
}
