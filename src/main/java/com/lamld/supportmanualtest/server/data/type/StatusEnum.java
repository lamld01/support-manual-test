package com.lamld.supportmanualtest.server.data.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusEnum {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    BANNED("Banned");

    private final String value;
}
