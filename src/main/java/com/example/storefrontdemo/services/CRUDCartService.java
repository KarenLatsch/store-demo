package com.example.storefrontdemo.services;

import com.example.storefrontdemo.domain.entities.Customer;

import java.util.List;

public interface CRUDCartService<T> {
    List<?> listAll();

    T findByCustomerId(Integer customerId);

    void createCart(Customer customer);

    T calculateCartQty(Integer customerId, T domainObject);

    T getById(Integer id);

    T saveOrUpdate(T domainObject);

    void delete(Integer id);
}
