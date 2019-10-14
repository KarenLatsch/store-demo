package com.example.storefrontdemo.services;

import com.example.storefrontdemo.domain.entities.CreditCard;
import com.example.storefrontdemo.domain.forms.CreditCardForm;

import java.util.List;

public interface CreditCardService extends CRUDService<CreditCard> {

    List<CreditCard> findByCustomerId(Integer customerId);

    String setUpdatedFields(CreditCard creditCard, CreditCard passedInCreditCard);

    String setNewFields(CreditCard passedInCreditCard);

    String validateCreditCard(CreditCardForm creditCardForm);

    CreditCard findByCustomerIdAndCreditCardId(Integer customerId, Integer creditCardId);
}