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
import org.benben.modules.shiro.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
     * showdoc
     * @catalog 用户接口
     * @title 充值记录
     * @description 充值记录
     * @method GET
     * @url /nckf-boot/api/v1/recharge/queryRecharge
     * @return {"code": 1,"data": {"current": 1,"pages": 0,"records": [],"searchCount": true,"size": 10,"total": 0},"msg": "操作成功","time": "1561013320530"}
     * @return_param code String 响应状态
     * @return_param data List 充值信息
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 20
     */
    @GetMapping(value = "/queryRecharge")
    @ApiOperation(value = "充值记录", tags = {"用户接口"}, notes = "充值记录")
    public RestResponseBean queryRecharge(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

		User user = (User) LoginUser.getCurrentUser();

		if(user == null){
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

        QueryWrapper<Recharge> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Recharge::getUserId,user.getId()).orderByDesc(Recharge::getCreateTime);
        Page<Recharge> page = new Page<Recharge>(pageNo, pageSize);
        IPage<Recharge> pageList = rechargeService.page(page, queryWrapper);

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), pageList);
    }


    /**
     * showdoc
     * @catalog 用户接口
     * @title 充值详细
     * @description 充值详细
     * @method GET
     * @url /nckf-boot/api/v1/recharge/queryRechargeById
     * @param id 必填 String 充值id
     * @return {"code": 1,"data": {"createBy": null,"createTime": 1559099515000,"id": "25231bca915e8c37ae8fe5bff0002470","orderNo": null,"rechargeMoney": 0,"rechargeType": "1","status": "2","updateBy": null,"updateTime": null,"userId": "67450f9df12e7b4b415c931aa6cd58f4"},"msg": "操作成功","time": "1561013831784"}
     * @return_param code String 响应状态
     * @return_param data Object 充值对象
     * @return_param createBy String 创建人
     * @return_param createTime Date 创建时间
     * @return_param id String 充值id
     * @return_param orderNo String 第三方订单号
     * @return_param rechargeMoney Double 充值金额
     * @return_param rechargeType String 充值方式(1:支付宝 2:微信)
     * @return_param status String 是否充值成功（0:失败 1:成功）
     * @return_param updateBy String 编辑人
     * @return_param updateTime Date 更新时间
     * @return_param userId String 用户id
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 21
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

    /**
     * showdoc
     * @catalog 用户接口
     * @title 钱包充值
     * @description 钱包充值
     * @method POST
     * @url /nckf-boot/api/v1/recharge/rechargeRecharge
     * @param money 必填 Double 充值金额
     * @param type 必填 String 充值方式
     * @return {"code": 1,"data": "alipay_sdk=alipay-sdk-java-dynamicVersionNo&biz_content=%7B%22body%22%3A%22%E9%B8%9F%E5%B7%A2%E5%92%96%E5%95%A1%E5%85%85%E5%80%BC%22%2C%22out_trade_no%22%3A%226b6279d168c1b2c7e5da22afa5778a3b%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22recharge%22%2C%22timeout_express%22%3A%2215m%22%2C%22total_amount%22%3A%22100.0%22%7D&charset=UTF-8&method=alipay.trade.app.pay&timestamp=2019-06-20+14%3A55%3A43&version=1.0","msg": "操作成功","time": "1561013743400"}
     * @return_param code String 响应状态
     * @return_param data String 秘钥
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 22
     */
    @PostMapping(value = "/rechargeRecharge")
    @ApiOperation(value = "钱包充值", tags = {"用户接口"}, notes = "钱包充值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "money",value = "充值金额",dataType = "double",defaultValue = "1.00",required = true),
            @ApiImplicitParam(name = "type",value = "充值方式1：支付宝 2：微信",dataType = "String",required = true)
    })
    public RestResponseBean rechargeRecharge(@RequestParam double money,@RequestParam String type) {

		User user = (User) LoginUser.getCurrentUser();

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
