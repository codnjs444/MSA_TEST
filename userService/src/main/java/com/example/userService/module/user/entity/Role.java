package com.example.userService.module.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

	GUEST("ROLE_GUEST"), USER("ROLE_USER"), ADMIN("ROLE_ADMIN"), READY("ROLE_READY");

    private final String key;
}
