package com.lamld.supportmanualtest.server.data.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UpdateTypeEnum {
    ADD("Add"),
    SUBTRACT("Subtract");

    private final String value;
}
