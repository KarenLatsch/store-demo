package com.example.storefrontdemo.services;

import com.example.storefrontdemo.domain.entities.Order;
import com.example.storefrontdemo.domain.entities.Product;
import com.example.storefrontdemo.domain.enums.ProductStatus;

import java.util.List;

public interface ProductService extends CRUDService<Product> {

    List<Product> findProductsAvailable(ProductStatus status);

    String setUpdatedFields(Product product, Product passedInProduct);

    String setNewFields(Product passedInProduct);

    Product findProductByName(String productName);

    void updateProductQtyForNewOrder(Order order);

    void updateProductQtyForShippedOrder(Order savedOrder);

    void updateProductQtyForCancelledOrder(Order savedOrder);
}

