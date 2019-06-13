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
import org.apache.shiro.SecurityUtils;
import org.benben.common.XXPay.service.XXPayService;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.recharge.entity.Recharge;
import org.benben.modules.business.recharge.service.IRechargeService;
import org.benben.modules.business.user.entity.User;
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
@RequestMapping("/api/v1/recharge")
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
     * @param rechargeType
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/queryRecharge")
    @ApiOperation(value = "充值记录", tags = {"用户接口"}, notes = "充值记录")
	@ApiImplicitParam(name = "rechargeType",value = "1：支付宝 2：微信",dataType = "String",required = true)
    public RestResponseBean queryRecharge(@RequestParam(name = "rechargeType", required = true)String rechargeType,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if(user == null){
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

        QueryWrapper<Recharge> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Recharge::getUserId,user.getId()).eq(Recharge::getRechargeType,rechargeType);
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
    @GetMapping(value = "/queryRechargeById")
    @ApiOperation(value = "充值详细", tags = {"用户接口"}, notes = "充值详细")
    @ApiImplicitParam(name = "id",value = "充值的ID",dataType = "String",defaultValue = "1",required = true)
    public RestResponseBean queryRechargeById(@RequestParam(name = "id", required = true) String id) {

        Recharge recharge = rechargeService.getById(id);

        if (recharge == null) {
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(), ResultEnum.QUERY_NOT_EXIST.getDesc(), null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), recharge);
    }


    @PostMapping(value = "/rechargeRecharge")
    @ApiOperation(value = "钱包充值", tags = {"用户接口"}, notes = "钱包充值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "money",value = "充值金额",dataType = "double",defaultValue = "1.00",required = true),
            @ApiImplicitParam(name = "type",value = "充值方式1：支付宝 2：微信",dataType = "String",required = true)
    })
    public RestResponseBean rechargeRecharge(@RequestParam double money,@RequestParam String type) {

		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if(user == null){
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

        Recharge recharge = rechargeService.recharge(user.getId(),money,type);

        if(recharge == null){
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
        }
        // 调用支付宝统一下单接口
        if(StringUtils.equals(type,"1")){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),xxPayService.getAliPayOrderStr(recharge.getId(),recharge.getRechargeMoney(),"recharge","鸟巢咖啡充值"));
        }
        // 调用微信下单接口

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),xxPayService.getWxParOederStr(recharge.getId(),recharge.getRechargeMoney(),"recharge","鸟巢咖啡充值"));
    }


}