package com.example.storefrontdemo.services.Repository;

import com.example.storefrontdemo.domain.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepoServiceRepository extends JpaRepository<Cart, Integer> {

     Cart findByCustomerId(Integer customerID);
}
