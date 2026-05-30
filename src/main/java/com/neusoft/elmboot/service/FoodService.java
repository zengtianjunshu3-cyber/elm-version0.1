package com.neusoft.elmboot.service;

import com.neusoft.elmboot.po.Food;

import java.util.List;

public interface FoodService {
    List<Food> listFoodByBusinessId(Integer businessId);
    int saveFood(Food food);
    int removeFood(Integer foodId);
}
