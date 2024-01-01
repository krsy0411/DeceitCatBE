package com.capstone.backend.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    GUEST("GUEST"), PARENT("PARENT"), TEACHER("TEACHER");
    private final String key;
}
