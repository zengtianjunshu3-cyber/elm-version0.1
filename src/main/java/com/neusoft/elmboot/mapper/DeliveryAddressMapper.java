package com.neusoft.elmboot.mapper;

import com.neusoft.elmboot.po.DeliveryAddress;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeliveryAddressMapper {

    @Select("select * from deliveryaddress where userId=#{userId} order by daId")
    List<DeliveryAddress> listDeliveryAddressByUserId(String userId);

    @Select("select * from deliveryaddress where daId=#{daId}")
    DeliveryAddress getDeliveryAddressById(Integer daId);

    @Insert("insert into deliveryaddress values(null,#{contactName},#{contactSex},#{contactTel},#{address},#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "daId")
    int saveDeliveryAddress(DeliveryAddress deliveryAddress);

    @Update("update deliveryaddress set contactName=#{contactName},contactSex=#{contactSex},contactTel=#{contactTel},address=#{address} where daId=#{daId}")
    int updateDeliveryAddress(DeliveryAddress deliveryAddress);

    @Delete("delete from deliveryaddress where daId=#{daId}")
    int removeDeliveryAddress(Integer daId);
}
