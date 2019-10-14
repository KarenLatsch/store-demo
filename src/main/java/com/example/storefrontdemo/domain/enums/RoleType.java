package com.example.storefrontdemo.domain.enums;

public enum RoleType {
    ADMIN ("Administrator"),
    MANAGER ("Manager"),
    USER ("User");

    private final String name;

    RoleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
