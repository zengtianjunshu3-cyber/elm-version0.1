package com.neusoft.elmboot.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private Integer cartId;
    private Integer foodId;
    private Integer businessId;
    private String userId;
    private Integer quantity;

    // 多对一：购物车项对应的食品
    private Food food;
    // 多对一：购物车项对应的商家
    private Business business;
}
