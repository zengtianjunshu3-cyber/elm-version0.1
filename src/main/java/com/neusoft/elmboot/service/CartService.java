package com.neusoft.elmboot.service;

import com.neusoft.elmboot.po.Cart;

import java.util.List;

public interface CartService {
    List<Cart> listCart(Cart cart);
    int saveCart(Cart cart);
    int updateCart(Cart cart);
    int removeCart(Cart cart);
    int removeCartBatch(Cart cart);
}
