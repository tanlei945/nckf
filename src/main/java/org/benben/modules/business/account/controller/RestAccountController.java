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




@RestController
@RequestMapping("/api/v1/account")
@Slf4j
@Api(tags = {"用户接口"})
public class RestAccountController {

   @Autowired
   private IAccountService accountService;

	@Autowired
	private ISMSService ismsService;



    /**
     * showdoc
     * @catalog 用户接口
     * @title 账单/钱包详情
     * @description 账单/钱包详情
     * @method GET
     * @url /nckf-boot/api/v1/account/queryAccount
     * @return { "code": 1, "data": { "accountNo": null, "accountType": null, "createBy": "benben-boot", "createTime": 1560999578000, "id": "03e530a22b6338053b84067fd06483b9", "money": 0,"payPassword": null,"salt": null,"updateBy": null,"updateTime": null,"userId": "4b1f0ae074d2d117d0295680523fa9f8","vipFlag": null},"msg": "操作成功","time": "1561011407208"}
     * @return_param code String 响应状态
     * @return_param accountNo String 支付账号或微信账号或银行卡号
     * @return_param accountType String 支付方式
     * @return_param id String 用户账户id
     * @return_param money Double 余额
     * @return_param payPassword String 支付密码
     * @return_param salt String 密码盐
     * @return_param vipFlag String 是否是Vip(0:非会员 1：会员)
     * @return_param userId String 用户id
     * @return_param createBy String 创建者
     * @return_param createTime Date 创建时间
     * @return_param updateBy String 更新人
     * @return_param updateTime Date 更新时间
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 1
     */
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
    /**
     * showdoc
     * @catalog 用户接口
     * @title 是否设置支付密码
     * @description 是否设置支付密码
     * @method GET
     * @url /nckf-boot/api/v1/account/isPayPassword
     * @return {"code": 1,"data": null,"msg": "已设置支付密码","time": "1561014921063"}
     * @return_param code String 响应状态
     * @return_param data String 没有含义
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 2
     */
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

    /**
     * showdoc
     * @catalog 用户接口
     * @title 是否设置收款账户
     * @description 是否设置收款账户
     * @method GET
     * @url /nckf-boot/api/v1/account/isWithdrawAccount
     * @param userId 必填 String 用户id
     * @return {"code": 1,"data": null,"msg": "已设置收款账户","time": "1561014836809"}
     * @return_param code String 响应状态
     * @return_param data String 没有含义
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 3
     */
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

    /**
     * showdoc
     * @catalog 用户接口
     * @title 是否设置收款账户
     * @description 是否设置收款账户
     * @method GET
     * @url /nckf-boot/api/v1/account/checkPayPassword
     * @param payPassword 必填 String 支付密码
     * @return {"code": 1,"data": null,"msg": "支付密码正确","time": "1561015159420"}
     * @return_param code String 响应状态
     * @return_param data String 没有含义
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 4
     */
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

    /**
     * showdoc
     * @catalog 用户接口
     * @title 重置支付密码
     * @description 重置支付密码
     * @method GET
     * @url /nckf-boot/api/v1/account/resetPayPassword
     * @param captcha 必填 String 验证码
     * @param event 必填 String 事件
     * @param mobile 必填 String 用户手机号
     * @param payPassword 必填 String 用户支付密码
     * @return {"code": 1,"data": null,"msg": "操作成功","time": "1561015116988"}
     * @return_param code String 响应状态
     * @return_param data String 没有含义
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 5
     */
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
