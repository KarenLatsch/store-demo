package com.example.storefrontdemo.services.Repository;

import com.example.storefrontdemo.domain.entities.CartDetail;
import com.example.storefrontdemo.domain.entities.Customer;
import com.example.storefrontdemo.domain.entities.Product;
import com.example.storefrontdemo.services.CartDetailService;
import com.example.storefrontdemo.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Profile("jpadao")
public class CartDetailRepoServiceImpl implements CartDetailService {

    private CartDetailRepoServiceRepository cartDetailRepoServiceRepository;
    private CartDetailService cartDetailService;
    private CustomerService customerService;

    @Autowired
    public void setCartDetailRepoServiceRepository(CartDetailRepoServiceRepository cartDetailRepoServiceRepository) {
        this.cartDetailRepoServiceRepository = cartDetailRepoServiceRepository;
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
    public List<CartDetail> listAll() {
        return this.cartDetailRepoServiceRepository.findAll();
    }

    @Override
    public List<CartDetail> findCartDetailsByCartId(Integer cartId) {
        return this.cartDetailRepoServiceRepository.findCartDetailsByCartId(cartId);
    }

    @Override
    public CartDetail findCartDetailByCartIdAndCartDetailId(Integer cartId, Integer cartDetailId) {
        return this.cartDetailRepoServiceRepository.findCartDetailByCartIdAndId(cartId, cartDetailId);
    }

    @Override
    public CartDetail findCartDetailByCartIdAndProductId(Integer cartId, Integer productId) {
        return this.cartDetailRepoServiceRepository.findCartDetailByCartIdAndProductId(cartId, productId);
    }

    @Override
    public CartDetail getById(Integer id) {
        Optional<CartDetail> optionalCartDetail = this.cartDetailRepoServiceRepository.findById(id);
        return optionalCartDetail.get();
    }

    @Override
    public CartDetail saveOrUpdate(CartDetail domainObject) {

        return this.cartDetailRepoServiceRepository.save(domainObject);
    }

    @Override
    public BigDecimal calculateCartTotalAmount(Integer customerCartId) {
        BigDecimal cartTotalAmount = new BigDecimal(0);
        int i;

        List<CartDetail> cartDetails = cartDetailService.findCartDetailsByCartId(customerCartId);
        for (i = 0; i < cartDetails.size(); i++) {
            cartTotalAmount = cartTotalAmount.add(cartDetails.get(i).getTotalAmount());
        }
        return cartTotalAmount;
    }

    @Override
    public CartDetail addToCartDetail(Integer customerId, CartDetail cartDetail, Integer quantity) {
        Customer customer = customerService.getById(customerId);

        customer.getCart().removeCartDetail(cartDetail);
        cartDetail.setQuantity(cartDetail.getQuantity() + quantity);

        customer.getCart().addCartDetail(cartDetail);
        return cartDetail;
    }

    @Override
    public CartDetail createCartDetail(Integer customerId, Product product, Integer quantity) {

        Customer customer = customerService.getById(customerId);

        CartDetail cartDetail = new CartDetail();
        cartDetail.setProduct(product);
        cartDetail.setImageUrl(product.getImageUrl());
        cartDetail.setName(product.getName());
        cartDetail.setDescription(product.getDescription());
        cartDetail.setPrice(product.getPrice());
        cartDetail.setQuantity(quantity);

        customer.getCart().addCartDetail(cartDetail);
        return cartDetail;
    }

    @Override
    public void delete(Integer id) {
        cartDetailRepoServiceRepository.deleteById(id);
    }

    public void deleteAll(Customer customer) {
        List<CartDetail> cartDetails = cartDetailService.findCartDetailsByCartId(customer.getCart().getId());

        cartDetails.forEach(cartDetail -> {
            customer.getCart().removeCartDetail(cartDetail);
            cartDetailRepoServiceRepository.deleteById(cartDetail.getId());
        });
    }
}





