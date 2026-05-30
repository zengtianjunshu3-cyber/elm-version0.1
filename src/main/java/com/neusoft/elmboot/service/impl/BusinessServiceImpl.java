package com.neusoft.elmboot.service.impl;

import com.neusoft.elmboot.mapper.BusinessMapper;
import com.neusoft.elmboot.po.Business;
import com.neusoft.elmboot.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private BusinessMapper businessMapper;

    @Override
    public List<Business> listBusinessByOrderTypeId(Integer orderTypeId) {
        return businessMapper.listBusinessByOrderTypeId(orderTypeId);
    }

    @Override
    public List<Business> listBusinessByName(String businessName) {
        return businessMapper.listBusinessByName(businessName);
    }

    @Override
    public List<Business> listBusiness() {
        return businessMapper.listBusiness();
    }

    @Override
    public Business getBusinessById(Integer businessId) {
        return businessMapper.getBusinessById(businessId);
    }

    @Override
    public int saveBusiness(Business business) {
        return businessMapper.saveBusiness(business);
    }

    @Override
    public int removeBusiness(Integer businessId) {
        return businessMapper.removeBusiness(businessId);
    }

    @Override
    public int updateBusiness(Business business) {
        return businessMapper.updateBusiness(business);
    }
}
