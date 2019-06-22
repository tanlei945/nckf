package org.benben.modules.business.commen.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.constant.CommonConstant;
import org.benben.common.menu.ResultEnum;
import org.benben.common.menu.SMSResultEnum;
import org.benben.common.util.DateUtils;
import org.benben.common.util.RedisUtil;
import org.benben.modules.business.commen.dto.SmsDTO;
import org.benben.modules.business.commen.service.ISMSService;
import org.benben.modules.business.message.service.IMessageService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;



@Slf4j
@RestController
@RequestMapping("/api/v1/sms/")
@Api(tags = {"短信接口"})
public class RestSmsController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ISMSService ismsService;

    @Autowired
    private IUserService userService;


    @PostMapping(value = "/thirdSend")
//    @ApiOperation(value = "发送验证码 ",tags = {"短信接口"},notes = "发送验证码 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile",value = "手机号",dataType = "String",required = true),
            @ApiImplicitParam(name = "event",value = "事件",dataType = "String",defaultValue = "register",required = true)
    })
    public RestResponseBean thirdSend(@RequestParam String mobile,@RequestParam String event) {

        if (StringUtils.isBlank(mobile)|| StringUtils.isBlank(event)) {
            return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(), ResultEnum.PARAMETER_MISSING.getDesc(), null);
        }

        String returncode =  ismsService.send(mobile,event);

        if(returncode == null || StringUtils.equals(returncode,"")){
            return new RestResponseBean(ResultEnum.SMS_SEND_FAIL.getValue(), ResultEnum.SMS_SEND_FAIL.getDesc(), null);
        }

        String message = this.getResult(returncode);

        if (!StringUtils.equals(message,SMSResultEnum.SEND_SUCCESS.getMessage())) {
            return new RestResponseBean(ResultEnum.SMS_SEND_FAIL.getValue(), message, null);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), "发送成功", null);

    }

    @PostMapping(value = "/aliSend")
//    @ApiOperation(value = "使用阿里云发送验证码 ",tags = {"短信接口"},notes = "使用阿里云发送验证码 ")
    public RestResponseBean aliSend(@RequestParam String mobile,@RequestParam String event) {

        if (StringUtils.isBlank(mobile)|| StringUtils.isBlank(event)) {
            return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(), ResultEnum.PARAMETER_MISSING.getDesc(), null);
        }

        //发送短信
        SendSmsResponse sendSmsResponse =  ismsService.aliSend(mobile,event);

        log.error("短信返回请求时间"+ DateUtils.now() +"返回code"+sendSmsResponse.getCode());
        if(sendSmsResponse.getCode()!= null && sendSmsResponse.getCode().equals("OK")){

            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);
        }

        return new RestResponseBean(ResultEnum.SMS_SEND_FAIL.getValue(), ResultEnum.SMS_SEND_FAIL.getDesc(), null);

    }

    @PostMapping(value = "/tencentSend")
//    @ApiOperation(value = "使用腾讯云发送验证码 ",tags = {"短信接口"},notes = "使用腾讯云发送验证码 ")
    public RestResponseBean tencentSend(@RequestParam String mobile,@RequestParam String event) {

        if (StringUtils.isBlank(mobile)|| StringUtils.isBlank(event)) {
            return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(), ResultEnum.PARAMETER_MISSING.getDesc(), null);
        }

        //发送短信
        Integer code = (int) ((Math.random() * 9 + 1) * 100000);
        String sendMess = ismsService.tencentSendMesModel(mobile);

//        log.error("短信返回请求时间"+ DateUtils.now() +"返回code"+sendSmsResponse.getCode());
//        if(sendSmsResponse.getCode()!= null && sendSmsResponse.getCode().equals("OK")){
        if(sendMess != null){

            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);
        }

        return new RestResponseBean(ResultEnum.SMS_SEND_FAIL.getValue(), ResultEnum.SMS_SEND_FAIL.getDesc(), null);

    }

    /**
     * showdoc
     * @catalog 短信接口
     * @title 检验短信验证码
     * @description 检验短信验证码
     * @method GET
     * @url /nckf-boot/api/v1/sms/checkSend
     * @json_param mobile 必填 String 登录手机号
     * @json_param event 必填 String 短信事件
     * @return {"code": 1,"data": null,"msg": "短信验证通过","time": "1561015747083"}
     * @return_param code String 响应状态
     * @return_param data String 没有含义
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 
     * @number 2
     */
    @GetMapping(value = "/checkSend")
    @ApiOperation(value = "检验短信验证码", tags = {"短信接口"},notes = "检验短信验证码")
    public RestResponseBean checkSend(@Valid SmsDTO smsDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors() || StringUtils.isBlank(smsDTO.getCaptcha())) {
            return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(), ResultEnum.PARAMETER_MISSING.getDesc(), null);
        }

        int result = ismsService.check(smsDTO.getMobile(), smsDTO.getEvent(), smsDTO.getCaptcha());

        switch (result) {
            case 1:
                return new RestResponseBean(ResultEnum.SMS_CODE_OVERTIME.getValue(), ResultEnum.SMS_CODE_OVERTIME.getDesc(), null);
            case 2:
                return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(), ResultEnum.SMS_CODE_ERROR.getDesc(), null);
        }

        return new RestResponseBean(ResultEnum.SMS_VALIDATE_SUCCESS.getValue(), ResultEnum.SMS_VALIDATE_SUCCESS.getDesc(), null);
    }

    /**
     * 返回短信调用错误码
     * @param returncode
     * @return
     */
    protected String getResult(String returncode) {

        String message = "";

        if (StringUtils.isBlank(returncode) || returncode == null) {
            message = "发送失败";
        }

        for (SMSResultEnum smsEnum : SMSResultEnum.values()) {
            if (StringUtils.equals(smsEnum.getCode(), returncode)) {
                message = smsEnum.getMessage();
            }
        }

        return message;
    }

    /**
     * showdoc
     * @catalog 短信接口
     * @title 测试发送验证码
     * @description 测试发送验证码
     * @method POST
     * @url /nckf-boot/api/v1/sms/testSend
     * @param mobile 必填 String 登录手机号
     * @param event 必填 String 短信事件
     * @param captcha 选填 String 验证码
     * @return {"code": 1,"data": 842811,"msg": "验证码发送成功","time": "1561015290370"}
     * @return_param code String 响应状态
     * @return_param data String 验证码信息
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark
     * @number 1
     */
    @PostMapping(value = "/testSend")
    @ApiOperation(value = "测试发送验证码", tags = {"短信接口"},notes = "测试发送验证码")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "mobile",value = "手机号",dataType = "String",required = true),
			@ApiImplicitParam(name = "event",value = "login、register、forget、changePwd、changePayPwd、binding",dataType = "String",required = true)
	})
    public RestResponseBean testSend(@RequestParam String mobile, @RequestParam String event) {

        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(event)) {
            return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(), ResultEnum.PARAMETER_MISSING.getDesc(), null);
        }

//		User user = userService.queryByMobile(mobile);
//
//		if(StringUtils.equals(event,CommonConstant.SMS_EVENT_REGISTER) || StringUtils.equals(event,CommonConstant.SMS_EVENT_BINGDING)){ //注册、换绑
//
//			if(user != null ){
//				return new RestResponseBean(ResultEnum.MOBILE_EXIST_REGISTER.getValue(),ResultEnum.MOBILE_EXIST_REGISTER.getDesc(),null);
//			}
//
//		}else{ //忘记密码/重置支付密码/登录
//
//        	if(user == null){
//				return new RestResponseBean(ResultEnum.MOBILE_NOT_EXIST.getValue(),ResultEnum.MOBILE_NOT_REGISTER.getDesc(),null);
//			}
//
//		}

        Integer code = (int) ((Math.random() * 9 + 1) * 100000);

        redisUtil.set(mobile + "," + event, String.valueOf(code), 300);

        System.out.println("验证码:" + code);

        return new RestResponseBean(ResultEnum.SMS_SEND_SUCCESS.getValue(), ResultEnum.SMS_SEND_SUCCESS.getDesc(), code);

    }


}