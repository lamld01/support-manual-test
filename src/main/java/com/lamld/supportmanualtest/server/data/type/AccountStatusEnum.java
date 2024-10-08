package com.lamld.supportmanualtest.server.data.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountStatusEnum {
    NEW("New"),
    PROFILE_DONE("Profile Done"),
    ACTIVE("Active"),
    BANNED("Banned");

    private final String value;
}
