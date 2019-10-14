package com.example.storefrontdemo.domain.forms;

import com.example.storefrontdemo.domain.enums.CreditCardType;

public class CreditCardForm {

    private CreditCardType creditCardType;
    private String displayCardNumber;
    private String displayExpMonth;
    private String displayExpYear;
    private String displayVerificationCode;

    public CreditCardForm() {
        super();
    }

    public CreditCardType getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(CreditCardType creditCardType) {
        this.creditCardType = creditCardType;
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
}
