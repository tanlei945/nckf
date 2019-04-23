package org.benben.modules.business.address.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.modules.business.address.entity.Address;
import org.benben.modules.business.address.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/address")
@Slf4j
public class RestAddressController {
    @Autowired
    private IAddressService addressService;

    @GetMapping("/queryDistance")
    @ApiOperation(value = "骑手距离", notes = "骑手距离")
    public String queryDistance(@RequestParam String lng, @RequestParam String lat, @RequestParam String userId){
        //获取默认的地址信息
        Address address = addressService.queryAddress(userId);
        //计算距离
        String distance = addressService.queryDistance(Double.parseDouble(lng), Double.parseDouble(lat), address.getLng(), address.getLat());

        return distance;
    }


}
