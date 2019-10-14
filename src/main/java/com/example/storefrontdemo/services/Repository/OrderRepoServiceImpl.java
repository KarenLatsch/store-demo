package com.example.storefrontdemo.services.Repository;

import com.example.storefrontdemo.domain.entities.*;
import com.example.storefrontdemo.domain.enums.OrderStatus;
import com.example.storefrontdemo.domain.forms.CreditCardForm;
import com.example.storefrontdemo.services.CartDetailService;
import com.example.storefrontdemo.services.CustomerService;
import com.example.storefrontdemo.services.OrderDetailService;
import com.example.storefrontdemo.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Profile("jpadao")
public class OrderRepoServiceImpl implements OrderService {

    private OrderRepoServiceRepository orderRepoServiceRepository;
    private OrderDetailService orderDetailService;
    private CartDetailService cartDetailService;
    private CustomerService customerService;

    @Autowired
    public void setOrderRepoServiceRepository(OrderRepoServiceRepository orderRepoServiceRepository) {
        this.orderRepoServiceRepository = orderRepoServiceRepository;
    }

    @Autowired
    public void setOrderDetailService(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @Autowired
    public void setCartDetailService(CartDetailService cartDetailService) {
        this.cartDetailService = cartDetailService;
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public List<Order> listAll() {
        return this.orderRepoServiceRepository.findAll();
    }

    @Override
    public List<Order> findOrdersByLastNameIsLike(String lastName) {
        return this.orderRepoServiceRepository.findOrdersByLastNameIsLike(lastName);
    }

    @Override
    public List<Order> findOrdersByCustomerId(Integer customerId) {
        return this.orderRepoServiceRepository.findOrdersByCustomerId(customerId);
    }

    @Override
    public Order getById(Integer id) {
        Optional<Order> optionalOrder = this.orderRepoServiceRepository.findById(id);
        return optionalOrder.get();
    }

    @Override
    public String setUpdatedField(Order order, Order passedInOrder) {
        String message = null;
        OrderStatus passedInOrderStatus = passedInOrder.getOrderStatus();
        OrderStatus currentOrderStatus = order.getOrderStatus();

            switch (currentOrderStatus) {
                case CANCELLED:
                    if(passedInOrderStatus != OrderStatus.CANCELLED) {
                        message = "Order has been Cancelled, status can not be changed";
                    }
                    break;
                case SHIPPED:
                    if(passedInOrderStatus != OrderStatus.SHIPPED) {
                        message = "Order has been Shipped, status can not be changed";
                    }
                    break;
                case NEW:
                    if(passedInOrderStatus == OrderStatus.NEW){
                        message = "Order has been already been set to NEW, status can not be changed";
                    }
                    order.setOrderStatus(passedInOrder.getOrderStatus());
                    break;
                case PROCESSING:
                    if(passedInOrderStatus == OrderStatus.NEW){
                        message = "Order is being Processed, status can not be changed to NEW";
                    }
                    order.setOrderStatus(passedInOrder.getOrderStatus());
                    break;
                default:
                    break;
            }
        return message;
    }

    @Override
    public Order saveOrUpdate(Order domainObject) {
        return this.orderRepoServiceRepository.save(domainObject);
    }

    @Override
    public void delete(Integer id) {
        orderRepoServiceRepository.deleteById(id);
    }

    @Override
    public Boolean checkOnHandQtyForOrder(Order order) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(order.getId());

        for (int i = 0; i < orderDetails.size(); i++) {
            if(orderDetails.get(i).getProduct().getOnHand() < orderDetails.get(i).getQuantity()){
                return false;
            }
        }
    return true;
    }

    @Override
    public Order createOrder(Customer passedInCustomer, CreditCardForm creditCardForm, Integer customerId) {

        Customer customer = customerService.getById(customerId);
        List<CartDetail> cartDetails = cartDetailService.findCartDetailsByCartId(customer.getCart().getId());
        Collections.sort(cartDetails);
        Order order = new Order();

        if (cartDetails.isEmpty()) {
            return order;
        }

        order.setCustomer(customer);
        order.setFirstName(passedInCustomer.getFirstName());
        order.setLastName(passedInCustomer.getLastName());
        order.setEmail(passedInCustomer.getEmail());
        order.setPhoneNumber(passedInCustomer.getPhoneNumber());
        order.setDateOrdered(new Date());
        order.setShipToAddress(new Address());
        order.setShipToAddress(passedInCustomer.getShippingAddress());

        String displayCardNum = creditCardForm.getDisplayCardNumber().substring(1, 12);
        if(displayCardNum.equals("XXXXXXXXXXXX")) {
            order.setDisplayCardNumber(creditCardForm.getDisplayCardNumber());
        } else {
//                CreditCardType creditCardType = creditCardForm.getCreditCardType();
            String cardNumber = creditCardForm.getDisplayCardNumber();
            String x = "XXXXXXXXXXXX";
            String last4Digits = cardNumber.substring(11, 15);
            order.setDisplayCardNumber(x + last4Digits);
        }

//   mark as Shipped.
        order.setOrderStatus(OrderStatus.NEW);

        //  add in each Product
        cartDetails.forEach(cartDetail -> {
//  create a orderDetail set that product to it
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(cartDetail.getProduct());
            orderDetail.setName(cartDetail.getName());
            orderDetail.setDescription(cartDetail.getDescription());
            orderDetail.setPrice(cartDetail.getPrice());
            orderDetail.setQuantity(cartDetail.getQuantity());
            orderDetail.setTotalAmount(cartDetail.getTotalAmount());
//  add that orderDetail to the Order
            order.addToOrderDetails(orderDetail);
        });

        return order;
    }

}