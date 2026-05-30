package com.neusoft.elmboot.mapper;

import com.neusoft.elmboot.po.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface OrdersMapper {

    @Options(useGeneratedKeys = true, keyProperty = "orderId", keyColumn = "orderId")
    int saveOrders(Orders orders);

    Orders getOrdersById(Integer orderId);

    List<Orders> listOrdersByUserId(String userId);

    int updateOrdersState(Orders orders);

    int removeOrders(Integer orderId);
}
