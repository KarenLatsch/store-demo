package com.example.storefrontdemo.services;

import com.example.storefrontdemo.domain.entities.Customer;

public interface CustomerService extends CRUDService<Customer>{

    Customer loginCustomer(String Username, String password);

    Customer findByUsername(String username);

    String setUpdatedFields(Customer customer, Customer passedInCustomer);

    String setNewFields(Customer passedInCustomer);
}