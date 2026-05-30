package com.neusoft.elmboot.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    private Integer orderId;
    private String userId;
    private Integer businessId;
    private String orderDate;
    private BigDecimal orderTotal;
    private Integer daId;
    private Integer orderState;
    private Double usedRedEnvelope; // 支付时使用的红包金额

    // 多对一：订单所属商家
    private Business business;
    // 一对多：订单包含的明细
    private List<OrderDetailet> list;
    // 多对一：订单送货地址
    private DeliveryAddress deliveryAddress;
}
