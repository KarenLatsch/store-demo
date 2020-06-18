package com.example.storefrontdemo.services.Repository;

import com.example.storefrontdemo.domain.entities.Order;
import com.example.storefrontdemo.domain.entities.OrderDetail;
import com.example.storefrontdemo.domain.entities.Product;
import com.example.storefrontdemo.domain.enums.ProductStatus;
import com.example.storefrontdemo.services.OrderDetailService;
import com.example.storefrontdemo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Profile("jpadao")
public class ProductRepoServiceImpl implements ProductService {

    private ProductRepoServiceRepository productRepoServiceRepository;
    private ProductService productService;
    private OrderDetailService orderDetailService;

    @Autowired
    public void setProductRepoServiceRepository(ProductRepoServiceRepository productRepoServiceRepository) {
        this.productRepoServiceRepository = productRepoServiceRepository;
    }
    @Autowired
    public void setOrderDetailService(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public List<Product> listAll() {
        return this.productRepoServiceRepository.findAll();
    }

    @Override
    public List<Product> findProductsAvailable(ProductStatus status) {
        return this.productRepoServiceRepository.findProductsByProductStatusEquals(status);
    }

    @Override
    public Product getById(Integer id) {
        Optional<Product> optionalProduct = this.productRepoServiceRepository.findById(id);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        }
        return null;
    }

    @Override
    public Product findProductByName(String productName) {
        return this.productRepoServiceRepository.findProductByName(productName);
    }

    @Override
    public String setNewFields(Product passedInProduct) {
        String message = null;

        Product getProduct = productService.findProductByName(passedInProduct.getName());
        if(getProduct != null){
            message = "Product name already exist";
        }
        Product product = new Product();
        product.setName(passedInProduct.getName());
        product.setDescription(passedInProduct.getDescription());
        product.setProductStatus(ProductStatus.NEW);
        product.setPrice(passedInProduct.getPrice());
        product.setImageUrl(passedInProduct.getImageUrl());
        product.setOnHand(passedInProduct.getOnHand());
        passedInProduct.setAvailable(passedInProduct.getOnHand());
        return message;
    }

    @Override
    public String setUpdatedFields(Product product, Product passedInProduct) {
        String message = null;
        ProductStatus passedInProductStatus = passedInProduct.getProductStatus();
        ProductStatus currentProductStatus = product.getProductStatus();

        product.setDescription(passedInProduct.getDescription());
        product.setProductStatus(passedInProduct.getProductStatus());
        product.setPrice(passedInProduct.getPrice());
        product.setImageUrl(passedInProduct.getImageUrl());
        product.setOnHand(passedInProduct.getOnHand());

        Integer newAvailableQty = passedInProduct.getOnHand() - product.getAllocated();
        product.setAvailable(newAvailableQty);

        switch (currentProductStatus) {
            case AVAILABLE:
                if(passedInProductStatus == ProductStatus.NEW) {
                    message = "Product has been made Available, status can not be changed to New";
                    break;
                }
            case DISCONTINUED:
                if(passedInProductStatus == ProductStatus.NEW) {
                    message = "Product has been made Available, status can not be changed to New";
                    break;
                }
            default:
            break;
        }
        return message;
    }

    @Override
    public void updateProductQtyForNewOrder(Order savedOrder) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(savedOrder.getId());
        for (int i = 0; i < orderDetails.size(); i++) {

            Integer newAllocatedQty = orderDetails.get(i).getQuantity() + orderDetails.get(i).getProduct().getAllocated();
            Integer newAvailableQty = orderDetails.get(i).getProduct().getOnHand() - newAllocatedQty;

            orderDetails.get(i).getProduct().setAllocated(newAllocatedQty);
            orderDetails.get(i).getProduct().setAvailable(newAvailableQty);
            productService.saveOrUpdate(orderDetails.get(i).getProduct());
        }
    }

    @Override
    public void updateProductQtyForShippedOrder(Order savedOrder){
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(savedOrder.getId());
        for (int i = 0; i < orderDetails.size(); i++) {

            Integer newAllocatedQty = orderDetails.get(i).getProduct().getAllocated() - orderDetails.get(i).getQuantity();
            Integer newOnHandQty = orderDetails.get(i).getProduct().getOnHand() - orderDetails.get(i).getQuantity();
            Integer newAvailableQty = newOnHandQty - newAllocatedQty;

            orderDetails.get(i).getProduct().setAllocated(newAllocatedQty);
            orderDetails.get(i).getProduct().setOnHand(newOnHandQty);
            orderDetails.get(i).getProduct().setAvailable(newAvailableQty);
            productService.saveOrUpdate(orderDetails.get(i).getProduct());
        }
    }

    @Override
    public void updateProductQtyForCancelledOrder(Order savedOrder) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(savedOrder.getId());
        for (int i = 0; i < orderDetails.size(); i++) {

            Integer orderQty = orderDetails.get(i).getQuantity();
            Integer newAllocatedQty = orderDetails.get(i).getProduct().getAllocated() - orderQty;
            Integer onHandQty = orderDetails.get(i).getProduct().getOnHand();
            Integer newAvailableQty = onHandQty - newAllocatedQty;

            orderDetails.get(i).getProduct().setAllocated(newAllocatedQty);
            orderDetails.get(i).getProduct().setAvailable(newAvailableQty);
            productService.saveOrUpdate(orderDetails.get(i).getProduct());
        }
    }

    @Override
    public Product saveOrUpdate(Product domainObject) {
        return this.productRepoServiceRepository.save(domainObject);
    }

    @Override
    public void delete(Integer id) {
        productRepoServiceRepository.deleteById(id);
    }
}