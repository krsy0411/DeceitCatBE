package com.capstone.backend.domain.user.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "PARENTS")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    private int childNum;

    @OneToMany(mappedBy = "parent")
    private List<Child> children = new ArrayList<>(); // 본인 자녀

//    @ManyToMany(mappedBy = "manageParents")
    @ManyToMany
    @JoinTable(
            name = "MANAGES",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private List<Teacher> manageTeachers; // 자녀의 선생님 목록

    public Parent(User user, int childNum) {
        this.user = user;
        this.user.setRole(Role.PARENT);
        this.childNum = childNum;
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
