package com.example.storefrontdemo.domain.forms;

public class BuyProductForm {

    private Integer quantity;
    private boolean invalidQty;

    public BuyProductForm() {
        super();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public boolean isInvalidQty() {
        return invalidQty;
    }

    public void setInvalidQty(boolean invalidQty) {
        this.invalidQty = invalidQty;
    }
}
