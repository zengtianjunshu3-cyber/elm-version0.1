package com.neusoft.elmboot.mapper;

import com.neusoft.elmboot.po.Business;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BusinessMapper {

    @Select("select * from business where orderTypeId=#{orderTypeId} order by businessId")
    public List<Business> listBusinessByOrderTypeId(Integer orderTypeId);

    @Select("select * from business where businessName like concat('%', #{businessName}, '%') order by businessId")
    public List<Business> listBusinessByName(String businessName);

    @Select("select * from business where businessId=#{businessId}")
    Business getBusinessById(Integer businessId);

    @Select("select * from business order by businessId")
    List<Business> listBusiness();

    @Insert("insert into business(businessName,businessAddress,businessExplain,businessImg,orderTypeId,starPrice,deliveryPrice,remarks) " +
            "values(#{businessName},#{businessAddress},#{businessExplain},#{businessImg},#{orderTypeId},#{starPrice},#{deliveryPrice},'无')")
    @Options(useGeneratedKeys = true, keyProperty = "businessId")
    int saveBusiness(Business business);

    @Delete("delete from business where businessId=#{businessId}")
    int removeBusiness(Integer businessId);

    @Update("update business set businessName=#{businessName}, businessAddress=#{businessAddress}, businessExplain=#{businessExplain}, businessImg=#{businessImg}, orderTypeId=#{orderTypeId}, starPrice=#{starPrice}, deliveryPrice=#{deliveryPrice} where businessId=#{businessId}")
    int updateBusiness(Business business);
}
