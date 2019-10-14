package com.example.storefrontdemo.services.Repository;

import com.example.storefrontdemo.domain.entities.Cart;
import com.example.storefrontdemo.domain.entities.Customer;
import com.example.storefrontdemo.services.CartService;
import com.example.storefrontdemo.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Profile("jpadao")
public class CartRepoServiceImpl implements CartService {

    private CartRepoServiceRepository cartRepoServiceRepository;
    private CustomerService customerService;

    @Autowired
    public void setCartRepoServiceRepository(CartRepoServiceRepository cartRepoServiceRepository) {
        this.cartRepoServiceRepository = cartRepoServiceRepository;
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public List<Cart> listAll() {
        return this.cartRepoServiceRepository.findAll();
    }

    @Override
    public Cart findByCustomerId(Integer customerId) {
        return this.cartRepoServiceRepository.findByCustomerId(customerId);
    }

    @Override
    public Cart getById(Integer id) {
        Optional<Cart> optionalCart = this.cartRepoServiceRepository.findById(id);
        return optionalCart.get();
    }

    @Override
    public Cart calculateCartQty(Integer customerId, Cart domainObject) {
        Customer customer = customerService.getById(customerId);
        customer.getCart().calculateQuantity();
        return this.cartRepoServiceRepository.save(domainObject);
    }

    @Override
    public Cart saveOrUpdate(Cart domainObject) {
        return this.cartRepoServiceRepository.save(domainObject);
    }

    @Override
    public void delete(Integer id) {
        cartRepoServiceRepository.deleteById(id);
    }

    @Override
    public void createCart(Customer customer) {
        customer.setCart(new Cart());
    }
}