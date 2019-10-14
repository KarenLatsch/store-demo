package com.example.storefrontdemo.controllers;

import com.example.storefrontdemo.domain.entities.Order;
import com.example.storefrontdemo.domain.enums.OrderStatus;
import com.example.storefrontdemo.services.OrderService;
import com.example.storefrontdemo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RequestMapping("/order")
@Controller
@SessionAttributes("loggedInUser")
public class OrderController {

    private OrderService orderService;
    private ProductService productService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public String listOrders(
            @RequestParam(required = false) String customerSearch,
            @RequestParam(required = false) String statusSearch,
            Model model
    ) {
        if(customerSearch == null || customerSearch == "") {
            List<Order> orders = orderService.listAll();
            Collections.sort(orders, Collections.reverseOrder());
            model.addAttribute("orders", orders);
        } else {
            List<Order> orders = orderService.findOrdersByLastNameIsLike(customerSearch);
            Collections.sort(orders, Collections.reverseOrder());
            model.addAttribute("orders", orders);
        }
        model.addAttribute("genericHeader", "Order List");
        return "order/list";
    }

    @GetMapping("/list/{customerId}")
    public String listOrders(
            @PathVariable Integer customerId,
            Model model
    ) {
        model.addAttribute("orders", orderService.findOrdersByCustomerId(customerId));
        model.addAttribute("genericHeader", "Order List");
        return "order/list";
    }

    @GetMapping("/show/{id}")
    public String getOrder(
            @PathVariable Integer id,
            Model model
    ){
        model.addAttribute("order", orderService.getById(id));
        model.addAttribute("genericHeader", "Order Information");
        return "order/show";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Integer id,
            Model model
    ){
        model.addAttribute("order", orderService.getById(id));
        model.addAttribute("orderStatuses", OrderStatus.values());
        model.addAttribute("genericHeader", "Order Update");
        return "order/orderform";
    }

    @PostMapping("/edit")
    public String saveOrUpdateOrder(
            Order passedInOrder,
            Model model
    ){
        String errorMessage = null;

        Order order = orderService.getById(passedInOrder.getId());

        if(
                (passedInOrder.getOrderStatus() == OrderStatus.SHIPPED && order.getOrderStatus() != OrderStatus.SHIPPED) ||
                (passedInOrder.getOrderStatus() == OrderStatus.PROCESSING && order.getOrderStatus() != OrderStatus.PROCESSING)
        ){
            Boolean sufficientQtyToShip = orderService.checkOnHandQtyForOrder(passedInOrder);
            if(sufficientQtyToShip == false){
                errorMessage = "There is insufficient quantity on hand to ship this order.";
            }
        }

        if(errorMessage == null) {
            errorMessage = orderService.setUpdatedField(order, passedInOrder);
        }

        if(errorMessage != null) {
            model.addAttribute("order", orderService.getById(passedInOrder.getId()));
            model.addAttribute("orderStatuses", OrderStatus.values());
            order.setOrderStatus(passedInOrder.getOrderStatus());
            model.addAttribute("orderStatusError", errorMessage);
          return "order/orderform";
        }

        Order savedOrder = orderService.saveOrUpdate(order);
        model.addAttribute("genericHeader", "Order Information");

        if(savedOrder.getOrderStatus() == OrderStatus.SHIPPED){
            productService.updateProductQtyForShippedOrder(savedOrder);
        }
        return "redirect:/order/show/" + savedOrder.getId();
    }
}