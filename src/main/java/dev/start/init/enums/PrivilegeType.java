package dev.start.init.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum PrivilegeType {
    WRITE("WRITE_PRIVILEGE"),
    READ("READ_PRIVILEGE"),
    EDIT("EDIT_PRIVILEGE"),
    DELETE("DELETE_PRIVILEGE");
    private final String name;
}
