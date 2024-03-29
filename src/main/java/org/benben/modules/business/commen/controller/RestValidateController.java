package org.benben.modules.business.commen.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.modules.business.commen.service.IEmailServe;
import org.benben.modules.business.commen.service.IValidateService;
import org.benben.modules.business.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 检验接口
 */
@RestController
@RequestMapping(value = "/api/v1/validate")
//@Api(tags = {"检测接口"})
public class RestValidateController {

    @Autowired
    private IValidateService validateService;

    @Autowired
    private IEmailServe emailServe;

    @GetMapping(value = "/checkEmailAvailable")
    //@ApiOperation(value = "检测邮箱",tags = {"检测接口"},notes = "检测邮箱")
    public RestResponseBean checkEmailAvailable(@RequestParam(name = "email")String email, @RequestParam(name="id")String id){
        String msg ="";
        int code = 0;
        try {
            User userInfo = validateService.userInfoValidate(id);
            if(null!=userInfo){
                code = email.equals(userInfo.getEmail())?0:1;
                msg = email.equals(userInfo.getEmail())?"邮箱已经被占用":"";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        RestResponseBean RestResponseBean = new RestResponseBean(code,msg,null);
        return RestResponseBean;
    }
    @GetMapping("/checkEmailExist")
    //@ApiOperation(value = "检测邮箱",tags = {"检测接口"},notes = "检测邮箱")
    public RestResponseBean checkEmailExist(@RequestParam(name = "mobile")String email) {
        String msg = "";
        int code = 0;
        String i = validateService.isExistEmail(email);
        code = "".equals(i)?0:1;
        msg = "".equals(i)?"邮箱不存在":"邮箱已经被占用";
        RestResponseBean RestResponseBean = new RestResponseBean(code, msg, null);
        return RestResponseBean;
    }
    @GetMapping(value = "/checkUsernameAvailable")
    //@ApiOperation(value = "检测用户名",tags = {"检测接口"},notes = "检测用户名")
    public RestResponseBean checkUsernameAvailable(@RequestParam(name = "username")String username, @RequestParam(name="id")String id){
        String msg ="";
        int code = 0;
        try {
            User user = validateService.userInfoValidate(id);
            if(null!=user){
                code = username.equals(user.getEmail())?0:1;
                msg = username.equals(user.getEmail())?"用户名已经被占用":"";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        RestResponseBean RestResponseBean = new RestResponseBean(code,msg,null);
        return RestResponseBean;
    }
    @GetMapping(value = "/checkMobileAvailable")
    //@ApiOperation(value = "检测手机",tags = {"检测接口"},notes = "检测手机")
    public RestResponseBean checkMobileAvailable(@RequestParam(name = "mobile")String mobile, @RequestParam(name="id")String id){
        String msg ="";
        int code = 0;
        try {
            User user = validateService.userInfoValidate(id);
            if(null!=user){
                code = mobile.equals(user.getMobile())?0:1;
                msg = mobile.equals(user.getMobile())?"用户名已经被占用":"";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        RestResponseBean RestResponseBean = new RestResponseBean(code,msg,null);
        return RestResponseBean;
    }

    @GetMapping("/checkMobileExist")
    //@ApiOperation(value = "检测手机",tags = {"检测接口"},notes = "检测手机")
    public RestResponseBean checkMobileExist(@RequestParam(name = "mobile")String mobile) {
        String msg = "";
        int code = 0;
        String i = validateService.isExistMobile(mobile);
        code = "".equals(i)?0:1;
        msg = "".equals(i)?"邮箱不存在":"邮箱已经被占用";
        RestResponseBean RestResponseBean = new RestResponseBean(code, msg, null);
        return RestResponseBean;
    }
    @GetMapping("/checkEmsCorrect")
    //@ApiOperation(value = "检测邮箱验证码",tags = {"检测接口"},notes = "检测邮箱验证码")
    public RestResponseBean checkEmsCorrect(@RequestParam(name = "email")String email, @RequestParam(name = "captcha")String captcha,
                                            @RequestParam(name = "event")String event) {
        Boolean aBoolean = validateService.captchaValidate(email,captcha,event);
        if(aBoolean){
            return new RestResponseBean(0, "验证成功", null);
        }else{
            return new RestResponseBean(1, "验证失败", null);
        }
    }


    @GetMapping("/checkEmsMobile")
    public RestResponseBean checkEmsMobile(@RequestParam(name = "mobile")String mobile, @RequestParam(name = "captcha")String captcha,
                                             @RequestParam(name = "event")String event) {
        Boolean aBoolean = validateService.captchaValidate_mobile(mobile,captcha,event);
        if(aBoolean){
            return new RestResponseBean(0, "验证成功", null);
        }else{
            return new RestResponseBean(1, "验证失败", null);
        }
    }


    @GetMapping("/send")
    //@ApiOperation(value = "发送验证码",tags = {"检测接口"},notes = "发送验证码")
    public RestResponseBean offerCaptcha(@RequestParam(name = "email")String email, @RequestParam(name = "event")String event) {
        Boolean aBoolean = emailServe.offerCaptcha(email,event);
        if(aBoolean){
            return new RestResponseBean(200, "发送成功", null);
        }else{
            return new RestResponseBean(200, "发送失败", null);
        }
    }
}
