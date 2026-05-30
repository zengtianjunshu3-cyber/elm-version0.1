package com.neusoft.elmboot.mapper;

import com.neusoft.elmboot.po.Food;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FoodMapper {

    @Select("select * from food where businessId=#{businessId} order by foodId")
    List<Food> listFoodByBusinessId(Integer businessId);

    @Select("select * from food where foodId=#{foodId}")
    Food getFoodById(Integer foodId);

    @Insert("insert into food(foodName,foodExplain,foodImg,foodPrice,businessId,remarks) values(#{foodName},#{foodExplain},#{foodImg},#{foodPrice},#{businessId},#{remarks})")
    @Options(useGeneratedKeys = true, keyProperty = "foodId")
    int saveFood(Food food);

    @Delete("delete from food where foodId=#{foodId}")
    int removeFood(Integer foodId);
}
