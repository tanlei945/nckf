package org.benben.modules.business.withdraw.controller;

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
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.account.entity.Account;
import org.benben.modules.business.account.service.IAccountService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.withdraw.entity.Withdraw;
import org.benben.modules.business.withdraw.service.IWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;


@RestController
@RequestMapping("/api/v1/withdraw")
@Slf4j
@Api(tags = {"用户接口"})
public class RestWithdrawController {
    @Autowired
    private IWithdrawService withdrawService;
    @Autowired
    private IAccountService accountService;

    /**
     * showdoc
     * @catalog 用户接口
     * @title 提现记录
     * @description 提现记录
     * @method GET
     * @url /nckf-boot/api/v1/withdraw/queryWithdraw
     * @return {"code": 1,"data": {"c     * @return {"code": 1,"data": {"current": 1,"pages": 0,"records": [],"searchCount": true,"size": 10,"total": 0},"msg": "操作成功","time": "1561016605235"}urrent": 1,"pages": 0,"records": [],"searchCount": true,"size": 10,"total": 0},"msg": "操作成功","time": "1561016605235"}
     * @return_param code String 响应状态
     * @return_param data List 提现信息
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 23
     */
    @GetMapping(value = "/queryWithdraw")
    @ApiOperation(value = "提现记录", tags = {"用户接口"}, notes = "提现记录")
	@ApiImplicitParams({@ApiImplicitParam(name = "pageNo", value = "当前页", dataType = "Integer", defaultValue = "1"),
			            @ApiImplicitParam(name = "pageSize", value = "每页显示条数", dataType = "Integer", defaultValue = "10")})
    public RestResponseBean queryWithdraw(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if(user == null){
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

        QueryWrapper<Withdraw> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Withdraw::getUserId,user.getId());
        Page<Withdraw> page = new Page<Withdraw>(pageNo, pageSize);
        IPage<Withdraw> pageList = withdrawService.page(page, queryWrapper);

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), pageList);
    }


    /**
     * showdoc
     * @catalog 用户接口
     * @title 提现详情
     * @description 提现详情
     * @method GET
     * @url /nckf-boot/api/v1/withdraw/queryWithdrawById
     * @param id 必填 String 提现id
     * @return {"code": 1,"data": {"createBy": null,"createTime": 1556361155000,"id": "be57effe75b5c37a9acd075a2d3f022f","money": 1,"orderNo": null,"status": "0","updateBy": null,"updateTime": null,"userId": "c73ee7f3d95a74f9970eaac804548f78","withdrawType": null},"msg": "操作成功","time": "1561016682645"}
     * @return_param code String 响应状态
     * @return_param createBy String 创建人
     * @return_param createTime Date 创建时间
     * @return_param id String 提现id
     * @return_param money Double 充值金额
     * @return_param orderNo String 第三方订单号
     * @return_param status String 提现状态(0:未审核 1:审核未通过 2:审核已通过)
     * @return_param updateBy String 编辑人
     * @return_param updateTime Date 更新时间
     * @return_param userId String 用户id
     * @return_param withdrawType String 提现至(1:支付宝 2:微信)
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 24
     */
    @GetMapping(value = "/queryWithdrawById")
    @ApiOperation(value = "提现详情", tags = {"用户接口"}, notes = "提现详情")
    public RestResponseBean queryWithdrawById(@RequestParam(name = "id", required = true) String id) {

        Withdraw withdraw = withdrawService.getById(id);

        if (withdraw == null) {
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(), ResultEnum.QUERY_NOT_EXIST.getDesc(), null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), withdraw);
    }

    /**
     * showdoc
     * @catalog 用户接口
     * @title 账户提现申请
     * @description 账户提现申请
     * @method POST
     * @url /nckf-boot/api/v1/withdraw/withdrawApply
     * @param money 必填 Double 提现金额
     * @return {"code": 0,"data": null,"msg": "余额不足","time": "1561016713997"}
     * @return_param code String 响应状态
     * @return_param data String 没有含义
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 25
     */
    @PostMapping(value = "/withdrawApply")
    @ApiOperation(value = "账户提现申请", tags = {"用户接口"}, notes = "账户提现申请")
	@ApiImplicitParam(name = "money", value = "提现金额", dataType = "Double")
    public RestResponseBean withdrawApply(@RequestParam Double money) {


		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if(user == null){
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

        BigDecimal bigDecimal = new BigDecimal(1.00);

        if(new BigDecimal(money).compareTo(bigDecimal) < 0){
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),"提现金额不足1元",null);
        }

        Account account = accountService.queryByUserId(user.getId());

        if(account == null){
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
        }

        if(new BigDecimal(account.getMoney()).compareTo(new BigDecimal(money)) < 0){
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),"余额不足",null);
        }

        if(withdrawService.withdrawApply(user.getId(),money)){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }


}
