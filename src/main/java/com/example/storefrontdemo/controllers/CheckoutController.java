package com.example.storefrontdemo.controllers;

import com.example.storefrontdemo.domain.entities.Cart;
import com.example.storefrontdemo.domain.entities.CreditCard;
import com.example.storefrontdemo.domain.entities.Customer;
import com.example.storefrontdemo.domain.entities.Order;
import com.example.storefrontdemo.domain.enums.CreditCardType;
import com.example.storefrontdemo.domain.forms.CreditCardForm;
import com.example.storefrontdemo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RequestMapping("/checkout")
@Controller
public class CheckoutController {
    private CustomerService customerService;
    private CartService cartService;
    private CartDetailService cartDetailService;
    private CreditCardService creditCardService;
    private OrderService orderService;
    private ProductService productService;

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Autowired
    public void setCartDetailService(CartDetailService cartDetailService) {
        this.cartDetailService = cartDetailService;
    }

    @Autowired
    public void setCreditCardService(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }
    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{customerId}")
    public String checkoutForm(
            @PathVariable Integer customerId,
            @ModelAttribute(name = "creditcardform") CreditCardForm creditcardForm,
            Model model,
            HttpSession session
    ){
        Integer loggedInCustomerId = (Integer) session.getAttribute("loggedInCustomerId");
        if(customerId != loggedInCustomerId){
            model.addAttribute("unauthorizedError", "Unauthorized request, please login");
            return "/storefront/unauthorized";
        }

        Cart cart = cartService.findByCustomerId(customerId);
        model.addAttribute("customerCartQty", cart.getQuantity());
        if(cart.getQuantity() < 1) {
            if(customerId != null) {
                return "redirect:/storefront/listproducts/" + (customerId);
            } else {
                return "storefront/listproducts";
            }
        }
        model.addAttribute("customer", customerService.getById(customerId));
        Customer customer = customerService.getById(customerId);

        if(customer.getCreditCards().size()  > 0) {
            CreditCard creditCard = creditCardService.findByCustomerIdAndCreditCardId(customerId, customer.getCreditCards().get(0).getId());
            model.addAttribute("creditCard", creditCardService.findByCustomerIdAndCreditCardId(customerId, customer.getCreditCards().get(0).getId()));
        } else {
            model.addAttribute("creditCard", new CreditCard());
        }
        model.addAttribute("creditCardTypes", CreditCardType.values());
        return "/checkout/checkoutform";
    }

    @PostMapping("/{customerId}")
    public String validateCheckoutForm(
            @PathVariable Integer customerId,
            Customer passedInCustomer,
            CreditCard creditCard,
            @ModelAttribute(name = "creditcardform") CreditCardForm creditcardForm,
            Model model,
            HttpSession session
    ){
        String errorMessage = null;

        Integer loggedInCustomerId = (Integer) session.getAttribute("loggedInCustomerId");
        if(customerId != loggedInCustomerId){
            model.addAttribute("unauthorizedError", "Unauthorized request, please login");
            return "/storefront/unauthorized";
        }
        String displayCreditCard = creditcardForm.getDisplayCardNumber();
        String displayCard = displayCreditCard.substring(0, 12);
        if(!displayCard.equals("XXXXXXXXXXXX")) {
            errorMessage = creditCardService.validateCreditCard(creditcardForm);
        }

        if (errorMessage != null) {
            model.addAttribute("creditCardError", errorMessage);
            model.addAttribute("creditCardTypes", CreditCardType.values());
            creditCard.setCreditCardType(creditcardForm.getCreditCardType());

            Cart cart = cartService.findByCustomerId(customerId);
            model.addAttribute("customerCartQty", cart.getQuantity());
            return "/checkout/checkoutform";
        }

        Order order = orderService.createOrder(passedInCustomer, creditcardForm, customerId);

        if(order != null){
            Order savedOrder = orderService.saveOrUpdate(order);
            Customer customer = customerService.getById(customerId);
            cartDetailService.deleteAll(customer);
            cartService.saveOrUpdate(customer.getCart());
            productService.updateProductQtyForNewOrder(savedOrder);
            return "/checkout/checkoutcomplete";
        }
        return "redirect:/storefront/list/" +  (customerId);
    }
}