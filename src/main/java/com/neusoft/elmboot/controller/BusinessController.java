package com.neusoft.elmboot.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.neusoft.elmboot.po.Business;
import com.neusoft.elmboot.service.BusinessService;

@RestController
@RequestMapping("/BusinessController")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @RequestMapping("/listBusinessByOrderTypeId")
    public List<Business> listBusinessByOrderTypeId(Business business) throws Exception {
        if (business == null || business.getOrderTypeId() == null || business.getOrderTypeId() <= 0) {
            return businessService.listBusiness();
        }
        return businessService.listBusinessByOrderTypeId(business.getOrderTypeId());
    }

    @RequestMapping("/listBusinessByName")
    public List<Business> listBusinessByName(Business business) throws Exception {
        return businessService.listBusinessByName(business.getBusinessName());
    }

    @RequestMapping("/listBusiness")
    public List<Business> listBusiness() throws Exception {
        return businessService.listBusiness();
    }

    @RequestMapping("/getBusinessById")
    public Business getBusinessById(Business business) throws Exception {
        return businessService.getBusinessById(business.getBusinessId());
    }

    @RequestMapping("/saveBusiness")
    public int saveBusiness(Business business) throws Exception {
        return businessService.saveBusiness(business);
    }

    @RequestMapping("/removeBusiness")
    public int removeBusiness(Business business) throws Exception {
        return businessService.removeBusiness(business.getBusinessId());
    }

    @RequestMapping("/updateBusiness")
    public int updateBusiness(Business business) throws Exception {
        return businessService.updateBusiness(business);
    }
}
