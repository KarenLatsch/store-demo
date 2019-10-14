package com.example.storefrontdemo.domain.enums;

public enum OrderStatus {
    NEW ("New"),
    PROCESSING ("Processing"),
    CANCELLED ("Cancelled"),
    SHIPPED("Shipped");

    private final String name;

    OrderStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}