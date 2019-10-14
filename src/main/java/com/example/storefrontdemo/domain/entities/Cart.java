package com.example.storefrontdemo.domain.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart extends AbstractDomainClass {

    @OneToOne
    private Customer customer;

//    must add map by cart else it will look for a join table - dont want that
//    if cart get delete remove all the details as well

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart", orphanRemoval = true)
//    must initialized to a empty array else if you throw a null error
    private List<CartDetail> cartDetails = new ArrayList<>();

    private Integer quantity;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<CartDetail> getCartDetails() {
        return cartDetails;
    }

    public void setCartDetails(List<CartDetail> cartDetails) {
        this.cartDetails = cartDetails;
    }

    public void addCartDetail(CartDetail cartDetail) {
        cartDetails.add(cartDetail);
//        This sets the Cart on the CartDetail
        cartDetail.setCart(this);
//        this.setQuantity(calculateQuantity());
        calculateQuantity();

    }

    public void removeCartDetail(CartDetail cartDetail) {
        cartDetail.setCart(null);
        this.cartDetails.remove(cartDetail);
//        this.setQuantity(calculateQuantity());
        calculateQuantity();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void calculateQuantity() {
        int i;
        Integer newQty = 0;

        for (i = 0; i < cartDetails.size(); i++) {
            newQty += cartDetails.get(i).getQuantity();
        }
        this.setQuantity(newQty);
   }
}