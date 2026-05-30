package com.neusoft.elmboot.service.impl;

import com.neusoft.elmboot.mapper.CartMapper;
import com.neusoft.elmboot.mapper.OrderDetailetMapper;
import com.neusoft.elmboot.mapper.OrdersMapper;
import com.neusoft.elmboot.po.Cart;
import com.neusoft.elmboot.po.OrderDetailet;
import com.neusoft.elmboot.po.Orders;
import com.neusoft.elmboot.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderDetailetMapper orderDetailetMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private com.neusoft.elmboot.mapper.UserMapper userMapper;

    @Override
    @Transactional
    public int createOrders(Orders orders) {
        // 1. 查询当前用户购物车中当前商家的所有食品
        Cart cart = new Cart();
        cart.setUserId(orders.getUserId());
        cart.setBusinessId(orders.getBusinessId());
        List<Cart> cartList = cartMapper.listCart(cart);

        // 2. 创建订单
        // 设置订单日期 (格式：YYYY-MM-DD HH:mm:ss)
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        orders.setOrderDate(sdf.format(new java.util.Date()));
        
        ordersMapper.saveOrders(orders);
        int orderId = orders.getOrderId();

        // 3. 批量添加订单明细
        List<OrderDetailet> detailList = new ArrayList<>();
        for (Cart c : cartList) {
            OrderDetailet detail = new OrderDetailet();
            detail.setOrderId(orderId);
            detail.setFoodId(c.getFoodId());
            detail.setQuantity(c.getQuantity());
            detailList.add(detail);
        }
        orderDetailetMapper.saveOrderDetailetBatch(detailList);

        return orderId;
    }

    @Override
    public Orders getOrdersById(Integer orderId) {
        return ordersMapper.getOrdersById(orderId);
    }

    @Override
    public List<Orders> listOrdersByUserId(String userId) {
        return ordersMapper.listOrdersByUserId(userId);
    }

    @Override
    @Transactional
    public java.util.Map<String, Object> updateOrdersState(Orders orders) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        if (orders.getOrderState() == 1) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            orders.setOrderDate(sdf.format(new java.util.Date()));
        }
        int result = ordersMapper.updateOrdersState(orders);
        response.put("result", result);

        if (result > 0 && orders.getOrderState() == 1) {
            // 如果支付成功（orderState=1）
            Orders fullOrder = ordersMapper.getOrdersById(orders.getOrderId());
            
            // 1. 清空购物车
            Cart cart = new Cart();
            cart.setUserId(fullOrder.getUserId());
            cart.setBusinessId(fullOrder.getBusinessId());
            cartMapper.removeCartBatch(cart);

            // 2. 奖励逻辑与红包抵扣
            com.neusoft.elmboot.po.User user = userMapper.getUserByIdObject(fullOrder.getUserId());
            if (user != null) {
                // 初始化资产（如果为null）
                if (user.getRedEnvelope() == null) user.setRedEnvelope(0.0);
                if (user.getGoldCoins() == null) user.setGoldCoins(0);

                // a. 抵扣使用的红包
                if (orders.getUsedRedEnvelope() != null && orders.getUsedRedEnvelope() > 0) {
                    user.setRedEnvelope(user.getRedEnvelope() - orders.getUsedRedEnvelope());
                }

                // b. 随机赠送奖励
                // 红包: 0.5 - 2.0 元
                double rewardRed = parseFloat((Math.random() * 1.5 + 0.5));
                // 金币: 50 - 200 个
                int rewardGold = (int)(Math.random() * 150 + 50);

                user.setRedEnvelope(user.getRedEnvelope() + rewardRed);
                user.setGoldCoins(user.getGoldCoins() + rewardGold);

                // c. 更新用户信息
                userMapper.updateUser(user);

                // 将奖励信息放入返回结果
                response.put("rewardRed", rewardRed);
                response.put("rewardGold", rewardGold);
            }
        }
        return response;
    }

    private double parseFloat(double value) {
        return BigDecimal.valueOf(value).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @Override
    @Transactional
    public int removeOrders(Integer orderId) {
        orderDetailetMapper.removeOrderDetailetByOrderId(orderId);
        return ordersMapper.removeOrders(orderId);
    }
}
