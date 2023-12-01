package com.capstone.backend.domain.user.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "CHILDS")
@AllArgsConstructor
@Entity
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "child_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Column(name = "teacher_name")
    private String teacherName; // 선생님 이름

    @Column
    private String childName; // 자녀 이름

    @Column
    private String childSchool; // 자녀 학교

    @Column
    private String childClass; // 자녀 반
}
