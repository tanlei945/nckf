package org.benben.modules.business.rideraddress.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.modules.business.rideraddress.entity.RiderAddress;
import org.benben.modules.business.rideraddress.service.IRiderAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/riderAddress")
@Slf4j
@Api(tags = {"骑手位置接口"})
public class RestRiderAddressController {

    @Autowired
    private IRiderAddressService riderAddressService;

    /**
     *  新增和修改骑手位置
     * @param riderAddress
     * @return
     */
    @PostMapping(value = "/address_edit")
    @ApiOperation(value = "新增和修改骑手位置", tags = {"骑手位置接口"}, notes = "新增和修改骑手位置")
    public RestResponseBean edit(@RequestBody RiderAddress riderAddress) {
        QueryWrapper<RiderAddress> riderAddressQueryWrapper = new QueryWrapper<>();
        riderAddressQueryWrapper.eq("rider_id",riderAddress.getRiderId());
        RiderAddress riderAddressEntity = riderAddressService.getOne(riderAddressQueryWrapper);
        if(riderAddressEntity==null) {
            boolean save = riderAddressService.save(riderAddress);
            if(save){
                return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
            }

            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);

        }else {
            boolean update = riderAddressService.update(riderAddress, riderAddressQueryWrapper);

            if(update) {
                return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
            }
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }
    /**
     *  删除骑手位置
     * @param id
     * @return
     */
    @PostMapping(value = "/delete")
    @ApiOperation(value = "删除骑手位置", tags = {"骑手位置接口"}, notes = "删除骑手位置")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "骑手位置id",dataType = "String",required = true),
    })
    public RestResponseBean delete(@RequestParam(name="id",required=true) String id) {
        QueryWrapper<RiderAddress> riderAddressQueryWrapper = new QueryWrapper<>();
        riderAddressQueryWrapper.eq("rider_id",id);
        RiderAddress riderAddress = riderAddressService.getOne(riderAddressQueryWrapper);
        if(riderAddress==null) {
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
        }else {
            boolean ok = riderAddressService.remove(riderAddressQueryWrapper);
            if(ok) {
                return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
            }
        }
        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }
}
