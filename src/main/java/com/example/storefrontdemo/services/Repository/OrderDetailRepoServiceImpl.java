package com.example.storefrontdemo.services.Repository;

import com.example.storefrontdemo.domain.entities.OrderDetail;
import com.example.storefrontdemo.services.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Profile("jpadao")
public class OrderDetailRepoServiceImpl implements OrderDetailService {

    private OrderDetailRepoServiceRepository orderDetailRepoServiceRepository;

    @Autowired
    public void setOrderDetailRepoServiceRepository(OrderDetailRepoServiceRepository orderDetailRepoServiceRepository) {
        this.orderDetailRepoServiceRepository = orderDetailRepoServiceRepository;
    }

    @Override
    public List<OrderDetail> listAll() {
        return this.orderDetailRepoServiceRepository.findAll();
    }

    @Override
    public List<OrderDetail> findByOrderId(Integer orderId) {
        return this.orderDetailRepoServiceRepository.findByOrderId(orderId);
    }

    @Override
    public OrderDetail getById(Integer id) {
        Optional<OrderDetail> optionalOrderDetail = this.orderDetailRepoServiceRepository.findById(id);
        return optionalOrderDetail.get();
    }

    @Override
    public OrderDetail saveOrUpdate(OrderDetail domainObject) {
        return this.orderDetailRepoServiceRepository.save(domainObject);
    }

    @Override
    public void delete(Integer id) {
        orderDetailRepoServiceRepository.deleteById(id);
    }
}