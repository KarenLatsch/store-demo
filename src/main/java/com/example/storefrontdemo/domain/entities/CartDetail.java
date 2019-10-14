package com.example.storefrontdemo.domain.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Entity
public class CartDetail extends AbstractDomainClass implements Comparable<CartDetail> {

    @ManyToOne
    private Cart cart;

    @OneToOne
    private Product product;
    private String imageUrl;
    private String name;
    private String description;
    private Integer quantity = 0;
    private BigDecimal price = BigDecimal.ZERO;
    private BigDecimal totalAmount = BigDecimal.ZERO;

    public Cart getCart() { return cart; }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
   }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateTotalAmount();
    }

    private void calculateTotalAmount(){
        BigDecimal qty = new BigDecimal(this.getQuantity());
        this.setTotalAmount(this.getPrice().multiply(qty));
    }

    @Override
    public int compareTo(CartDetail o) {
        return (this.getProduct())
                .compareTo(o.getProduct());
    }
}