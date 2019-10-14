package com.example.storefrontdemo.domain.entities;

import com.example.storefrontdemo.domain.enums.OrderStatus;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table( name = "ORDER_HEADER" )
public class Order extends AbstractDomainClass implements Comparable<Order> {

    @ManyToOne
    private Customer customer;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    @Embedded
    private Address shipToAddress;
    private String displayCardNumber;

    // @Size( min = 1 )
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    private OrderStatus orderStatus;

    @DateTimeFormat(pattern = "dd-mm-yyyy")
    private Date dateShipped;
    @DateTimeFormat(pattern = "dd-mm-yyyy")
    private Date dateOrdered;

    private BigDecimal totalAmount;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getShipToAddress() {
        return shipToAddress;
    }

    public void setShipToAddress(Address shipToAddress) {
        this.shipToAddress = shipToAddress;
    }

    public String getDisplayCardNumber() {
        return displayCardNumber;
    }

    public void setDisplayCardNumber(String displayCardNumber) {
        this.displayCardNumber = displayCardNumber;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void addToOrderDetails(OrderDetail orderDetail) {
        orderDetail.setOrder(this);
        orderDetails.add(orderDetail);
        calculateTotalAmount();
    }

    public void removeOrderDetail(OrderDetail orderDetail) {
        orderDetail.setOrder(null);
        orderDetails.remove(orderDetail);
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
          this.orderStatus = orderStatus;
    }

    public Date getDateShipped() {
        return dateShipped;
    }

    public void setDateShipped(Date dateShipped) {
        this.dateShipped = dateShipped;
    }

    public Date getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(Date dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public int compareTo(Order o) {
        return (this.getDateOrdered() + this.getLastName() + this.getFirstName())
                .compareTo(o.getDateOrdered() + o.getLastName() + o.getFirstName());
    }

    private void calculateTotalAmount() {
        BigDecimal addToTotal = new BigDecimal(0);

        for (int i = 0; i < this.orderDetails.size(); i++) {
            addToTotal = addToTotal.add(this.orderDetails.get(i).getTotalAmount());
        }
        this.setTotalAmount(addToTotal);
    }
}