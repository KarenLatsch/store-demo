package com.example.storefrontdemo.domain.entities;

import com.example.storefrontdemo.domain.enums.ProductStatus;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class Product extends AbstractDomainClass implements Comparable<Product> {

    private String name;
    private String description;

    private ProductStatus productStatus= ProductStatus.NEW;

    private BigDecimal price = BigDecimal.ZERO;;
    private String imageUrl;

    // Total inventory
    private Integer onHand = 0;

    // Amount already in an order (after checkout)
    private Integer allocated = 0;

    // onHand - allocated
    private Integer available = 0;

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

    public ProductStatus getProductStatus() { return productStatus;  }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getOnHand() {
        return onHand;
    }

    public void setOnHand(Integer onHand) {
        this.onHand = onHand;
    }

    public Integer getAllocated() {
        return allocated;
    }

    public void setAllocated(Integer allocated) {
        this.allocated = allocated;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    @Override
    public int compareTo(Product o) {
        return (this.getName())
                .compareTo(o.getName());
    }
}
