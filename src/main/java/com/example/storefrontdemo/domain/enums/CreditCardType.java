package com.example.storefrontdemo.domain.enums;

public enum CreditCardType {
    VISA ("Visa"),
    MASTERCARD ("MasterCard"),
    DISCOVER ("Discover");

    private final String name;

    CreditCardType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
