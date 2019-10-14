package com.example.storefrontdemo.services;

import com.example.storefrontdemo.domain.entities.CartDetail;
import com.example.storefrontdemo.domain.entities.Customer;
import com.example.storefrontdemo.domain.entities.Product;

import java.math.BigDecimal;
import java.util.List;

public interface CartDetailService extends CRUDService<CartDetail> {

        List<CartDetail> findCartDetailsByCartId(Integer cartId);

        CartDetail findCartDetailByCartIdAndCartDetailId(Integer cartId, Integer cartDetailId);

        CartDetail findCartDetailByCartIdAndProductId(Integer cartId, Integer productId);

        CartDetail addToCartDetail(Integer customerId, CartDetail cartDetail, Integer quantity);

        CartDetail createCartDetail(Integer customerId, Product product, Integer quantity);

        BigDecimal calculateCartTotalAmount(Integer customerCartId);

        void deleteAll(Customer customer);
}
