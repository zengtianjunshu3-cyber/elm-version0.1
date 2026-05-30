package com.neusoft.elmboot.service;

import com.neusoft.elmboot.po.Business;

import java.util.List;

public interface BusinessService {
    List<Business> listBusinessByOrderTypeId(Integer orderTypeId);
    List<Business> listBusinessByName(String businessName);
    List<Business> listBusiness();
    Business getBusinessById(Integer businessId);
    int saveBusiness(Business business);
    int removeBusiness(Integer businessId);
    int updateBusiness(Business business);
}
