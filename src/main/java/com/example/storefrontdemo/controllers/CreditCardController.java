package com.example.storefrontdemo.controllers;

import com.example.storefrontdemo.domain.entities.Cart;
import com.example.storefrontdemo.domain.entities.CreditCard;
import com.example.storefrontdemo.domain.entities.Customer;
import com.example.storefrontdemo.domain.enums.CreditCardType;
import com.example.storefrontdemo.services.CartService;
import com.example.storefrontdemo.services.CreditCardService;
import com.example.storefrontdemo.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@RequestMapping("/creditcard")
@Controller
public class CreditCardController {

    private CreditCardService creditCardService;
    private CartService cartService;
    private CustomerService customerService;

    @Autowired
    public void setCreditCardService(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/list/{customerId}")
    public String listCreditCard(
            @PathVariable Integer customerId,
            Model model,
            HttpSession session
    ) {
        Integer loggedInCustomerId = (Integer) session.getAttribute("loggedInCustomerId");
        if(customerId != loggedInCustomerId){
            model.addAttribute("unauthorizedError", "Unauthorized request, please login");
            return "/storefront/unauthorized";
        }
        model.addAttribute("creditcards", creditCardService.findByCustomerId(customerId));

        Cart cart = cartService.findByCustomerId(customerId);
        model.addAttribute("customerCartQty", cart.getQuantity());
        return "creditcard/list";
    }

    @GetMapping("/edit/{creditCardId}")
    public String edit(
            @PathVariable Integer creditCardId,
            Model model,
            HttpSession session
    ) {
        Integer customerId = (Integer) session.getAttribute("loggedInCustomerId");
        CreditCard creditCard = creditCardService.findByCustomerIdAndCreditCardId(customerId, creditCardId);
        if(creditCard == null){
            model.addAttribute("unauthorizedError", "Unauthorized request, please login");
            return "/storefront/unauthorized";
        }
        model.addAttribute("creditCard", creditCardService.getById(creditCardId));
        model.addAttribute("creditCardTypes", CreditCardType.values());

        Cart cart = cartService.findByCustomerId(customerId);
        model.addAttribute("customerCartQty", cart.getQuantity());
        return "creditcard/creditcardform";
    }

    @GetMapping("/new")
    public String newCreditCard(
            Model model,
            HttpSession session
    ) {
        Integer customerId = (Integer) session.getAttribute("loggedInCustomerId");
        if(customerId == null){
            model.addAttribute("unauthorizedError", "Unauthorized request, please login");
            return "/storefront/unauthorized";
        }
        model.addAttribute("creditCard", new CreditCard());
        model.addAttribute("creditCardTypes", CreditCardType.values());

        Cart cart = cartService.findByCustomerId(customerId);
        model.addAttribute("customerCartQty", cart.getQuantity());
        return "creditcard/newcreditcardform";
    }

    @PostMapping("/edit")
    public String saveOrUpdate(
            CreditCard passedInCreditCard,
            HttpSession session,
            Model model
    ) {
        String errorMessage;
        Integer customerId = (Integer) session.getAttribute("loggedInCustomerId");

        if(passedInCreditCard.getId() != null){
            CreditCard creditCard = creditCardService.getById(passedInCreditCard.getId());
            errorMessage = creditCardService.setUpdatedFields(creditCard, passedInCreditCard);
        } else {
            errorMessage = creditCardService.setNewFields(passedInCreditCard);
        }
            if(errorMessage != null) {
                model.addAttribute("creditCardError", errorMessage);
                model.addAttribute("creditCardTypes", CreditCardType.values());
                passedInCreditCard.setCreditCardType(passedInCreditCard.getCreditCardType());

                Cart cart = cartService.findByCustomerId(customerId);
                model.addAttribute("customerCartQty", cart.getQuantity());

                if(passedInCreditCard.getId() != null){
                    return "creditcard/creditcardform";
                } else {
                    return "creditcard/newcreditcardform";
                }
            }

        if (passedInCreditCard.getId() != null) {
            CreditCard creditCard = creditCardService.getById(passedInCreditCard.getId());
            creditCardService.saveOrUpdate(creditCard);
        } else {
            Customer customer = customerService.getById(customerId);
            passedInCreditCard.setCustomer(customer);
            creditCardService.saveOrUpdate(passedInCreditCard);
        }
        return "redirect:/creditcard/list/" + customerId;
    }

    @PostMapping("/delete/{creditCardId}")
    public String delete(
            @PathVariable Integer creditCardId,
            HttpSession session,
            Model model
    ) {
        Integer customerId = (Integer) session.getAttribute("loggedInCustomerId");
        CreditCard creditCard = creditCardService.findByCustomerIdAndCreditCardId(customerId, creditCardId);
        if(creditCard == null){
            model.addAttribute("unauthorizedError", "Unauthorized request, please login");
            return "/storefront/unauthorized";
        }
        creditCardService.delete(creditCardId);
        return "redirect:/creditcard/list/" + customerId;
    }
}