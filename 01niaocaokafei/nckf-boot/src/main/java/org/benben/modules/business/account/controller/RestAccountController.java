package org.benben.modules.business.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.constant.CommonConstant;
import org.benben.common.menu.ResultEnum;
import org.benben.common.util.PasswordUtil;
import org.benben.modules.business.account.entity.Account;
import org.benben.modules.business.account.service.IAccountService;
import org.benben.modules.business.commen.service.ISMSService;
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
	private ISMSService ismsService;



    @GetMapping(value = "/queryAccount")
    @ApiOperation(value = "账单/钱包详情", tags = {"用户接口"}, notes = "账单/钱包详情")
    public RestResponseBean queryAccount() {

		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if(user == null){
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

        Account account = accountService.queryByUserId(user.getId());

        if(account==null) {
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),account);
    }

    @GetMapping(value = "/isPayPassword")
    @ApiOperation(value = "是否设置支付密码", tags = {"用户接口"}, notes = "是否设置支付密码")
    public RestResponseBean isPayPassword(){

		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if(user == null){
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

        if(accountService.isPayPassword(user.getId())){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),"未设置支付密码",null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),"已设置支付密码",null);
    }


    @GetMapping(value = "isWithdrawAccount")
    @ApiOperation(value = "是否设置收款账户", tags = {"用户接口"}, notes = "是否设置收款账户")
    public RestResponseBean isWithdrawAccount(String userId){

		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if(user == null){
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

        if(accountService.isWithdrawAccount(userId)){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),"未设置收款账户",null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),"已设置收款账户",null);
    }

    @GetMapping("/checkPayPassword")
    @ApiOperation(value = "支付密码是否正确",tags = {"用户接口"},notes = "支付密码是否正确")
	@ApiImplicitParam(name = "payPassword",value = "支付密码",dataType = "String",required = true)
    public RestResponseBean checkPayPassword(String payPassword){

		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if(user == null){
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

        Account account = accountService.queryByUserId(user.getId());

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
			@ApiImplicitParam(name = "mobile",value = "用户手机号",dataType = "String",required = true),
			@ApiImplicitParam(name = "event",value = "事件",dataType = "String",required = true),
			@ApiImplicitParam(name = "captcha",value = "验证码",dataType = "String",required = true),
			@ApiImplicitParam(name = "payPassword",value = "用户支付密码",dataType = "String",required = true)
	})
    public RestResponseBean resetPayPassword(@RequestParam String mobile, @RequestParam String event, @RequestParam String captcha,@RequestParam String payPassword){

		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if(user == null){
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

		if(StringUtils.isBlank(mobile)|| StringUtils.isBlank(event)|| StringUtils.isBlank(captcha) || StringUtils.isBlank(payPassword)){
			return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(),ResultEnum.PARAMETER_MISSING.getDesc(),null);
		}

		if(!StringUtils.equals(user.getMobile(),mobile)){
			return new RestResponseBean(ResultEnum.MOBILE_ERROR.getValue(),ResultEnum.MOBILE_ERROR.getDesc(),null);
		}

		if(!org.apache.commons.lang3.StringUtils.equals(CommonConstant.SMS_EVENT_CHANGE_PAY_PWD,event)){
			return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(),ResultEnum.SMS_CODE_ERROR.getDesc(),null);
		}

		switch (ismsService.check(mobile, event, captcha)) {
			case 1:
				return new RestResponseBean(ResultEnum.SMS_CODE_OVERTIME.getValue(), ResultEnum.SMS_CODE_OVERTIME.getDesc(), null);
			case 2:
				return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(), ResultEnum.SMS_CODE_ERROR.getDesc(), null);
		}

        if (accountService.resetPayPassword(user.getId(),payPassword)){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }


}