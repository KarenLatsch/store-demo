package com.example.storefrontdemo.domain.enums;

public enum ProductStatus {
    NEW ("New"),
    AVAILABLE ("Available"),
    DISCONTINUED ("Discontinued");

    private final String name;

    ProductStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
