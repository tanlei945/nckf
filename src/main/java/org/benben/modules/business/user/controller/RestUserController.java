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
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.constant.CommonConstant;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.api.ISysBaseAPI;
import org.benben.common.util.PasswordUtil;
import org.benben.common.util.RedisUtil;
import org.benben.modules.business.commen.dto.SmsDTO;
import org.benben.modules.business.commen.service.*;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.entity.UserThird;
import org.benben.modules.business.user.service.IUserService;
import org.benben.modules.business.user.service.IUserThirdService;
import org.benben.modules.business.user.vo.UserStoreVo;
import org.benben.modules.business.user.vo.UserVo;
import org.benben.modules.shiro.authc.util.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @Title: RestUserController
 * @Description: 用户
 * @author： jeecg-boot
 * @date： 2019-04-19
 * @version： V1.0
 */
@RestController
@RequestMapping("/api/v1/user")
@Api(tags = {"用户接口"})
@Slf4j
public class RestUserController {

	@Value(value = "${local.ip}")
	private String ip;

	@Value(value = "${server.servlet.context-path}")
	private String projectName;

	@Value(value = "${server.port}")
	private String port;

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserThirdService userThirdService;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Autowired
    private IWxService iWxService;

    @Autowired
    private IWbService iWbService;

    @Autowired
    private IQqService iQqService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ISMSService ismsService;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;



    /**
     * 用户详情
     * @return
     */
    @GetMapping(value = "/queryUserById")
    @ApiOperation(value = "用户详情", tags = {"用户接口"}, notes = "用户详情")
    public RestResponseBean queryUserById() {

		User user = (User) SecurityUtils.getSubject().getPrincipal();

        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

		UserVo userVo = userService.queryUserVo(user);
        if(StringUtils.isNotBlank(userVo.getAvatar()) && userVo.getAvatar().indexOf("http") == -1){
        	userVo.setAvatar(ip + ":" + port + projectName + "/" + userVo.getAvatar());
		}
        if(userVo == null){
        	return new RestResponseBean(ResultEnum.ERROR.getValue(),ResultEnum.ERROR.getDesc(),null);
		}

		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),userVo);
    }


    /**
     * 用户修改
     * @param userVo
     * @return
     */
    @PostMapping(value = "/editUser")
    @ApiOperation(value = "用户修改", tags = {"用户接口"}, notes = "用户修改")
    public RestResponseBean editUser(@RequestBody UserVo userVo) {

		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if(user==null) {
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

        User userEntity = userService.getById(user.getId());

        BeanUtils.copyProperties(userVo,userEntity);

        if(userEntity==null) {
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
        }else {

            boolean ok = userService.updateById(userEntity);

            if(!ok) {
                return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
            }
        }

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
    }

    @PostMapping(value = "/changeAvatar")
    @ApiOperation(value = "修改头像", tags = {"用户接口"}, notes = "修改头像")
	@ApiImplicitParam(name = "avatar",value = "头像图片",dataType = "String",required = true)
    public RestResponseBean changeAvatar(@RequestParam String avatar){

		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if(user==null) {
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

        User userEntity = userService.getById(user.getId());

		if(StringUtils.isNotBlank(userEntity.getAvatar())){
			userEntity.setOldAvatar(userEntity.getAvatar());
		}

		userEntity.setAvatar(avatar);

        if(userService.updateById(userEntity)){

            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.desc(),null);
        }

        return new RestResponseBean(ResultEnum.ERROR.getValue(),ResultEnum.ERROR.getDesc(),null);
    }

	@PostMapping(value = "/changeOldAvatar")
	@ApiOperation(value = "选择上一个头像", tags = {"用户接口"}, notes = "选择上一个头像")
	public RestResponseBean changeOldAvatar(){

		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if(user==null) {
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

		User userEntity = userService.getById(user.getId());

		if(StringUtils.isBlank(userEntity.getOldAvatar())){
			return new RestResponseBean(ResultEnum.NOT_EXIST_OLDAVATAR.getValue(),ResultEnum.NOT_EXIST_OLDAVATAR.getDesc(),null);
		}

		userEntity.setAvatar(userEntity.getOldAvatar());

		if(userService.updateById(userEntity)){

			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.desc(),null);
		}

		return new RestResponseBean(ResultEnum.ERROR.getValue(),ResultEnum.ERROR.getDesc(),null);
	}


    @PostMapping(value = "/changeUsername")
    @ApiOperation(value = "修改用户名", tags = {"用户接口"}, notes = "修改用户名")
	@ApiImplicitParam(name = "username",value = "用户名",dataType = "String",defaultValue = "1",required = true)
    public RestResponseBean changeUsername(@RequestParam String username){

		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if(user==null) {
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

		if(StringUtils.isBlank(username)){
            return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(),ResultEnum.PARAMETER_MISSING.getDesc(),null);
        }

        User userEntity = userService.getById(user.getId());
        if(userEntity == null){
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.desc(),null);
        }
		userEntity.setUsername(username);

        if(userService.updateById(userEntity)){

            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.desc(),null);
        }

        return new RestResponseBean(ResultEnum.ERROR.getValue(),ResultEnum.ERROR.getDesc(),null);
    }

    /**
     * 修改手机号
     * @param mobile
     * @return
     */
    @PostMapping(value = "/changeMobile")
    @ApiOperation(value = "修改手机号", tags = {"用户接口"}, notes = "修改手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile",value = "用户手机号",dataType = "String",required = true),
			@ApiImplicitParam(name = "event",value = "事件",dataType = "String",required = true),
			@ApiImplicitParam(name = "captcha",value = "验证码",dataType = "String",required = true)
    })
    public RestResponseBean changeMobile(@RequestParam String mobile,@RequestParam String event,@RequestParam String captcha) {

		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if(user==null) {
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

        if(StringUtils.isBlank(mobile)|| StringUtils.isBlank(event) || StringUtils.isBlank(captcha)){

            return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(),ResultEnum.PARAMETER_MISSING.desc(),null);
        }

		if(!StringUtils.equals(CommonConstant.SMS_EVENT_BINGDING,event)){
			return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(),ResultEnum.SMS_CODE_ERROR.getDesc(),null);
		}

		switch (ismsService.check(mobile, event, captcha)) {
			case 1:
				return new RestResponseBean(ResultEnum.SMS_CODE_OVERTIME.getValue(), ResultEnum.SMS_CODE_OVERTIME.getDesc(), null);
			case 2:
				return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(), ResultEnum.SMS_CODE_ERROR.getDesc(), null);
		}

        User userEntity = userService.getById(user.getId());

		userEntity.setMobile(mobile);

        if(userService.updateById(userEntity)){

            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.desc(),null);
        }

        return new RestResponseBean(ResultEnum.ERROR.getValue(),ResultEnum.ERROR.getDesc(),null);
    }


	@PostMapping(value = "/forgetPassword")
	@ApiOperation(value = "忘记密码", tags = {"用户接口"}, notes = "忘记密码")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "mobile",value = "用户手机号",dataType = "String",required = true),
			@ApiImplicitParam(name = "password",value = "用户新密码",dataType = "String",required = true),
			@ApiImplicitParam(name = "event",value = "事件",dataType = "String",defaultValue = CommonConstant.SMS_EVENT_FORGET,required = true),
			@ApiImplicitParam(name = "captcha",value = "验证码",dataType = "String",required = true)
	})
	public RestResponseBean forgetPassword(@RequestParam String mobile, @RequestParam String password,
			@RequestParam String event,@RequestParam String captcha){

		if(StringUtils.isBlank(mobile)||StringUtils.isBlank(password) || StringUtils.isBlank(event) ||StringUtils.isBlank(captcha)){
			return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(),ResultEnum.PARAMETER_MISSING.getDesc(),null);
		}

		if(!StringUtils.equals(CommonConstant.SMS_EVENT_FORGET,event)){
			return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(),ResultEnum.SMS_CODE_ERROR.getDesc(),null);
		}

		switch (ismsService.check(mobile, event, captcha)) {
			case 1:
				return new RestResponseBean(ResultEnum.SMS_CODE_OVERTIME.getValue(), ResultEnum.SMS_CODE_OVERTIME.getDesc(), null);
			case 2:
				return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(), ResultEnum.SMS_CODE_ERROR.getDesc(), null);
		}

		if(userService.changePassword(mobile,password) == 0){
			return new RestResponseBean(ResultEnum.ERROR.getValue(),ResultEnum.ERROR.getDesc(),null);
		}

		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
	}


	@PostMapping(value = "/changePassword")
	@ApiOperation(value = "修改密码", tags = {"用户接口"}, notes = "修改密码")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "oldPassword",value = "用户旧密码",dataType = "String"),
			@ApiImplicitParam(name = "newPassword",value = "用户新密码",dataType = "String"),
			@ApiImplicitParam(name = "mobile",value = "用户手机号",dataType = "String"),
			@ApiImplicitParam(name = "event",value = "事件",dataType = "String",defaultValue = CommonConstant.SMS_EVENT_CHANGE_PWD),
			@ApiImplicitParam(name = "captcha",value = "验证码",dataType = "String")
	})
	public RestResponseBean changePassword(@RequestParam String oldPassword, @RequestParam String newPassword,@RequestParam String mobile,
			@RequestParam String event,@RequestParam String captcha){

		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if(user == null){

			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

		User userEntity = userService.getById(user.getId()); //如果修改密码前端不设置重新登录,避免取到jwt中旧数据,应再次查询对象

		if(StringUtils.isBlank(mobile)){ //旧密码修改密码

			if(StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)){
				return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(),ResultEnum.PARAMETER_MISSING.getDesc(),null);
			}

			String passwordEncode = PasswordUtil.encrypt(oldPassword, oldPassword, userEntity.getSalt()); //输入的密码加密后

			if(!StringUtils.equals(userEntity.getPassword(),passwordEncode)){
				return new RestResponseBean(ResultEnum.PASSWORD_ERROR.getValue(),ResultEnum.PASSWORD_ERROR.getDesc(),null);
			}


		} else { //手机号修改密码

			if(StringUtils.isBlank(mobile)||StringUtils.isBlank(newPassword) || StringUtils.isBlank(event) ||StringUtils.isBlank(captcha)){
				return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(),ResultEnum.PARAMETER_MISSING.getDesc(),null);
			}

			if(!StringUtils.equals(CommonConstant.SMS_EVENT_CHANGE_PWD,event)){
				return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(),ResultEnum.SMS_CODE_ERROR.getDesc(),null);
			}

			switch (ismsService.check(mobile, event, captcha)) {
				case 1:
					return new RestResponseBean(ResultEnum.SMS_CODE_OVERTIME.getValue(), ResultEnum.SMS_CODE_OVERTIME.getDesc(), null);
				case 2:
					return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(), ResultEnum.SMS_CODE_ERROR.getDesc(), null);
			}

		}

		if(userService.changePassword(userEntity.getMobile(),newPassword) == 0){
			return new RestResponseBean(ResultEnum.ERROR.getValue(),ResultEnum.ERROR.getDesc(),null);
		}

		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
	}

    /**
     * 根据姓名查找
     *
     * @param username
     * @return
     */
    @GetMapping(value = "/queryByName")
    public User queryByName(@RequestParam String username) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        User user = userService.getOne(userQueryWrapper);
        return user;
    }

    /**
     * 用户注册
     * @param mobile
     * @param password
     * @return
     */
    @PostMapping(value = "/userRegister")
    @ApiOperation(value = "用户注册", tags = {"用户接口"}, notes = "用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile",value = "用户手机号",dataType = "String",required = true),
            @ApiImplicitParam(name = "password",value = "用户密码",dataType = "String",required = true),
            @ApiImplicitParam(name = "event",value = "事件",dataType = "String",required = true),
            @ApiImplicitParam(name = "captcha",value = "验证码",dataType = "String",required = true)
    })
    public RestResponseBean userRegister(@RequestParam String mobile, @RequestParam String password, @RequestParam String event, @RequestParam String captcha) {

		if(StringUtils.isBlank(mobile)||StringUtils.isBlank(password) || StringUtils.isBlank(event) ||StringUtils.isBlank(captcha)){
			return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(),ResultEnum.PARAMETER_MISSING.getDesc(),null);
		}

		if(!StringUtils.equals(CommonConstant.SMS_EVENT_REGISTER,event)){
			return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(),ResultEnum.SMS_CODE_ERROR.getDesc(),null);
		}

        try {

			switch (ismsService.check(mobile, event, captcha)) {
				case 1:
					return new RestResponseBean(ResultEnum.SMS_CODE_OVERTIME.getValue(), ResultEnum.SMS_CODE_OVERTIME.getDesc(), null);
				case 2:
					return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(), ResultEnum.SMS_CODE_ERROR.getDesc(), null);
			}

			User userEntity = userService.queryByMobile(mobile);
			if(userEntity != null){
				return new RestResponseBean(ResultEnum.MOBILE_EXIST_REGISTER.getValue(),ResultEnum.MOBILE_EXIST_REGISTER.getDesc(),null);
			}

			User user = userService.userRegister(mobile, password);

			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),tokenBuild(user));

        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
			return new RestResponseBean(ResultEnum.ERROR.getValue(),ResultEnum.ERROR.getDesc(),null);
        }



    }

    /**
     * 骑手注册
     * @param userStoreVo
     * @return
     */
    @PostMapping(value = "/riderRegister")
    @ApiOperation(value = "骑手注册", tags = {"用户接口"}, notes = "骑手注册")
    public RestResponseBean riderRegister(@RequestBody UserStoreVo userStoreVo) {

        try {

			User user = userService.riderRegister(userStoreVo);

			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),tokenBuild(user));

        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }

        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);

    }


    /**
     * 账户密码登录
     * @param mobile
     * @param password
     * @return
     */
    @PostMapping(value = "/login")
    @ApiOperation(value = "账户密码登录", tags = {"用户接口"}, notes = "账户密码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile",value = "用户手机号",dataType = "String",required = true),
            @ApiImplicitParam(name = "password",value = "用户密码",dataType = "String",required = true)
    })
    public RestResponseBean login(@RequestParam String mobile, @RequestParam String password) {

		Map<String,Object> obj = new HashMap<>();
        User user = userService.queryByMobile(mobile);

        if (user == null) {
            sysBaseAPI.addLog("登录失败，用户名:" + mobile + "不存在！", CommonConstant.LOG_TYPE_1, null);
            return new RestResponseBean(ResultEnum.USER_NOT_EXIST.getValue(), ResultEnum.USER_NOT_EXIST.getDesc(), null);
        } else {
            //密码验证
            String userpassword = PasswordUtil.encrypt(password, password, user.getSalt());
            String syspassword = user.getPassword();
            if (!syspassword.equals(userpassword)) {
                return new RestResponseBean(ResultEnum.USER_PWD_ERROR.getValue(), ResultEnum.USER_PWD_ERROR.getDesc(), null);
            }
            //调用公共方法
            obj = tokenBuild(user);
        }

        return new RestResponseBean(ResultEnum.LOGIN_SUCCESS.getValue(), ResultEnum.LOGIN_SUCCESS.getDesc(), obj);
    }


	/**
	 * 短信登录
	 * @param mobile
	 * @param event
	 * @param captcha
	 * @return
	 */
    @PostMapping(value = "/mobileLogin")
    @ApiOperation(value = "手机验证码登录", tags = {"用户接口"}, notes = "手机验证码登录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "mobile",value = "用户手机号",dataType = "String",required = true),
			@ApiImplicitParam(name = "event",value = "事件",dataType = "String",required = true),
			@ApiImplicitParam(name = "captcha",value = "验证码",dataType = "String",required = true)
	})
    public RestResponseBean mobilelogin(@RequestParam String mobile,@RequestParam String event,@RequestParam String captcha) {

        if (StringUtils.isBlank(mobile)|| StringUtils.isBlank(event)|| StringUtils.isBlank(captcha)) {
            return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(), ResultEnum.PARAMETER_MISSING.getDesc(), null);
        }

		if(!StringUtils.equals(CommonConstant.SMS_EVENT_LOGIN,event)){
			return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(),ResultEnum.SMS_CODE_ERROR.getDesc(),null);
		}

        switch (ismsService.check(mobile, event, captcha)) {
            case 1:
                return new RestResponseBean(ResultEnum.SMS_CODE_OVERTIME.getValue(), ResultEnum.SMS_CODE_OVERTIME.getDesc(), null);
            case 2:
                return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(), ResultEnum.SMS_CODE_ERROR.getDesc(), null);
        }

        User user = userService.queryByMobile(mobile);

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
    @GetMapping(value = "/thirdLogin")
    @ApiOperation(value = "三方登录", tags = {"用户接口"}, notes = "三方登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "platform",value = "平台类型('1':QQ,'2':微信,'3':微博)",dataType = "String",required = true),
            @ApiImplicitParam(name = "mobile",value = "用户手机号",dataType = "String",required = true)
    })
    public void thirdLogin(@RequestParam String platform, @RequestParam String mobile, HttpServletResponse response){

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
    @GetMapping(value = "/qqLoginCallback")
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
    @GetMapping(value = "/wxLoginCallBack")
    public RestResponseBean wxLoginCallBack(HttpServletRequest request) {

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
    @GetMapping(value = "/wbLoginCallBack")
    public RestResponseBean wbLoginCallBack(HttpServletRequest request) {

        User userEntity = null;
        //获取回调
        String openid = iWbService.callBack(request);
        String mobile = request.getParameter("state");

        return publicCallBack(openid,mobile,"2");
    }

    @GetMapping(value = "/isExistMobile")
    @ApiOperation(value = "手机号是否存在",tags = {"用户接口"},notes = "手机号是否存在")
	@ApiImplicitParam(name = "mobile",value = "手机号",dataType = "String",required = true)
    public RestResponseBean isExistMobile(@RequestParam String mobile){

        User user = userService.queryByMobile(mobile);

		if(user == null){

			return new RestResponseBean(ResultEnum.MOBILE_NOT_EXIST.getValue(),ResultEnum.MOBILE_NOT_EXIST.getDesc(),null);
		}

		return new RestResponseBean(ResultEnum.MOBILE_EXIST.getValue(),ResultEnum.MOBILE_EXIST.getDesc(),null);

    }


	@GetMapping(value = "/isExistMobileByUserId")
	@ApiOperation(value = "根据当前用户查询手机号是否正确",tags = {"用户接口"},notes = "根据当前用户查询手机号是否正确")
	@ApiImplicitParam(name = "mobile",value = "手机号",dataType = "String",required = true)
	public RestResponseBean isExistMobileByUserId(@RequestParam String mobile){

		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if(user == null){

			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

		User userEntity = userService.queryByMobileAndUserId(mobile,user.getId());

		if(userEntity == null){

			return new RestResponseBean(ResultEnum.MOBILE_ERROR.getValue(),ResultEnum.MOBILE_ERROR.getDesc(),null);
		}


		return new RestResponseBean(ResultEnum.MOBILE_RIGHT.getValue(),ResultEnum.MOBILE_RIGHT.getDesc(),null);

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
	private Map tokenBuild(User user) {

		Map<String,Object> map = new HashMap();
		//生成token
		String token = JwtUtil.signUser(CommonConstant.SIGN_MEMBER_USER + user.getMobile(), user.getPassword());

		//根据用户ID模糊查询
		Set<String> keys = stringRedisTemplate.keys("*" + user.getId());
		//清除redis中存在此手机号的token记录
		stringRedisTemplate.delete(keys);

		redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token + user.getId(), token);
		//设置超时时间
		redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token + user.getId(), JwtUtil.APP_EXPIRE_TIME / 1000);

		map.put("token", token);
		map.put("user", userService.queryUserVo(user));

		sysBaseAPI.addLog("手机号: " + user.getMobile() + ",登录成功！", CommonConstant.LOG_TYPE_1, null);

		return map;
	}

}