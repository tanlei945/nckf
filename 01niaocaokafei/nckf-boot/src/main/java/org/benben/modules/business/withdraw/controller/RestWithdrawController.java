package org.benben.modules.business.withdraw.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.account.entity.Account;
import org.benben.modules.business.account.service.IAccountService;
import org.benben.modules.business.withdraw.entity.Withdraw;
import org.benben.modules.business.withdraw.service.IWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @Title: RestWithdrawController
 * @Description: 提现
 * @author： WangHao
 * @date： 2019-04-25
 * @version： V1.0
 */
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
     * 提现记录
     *
     * @param withdraw
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "提现记录", tags = {"用户接口"}, notes = "提现记录")
    public RestResponseBean queryPageList(Withdraw withdraw,
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          HttpServletRequest req) {

        QueryWrapper<Withdraw> queryWrapper = QueryGenerator.initQueryWrapper(withdraw, req.getParameterMap());
        Page<Withdraw> page = new Page<Withdraw>(pageNo, pageSize);
        IPage<Withdraw> pageList = withdrawService.page(page, queryWrapper);

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), pageList);
    }


    /**
     * 提现详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/query_by_id")
    @ApiOperation(value = "提现详情", tags = {"用户接口"}, notes = "提现详情")
    public RestResponseBean queryById(@RequestParam(name = "id", required = true) String id) {

        Withdraw withdraw = withdrawService.getById(id);

        if (withdraw == null) {
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(), ResultEnum.QUERY_NOT_EXIST.getDesc(), null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), withdraw);
    }

    /**
     * 账户提现申请
     * @param userId
     * @param money
     * @return
     */
    @PostMapping(value = "/withdraw_apply")
    @ApiOperation(value = "账户提现申请", tags = {"用户接口"}, notes = "账户提现申请")
    public RestResponseBean withdrawApply(@RequestParam String userId,@RequestParam Double money) {

        BigDecimal bigDecimal = new BigDecimal(1.00);

        if(new BigDecimal(money).compareTo(bigDecimal) < 0){
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),"提现金额不足1元",null);
        }

        Account account = accountService.queryByUserId(userId);

        if(account == null){
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
        }

        if(new BigDecimal(account.getMoney()).compareTo(new BigDecimal(money)) < 0){
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),"余额不足",null);
        }

        if(withdrawService.withdrawApply(userId,money)){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }


}
