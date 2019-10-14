package com.example.storefrontdemo.services.Repository;

import com.example.storefrontdemo.domain.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepoServiceRepository extends JpaRepository<Customer, Integer> {

        Customer findByUsername(String username);
}
