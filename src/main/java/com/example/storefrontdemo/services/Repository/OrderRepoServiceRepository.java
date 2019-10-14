package com.example.storefrontdemo.services.Repository;

import com.example.storefrontdemo.domain.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepoServiceRepository extends JpaRepository<Order, Integer> {

    List<Order> findOrdersByLastNameIsLike(String lastName);

    List<Order> findOrdersByCustomerId(Integer CustomerId);
}