package com.neusoft.elmboot.mapper;

import com.neusoft.elmboot.po.OrderDetailet;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailetMapper {

    int saveOrderDetailetBatch(List<OrderDetailet> list);

    List<OrderDetailet> listOrderDetailetByOrderId(Integer orderId);

    int removeOrderDetailetByOrderId(Integer orderId);
}
