package com.example.storefrontdemo.controllers;

import com.example.storefrontdemo.domain.entities.Cart;
import com.example.storefrontdemo.domain.entities.Customer;
import com.example.storefrontdemo.services.CartService;
import com.example.storefrontdemo.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@RequestMapping("/customer")
@Controller
public class CustomerController {

    private CustomerService customerService;
    private CartService cartService;

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/edit/{customerId}")
    public String edit(
            @PathVariable Integer customerId,
            Model model,
            HttpSession session
    ){
        Integer loggedInCustomerId = (Integer) session.getAttribute("loggedInCustomerId");
        if(customerId != loggedInCustomerId){
            model.addAttribute("unauthorizedError", "Unauthorized request, please login");
            return "/storefront/unauthorized";
        }
        model.addAttribute("customer", customerService.getById(customerId));

        Cart cart = cartService.findByCustomerId(customerId);
        Integer cartQty = cart.getQuantity();
        model.addAttribute("customerCartQty", cartQty);
        return "customer/customerform";
    }

    @GetMapping("/new")
    public String newCustomer(
            Model model
    ){
        model.addAttribute("customer", new Customer());
        return "customer/newcustomerform";
    }

    @PostMapping("/edit")
    public String saveOrUpdateNew(
            Customer passedInCustomer,
            HttpSession session,
            Model model
    ){
        String errorMessage;

        if(passedInCustomer.getId() != null){
            Customer customer = customerService.getById(passedInCustomer.getId());
            errorMessage = customerService.setUpdatedFields(customer, passedInCustomer);
        } else {
            errorMessage = customerService.setNewFields(passedInCustomer);
        }

        if(errorMessage != null) {
            model.addAttribute("customerError", errorMessage);
            if(passedInCustomer.getId() != null){
                return "customer/customerform";
            } else {
                return "customer/newcustomerform";
            }
        }

        if(passedInCustomer.getId() != null){
            Customer customer = customerService.getById(passedInCustomer.getId());
            Customer saveCustomer = customerService.saveOrUpdate(customer);
// SET with updated username
            session.setAttribute("loggedInCustomerUsername", saveCustomer.getUsername());
        } else {
            Customer newCustomer = customerService.saveOrUpdate(passedInCustomer);
            //  Create a cart for new customer
            Cart cart = cartService.findByCustomerId(newCustomer.getId());
            if(cart == null){
                cartService.createCart(newCustomer);
                customerService.saveOrUpdate(passedInCustomer);
            }
        }
        String loggedInCustomer = (String) session.getAttribute("loggedInCustomer");
        if(loggedInCustomer != null) {
            Integer customerId = (Integer) session.getAttribute("loggedInCustomerId");
            return "redirect:/storefront/viewaccount/" + customerId;
        }
        return "redirect:/storefront/listproducts";
    }
}