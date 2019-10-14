package com.example.storefrontdemo.services;

import com.example.storefrontdemo.domain.entities.Cart;
import com.example.storefrontdemo.domain.entities.Customer;

public interface CartService extends CRUDService<Cart> {

        Cart findByCustomerId(Integer customerId);

        void createCart(Customer customer);

        Cart calculateCartQty(Integer customerId, Cart domainObject);
}
