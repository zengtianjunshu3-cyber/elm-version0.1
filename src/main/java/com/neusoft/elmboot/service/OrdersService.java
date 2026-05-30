package com.neusoft.elmboot.service;

import com.neusoft.elmboot.po.Orders;

import java.util.List;

public interface OrdersService {
    int createOrders(Orders orders);
    Orders getOrdersById(Integer orderId);
  public List<Orders> listOrdersByUserId(String userId);
    public java.util.Map<String, Object> updateOrdersState(Orders orders);
    public int removeOrders(Integer orderId);
}
