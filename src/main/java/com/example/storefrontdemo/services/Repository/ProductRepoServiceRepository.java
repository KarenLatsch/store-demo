package com.example.storefrontdemo.services.Repository;

import com.example.storefrontdemo.domain.entities.Product;
import com.example.storefrontdemo.domain.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepoServiceRepository extends JpaRepository<Product, Integer> {

    List<Product> findProductsByProductStatusEquals(ProductStatus status);

    Product findProductByName(String productName);
}

