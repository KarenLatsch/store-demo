package com.example.storefrontdemo.controllers;

import com.example.storefrontdemo.domain.entities.Product;
import com.example.storefrontdemo.domain.enums.ProductStatus;
import com.example.storefrontdemo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RequestMapping("/product")
@Controller
@SessionAttributes("loggedInUser")
public class ProductController {

    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public String listProducts(
            Model model
    ) {
        List<Product> products = productService.listAll();
        Collections.sort(products);

        model.addAttribute("products", products);
        model.addAttribute("genericHeader", "Product List");
        return "product/list";
    }

    @GetMapping("/show/{id}")
    public String getProduct(
            @PathVariable Integer id,
            Model model
    ){
        model.addAttribute("product", productService.getById(id));
        model.addAttribute("genericHeader", "Product Detail");
        return "product/show";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(
            @PathVariable Integer id,
            Model model
    ){
        model.addAttribute("product", productService.getById(id));
        model.addAttribute("productStatuses", ProductStatus.values());
        model.addAttribute("genericHeader", "Product Update");
        return "product/productform";
    }

    @GetMapping("/new")
    public String newProduct(
            Model model
    ){
        model.addAttribute("product", new Product());
        model.addAttribute("productStatuses", ProductStatus.values());
        model.addAttribute("genericHeader", "Product Add");
        return "product/newproductform";
    }

    @PostMapping("/edit")
    public String saveOrUpdateProduct(
            Product passedInProduct,
            Model model){
        String errorMessage = null;
        Product savedProduct;

        if(passedInProduct.getId() != null){
            Product product = productService.getById(passedInProduct.getId());
            errorMessage = productService.setUpdatedFields(product, passedInProduct);
        } else {
            errorMessage = productService.setNewFields(passedInProduct);
        }

        if(errorMessage != null) {
            model.addAttribute("productStatusError", errorMessage);
            model.addAttribute("productStatuses", ProductStatus.values());
            passedInProduct.setProductStatus(passedInProduct.getProductStatus());
            if(passedInProduct.getId() != null){
                return "product/productform";
            } else {
                return "product/newproductform";
            }
        }

         if(passedInProduct.getId() != null){
            Product product = productService.getById(passedInProduct.getId());
            savedProduct = productService.saveOrUpdate(product);
        } else {
            savedProduct = productService.saveOrUpdate(passedInProduct);
        }
        model.addAttribute("genericHeader", "Product Detail");
        return "redirect:/product/show/" + savedProduct.getId();
    }
}