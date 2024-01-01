package com.capstone.backend.domain.user.entity;

import lombok.*;

import javax.persistence.*;


@Table(name = "CHILDS")
@Entity
@Getter
@Setter
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Column
    private String childName; // 자녀 이름

    @Column
    private String childSchool; // 자녀 학교

    @Column
    private String childClass; // 자녀 반

//    @Column(name = "teacher_name")
//    private String teacherName; // 선생님 이름
}
