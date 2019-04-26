package org.benben.modules.business.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.common.util.PasswordUtil;
import org.benben.modules.business.account.entity.Account;
import org.benben.modules.business.account.service.IAccountService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
* @Title: Controller
* @Description: 钱包表
* @author： jeecg-boot
* @date：   2019-04-24
* @version： V1.0
*/
@RestController
@RequestMapping("/api/account")
@Slf4j
@Api(tags = {"账户/钱包接口"})
public class RestAccountController {

   @Autowired
   private IAccountService accountService;

   @Autowired
   private IUserService userService;



    @GetMapping(value = "/query_by_id")
    @ApiOperation(value = "账单/钱包详情", tags = {"账户/钱包接口"}, notes = "账单/钱包详情")
    public RestResponseBean queryById(@RequestParam String userId) {

        Account account = accountService.queryByUserId(userId);

        if(account==null) {
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),account);
    }

    @GetMapping(value = "/is_pay_password")
    @ApiOperation(value = "是否设置支付密码", tags = {"账户/钱包接口"}, notes = "是否设置支付密码")
    public RestResponseBean isPayPassword(String userId){

        if(accountService.isPayPassword(userId)){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),"未设置支付密码",null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),"已设置支付密码",null);
    }


    @GetMapping(value = "is_withdraw_account")
    @ApiOperation(value = "是否设置收款账户", tags = {"账户/钱包接口"}, notes = "是否设置收款账户")
    public RestResponseBean isWithdrawAccount(String userId){

        if(accountService.isWithdrawAccount(userId)){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),"未设置收款账户",null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),"已设置收款账户",null);
    }

    @GetMapping("/check_pay_password")
    @ApiOperation(value = "支付密码是否正确",tags = {"账户/钱包接口"},notes = "支付密码是否正确")
    public RestResponseBean checkPayPassword(String userId,String payPassword){

        User user = userService.getById(userId);
        Account account = accountService.queryByUserId(userId);

        if(user == null || account == null){
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
        }

        String userpassword = PasswordUtil.encrypt(user.getMobile(), payPassword, account.getSalt());
        if (!account.getPayPassword().equals(userpassword)) {
            return new RestResponseBean(ResultEnum.PAY_PASSWORD_ERROR.getValue(), ResultEnum.PAY_PASSWORD_ERROR.getDesc(), null);
        }

        return new RestResponseBean(ResultEnum.PAY_PASSWORD_RIGHT.getValue(), ResultEnum.PAY_PASSWORD_RIGHT.getDesc(), null);
    }


    @GetMapping(value = "/reset_pay_password")
    @ApiOperation(value = "重置支付密码", tags = {"账户/钱包接口"}, notes = "重置支付密码")
    public RestResponseBean resetPayPassword(String userId,String newPayPassword){

        if (accountService.resetPayPassword(userId,newPayPassword)){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }

   @PostMapping(value = "/recharge")
   @ApiOperation(value = "钱包充值", tags = {"账户/钱包接口"}, notes = "钱包充值")
   public RestResponseBean recharge(@RequestParam String userId,@RequestParam double money,@RequestParam String type) {
       String state = "";
       String orderNo = "";

       //TODO支付调用

       accountService.recharge(userId,money,type,state,orderNo);

       return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
   }

    /**
     * 账户提现申请
     * @param userId
     * @param money
     * @return
     */
    @PostMapping(value = "/withdraw")
    @ApiOperation(value = "账户提现申请", tags = {"账户/钱包接口"}, notes = "账户提现申请")
    public RestResponseBean withdraw(@RequestParam String userId,@RequestParam double money) {

        if(accountService.withdraw(userId,money)){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }

}
