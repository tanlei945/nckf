package org.benben.modules.business.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.modules.business.account.entity.Account;
import org.benben.modules.business.account.service.IAccountService;
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
@Api(tags = {"账户接口"})
public class RestAccountController {

   @Autowired
   private IAccountService accountService;



    @GetMapping(value = "/queryById")
    @ApiOperation(value = "账单/钱包详情", tags = {"账户接口"}, notes = "账单/钱包详情")
    public RestResponseBean queryById(@RequestParam(name="id",required=true) String id) {

        Account account = accountService.getById(id);

        if(account==null) {
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),account);
    }

    @GetMapping(value = "/is_PayPassword")
    @ApiOperation(value = "是否设置支付密码", tags = {"账户接口"}, notes = "是否设置支付密码")
    public RestResponseBean isPayPassword(String userId){

        if(accountService.isPayPassword(userId)){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),"已未设置支付密码",null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),"未设置支付密码",null);
    }


    @GetMapping(value = "is_withdraw_account")
    @ApiOperation(value = "是否设置收款账户", tags = {"账户接口"}, notes = "是否设置收款账户")
    public RestResponseBean isWithdrawAccount(String userId){

        if(accountService.isWithdrawAccount(userId)){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),"已设置收款账户",null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),"未设置收款账户",null);
    }

    @GetMapping(value = "/reset_PayPassword")
    @ApiOperation(value = "重置支付密码", tags = {"账户接口"}, notes = "重置支付密码")
    public RestResponseBean resetPayPassword(String userId,String newPayPassword){

        if (accountService.resetPayPassword(userId,newPayPassword)){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }

   /**
    * 充值
    * @param account
    * @return
    */
   @PostMapping(value = "/recharge")
   @ApiOperation(value = "钱包充值", tags = {"账户接口"}, notes = "钱包充值")
   public RestResponseBean recharge(@RequestBody Account account) {

       Account accountEntity = accountService.getById(account.getId());

       if(accountEntity==null) {
           return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
       }else {
           boolean ok = accountService.updateById(account);
           if(ok) {
               return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
           }
       }

       return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
   }

    /**
     * 账户提现申请
     * @param userId
     * @param money
     * @return
     */
    @PostMapping(value = "/withdraw")
    @ApiOperation(value = "账户提现申请", tags = {"账户接口"}, notes = "账户提现申请")
    public RestResponseBean withdraw(@RequestParam String userId,@RequestParam double money) {

        if(accountService.withdraw(userId,money)){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }

}
