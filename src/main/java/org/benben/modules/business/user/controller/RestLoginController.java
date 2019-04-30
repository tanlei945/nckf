package org.benben.modules.business.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.constant.CommonConstant;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.api.ISysBaseAPI;
import org.benben.common.util.PasswordUtil;
import org.benben.common.util.RedisUtil;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.account.entity.Account;
import org.benben.modules.business.account.service.IAccountService;
import org.benben.modules.business.commen.dto.SmsDTO;
import org.benben.modules.business.commen.service.*;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.entity.UserThird;
import org.benben.modules.business.user.service.IUserService;
import org.benben.modules.business.user.service.IUserThirdService;
import org.benben.modules.business.user.vo.UserStoreVo;
import org.benben.modules.business.userstore.entity.UserStore;
import org.benben.modules.business.userstore.service.IUserStoreService;
import org.benben.modules.shiro.authc.util.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @Title: RestLoginController
 * @Description: 用户
 * @author： jeecg-boot
 * @date： 2019-04-19
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/login")
@Api(tags = {"登录接口"})
@Slf4j
public class RestLoginController {
    @Autowired
    private IUserService userService;

    @Autowired
    private IUserThirdService userThirdService;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Autowired
    private ISMSService ismsService;

    @Autowired
    private IWxService iWxService;

    @Autowired
    private IWbService iWbService;

    @Autowired
    private IQqService iQqService;

    @Autowired
    private IAccountService iAccountService;

    @Autowired
    private IUserStoreService userStoreService;

    @Autowired
    private RedisUtil redisUtil;



    /**
     * 用户注册
     * @param mobile
     * @param password
     * @return
     */
    @PostMapping(value = "/user_register")
    @ApiOperation(value = "用户注册", tags = {"登录接口"}, notes = "用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile",value = "用户手机号",dataType = "String",defaultValue = "1",required = true),
            @ApiImplicitParam(name = "password",value = "用户密码",dataType = "String",defaultValue = "1",required = true)
    })
    public RestResponseBean register(@RequestParam String mobile, @RequestParam String password) {

        User user = new User();
        Account account = new Account();

        if(StringUtils.equals(mobile,"")||StringUtils.equals(password,"")){
            return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(),ResultEnum.PARAMETER_MISSING.getDesc(),null);
        }

        try {
            //保存用户信息
            user.setMobile(mobile);
            String salt = oConvertUtils.randomGen(8);
            user.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(mobile, password, salt);
            user.setPassword(passwordEncode);
            userService.save(user);
            //生成钱包
            account.setUserId(user.getId());
            iAccountService.save(account);
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),user);

        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);

    }

    /**
     * 骑手注册
     * @param userStoreVo
     * @return
     */
    @PostMapping(value = "/rider_register")
    @ApiOperation(value = "骑手注册", tags = {"登录接口"}, notes = "骑手注册")
    public RestResponseBean riderRegister(@RequestBody UserStoreVo userStoreVo) {

        User user = new User();
        UserStore userStore = new UserStore();
        Account account = new Account();

        try {

            BeanUtils.copyProperties(userStoreVo,user);
            BeanUtils.copyProperties(userStoreVo,userStore);
//            user.setUsername(userStoreVo.getMobile());
            user.setUserType("1");
            String salt = oConvertUtils.randomGen(8);
            user.setSalt(salt);
            String passwordEncode = PasswordUtil.encrypt(user.getMobile(), CommonConstant.NCKF_PWD, salt);
            user.setPassword(passwordEncode);
            userService.save(user);
            // 保存骑手信息
            userStore.setUserId(user.getId());
            userStore.setCompleteFlag("0");
            userStoreService.save(userStore);
            // 生成账单
            account.setUserId(user.getId());
            iAccountService.save(account);
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),user);

        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),user);

    }


    /**
     * 账户密码登录
     * @param mobile
     * @param password
     * @return
     */
    @PostMapping(value = "/login")
    @ApiOperation(value = "账户密码登录", tags = {"登录接口"}, notes = "账户密码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile",value = "用户手机号",dataType = "String",defaultValue = "1",required = true),
            @ApiImplicitParam(name = "password",value = "用户密码",dataType = "String",defaultValue = "1",required = true)
    })
    public RestResponseBean login(@RequestParam String mobile, @RequestParam String password) {

        JSONObject obj = new JSONObject();
        User user = userService.queryByMobile(mobile);

        if (user == null) {
            sysBaseAPI.addLog("登录失败，用户名:" + mobile + "不存在！", CommonConstant.LOG_TYPE_1, null);
            return new RestResponseBean(ResultEnum.USER_NOT_EXIST.getValue(), ResultEnum.USER_NOT_EXIST.getDesc(), null);
        } else {
            //密码验证
            String userpassword = PasswordUtil.encrypt(mobile, password, user.getSalt());
            String syspassword = user.getPassword();
            if (!syspassword.equals(userpassword)) {
                return new RestResponseBean(ResultEnum.USER_PWD_ERROR.getValue(), ResultEnum.USER_PWD_ERROR.getDesc(), null);
            }
            //调用公共方法
            obj = tokenBuild(user);
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), obj);
    }

    /**
     * 退出
     *
     * @return
     */
    @GetMapping("/logOut")
    @ApiOperation(value = "退出", tags = {"登录接口"}, notes = "退出")
    public RestResponseBean logOut() {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String token = JwtUtil.sign(CommonConstant.SIGN_MEMBER_USER + user.getMobile(), user.getPassword());
        redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
        Subject subject = SecurityUtils.getSubject();
        subject.logout();

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
    }

    /**
     * 短信登录
     *
     * @param smsDTO
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/mobile_login")
    @ApiOperation(value = "手机验证码登录", tags = {"登录接口"}, notes = "手机验证码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile",value = "用户手机号",dataType = "String",defaultValue = "1",required = true),
            @ApiImplicitParam(name = "password",value = "用户密码",dataType = "String",defaultValue = "1",required = true)
    })
    public RestResponseBean mobilelogin(@RequestBody @Valid SmsDTO smsDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors() && StringUtils.isBlank(smsDTO.getCaptcha())) {
            return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(), ResultEnum.PARAMETER_MISSING.getDesc(), null);
        }

        switch (ismsService.check(smsDTO.getMobile(), smsDTO.getEvent(), smsDTO.getCaptcha())) {
            case 1:
                return new RestResponseBean(ResultEnum.SMS_CODE_OVERTIME.getValue(), ResultEnum.SMS_CODE_OVERTIME.getDesc(), null);
            case 2:
                return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(), ResultEnum.SMS_CODE_ERROR.getDesc(), null);
        }

        User user = userService.queryByMobile(smsDTO.getMobile());

        if (user == null) {
            return new RestResponseBean(ResultEnum.USER_NOT_EXIST.getValue(), ResultEnum.USER_NOT_EXIST.getDesc(), null);
        }

        return new RestResponseBean(ResultEnum.LOGIN_SUCCESS.getValue(), ResultEnum.LOGIN_SUCCESS.getDesc(), tokenBuild(user));
    }

//    /**
//     * 三方登录(后端调起授权)
//     * @param platform
//     * @param mobile
//     */
//    @GetMapping(value = "/third")
//    @ApiOperation(value = "三方登录", tags = {"用户接口"}, notes = "三方登录")
//    public void third(@RequestParam String platform,@RequestParam String mobile,HttpServletResponse response){
//
//        switch (platform){
//            case "1":
//                iQqService.login(mobile,response);
//                break;
//            case "2":
//                iWxService.wxLogin(mobile, response);
//                break;
//            default:
//                iWbService.login(mobile, response);
//                break;
//        }
//
//    }

    /**
     * 三方登录(后端调起授权)
     * @param platform
     * @param mobile
     */
    @GetMapping(value = "/third")
    @ApiOperation(value = "三方登录", tags = {"登录接口"}, notes = "三方登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "platform",value = "平台类型('1':QQ,'2':微信,'3':微博)",dataType = "String",defaultValue = "1",required = true),
            @ApiImplicitParam(name = "mobile",value = "用户密码",dataType = "String",defaultValue = "1",required = true)
    })
    public void third(@RequestParam String platform,@RequestParam String mobile,HttpServletResponse response){

        switch (platform){
            case "1":
                iQqService.login(mobile,response);
                break;
            case "2":
                iWxService.wxLogin(mobile, response);
                break;
            default:
                iWbService.login(mobile, response);
                break;
        }

    }

//    /**
//     * 三方登录(IP端发起授权)
//     * @param platform 登录平台类型c
//     * @param code 回执码
//     * @return
//     */
//    @GetMapping(value = "/ip_third")
//    @ApiImplicitParams(value = {
//            @ApiImplicitParam(name = "platform", value = "登录平台类型", required = true, paramType = "query", dataType = "String"),
//            @ApiImplicitParam(name = "code", value = "回执码", required = true, paramType = "query", dataType = "String"),
//    })
//    @ApiOperation(value = "手机验证码登录", tags = {"用户接口"}, notes = "手机验证码登录")
//    public RestResponseBean third(HttpServletRequest request) {
//
//        String mobile = "";
//        String openid = "";
//        String platform = request.getParameter("platform");
//
//        User user = (User) SecurityUtils.getSubject().getPrincipal();
//        if(user != null){
//            mobile = user.getMobile();
//        }
//
//        switch (platform){
//            case "0":
//                //获取回调
//                openid = iQqService.backCallBack(request);
//                break;
//            case "1":
//                //获取回调
//                openid = iWxService.callBack(request);
//                break;
//            case "2":
//                //获取回调
//                openid = iWbService.callBack(request);
//                break;
//            default:
//                return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(),ResultEnum.PARAMETER_MISSING.getDesc(),null);
//        }
//
//        return publicCallBack(openid,mobile,platform);
//    }


    /**
     * qq登录回调
     * @param request
     * @return
     */
    @GetMapping(value = "/qq_login_callback")
    public RestResponseBean qqLoginCallback(HttpServletRequest request) {

        //获取回调
        String openid = iQqService.backCallBack(request);
        String mobile = request.getParameter("state");

        return publicCallBack(openid,mobile,"0");
    }


    /**
     * 微信回调
     * @param request
     * @return
     */
    @GetMapping(value = "/wx_login_callBack")
    public RestResponseBean wxCallBack(HttpServletRequest request) {

        User userEntity = null;
        //获取回调
        String openid = iWxService.callBack(request);
        String mobile = request.getParameter("state");

        return publicCallBack(openid,mobile,"1");
    }


    /**
     * 微博回调
     * @param request
     * @return
     */
    @GetMapping(value = "/wb_login_callback")
    public RestResponseBean wbCallBack(HttpServletRequest request) {

        User userEntity = null;
        //获取回调
        String openid = iWbService.callBack(request);
        String mobile = request.getParameter("state");

        return publicCallBack(openid,mobile,"2");
    }

    @GetMapping(value = "is_exist_mobile")
    @ApiOperation(value = "手机号是否已被注册",tags = {"登录接口"},notes = "手机号是否已被注册")
    @ApiImplicitParam(name = "mobile",value = "用户手机号",dataType = "String",defaultValue = "1",required = true)
    public RestResponseBean isExistMobile(String mobile){

        User user = userService.queryByMobile(mobile);

        if(user == null){
            return new RestResponseBean(ResultEnum.MOBILE_NOT_EXIST.getValue(),ResultEnum.MOBILE_NOT_EXIST.getDesc(),null);
        }

        return new RestResponseBean(ResultEnum.MOBILE_EXIST.getValue(),ResultEnum.MOBILE_EXIST.getDesc(),null);
    }



    /**
     * 公共方法,三方登录公共回调
     * @param openid
     * @param mobile
     * @param type
     * @return
     */
    private RestResponseBean publicCallBack(String openid,String mobile,String type){

        User userEntity = null;

        //返回空则表明回调异常
        if (StringUtils.isBlank(openid)) {
            return new RestResponseBean(ResultEnum.ERROR.getValue(), ResultEnum.ERROR.getDesc(), null);
        }
        //查询登录状态
        User user = userService.queryByMobile(mobile);
        //查询是否已有账户绑定该微博
        UserThird userThird = userThirdService.queryByOpenid(openid);
        if(userThird != null){
            //查询绑定的用户信息
            userEntity = userService.getById(userThird.getUserId());
        }
        //未登录
        if (user == null) {
            //未登录,未绑定
            if (userEntity == null) {
                return new RestResponseBean(ResultEnum.UNBOUND_OPENID.getValue(), ResultEnum.UNBOUND_OPENID.getDesc(), null);
            }
            //未登录,已绑定,返回登录信息token等
            return new RestResponseBean(ResultEnum.LOGIN_SUCCESS.getValue(), ResultEnum.LOGIN_SUCCESS.getDesc(), tokenBuild(userEntity));
        }
        //已登录,未绑定
        if (userEntity == null) {

            switch (type){
                case "0":
                    //绑定QQ
                    if (userService.bindingThird(openid,user.getId(),"0") == 1) {
                        return new RestResponseBean(ResultEnum.BINDING_SUCCESS.getValue(), ResultEnum.BINDING_SUCCESS.getDesc(), null);
                    }
                    break;
                case "1":
                    //绑定微信
                    if (userService.bindingThird(openid,user.getId(),"1") == 1) {
                        return new RestResponseBean(ResultEnum.BINDING_SUCCESS.getValue(), ResultEnum.BINDING_SUCCESS.getDesc(), null);
                    }
                    break;
                default :
                    //绑定微博
                    if (userService.bindingThird(openid,user.getId(),"2") == 1) {
                        return new RestResponseBean(ResultEnum.BINDING_SUCCESS.getValue(), ResultEnum.BINDING_SUCCESS.getDesc(), null);
                    }
                    break;
            }

            //绑定失败
            return new RestResponseBean(ResultEnum.BINDING_FAIL.getValue(), ResultEnum.BINDING_FAIL.getDesc(), null);
        }
        //已登录,禁止重复绑定
        return new RestResponseBean(ResultEnum.REPEATED_BINDING.getValue(), ResultEnum.REPEATED_BINDING.getDesc(), null);
    }

    /**
     * 公共方法,三方登录返回信息token
     *
     * @param user 根据传参查询到的实体
     * @return
     */
    private JSONObject tokenBuild(User user) {

        JSONObject obj = new JSONObject();
        //生成token
        String token = JwtUtil.signUser(CommonConstant.SIGN_MEMBER_USER + user.getMobile(), user.getPassword());
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        //设置超时时间
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME / 1000);

        obj.put("token", token);
        obj.put("user", user);

        sysBaseAPI.addLog("手机号: " + user.getMobile() + ",登录成功！", CommonConstant.LOG_TYPE_1, null);

        return obj;
    }


}
