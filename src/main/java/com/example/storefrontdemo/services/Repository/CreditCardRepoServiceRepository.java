package com.example.storefrontdemo.services.Repository;

import com.example.storefrontdemo.domain.entities.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardRepoServiceRepository extends JpaRepository<CreditCard, Integer> {

    List<CreditCard> findByCustomerId(Integer customerId);

    CreditCard findByCustomerIdAndId(Integer customerId, Integer creditCardId);
  }
