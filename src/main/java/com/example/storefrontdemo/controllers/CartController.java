package com.example.storefrontdemo.controllers;

import com.example.storefrontdemo.domain.entities.Cart;
import com.example.storefrontdemo.domain.entities.CartDetail;
import com.example.storefrontdemo.domain.entities.Customer;
import com.example.storefrontdemo.domain.entities.Product;
import com.example.storefrontdemo.domain.forms.BuyProductForm;
import com.example.storefrontdemo.services.CartDetailService;
import com.example.storefrontdemo.services.CartService;
import com.example.storefrontdemo.services.CustomerService;
import com.example.storefrontdemo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RequestMapping("/cart")
@Controller
public class CartController {

    private CartService cartService;
    private CartDetailService cartDetailService;
    private CustomerService customerService;
    private ProductService productService;

    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Autowired
    public void setCartDetailService(CartDetailService cartDetailService) { this.cartDetailService = cartDetailService; }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list/{cartId}")
    public String listCustomers(
            @PathVariable Integer cartId,
            Model model,
            HttpSession session
    ){
        Integer customerCartId = (Integer) session.getAttribute("loggedInCustomerCartId");
        if(cartId != customerCartId){
            model.addAttribute("unauthorizedError", "Unauthorized request, please login");
            return "/storefront/unauthorized";
        }
        List<CartDetail> cartDetails = cartDetailService.findCartDetailsByCartId(cartId);
        Collections.sort(cartDetails);

        model.addAttribute("cartDetails", cartDetails);
        BigDecimal cartTotalAmount = cartDetailService.calculateCartTotalAmount(customerCartId);
        model.addAttribute("cartTotalAmount", cartTotalAmount);

        Cart cart = cartService.getById(cartId);
        model.addAttribute("customerCartQty", cart.getQuantity());
        return "cart/list";
    }

    @GetMapping("/edit/{cartDetailId}")
    public String edit(
            @PathVariable Integer cartDetailId,
            Model model,
            HttpSession session
    ){
        Integer customerCartId = (Integer) session.getAttribute("loggedInCustomerCartId");
        CartDetail cartDetail = cartDetailService.findCartDetailByCartIdAndCartDetailId(customerCartId, cartDetailId);
        if(cartDetail == null){
            model.addAttribute("unauthorizedError", "Unauthorized request, please login");
            return "/storefront/unauthorized";
        }
        model.addAttribute("cartDetail", cartDetailService.getById(cartDetailId));

        Integer customerId = (Integer) session.getAttribute("loggedInCustomerId");
        Cart cart = cartService.findByCustomerId(customerId);
        model.addAttribute("customerCartQty", cart.getQuantity());
        return "cart/cartdetailform";
    }

    @PostMapping("/edit")
    public String saveOrUpdate(
            CartDetail passedInCartDetail,
            Model model,
            HttpSession session
    ){
        CartDetail cartDetail2 = cartDetailService.getById(passedInCartDetail.getId());
        cartDetail2.setQuantity(passedInCartDetail.getQuantity());
        cartDetailService.saveOrUpdate(cartDetail2);

        Integer customerId = (Integer) session.getAttribute("loggedInCustomerId");
        Cart cart = cartService.calculateCartQty(customerId, cartDetail2.getCart());
        model.addAttribute("customerCartQty", cart.getQuantity());
        return "redirect:/cart/list/" + cart.getId();
    }

    @PostMapping("/add_product_to_cart/{productId}")
    public String addToCart(
            @PathVariable Integer productId,
            @ModelAttribute(name = "buyproductform") BuyProductForm buyProductForm,
            Model model,
            HttpSession session
    ){

        Integer quantity = buyProductForm.getQuantity();

        Integer customerId = (Integer) session.getAttribute("loggedInCustomerId");
        Cart cart =  cartService.findByCustomerId(customerId);

//  Check to see if this product exist in cart detail
        CartDetail cartDetail = cartDetailService.findCartDetailByCartIdAndProductId(cart.getId(), productId);

        if(cartDetail != null) {
            CartDetail cartdetail = cartDetailService.addToCartDetail(customerId, cartDetail, quantity);
            cartDetailService.saveOrUpdate(cartDetail);
        } else {
            Product product = productService.getById(productId);
            CartDetail cartDetail2 = cartDetailService.createCartDetail(customerId, product, quantity);
            cartDetailService.saveOrUpdate(cartDetail2);

        }
        Integer cartQty = cart.getQuantity();
        model.addAttribute("customerCartQty", cartQty);
        return "redirect:/storefront/listproducts/" + (customerId);
    }

    @PostMapping("/delete/{cartDetailId}")
    public String delete(
            @PathVariable Integer cartDetailId,
            HttpSession session,
            Model model
    ){
        Integer customerCartId = (Integer) session.getAttribute("loggedInCustomerCartId");
        CartDetail checkForCartDetail = cartDetailService.findCartDetailByCartIdAndCartDetailId(customerCartId, cartDetailId);
        if(checkForCartDetail == null){
            model.addAttribute("unauthorizedError", "Unauthorized request, please login");
            return "/storefront/unauthorized";
        }
        CartDetail cartDetail = cartDetailService.getById(cartDetailId);

        Integer customerId = (Integer) session.getAttribute("loggedInCustomerId");
        Customer customer = customerService.getById(customerId);

        customer.getCart().removeCartDetail(cartDetail);

        cartDetailService.delete(cartDetailId);
        cartService.saveOrUpdate(customer.getCart());

        return "redirect:/cart/list/" + (customer.getCart().getId());
    }

    @PostMapping("/emptycart/{customerId}")
    public String emptyCart(
            @PathVariable Integer customerId,
            Model model,
            HttpSession session
    ){
        Integer loggedInCustomerId = (Integer) session.getAttribute("loggedInCustomerId");
        if(customerId != loggedInCustomerId){
            model.addAttribute("unauthorizedError", "Unauthorized request, please login");
            return "/storefront/unauthorized";
        }
        Customer customer = customerService.getById(customerId);
        Cart cart =  cartService.findByCustomerId(customerId);
        List<CartDetail> cartDetails = cartDetailService.findCartDetailsByCartId(cart.getId());

        cartDetailService.deleteAll(customer);

        Integer cartQty = cart.getQuantity();
        model.addAttribute("customerCartQty", cartQty);
        return "redirect:/storefront/listproducts/" + (customerId);
    }
}