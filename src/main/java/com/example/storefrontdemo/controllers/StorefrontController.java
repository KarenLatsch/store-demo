package com.example.storefrontdemo.controllers;

import com.example.storefrontdemo.domain.entities.Cart;
import com.example.storefrontdemo.domain.entities.Order;
import com.example.storefrontdemo.domain.entities.Product;
import com.example.storefrontdemo.domain.enums.ProductStatus;
import com.example.storefrontdemo.domain.forms.BuyProductForm;
import com.example.storefrontdemo.services.CartService;
import com.example.storefrontdemo.services.CustomerService;
import com.example.storefrontdemo.services.OrderService;
import com.example.storefrontdemo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@RequestMapping("/storefront")
@Controller
public class StorefrontController {

    private CustomerService customerService;
    private CartService cartService;
    private ProductService productService;
    private OrderService orderService;

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;  }

    //  Storefront without customer logged in
    @GetMapping("/listproducts")
    public String listProducts(
            Model model
    ) {
        List<Product> products = productService.findProductsAvailable(ProductStatus.AVAILABLE);
        Collections.sort(products);
        model.addAttribute("products", products);
        model.addAttribute("customerCartQty", 0);
        return "storefront/listproducts";
    }

//  Storefront with Customer logged in
    @GetMapping("/listproducts/{customerId}")
    public String listProductsAndCustomerInfo(
            @PathVariable Integer customerId,
            Model model
    ){
        List<Product> products = productService.findProductsAvailable(ProductStatus.AVAILABLE);
        Collections.sort(products);
        model.addAttribute("products", products);
        model.addAttribute("customer", customerService.getById(customerId));
        model.addAttribute("cart", cartService.findByCustomerId(customerId));

        Cart cart = cartService.findByCustomerId(customerId);
        model.addAttribute("customerCartQty", cart.getQuantity());
        return "storefront/listproducts";
    }

    @GetMapping("/viewproduct/{id}")
    public String viewProducts(
            @PathVariable Integer id,
            @ModelAttribute(name = "buyproductform") BuyProductForm buyProductForm,
            HttpSession session,
            Model model) {

        Product product = productService.getById(id);
        if(product == null){
            List<Product> products = productService.findProductsAvailable(ProductStatus.AVAILABLE);
            Collections.sort(products);
            model.addAttribute("products", products);

            Integer customerId = (Integer) session.getAttribute("loggedInCustomerId");
            if(customerId != null) {
                Cart cart = cartService.findByCustomerId(customerId);
                model.addAttribute("customerCartQty", cart.getQuantity());
                return "redirect:/storefront/listproducts/" + (customerId);
            } else {
                return "storefront/listproducts";
            }
        }
        model.addAttribute("product", productService.getById(id));
        buyProductForm.setQuantity(1);

        Integer customerId = (Integer) session.getAttribute("loggedInCustomerId");
        if(customerId != null) {
        Cart cart = cartService.findByCustomerId(customerId);
        model.addAttribute("customerCartQty", cart.getQuantity());
    }
        return "/storefront/viewproduct";
    }

    @GetMapping("/viewaccount/{customerId}")
    public String listOrders(
            @PathVariable Integer customerId,
            Model model,
            HttpSession session
    ) {
        Integer loggedInCustomerId = (Integer) session.getAttribute("loggedInCustomerId");
        if(customerId != loggedInCustomerId){
            model.addAttribute("unauthorizedError", "Unauthorized request, please login");
            return "/storefront/unauthorized";
        }
        List<Order> orders = orderService.findOrdersByCustomerId(customerId);
        Collections.sort(orders, Collections.reverseOrder());

        model.addAttribute("orders", orders);
        model.addAttribute("genericHeader", "Order List");

        Cart cart = cartService.findByCustomerId(customerId);
        if(cart != null) {
            model.addAttribute("customerCartQty", cart.getQuantity());
        }
        return "/storefront/viewaccount";
    }

    @GetMapping("/vieworderdetail/{id}")
    public String viewOrderDetail(
            @PathVariable Integer id,
            Model model,
            HttpSession session
    ){
        model.addAttribute("order", orderService.getById(id));
        model.addAttribute("genericHeader", "Order Detail");

//     get customer
        Integer customerId = (Integer) session.getAttribute("loggedInCustomerId");
        if(customerId != null) {
            Cart cart = cartService.findByCustomerId(customerId);
            model.addAttribute("customerCartQty", cart.getQuantity());
        }
        return "/storefront/vieworderdetail";
    }
}