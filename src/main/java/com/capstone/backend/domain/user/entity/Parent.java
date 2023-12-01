package com.capstone.backend.domain.user.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Table(name = "PARENTS")
@AllArgsConstructor
@Entity
public class Parent extends User {
    @Column
    private int childNum; // 자녀 수

    @OneToMany(mappedBy = "parent")
    private List<Child> childs;
}
