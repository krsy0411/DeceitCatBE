package com.capstone.backend.domain.user.entity;

import lombok.*;

import javax.persistence.*;

@Table(name = "MANAGES")
@Entity
public class Manage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Parent parent;

    @Column
    private String childName; // 자녀 정보
}
