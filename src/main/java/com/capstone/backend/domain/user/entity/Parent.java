package com.capstone.backend.domain.user.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Getter
@SuperBuilder
@Table(name = "PARENTS")
@AllArgsConstructor
@Entity
public class Parent extends User {

    public Parent() {
        super();
    }

    @Column
    private int childNum; // 자녀 수

    @OneToMany(mappedBy = "parent")
    private List<Child> childs;

    public void setChildNum(int childNum) {
        this.childNum = childNum;
    }

}
