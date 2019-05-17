package org.benben.modules.business.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
@RequestMapping("/api/v1/account")
@Slf4j
@Api(tags = {"用户接口"})
public class RestAccountController {

   @Autowired
   private IAccountService accountService;

   @Autowired
   private IUserService userService;



    @GetMapping(value = "/queryAccount")
    @ApiOperation(value = "账单/钱包详情", tags = {"用户接口"}, notes = "账单/钱包详情")
    @ApiImplicitParam(name = "userId",value = "用户的ID",dataType = "String",required = true)
    public RestResponseBean queryAccount(@RequestParam String userId) {

        Account account = accountService.queryByUserId(userId);

        if(account==null) {
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),account);
    }

    @GetMapping(value = "/isPayPassword")
    @ApiOperation(value = "是否设置支付密码", tags = {"用户接口"}, notes = "是否设置支付密码")
    @ApiImplicitParam(name = "userId",value = "用户的ID",dataType = "String",required = true)
    public RestResponseBean isPayPassword(String userId){

        if(accountService.isPayPassword(userId)){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),"未设置支付密码",null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),"已设置支付密码",null);
    }


    @GetMapping(value = "isWithdrawAccount")
    @ApiOperation(value = "是否设置收款账户", tags = {"用户接口"}, notes = "是否设置收款账户")
    @ApiImplicitParam(name = "userId",value = "用户的ID",dataType = "String",required = true)
    public RestResponseBean isWithdrawAccount(String userId){

        if(accountService.isWithdrawAccount(userId)){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),"未设置收款账户",null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),"已设置收款账户",null);
    }

    @GetMapping("/checkPayPassword")
    @ApiOperation(value = "支付密码是否正确",tags = {"用户接口"},notes = "支付密码是否正确")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户的ID",dataType = "String",required = true),
            @ApiImplicitParam(name = "payPassword",value = "支付密码",dataType = "String",required = true)
    })
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


    @GetMapping(value = "/resetPayPassword")
    @ApiOperation(value = "重置支付密码", tags = {"用户接口"}, notes = "重置支付密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户的ID",dataType = "String",required = true),
            @ApiImplicitParam(name = "newPayPassword",value = "支付密码",dataType = "String",required = true)
    })
    public RestResponseBean resetPayPassword(@RequestParam String userId,@RequestParam String newPayPassword){

        if (accountService.resetPayPassword(userId,newPayPassword)){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }


}
