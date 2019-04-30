package org.benben.modules.business.recharge.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.benben.common.XXPay.service.XXPayService;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.recharge.entity.Recharge;
import org.benben.modules.business.recharge.service.IRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Title: Controller
 * @Description: 充值
 * @author： jeecg-boot
 * @date： 2019-04-25
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/recharge")
@Api(tags = {"用户接口"})
@Slf4j
public class RestRechargeController {
    @Autowired
    private IRechargeService rechargeService;
    @Autowired
    private XXPayService xxPayService;

    /**
     * 充值记录
     *
     * @param recharge
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "充值记录", tags = {"用户接口"}, notes = "充值记录")
    public RestResponseBean queryPageList(Recharge recharge,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          HttpServletRequest req) {

        QueryWrapper<Recharge> queryWrapper = QueryGenerator.initQueryWrapper(recharge, req.getParameterMap());
        Page<Recharge> page = new Page<Recharge>(pageNo, pageSize);
        IPage<Recharge> pageList = rechargeService.page(page, queryWrapper);

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), pageList);
    }


    /**
     * 充值详细
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    @ApiOperation(value = "充值详细", tags = {"用户接口"}, notes = "充值详细")
    @ApiImplicitParam(name = "id",value = "充值的ID",dataType = "String",defaultValue = "1",required = true)
    public RestResponseBean queryById(@RequestParam(name = "id", required = true) String id) {

        Recharge recharge = rechargeService.getById(id);

        if (recharge == null) {
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(), ResultEnum.QUERY_NOT_EXIST.getDesc(), null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), recharge);
    }


    @PostMapping(value = "/recharge")
    @ApiOperation(value = "钱包充值", tags = {"用户接口"}, notes = "钱包充值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户的ID",dataType = "String",defaultValue = "1",required = true),
            @ApiImplicitParam(name = "money",value = "充值金额",dataType = "double",defaultValue = "1.00",required = true),
            @ApiImplicitParam(name = "type",value = "充值方式1：支付宝 2：微信",dataType = "String",required = true)
    })
    public RestResponseBean recharge(@RequestParam String userId,@RequestParam double money,@RequestParam String type) {

        Recharge recharge = rechargeService.recharge(userId,money,type);

        if(recharge == null){
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
        }
        //TODO 调用支付宝统一下单接口
        if(StringUtils.equals(type,"1")){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),xxPayService.getAliPayOrderStr(recharge.getId(),recharge.getRechargeMoney(),"recharge","鸟巢咖啡充值"));
        }
        //TODO 调用微信下单接口

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),xxPayService.getWxParOederStr(recharge.getId(),recharge.getRechargeMoney(),"recharge","鸟巢咖啡充值"));
    }


}
