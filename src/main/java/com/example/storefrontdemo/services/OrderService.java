package com.example.storefrontdemo.services;

import com.example.storefrontdemo.domain.entities.Customer;
import com.example.storefrontdemo.domain.entities.Order;
import com.example.storefrontdemo.domain.forms.CreditCardForm;

import java.util.List;

public interface OrderService extends CRUDService<Order> {

     List<Order> findOrdersByLastNameIsLike(String lastName);

    List<Order> findOrdersByCustomerId(Integer CustomerId);

    String setUpdatedField(Order order, Order passedInOrder);

    Boolean checkOnHandQtyForOrder(Order order);

    Order createOrder(Customer customer, CreditCardForm creditCardForm, Integer customerId);
}
