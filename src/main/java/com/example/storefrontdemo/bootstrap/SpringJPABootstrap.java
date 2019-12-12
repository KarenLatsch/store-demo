package com.example.storefrontdemo.bootstrap;

import com.example.storefrontdemo.domain.entities.*;
import com.example.storefrontdemo.domain.enums.CreditCardType;
import com.example.storefrontdemo.domain.enums.OrderStatus;
import com.example.storefrontdemo.domain.enums.ProductStatus;
import com.example.storefrontdemo.domain.enums.RoleType;
import com.example.storefrontdemo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class SpringJPABootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private CustomerService customerService;
    private CreditCardService creditCardService;
    private ProductService productService;
    private OrderService orderService;
    private EmployeeService employeeService;

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    public void setCreditCardService(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        loadProducts();
        loadCustomers();
        loadEmployees();
        loadCarts();
        loadOrderHistory();
        loadProcessingOrders();
        loadOpenOrders();
    }

    private void loadOpenOrders() {
        List<Customer> customers = (List<Customer>) customerService.listAll();
        List<Product> products = productService.listAll();
        Collections.sort(products);

        customers.forEach(customer -> {
//  for each user create a new Order and set the Customer to the customer value of that User
            Order order = new Order();
            order.setCustomer(customer);
            order.setFirstName(customer.getFirstName());
            order.setLastName(customer.getLastName());
            order.setEmail(customer.getEmail());
            order.setPhoneNumber(customer.getPhoneNumber());

            order.setDateOrdered(new Date());

            order.setShipToAddress(new Address());
            order.setShipToAddress(customer.getShippingAddress());

            order.setDisplayCardNumber(customer.getCreditCards().get(0).getDisplayCardNumber());

//   mark as Shipped.
            order.setOrderStatus(OrderStatus.NEW);

//  add in each Product
            products.forEach(product -> {
//  create a orderDetail set that product to it
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProduct(product);
                orderDetail.setName(product.getName());
                orderDetail.setDescription(product.getDescription());
                orderDetail.setPrice(product.getPrice());
                orderDetail.setQuantity(3);

//  add that orderDetail to the Order
                order.addToOrderDetails(orderDetail);
            });
            Order savedOrder = orderService.saveOrUpdate(order);
            if(savedOrder.getOrderStatus() == OrderStatus.NEW){
                productService.updateProductQtyForNewOrder(savedOrder);
            }
        });
    }

    private void loadProcessingOrders() {
        List<Customer> customers = (List<Customer>) customerService.listAll();
        List<Product> products = productService.listAll();
        Collections.sort(products);

        customers.forEach(customer -> {
//  for each user create a new Order and set the Customer to the customer value of that User
            Order order = new Order();
            order.setCustomer(customer);
            order.setFirstName(customer.getFirstName());
            order.setLastName(customer.getLastName());
            order.setEmail(customer.getEmail());
            order.setPhoneNumber(customer.getPhoneNumber());

            order.setDateOrdered(new Date());

            order.setShipToAddress(new Address());
            order.setShipToAddress(customer.getShippingAddress());

            order.setDisplayCardNumber(customer.getCreditCards().get(0).getDisplayCardNumber());

//   mark as Shipped.
            order.setOrderStatus(OrderStatus.PROCESSING);

//  add in each Product
            products.forEach(product -> {
//  create a orderDetail set that product to it
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProduct(product);
                orderDetail.setName(product.getName());
                orderDetail.setDescription(product.getDescription());
                orderDetail.setPrice(product.getPrice());
                orderDetail.setQuantity(1);
//  add that orderDetail to the Order
                order.addToOrderDetails(orderDetail);
            });
            orderService.saveOrUpdate(order);
        });
    }

    private void loadOrderHistory() {
        List<Customer> customers = (List<Customer>) customerService.listAll();
        List<Product> products = productService.listAll();
        Collections.sort(products);

        customers.forEach(customer -> {
//  for each user create a new Order and set the Customer to the customer value of that User
            Order order = new Order();
            order.setCustomer(customer);
            order.setFirstName(customer.getFirstName());
            order.setLastName(customer.getLastName());
            order.setEmail(customer.getEmail());
            order.setPhoneNumber(customer.getPhoneNumber());

            order.setDateOrdered(new Date());
            order.setDateShipped(new Date());

            order.setShipToAddress(new Address());
            order.setShipToAddress(customer.getShippingAddress());

            order.setDisplayCardNumber(customer.getCreditCards().get(0).getDisplayCardNumber());

//   mark as Shipped.
            order.setOrderStatus(OrderStatus.SHIPPED);

//  add in each Product
            products.forEach(product -> {
//  create a orderDetail set that product to it
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProduct(product);
                orderDetail.setName(product.getName());
                orderDetail.setDescription(product.getDescription());
                orderDetail.setPrice(product.getPrice());
                orderDetail.setQuantity(1);
//  add that orderDetail to the Order
                order.addToOrderDetails(orderDetail);
            });
            Order savedOrder= orderService.saveOrUpdate(order);

        });
    }

    private void loadCustomers() {
        Customer customer1 = new Customer();
        customer1.setFirstName("Micheal");
        customer1.setLastName("Weston");
        customer1.setEmail("micheal@burnnotice.com");
        customer1.setPhoneNumber("305.333.0101");

        customer1.setShippingAddress(new Address());
        customer1.getShippingAddress().setAddressLine1("1 Main St");
        customer1.getShippingAddress().setCity("Miami");
        customer1.getShippingAddress().setState("Florida");
        customer1.getShippingAddress().setZipCode("33101");

        customer1.setUsername("mweston");
        customer1.setPassword("password");
        customer1.setConfirmPassword("password");

        CreditCard creditCard1 = new CreditCard();
        creditCard1.setFirstName("Micheal");
        creditCard1.setLastName("Weston");
        creditCard1.setBillingAddress(new Address());
        creditCard1.getBillingAddress().setAddressLine1("1 Main St");
        creditCard1.getBillingAddress().setCity("Miami");
        creditCard1.getBillingAddress().setState("Florida");
        creditCard1.getBillingAddress().setZipCode("33101");
        creditCard1.setCreditCardType(CreditCardType.DISCOVER);
        creditCard1.setCardNumber("1111222233334444");
        creditCard1.setExpirationMonth("12");
        creditCard1.setExpirationYear("20");
        creditCard1.setVerificationCode("123");
        creditCard1.setCustomer(customer1);

        customer1.addCreditCard(creditCard1);
        customerService.saveOrUpdate(customer1);
        creditCardService.saveOrUpdate(creditCard1);

        Customer customer2 = new Customer();
        customer2.setFirstName("Stacey");
        customer2.setLastName("Smith");
        customer2.setEmail("stacey@gmail.com");
        customer2.setPhoneNumber("305.323.0233");

        customer2.setShippingAddress(new Address());
        customer2.getShippingAddress().setAddressLine1("1 Key Biscane Ave");
        customer2.getShippingAddress().setCity("Miami");
        customer2.getShippingAddress().setState("Florida");
        customer2.getShippingAddress().setZipCode("33101");

        customer2.setUsername("ssmith");
        customer2.setPassword("password");
        customer2.setConfirmPassword("password");

        CreditCard creditCard2 = new CreditCard();
        creditCard2.setFirstName("Stacey");
        creditCard2.setLastName("Smith");
        creditCard2.setBillingAddress(new Address());
        creditCard2.getBillingAddress().setAddressLine1("1 Key Biscane Ave");
        creditCard2.getBillingAddress().setCity("Miami");
        creditCard2.getBillingAddress().setState("Florida");
        creditCard2.getBillingAddress().setZipCode("33101");
        creditCard2.setCreditCardType(CreditCardType.MASTERCARD);
        creditCard2.setCardNumber("2222333344445555");
        creditCard2.setExpirationMonth("12");
        creditCard2.setExpirationYear("20");
        creditCard2.setVerificationCode("123");
        creditCard2.setCustomer(customer2);
        customer2.addCreditCard(creditCard2);

        CreditCard creditCard22 = new CreditCard();
        creditCard22.setFirstName("Stacey");
        creditCard22.setLastName("Smith");
        creditCard22.setBillingAddress(new Address());
        creditCard22.getBillingAddress().setAddressLine1("1 Key Biscane Ave");
        creditCard22.getBillingAddress().setCity("Miami");
        creditCard22.getBillingAddress().setState("Florida");
        creditCard22.getBillingAddress().setZipCode("33101");
        creditCard22.setCreditCardType(CreditCardType.VISA);
        creditCard22.setCardNumber("2222444455556666");
        creditCard22.setExpirationMonth("12");
        creditCard22.setExpirationYear("20");
        creditCard22.setVerificationCode("123");
        creditCard22.setCustomer(customer2);
        customer2.addCreditCard(creditCard22);

        customerService.saveOrUpdate(customer2);
        creditCardService.saveOrUpdate(creditCard2);
        creditCardService.saveOrUpdate(creditCard22);

        Customer customer3 = new Customer();
        customer3.setFirstName("Sam");
        customer3.setLastName("Axe");
        customer3.setEmail("sam@burnnotice.com");
        customer3.setPhoneNumber("305.426.9832");

        customer3.setShippingAddress(new Address());
        customer3.getShippingAddress().setAddressLine1("1 Little Cuba Road");
        customer3.getShippingAddress().setCity("Miami");
        customer3.getShippingAddress().setState("Florida");
        customer3.getShippingAddress().setZipCode("33101");

        customer3.setUsername("saxe");
        customer3.setPassword("password");
        customer3.setConfirmPassword("password");

        CreditCard creditCard3 = new CreditCard();
        creditCard3.setFirstName("Sam");
        creditCard3.setLastName("Axe");
        creditCard3.setBillingAddress(new Address());
        creditCard3.getBillingAddress().setAddressLine1("1 Little Cuba Road");
        creditCard3.getBillingAddress().setCity("Miami");
        creditCard3.getBillingAddress().setState("Florida");
        creditCard3.getBillingAddress().setZipCode("33101");
        creditCard3.setCreditCardType(CreditCardType.VISA);
        creditCard3.setCardNumber("3333444455556666");
        creditCard3.setExpirationMonth("12");
        creditCard3.setExpirationYear("20");
        creditCard3.setVerificationCode("123");
        creditCard3.setCustomer(customer3);
        customer3.addCreditCard(creditCard3);
        customerService.saveOrUpdate(customer3);
        creditCardService.saveOrUpdate(creditCard3);
    }

    private void loadCarts() {
//  go get a list of Customers from the Customer Service and a list of products
        List<Customer> customers = (List<Customer>) customerService.listAll();
        List<Product> products = (List<Product>) productService.listAll();

        customers.forEach(customer -> {
//  for each customer create a new cart (customer.setCart)
            customer.setCart(new Cart());

//  created a new cartDetail from the products list
            products.forEach(product -> {
                CartDetail cartDetail = new CartDetail();
                cartDetail.setProduct(product);
                cartDetail.setImageUrl(product.getImageUrl());
                cartDetail.setName(product.getName());
                cartDetail.setDescription(product.getDescription());
                cartDetail.setPrice(product.getPrice());
                cartDetail.setQuantity(3);
                //  take the customer Cart and add the Detail
                customer.getCart().addCartDetail(cartDetail);
            });
            customerService.saveOrUpdate(customer);
        });
    }

    private void loadProducts() {
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setDescription("Product 1 - Description");
        product1.setPrice(new BigDecimal("12.99"));
        product1.setImageUrl("product1.JPG");
        product1.setProductStatus(ProductStatus.AVAILABLE);
        product1.setAllocated(0);
        product1.setAvailable(100);
        product1.setOnHand(100);
        productService.saveOrUpdate(product1);

        Product product2 = new Product();
        product2.setName(" A Product 2");
        product2.setDescription("Product 2 - Description");
        product2.setPrice(new BigDecimal("14.99"));
        product2.setImageUrl("product2.JPG");
        product2.setProductStatus(ProductStatus.AVAILABLE);
        product2.setAllocated(0);
        product2.setAvailable(100);
        product2.setOnHand(100);
        productService.saveOrUpdate(product2);

        Product product3 = new Product();
        product3.setName("Product 3");
        product3.setDescription("Product 3 - Description");
        product3.setPrice(new BigDecimal("34.99"));
        product3.setImageUrl("product3.JPG");
        product3.setProductStatus(ProductStatus.AVAILABLE);
        product3.setAllocated(0);
        product3.setAvailable(100);
        product3.setOnHand(100);
        productService.saveOrUpdate(product3);

        Product product4 = new Product();
        product4.setName(" C Product 4");
        product4.setDescription("Product 4 - Description");
        product4.setPrice(new BigDecimal("44.99"));
        product4.setImageUrl("product4.JPG");
        product4.setProductStatus(ProductStatus.AVAILABLE);
        product4.setAllocated(0);
        product4.setAvailable(100);
        product4.setOnHand(100);
        productService.saveOrUpdate(product4);

        Product product5 = new Product();
        product5.setName("Product 5");
        product5.setDescription("Product 5 - Description");
        product5.setPrice(new BigDecimal("25.99"));
        product5.setImageUrl("product5.JPG");
        product5.setProductStatus(ProductStatus.AVAILABLE);
        product5.setAllocated(0);
        product5.setAvailable(100);
        product5.setOnHand(100);
        productService.saveOrUpdate(product5);

    }

    private void loadEmployees() {
        Employee employee1 = new Employee();
        employee1.setFirstName("David");
        employee1.setLastName("Jones");
        employee1.setEmail("djones@mystorefront.com");
        employee1.setPhoneNumber("305.332.0991");

        employee1.setAddress(new Address());
        employee1.getAddress().setAddressLine1("3 Main St");
        employee1.getAddress().setCity("Miami");
        employee1.getAddress().setState("Florida");
        employee1.getAddress().setZipCode("33101");

        employee1.setUsername("djones");
        employee1.setPassword("password");
        employee1.setConfirmPassword("password");
        employee1.setEnabled(true);

        employee1.setRoleType(RoleType.ADMIN);
        employeeService.saveOrUpdate(employee1);

        Employee employee2 = new Employee();
        employee2.setFirstName("Tim");
        employee2.setLastName("Landon");
        employee2.setEmail("tlandon@mystorefront.com");
        employee2.setPhoneNumber("305.333.0771");

        employee2.setAddress(new Address());
        employee2.getAddress().setAddressLine1("9 Main St");
        employee2.getAddress().setCity("Miami");
        employee2.getAddress().setState("Florida");
        employee2.getAddress().setZipCode("33101");

        employee2.setUsername("tlandon");
        employee2.setPassword("password");
        employee2.setConfirmPassword("password");
        employee2.setEnabled(true);

        employee2.setRoleType(RoleType.MANAGER);
        employeeService.saveOrUpdate(employee2);

        Employee employee3 = new Employee();
        employee3.setFirstName("Susan");
        employee3.setLastName("Black");
        employee3.setEmail("sblack@mystorefront.com");
        employee3.setPhoneNumber("305.333.0551");

        employee3.setAddress(new Address());
        employee3.getAddress().setAddressLine1("109 Main St");
        employee3.getAddress().setCity("Miami");
        employee3.getAddress().setState("Florida");
        employee3.getAddress().setZipCode("33101");

        employee3.setUsername("sblack");
        employee3.setPassword("sblack");
        employee3.setConfirmPassword("sblack");
        employee3.setEnabled(true);

        employee3.setRoleType(RoleType.USER);
        employeeService.saveOrUpdate(employee3);
    }
}