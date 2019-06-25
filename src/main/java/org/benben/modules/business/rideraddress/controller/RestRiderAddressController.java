package org.benben.modules.business.rideraddress.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.modules.business.rideraddress.entity.RiderAddress;
import org.benben.modules.business.rideraddress.service.IRiderAddressService;
import org.benben.modules.business.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("/api/v1/riderAddress")
@Slf4j
@Api(tags = {"骑手位置接口"})
public class RestRiderAddressController {

    @Autowired
    private IRiderAddressService riderAddressService;

    /**
     * 新增和修改骑手位置
     *
     * @return
     */
    /**
     * showdoc
     * @catalog 骑手位置接口
     * @title 新增和修改骑手位置
     * @description 新增和修改骑手位置
     * @json_param id 非必填 String ID
     * @json_param lat 必填 Double 经度
     * @json_param lng 必填 Double 纬度
     * @json_param riderId 必填 String 骑手id
     * @json_param createBy 非必填 String 创建人
     * @json_param createTime 非必填 Date 创建时间
     * @json_param updateBy 非必填 String 修改人
     * @json_param updateTime 非必填 Date 修改时间
     * @method POST
     * @url /nckf-boot/api/v1/riderAddress/updateRiderAddress
     * @return {"code": 1,"data": null,"msg": "操作成功", "time": "1561013457705"}
     * @return_param code String 响应状态
     * @return_param data String null
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark
     * @number 1
     */
    @PostMapping(value = "/updateRiderAddress")
    @ApiOperation(value = "新增和修改骑手位置", tags = {"骑手位置接口"}, notes = "新增和修改骑手位置")
    public RestResponseBean updateRiderAddress( @RequestParam String lat,@RequestParam String lng) {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        QueryWrapper<RiderAddress> riderAddressQueryWrapper = new QueryWrapper<>();
        riderAddressQueryWrapper.eq("rider_id",user.getId());
        RiderAddress riderAddressEntity = riderAddressService.getOne(riderAddressQueryWrapper);
        if(riderAddressEntity==null) {
            riderAddressEntity.setCreateBy(user.getRealname());
            riderAddressEntity.setCreateTime(new Date());
            boolean save = riderAddressService.save(riderAddressEntity);
            if(save){
                return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
            }

            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);

        }else {
            if(lat!=null || lat !="" && lng !=null || lng != ""){
                riderAddressEntity.setLat(Double.parseDouble(lat));
                riderAddressEntity.setLat(Double.parseDouble(lng));
                riderAddressEntity.setUpdateBy(user.getRealname());
                riderAddressEntity.setUpdateTime(new Date());
                riderAddressService.update(riderAddressEntity, riderAddressQueryWrapper);
                return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
            }
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }


    /**
     * showdoc
     * @catalog 骑手位置接口
     * @title 删除骑手位置接口
     * @description 删除骑手位置接口
     * @method POST
     * @url /nckf-boot/api/v1/riderAddress/deleteRiderAddress
     * @return {"code": 1,"data": null,"msg": "操作成功","time": "1561013457705"}
     * @return_param code String 响应状态
     * @return_param data String null
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark
     * @number 1
     */

    @PostMapping(value = "/deleteRiderAddress")
    @ApiOperation(value = "删除骑手位置", tags = {"骑手位置接口"}, notes = "删除骑手位置")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "骑手位置id",dataType = "String",required = true),
    })
    public RestResponseBean deleteRiderAddress(@RequestParam(name="id",required=true) String id) {
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
