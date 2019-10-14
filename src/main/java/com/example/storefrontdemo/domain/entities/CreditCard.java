package com.example.storefrontdemo.domain.entities;

import com.example.storefrontdemo.domain.enums.CreditCardType;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class CreditCard extends AbstractDomainClass {

    // @OneToOne(cascade = {CascadeType.ALL})
    @ManyToOne
    private Customer customer;
    private String firstName;
    private String lastName;

    @Embedded
    private Address billingAddress;
    private CreditCardType creditCardType;

    @Transient
    private String cardNumber;
    private String displayCardNumber;
    private String displayExpMonth;
    private String displayExpYear;
    private String displayVerificationCode;

    @Transient
    private String expirationMonth;
    @Transient
    private String expirationYear;
    @Transient
    private String verificationCode;
    private String encryptedCardNumber;
    private String encryptedExpirationMonth;
    private String encryptedExpirationYear;
    private String encryptedVerificationCode;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public CreditCardType getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(CreditCardType creditCardType) {
        this.creditCardType = creditCardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getDisplayCardNumber() {
        return displayCardNumber;
    }

    public void setDisplayCardNumber(String displayCardNumber) {
        this.displayCardNumber = displayCardNumber;
    }

    public String getDisplayExpMonth() {
        return displayExpMonth;
    }

    public void setDisplayExpMonth(String displayExpMonth) {
        this.displayExpMonth = displayExpMonth;
    }

    public String getDisplayExpYear() {
        return displayExpYear;
    }

    public void setDisplayExpYear(String displayExpYear) {
        this.displayExpYear = displayExpYear;
    }

    public String getDisplayVerificationCode() {
        return displayVerificationCode;
    }

    public void setDisplayVerificationCode(String displayVerificationCode) {
        this.displayVerificationCode = displayVerificationCode;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getEncryptedCardNumber() {
        return encryptedCardNumber;
    }

    public void setEncryptedCardNumber(String encryptedCardNumber) {
        this.encryptedCardNumber = encryptedCardNumber;
    }

    public String getEncryptedExpirationMonth() {
        return encryptedExpirationMonth;
    }

    public void setEncryptedExpirationMonth(String encryptedExpirationMonth) {
        this.encryptedExpirationMonth = encryptedExpirationMonth;
    }

    public String getEncryptedExpirationYear() {
        return encryptedExpirationYear;
    }

    public void setEncryptedExpirationYear(String encryptedExpirationYear) {
        this.encryptedExpirationYear = encryptedExpirationYear;
    }

    public String getEncryptedVerificationCode() {
        return encryptedVerificationCode;
    }

    public void setEncryptedVerificationCode(String encryptedVerificationCode) {
        this.encryptedVerificationCode = encryptedVerificationCode;
    }
}
