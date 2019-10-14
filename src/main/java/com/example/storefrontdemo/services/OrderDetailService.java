package com.example.storefrontdemo.services;

import com.example.storefrontdemo.domain.entities.OrderDetail;

import java.util.List;

public interface OrderDetailService extends CRUDService<OrderDetail> {

     List<OrderDetail> findByOrderId(Integer orderId);
 }
