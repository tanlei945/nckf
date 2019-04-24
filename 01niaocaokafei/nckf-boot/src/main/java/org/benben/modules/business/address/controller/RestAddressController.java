package org.benben.modules.business.address.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
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
@Api(tags = "骑手距离接口")
public class RestAddressController {
    @Autowired
    private IAddressService addressService;

    @GetMapping("/query_distance")
    @ApiOperation(value = "骑手距离", notes = "骑手距离",tags = "骑手距离")
    public RestResponseBean queryDistance(@RequestParam String lng, @RequestParam String lat, @RequestParam String userId){
        //获取默认的地址信息
        Address address = addressService.queryAddress(userId);
        //计算距离
        String distance = addressService.queryDistance(Double.parseDouble(lng), Double.parseDouble(lat), address.getLng(), address.getLat());

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),distance);
    }


}
