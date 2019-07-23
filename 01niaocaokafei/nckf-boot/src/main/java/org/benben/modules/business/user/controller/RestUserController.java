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
import org.benben.modules.business.userstore.entity.UserStore;
import org.benben.modules.business.userstore.service.IUserStoreService;
import org.benben.modules.shiro.LoginUser;
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


@RestController
@RequestMapping("/api/v1/user")
@Api(tags = {"用户接口"})
@Slf4j
public class RestUserController {

	@Autowired
	private ICommonService commonService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IUserThirdService userThirdService;

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
	private IUserStoreService userStoreService;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;


	/**
	 * showdoc
	 * @catalog 用户接口
	 * @title 通用-->用户详情
	 * @description 通用-->用户详情
	 * @method GET
	 * @url /nckf-boot/api/v1/user/queryUserById
	 * @return {"code": 1,"data": {"avatar": null,"couponsNumber": 0,"id": "c73ee7f3d95a74f9970eaac804548f78","mobile": "18503840250","money": 0,"sex": 0,"userType": "0","username": null},"msg": "操作成功","time": "1561012425988"}
	 * @return_param code String 响应状态
	 * @return_param user Object 用户信息
	 * @return_param avatar String 头像
	 * @return_param couponsNumber String 优惠劵数量
	 * @return_param id String 用户id
	 * @return_param mobile String 手机号
	 * @return_param money Double 余额
	 * @return_param sex String 性别(0:男 1:女)
	 * @return_param userType String 用户类型（0:普通用户 1:骑手）
	 * @return_param username String 用户名
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark
	 * @number 26
	 */
	@GetMapping(value = "/queryUserById")
	@ApiOperation(value = "通用-->用户详情", tags = {"用户接口"}, notes = "通用-->用户详情")
	public RestResponseBean queryUserById() {

		User user = (User) LoginUser.getCurrentUser();

		if (user == null) {
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
		}

		UserVo userVo = userService.queryUserVo(user);
		if (StringUtils.isNotBlank(userVo.getAvatar()) && userVo.getAvatar().indexOf("http") == -1) {
			userVo.setAvatar(commonService.getLocalUrl(userVo.getAvatar()));
		}
		if (userVo == null) {
			return new RestResponseBean(ResultEnum.ERROR.getValue(), ResultEnum.ERROR.getDesc(), null);
		}

		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				userVo);
	}


	/**
	 * showdoc
	 * @catalog 用户接口
	 * @title 通用-->用户修改
	 * @description 通用-->用户修改
	 * @method POST
	 * @url /nckf-boot/api/v1/user/editUser
	 * @param userVo 必填 Object 用户对象
	 * @json_param avatar 必填 String 头像
	 * @json_param couponsNumber 必填 String 优惠券数量
	 * @json_param id 必填 String 用户id
	 * @json_param mobile 必填 String 手机号
	 * @json_param money 必填 String 余额
	 * @json_param sex 必填 String 性别
	 * @json_param userType 必填 String 用户类型(0:普通用户 1:骑手)
	 * @json_param username 必填 String 用户名
	 * @return {"code": 1,"data": null,"msg": "操作成功","time": "1561015942780"}
	 * @return_param code String 响应状态
	 * @return_param data String 没有含义
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark
	 * @number 27
	 */
	@PostMapping(value = "/editUser")
	@ApiOperation(value = "通用-->用户修改", tags = {"用户接口"}, notes = "通用-->用户修改")
	public RestResponseBean editUser(@RequestBody UserVo userVo) {

		User user = (User) LoginUser.getCurrentUser();

		if (user == null) {
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
		}

		User userEntity = userService.getById(user.getId());

		BeanUtils.copyProperties(userVo, userEntity);

		if (userEntity == null) {
			return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(), ResultEnum.QUERY_NOT_EXIST.getDesc(),
					null);
		} else {

			boolean ok = userService.updateById(userEntity);

			if (!ok) {
				return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(),
						null);
			}
		}
		//刷新用户信息到缓存中
		redisUtil.set(CommonConstant.SIGN_PHONE_USER + userEntity.getId(), userEntity, JwtUtil.APP_EXPIRE_TIME / 1000);

		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				null);
	}

	/**
	 * showdoc
	 * @catalog 用户接口
	 * @title 通用-->修改头像
	 * @description 通用-->修改头像
	 * @method POST
	 * @url /nckf-boot/api/v1/user/changeAvatar
	 * @param avatar 必填 String 头像图片
	 * @return {"code": 1,"data": null,"msg": "操作成功","time": "1561013615213"}
	 * @return_param code String 响应状态
	 * @return_param data String 没有含义
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark
	 * @number 28
	 */
	@PostMapping(value = "/changeAvatar")
	@ApiOperation(value = "通用-->修改头像", tags = {"用户接口"}, notes = "通用-->修改头像")
	@ApiImplicitParam(name = "avatar", value = "头像图片", dataType = "String", required = true)
	public RestResponseBean changeAvatar(@RequestParam String avatar) {

		User user = (User) LoginUser.getCurrentUser();

		if (user == null) {
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
		}

		User userEntity = userService.getById(user.getId());

		if (StringUtils.isNotBlank(userEntity.getAvatar())) {
			userEntity.setOldAvatar(userEntity.getAvatar());
		}

		userEntity.setAvatar(avatar);

		if (userService.updateById(userEntity)) {
			//刷新用户信息到缓存中
			redisUtil.set(CommonConstant.SIGN_PHONE_USER + userEntity.getId(), userEntity,
					JwtUtil.APP_EXPIRE_TIME / 1000);

			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.desc(),
					null);
		}

		return new RestResponseBean(ResultEnum.ERROR.getValue(), ResultEnum.ERROR.getDesc(), null);
	}

	/**
	 * showdoc
	 * @catalog 用户接口
	 * @title 通用-->选择上一个头像
	 * @description 通用-->选择上一个头像
	 * @method POST
	 * @url /nckf-boot/api/v1/user/changeOldAvatar
	 * @return {"code": 0,"data": null,"msg": "没有上一个头像","time": "1561013111479"}
	 * @return_param code String 响应状态
	 * @return_param data String 没有含义
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark
	 * @number 29
	 */
	@PostMapping(value = "/changeOldAvatar")
	@ApiOperation(value = "通用-->选择上一个头像", tags = {"用户接口"}, notes = "通用-->选择上一个头像")
	public RestResponseBean changeOldAvatar() {

		User user = (User) LoginUser.getCurrentUser();

		if (user == null) {
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
		}

		User userEntity = userService.getById(user.getId());

		if (StringUtils.isBlank(userEntity.getOldAvatar())) {
			return new RestResponseBean(ResultEnum.NOT_EXIST_OLDAVATAR.getValue(),
					ResultEnum.NOT_EXIST_OLDAVATAR.getDesc(), null);
		}

		userEntity.setAvatar(userEntity.getOldAvatar());

		if (userService.updateById(userEntity)) {
			//刷新用户信息到缓存中
			redisUtil.set(CommonConstant.SIGN_PHONE_USER + userEntity.getId(), userEntity,
					JwtUtil.APP_EXPIRE_TIME / 1000);

			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.desc(),
					null);
		}

		return new RestResponseBean(ResultEnum.ERROR.getValue(), ResultEnum.ERROR.getDesc(), null);
	}

	/**
	 * showdoc
	 * @catalog 用户接口
	 * @title 通用-->修改用户名
	 * @description 通用-->修改用户名
	 * @method POST
	 * @url /nckf-boot/api/v1/user/changeUsername
	 * @param realname 必填 String 用户名
	 * @return {"code": 1,"data": null,"msg": "操作成功","time": "1561013076472"}
	 * @return_param code String 响应状态
	 * @return_param data String 没有含义
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark
	 * @number 31
	 */
	@PostMapping(value = "/changeRealname")
	@ApiOperation(value = "通用-->修改用户名", tags = {"用户接口"}, notes = "通用-->修改用户名")
	@ApiImplicitParam(name = "realname", value = "用户名", dataType = "String", defaultValue = "1", required = true)
	public RestResponseBean changeUsername(@RequestParam String realname) {

		User user = (User) LoginUser.getCurrentUser();

		if (user == null) {
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
		}

		if (StringUtils.isBlank(realname)) {
			return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(), ResultEnum.PARAMETER_MISSING.getDesc(),
					null);
		}

		User userEntity = userService.getById(user.getId());
		if (userEntity == null) {
			return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(), ResultEnum.QUERY_NOT_EXIST.desc(), null);
		}
		userEntity.setRealname(realname);

		if (userService.updateById(userEntity)) {
			//刷新用户信息到缓存中
			redisUtil.set(CommonConstant.SIGN_PHONE_USER + userEntity.getId(), userEntity,
					JwtUtil.APP_EXPIRE_TIME / 1000);
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.desc(),
					null);
		}

		return new RestResponseBean(ResultEnum.ERROR.getValue(), ResultEnum.ERROR.getDesc(), null);
	}

	/**
	 * showdoc
	 * @catalog 用户接口
	 * @title 通用-->修改手机号
	 * @description 通用-->修改手机号
	 * @method POST
	 * @url /nckf-boot/api/v1/user/changeMobile
	 * @param captcha 必填 String 验证码
	 * @param event 必填 String 事件
	 * @param mobile 必填 String 用户手机号
	 * @return {"code": 1,"data": null,"msg": "操作成功","time": "1561013383230"}
	 * @return_param code String 响应状态
	 * @return_param data String 没有含义
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark
	 * @number 32
	 */
	@PostMapping(value = "/changeMobile")
	@ApiOperation(value = "通用-->修改手机号", tags = {"用户接口"}, notes = "通用-->修改手机号")
	@ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "用户手机号", dataType = "String", required = true),
			@ApiImplicitParam(name = "event", value = "事件", dataType = "String", required = true),
			@ApiImplicitParam(name = "captcha", value = "验证码", dataType = "String", required = true)})
	public RestResponseBean changeMobile(@RequestParam String mobile, @RequestParam String event,
			@RequestParam String captcha) {

		User user = (User) LoginUser.getCurrentUser();

		if (user == null) {
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
		}

		if (StringUtils.isBlank(mobile) || StringUtils.isBlank(event) || StringUtils.isBlank(captcha)) {

			return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(), ResultEnum.PARAMETER_MISSING.desc(),
					null);
		}

		if (!StringUtils.equals(CommonConstant.SMS_EVENT_BINGDING, event)) {
			return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(), ResultEnum.SMS_CODE_ERROR.getDesc(),
					null);
		}

		switch (ismsService.check(mobile, event, captcha)) {
			case 1:
				return new RestResponseBean(ResultEnum.SMS_CODE_OVERTIME.getValue(),
						ResultEnum.SMS_CODE_OVERTIME.getDesc(), null);
			case 2:
				return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(), ResultEnum.SMS_CODE_ERROR.getDesc(),
						null);
		}

		User userEntity = userService.getById(user.getId());

		userEntity.setMobile(mobile);

		if (userService.updateById(userEntity)) {
			//刷新用户信息到缓存中
			redisUtil.set(CommonConstant.SIGN_PHONE_USER + userEntity.getId(), userEntity,
					JwtUtil.APP_EXPIRE_TIME / 1000);

			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.desc(),
					null);
		}

		return new RestResponseBean(ResultEnum.ERROR.getValue(), ResultEnum.ERROR.getDesc(), null);
	}

	/**
	 * showdoc
	 * @catalog 用户接口
	 * @title 通用-->忘记密码
	 * @description 通用-->忘记密码
	 * @method POST
	 * @url /nckf-boot/api/v1/user/forgetPassword
	 * @param mobile 必填 String 用户手机号
	 * @param event 必填 String 事件
	 * @param captcha 必填 String 验证码
	 * @param password 必填 String 新密码
	 * @param userType 必填 String 用户类型  0/普通用户,1/骑手
	 * @return {code": 1,"data": null,"msg": "操作成功","time": "1561012941348"}
	 * @return_param code String 响应状态
	 * @return_param data String 没有含义
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark
	 * @number 33
	 */
	@PostMapping(value = "/forgetPassword")
	@ApiOperation(value = "通用-->忘记密码", tags = {"用户接口"}, notes = "通用-->忘记密码")
	@ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "用户手机号", dataType = "String", required = true),
			@ApiImplicitParam(name = "password", value = "用户新密码", dataType = "String", required = true),
			@ApiImplicitParam(name = "event", value = "事件", dataType = "String", defaultValue = CommonConstant.SMS_EVENT_FORGET, required = true),
			@ApiImplicitParam(name = "captcha", value = "验证码", dataType = "String", required = true),
			@ApiImplicitParam(name = "userType", value = "用户类型  0/普通用户,1/骑手", dataType = "String", required = true)})
	public RestResponseBean forgetPassword(@RequestParam String mobile, @RequestParam String password,
			@RequestParam String event, @RequestParam String captcha, @RequestParam String userType) {

		if (StringUtils.isBlank(mobile) || StringUtils.isBlank(password) || StringUtils.isBlank(event) || StringUtils
				.isBlank(captcha)) {
			return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(), ResultEnum.PARAMETER_MISSING.getDesc(),
					null);
		}

		if (!StringUtils.equals(CommonConstant.SMS_EVENT_FORGET, event)) {
			return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(), ResultEnum.SMS_CODE_ERROR.getDesc(),
					null);
		}

		switch (ismsService.check(mobile, event, captcha)) {
			case 1:
				return new RestResponseBean(ResultEnum.SMS_CODE_OVERTIME.getValue(),
						ResultEnum.SMS_CODE_OVERTIME.getDesc(), null);
			case 2:
				return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(), ResultEnum.SMS_CODE_ERROR.getDesc(),
						null);
		}

		if (userService.changePassword(mobile, password, userType) == 0) {
			return new RestResponseBean(ResultEnum.ERROR.getValue(), ResultEnum.ERROR.getDesc(), null);
		}

		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				null);
	}


	/**
	 * showdoc
	 * @catalog 用户接口
	 * @title 通用-->修改密码
	 * @description 通用-->修改密码
	 * @method POST
	 * @url /nckf-boot/api/v1/user/forgetPassword
	 * @param mobile 必填 String 用户手机号
	 * @param event 必填 String 事件
	 * @param captcha 必填 String 验证码
	 * @param newPassword 必填 String 新密码
	 * @return {code": 1,"data": null,"msg": "操作成功","time": "1561012941348"}
	 * @return_param code String 响应状态
	 * @return_param data String 没有含义
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark
	 * @number 34
	 */
	@PostMapping(value = "/changePassword")
	@ApiOperation(value = "通用-->修改密码", tags = {"用户接口"}, notes = "通用-->修改密码")
	@ApiImplicitParams({@ApiImplicitParam(name = "newPassword", value = "用户新密码", dataType = "String"),
			@ApiImplicitParam(name = "mobile", value = "用户手机号", dataType = "String"),
			@ApiImplicitParam(name = "event", value = "事件", dataType = "String", defaultValue = CommonConstant.SMS_EVENT_CHANGE_PWD),
			@ApiImplicitParam(name = "captcha", value = "验证码", dataType = "String")})
	public RestResponseBean changePassword(@RequestParam String newPassword, @RequestParam String mobile,
			@RequestParam String event, @RequestParam String captcha) {

		User user = (User) LoginUser.getCurrentUser();

		if (user == null) {

			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
		}

		User userEntity = userService.getById(user.getId()); //如果修改密码前端不设置重新登录,避免取到jwt中旧数据,应再次查询对象

		if (StringUtils.isBlank(mobile) || StringUtils.isBlank(newPassword) || StringUtils.isBlank(event) || StringUtils
				.isBlank(captcha)) {
			return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(), ResultEnum.PARAMETER_MISSING.getDesc(),
					null);
		}

		if (!StringUtils.equals(CommonConstant.SMS_EVENT_CHANGE_PWD, event)) {
			return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(), ResultEnum.SMS_CODE_ERROR.getDesc(),
					null);
		}

		switch (ismsService.check(mobile, event, captcha)) {
			case 1:
				return new RestResponseBean(ResultEnum.SMS_CODE_OVERTIME.getValue(),
						ResultEnum.SMS_CODE_OVERTIME.getDesc(), null);
			case 2:
				return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(), ResultEnum.SMS_CODE_ERROR.getDesc(),
						null);
		}

		if (userService.changePassword(userEntity.getMobile(), newPassword, user.getUserType()) == 0) {
			return new RestResponseBean(ResultEnum.ERROR.getValue(), ResultEnum.ERROR.getDesc(), null);
		}

		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				null);
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
	 * showdoc
	 * @catalog 用户接口
	 * @title 用户-->用户注册
	 * @description 用户-->用户注册
	 * @method POST
	 * @url /nckf-boot/api/v1/user/userRegister
	 * @param captcha 必填 String 验证码
	 * @param event 必填 String 事件
	 * @param mobile 必填 String 用户手机号
	 * @param password 必填 String 用户密码
	 * @return {"code": 1,"data": {"user": {"avatar": null,"couponsNumber": 0,"id": "c73ee7f3d95a74f9970eaac804548f78","mobile": "18503840250","money": 0,"sex": null,"userType": null,"username": null},"token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtb2JpbGUiOiJtZW1iZXJVc2VyQDE4NTAzODQwMjUwIiwiZXhwIjoxNTYxNjE3MTQxfQ.NJ99mTUKQHzO6diCpJ3wzdaAFrlVKv0TIjizmibRwKE"},"msg": "操作成功","time": "1561012341771"}
	 * @return_param code String 响应状态
	 * @return_param user Object 用户信息
	 * @return_param avatar String 头像
	 * @return_param couponsNumber String 优惠劵数量
	 * @return_param id String 用户id
	 * @return_param mobile String 手机号
	 * @return_param money Double 余额
	 * @return_param sex String 性别(0:男 1:女)
	 * @return_param userType String 用户类型（0:普通用户 1:骑手）
	 * @return_param username String 用户名
	 * @return_param token String 访问权限
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark
	 * @number 35
	 */
	@PostMapping(value = "/userRegister")
	@ApiOperation(value = "用户-->用户注册", tags = {"用户接口"}, notes = "用户-->用户注册")
	@ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "用户手机号", dataType = "String", required = true),
			@ApiImplicitParam(name = "password", value = "用户密码", dataType = "String", required = true),
			@ApiImplicitParam(name = "event", value = "事件", dataType = "String", required = true),
			@ApiImplicitParam(name = "captcha", value = "验证码", dataType = "String", required = true)})
	public RestResponseBean userRegister(@RequestParam String mobile, @RequestParam String password,
			@RequestParam String event, @RequestParam String captcha) {

		if (StringUtils.isBlank(mobile) || StringUtils.isBlank(password) || StringUtils.isBlank(event) || StringUtils
				.isBlank(captcha)) {
			return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(), ResultEnum.PARAMETER_MISSING.getDesc(),
					null);
		}

		if (!StringUtils.equals(CommonConstant.SMS_EVENT_REGISTER, event)) {
			return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(), ResultEnum.SMS_CODE_ERROR.getDesc(),
					null);
		}

		try {

			switch (ismsService.check(mobile, event, captcha)) {
				case 1:
					return new RestResponseBean(ResultEnum.SMS_CODE_OVERTIME.getValue(),
							ResultEnum.SMS_CODE_OVERTIME.getDesc(), null);
				case 2:
					return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(),
							ResultEnum.SMS_CODE_ERROR.getDesc(), null);
			}

			User userEntity = userService.queryByMobileAndUserType(mobile, "0");
			if (userEntity != null) {
				return new RestResponseBean(ResultEnum.MOBILE_EXIST_REGISTER.getValue(),
						ResultEnum.MOBILE_EXIST_REGISTER.getDesc(), null);
			}

			User user = userService.userRegister(mobile, password);

			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
					tokenBuild(user));

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return new RestResponseBean(ResultEnum.ERROR.getValue(), ResultEnum.ERROR.getDesc(), null);
		}


	}

	/**
	 * showdoc
	 * @catalog 用户接口
	 * @title 骑手-->注册
	 * @description 骑手-->注册
	 * @method POST
	 * @url /nckf-boot/api/v1/user/riderRegister
	 * @param userStoreVo 必填 Object 骑手对象
	 * @json_param age 必填 String 年龄
	 * @json_param backUrl 必填 String 身份证反面照
	 * @json_param frontUrl 必填 String 身份证正面照
	 * @json_param idCard 必填 String 身份证号
	 * @json_param mobile 必填 String 手机号
	 * @json_param password 必填 String 密码
	 * @json_param realname 必填 String 真实姓名
	 * @json_param sex 必填 String 性别
	 * @return {"code": 1,"data": {"user": {"avatar": null,"couponsNumber": 0,"id": "1de3acc53edd742aa45a6d7d76930451","mobile": "1","money": 0,"sex": 0,"userType": "1","username": null},"token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtb2JpbGUiOiJtZW1iZXJVc2VyQDEiLCJleHAiOjE1NjE2MjExMjN9.VCH-9r6lrB7SL_W4vkByxBQRMsNcjZNdr-t_7_676ag"},"msg": "操作成功","time": "1561016323262"}
	 * @return_param code String 响应状态
	 * @return_param user Object 用户信息
	 * @return_param avatar String 头像
	 * @return_param couponsNumber String 优惠劵数量
	 * @return_param id String 用户id
	 * @return_param mobile String 手机号
	 * @return_param money Double 余额
	 * @return_param sex String 性别(0:男 1:女)
	 * @return_param userType String 用户类型（0:普通用户 1:骑手）
	 * @return_param username String 用户名
	 * @return_param token String 访问权限
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark
	 * @number 36
	 */
	@PostMapping(value = "/riderRegister")
	@ApiOperation(value = "骑手-->注册", tags = {"用户接口"}, notes = "骑手-->注册")
	public RestResponseBean riderRegister(@RequestBody UserStoreVo userStoreVo) {

		try {

			User userEntity = userService.queryByMobileAndUserType(userStoreVo.getMobile(), "1");

			if (userEntity != null) {
				return new RestResponseBean(ResultEnum.MOBILE_EXIST_REGISTER.getValue(),
						ResultEnum.MOBILE_EXIST_REGISTER.getDesc(), null);
			}

			User user = userService.riderRegister(userStoreVo);

			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
					tokenBuild(user));

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(),
					null);
		}


	}


	/**
	 * showdoc
	 * @catalog 用户接口
	 * @title 骑手-->账户密码登录
	 * @description 骑手-->账户密码登录
	 * @method POST
	 * @url /nckf-boot/api/v1/user/riderlogin
	 * @param mobile 必填 String 用户手机号
	 * @param password 必填 String 用户密码
	 * @return {"code": 1,"data": {"user": {"avatar": null,"couponsNumber": 0,"id": "c73ee7f3d95a74f9970eaac804548f78","mobile": "18503840250","money": 0,"sex": 0,"userType": "0","username": null},"token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtb2JpbGUiOiJtZW1iZXJVc2VyQDE4NTAzODQwMjUwIiwiZXhwIjoxNTYxNjE3MjY2fQ.mpFv9F-QC97eUOZrvyEk7_YYcMCuAzuI-j2a6SpOAI0"},"msg": "登陆成功","time": "1561012466360"}
	 * @return_param code String 响应状态
	 * @return_param user Object 用户信息
	 * @return_param avatar String 头像
	 * @return_param couponsNumber String 优惠劵数量
	 * @return_param id String 用户id
	 * @return_param mobile String 手机号
	 * @return_param money Double 余额
	 * @return_param sex String 性别(0:男 1:女)
	 * @return_param userType String 用户类型（0:普通用户 1:骑手）
	 * @return_param username String 用户名
	 * @return_param token String 访问权限
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark
	 * @number 37
	 */
	@PostMapping(value = "/riderlogin")
	@ApiOperation(value = "骑手-->账户密码登录", tags = {"用户接口"}, notes = "骑手-->账户密码登录")
	@ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "用户手机号", dataType = "String", required = true),
			@ApiImplicitParam(name = "password", value = "用户密码", dataType = "String", required = true)})
	public RestResponseBean riderlogin(@RequestParam String mobile, @RequestParam String password) {

		Map<String, Object> obj = new HashMap<>();

		User user = userService.queryByMobileAndUserType(mobile, "1");

		if (user == null) {
			return new RestResponseBean(ResultEnum.USER_NOT_EXIST.getValue(), ResultEnum.USER_NOT_EXIST.getDesc(),
					null);
		}

		UserStore userStore = userStoreService.queryByUserId(user.getId());

		if (StringUtils.equals(userStore.getCompleteFlag(), "0")) {
			return new RestResponseBean(ResultEnum.COMPLETE_UNDER_WAY.getValue(),
					ResultEnum.COMPLETE_UNDER_WAY.getDesc(), null);
		}

		if (StringUtils.equals(userStore.getCompleteFlag(), "1")) {
			return new RestResponseBean(ResultEnum.COMPLETE_NOT_PASS.getValue(), ResultEnum.COMPLETE_NOT_PASS.getDesc(),
					null);
		}
		//密码验证
		String userpassword = PasswordUtil.encrypt(password, password, user.getSalt());
		String syspassword = user.getPassword();
		if (!syspassword.equals(userpassword)) {
			return new RestResponseBean(ResultEnum.USER_PWD_ERROR.getValue(), ResultEnum.USER_PWD_ERROR.getDesc(),
					null);
		}

		return new RestResponseBean(ResultEnum.LOGIN_SUCCESS.getValue(), ResultEnum.LOGIN_SUCCESS.getDesc(),
				tokenBuild(user));
	}


	/**
	 * showdoc
	 * @catalog 用户接口
	 * @title 用户-->账户密码登录
	 * @description 用户-->账户密码登录
	 * @method POST
	 * @url /nckf-boot/api/v1/user/userlogin
	 * @param mobile 必填 String 用户手机号
	 * @param password 必填 String 用户密码
	 * @return {"code": 1,"data": {"user": {"avatar": null,"couponsNumber": 0,"id": "c73ee7f3d95a74f9970eaac804548f78","mobile": "18503840250","money": 0,"sex": 0,"userType": "0","username": null},"token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtb2JpbGUiOiJtZW1iZXJVc2VyQDE4NTAzODQwMjUwIiwiZXhwIjoxNTYxNjE3MjY2fQ.mpFv9F-QC97eUOZrvyEk7_YYcMCuAzuI-j2a6SpOAI0"},"msg": "登陆成功","time": "1561012466360"}
	 * @return_param code String 响应状态
	 * @return_param user Object 用户信息
	 * @return_param avatar String 头像
	 * @return_param couponsNumber String 优惠劵数量
	 * @return_param id String 用户id
	 * @return_param mobile String 手机号
	 * @return_param money Double 余额
	 * @return_param sex String 性别(0:男 1:女)
	 * @return_param userType String 用户类型（0:普通用户 1:骑手）
	 * @return_param username String 用户名
	 * @return_param token String 访问权限
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark
	 * @number 37
	 */
	@PostMapping(value = "/userlogin")
	@ApiOperation(value = "用户-->账户密码登录", tags = {"用户接口"}, notes = "用户-->账户密码登录")
	@ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "用户手机号", dataType = "String", required = true),
			@ApiImplicitParam(name = "password", value = "用户密码", dataType = "String", required = true)})
	public RestResponseBean userlogin(@RequestParam String mobile, @RequestParam String password) {

		Map<String, Object> obj = new HashMap<>();
		User user = userService.queryByMobileAndUserType(mobile, "0");

		if (user == null) {
			return new RestResponseBean(ResultEnum.USER_NOT_EXIST.getValue(), ResultEnum.USER_NOT_EXIST.getDesc(),
					null);
		} else {
			//密码验证
			String userpassword = PasswordUtil.encrypt(password, password, user.getSalt());
			String syspassword = user.getPassword();
			if (!syspassword.equals(userpassword)) {
				return new RestResponseBean(ResultEnum.USER_PWD_ERROR.getValue(), ResultEnum.USER_PWD_ERROR.getDesc(),
						null);
			}
			//调用公共方法
			obj = tokenBuild(user);
		}

		return new RestResponseBean(ResultEnum.LOGIN_SUCCESS.getValue(), ResultEnum.LOGIN_SUCCESS.getDesc(), obj);
	}


	/**
	 * showdoc
	 * @catalog 用户接口
	 * @title 用户-->手机验证码登录
	 * @description 用户-->手机验证码登录
	 * @method POST
	 * @url /nckf-boot/api/v1/user/mobileLogin
	 * @param mobile 必填 String 用户手机号
	 * @param event 必填 String 事件
	 * @param captcha 必填 String 验证码
	 * @return {"code": 1,"data": {"user": {"avatar": null,"couponsNumber": 0,"id": "c73ee7f3d95a74f9970eaac804548f78","mobile": "18503840250","money": 0,"sex": 0,"userType": "0","username": null},"token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtb2JpbGUiOiJtZW1iZXJVc2VyQDE4NTAzODQwMjUwIiwiZXhwIjoxNTYxNjE3NDAxfQ.IBIC7Qc4LiL872t2b7dlbBIoRizFppCFo3P8i4jJGwE"},"msg": "登陆成功","time": "1561012601392"}
	 * @return_param code String 响应状态
	 * @return_param user Object 用户信息
	 * @return_param avatar String 头像
	 * @return_param couponsNumber String 优惠劵数量
	 * @return_param id String 用户id
	 * @return_param mobile String 手机号
	 * @return_param money Double 余额
	 * @return_param sex String 性别(0:男 1:女)
	 * @return_param userType String 用户类型（0:普通用户 1:骑手）
	 * @return_param username String 用户名
	 * @return_param token String 访问权限
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark
	 * @number 38
	 */
	@PostMapping(value = "/mobileLogin")
	@ApiOperation(value = "用户-->手机验证码登录", tags = {"用户接口"}, notes = "用户-->手机验证码登录")
	@ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "用户手机号", dataType = "String", required = true),
			@ApiImplicitParam(name = "event", value = "事件", dataType = "String", required = true),
			@ApiImplicitParam(name = "captcha", value = "验证码", dataType = "String", required = true)})
	public RestResponseBean mobilelogin(@RequestParam String mobile, @RequestParam String event,
			@RequestParam String captcha) {

		if (StringUtils.isBlank(mobile) || StringUtils.isBlank(event) || StringUtils.isBlank(captcha)) {
			return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(), ResultEnum.PARAMETER_MISSING.getDesc(),
					null);
		}

		if (!StringUtils.equals(CommonConstant.SMS_EVENT_LOGIN, event)) {
			return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(), ResultEnum.SMS_CODE_ERROR.getDesc(),
					null);
		}

		switch (ismsService.check(mobile, event, captcha)) {
			case 1:
				return new RestResponseBean(ResultEnum.SMS_CODE_OVERTIME.getValue(),
						ResultEnum.SMS_CODE_OVERTIME.getDesc(), null);
			case 2:
				return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(), ResultEnum.SMS_CODE_ERROR.getDesc(),
						null);
		}

		User user = userService.queryByMobileAndUserType(mobile, "0");

		if (user == null) {
			return new RestResponseBean(ResultEnum.USER_NOT_EXIST.getValue(), ResultEnum.USER_NOT_EXIST.getDesc(),
					null);
		}

		return new RestResponseBean(ResultEnum.LOGIN_SUCCESS.getValue(), ResultEnum.LOGIN_SUCCESS.getDesc(),
				tokenBuild(user));
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
	@GetMapping(value = "/pcThirdLogin")
	@ApiOperation(value = "PC三方登录", tags = {"用户接口"}, notes = "PC三方登录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "platform", value = "平台类型('0':QQ,'1':微信,'2':微博)", dataType = "String", required = true),
			@ApiImplicitParam(name = "mobile", value = "用户手机号", dataType = "String", required = true)})
	public void pcThirdLogin(@RequestParam String platform, @RequestParam String mobile, HttpServletResponse response) {

		switch (platform) {
			case "0":
				iQqService.login(mobile, response);
				break;
			case "1":
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
	//        User user = (User) LoginUser.getCurrentUser();
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

		return publicCallBack(openid, mobile, "0");
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

		return publicCallBack(openid, mobile, "1");
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

		return publicCallBack(openid, mobile, "2");
	}

	/**
	 * showdoc
	 * @catalog 用户接口
	 * @title 通用-->手机号是否存在
	 * @description 通用-->手机号是否存在
	 * @method GET
	 * @url /nckf-boot/api/v1/user/isExistMobile
	 * @param mobile 必填 String 用户手机号
	 * @return {"code": 0,"data": null,"msg": "手机号已注册","time": "1561012507468"}
	 * @return_param code String 响应状态
	 * @return_param data String 没有含义
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark 验证方式(0 : 登录 1 : 注册 2 : 忘记密码)
	 * @number 39
	 */
	@GetMapping(value = "/isExistMobile")
	@ApiOperation(value = "通用-->手机号是否存在", tags = {"用户接口"}, notes = "通用-->手机号是否存在")
	@ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String", required = true),
			@ApiImplicitParam(name = "userType", value = "用户类型  0/普通用户,1/骑手", dataType = "String", required = true)})
	public RestResponseBean isExistMobile(@RequestParam String mobile, @RequestParam String userType) {

		User user = userService.queryByMobileAndUserType(mobile, userType);

		if (user == null) {

			return new RestResponseBean(ResultEnum.MOBILE_NOT_EXIST.getValue(), ResultEnum.MOBILE_NOT_EXIST.getDesc(),
					null);
		}

		return new RestResponseBean(ResultEnum.MOBILE_EXIST.getValue(), ResultEnum.MOBILE_EXIST.getDesc(), null);

	}


	@GetMapping(value = "/isExistMobileByUserId")
	@ApiOperation(value = "根据当前用户查询手机号是否正确", tags = {"用户接口"}, notes = "根据当前用户查询手机号是否正确")
	@ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String", required = true)
	public RestResponseBean isExistMobileByUserId(@RequestParam String mobile) {

		User user = (User) LoginUser.getCurrentUser();

		if (user == null) {

			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
		}

		User userEntity = userService.queryByMobileAndUserId(mobile, user.getId());

		if (userEntity == null) {

			return new RestResponseBean(ResultEnum.MOBILE_ERROR.getValue(), ResultEnum.MOBILE_ERROR.getDesc(), null);
		}


		return new RestResponseBean(ResultEnum.MOBILE_RIGHT.getValue(), ResultEnum.MOBILE_RIGHT.getDesc(), null);

	}


	@GetMapping(value = "/changeWorkStatus")
	@ApiOperation(value = "更改上班状态", tags = {"用户接口"}, notes = "更改上班状态")
	@ApiImplicitParam(name = "status", value = "当前上班状态", dataType = "String", required = true)
	public RestResponseBean changeWorkStatus(@RequestParam(name = "status") String status) {

		User user = (User) LoginUser.getCurrentUser();

		if (user == null) {

			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
		}

		if ("0".equals(status)) {
			boolean ok = userService.changeWorkStatus("1", user.getId());
			if (ok) {
				return new RestResponseBean(ResultEnum.CHANGE_WORK_STATUS.getValue(),
						ResultEnum.CHANGE_WORK_STATUS.getDesc(), "1");
			}
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(),
					null);
		} else {
			boolean ok = userService.changeWorkStatus("0", user.getId());
			if (ok) {

				if (StringUtils.equals(status, "0")) {
					user.setWorkFlag("1");
				} else {
					user.setWorkFlag("0");
				}
				//刷新用户信息到缓存中
				redisUtil.set(CommonConstant.SIGN_PHONE_USER + user.getId(), user, JwtUtil.APP_EXPIRE_TIME / 1000);

				return new RestResponseBean(ResultEnum.CHANGE_WORK_STATUS.getValue(),
						ResultEnum.CHANGE_WORK_STATUS.getDesc(), "0");
			}
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(),
					null);
		}

	}


	@GetMapping(value = "/mobileThirdLogin")
	@ApiOperation(value = "手机三方登录", tags = {"用户接口"}, notes = "手机三方登录")
	@ApiImplicitParam(name = "openId", value = "三方唯一标识ID", dataType = "String", required = true)
	public RestResponseBean mobileThirdLogin(@RequestParam(name = "openId") String openId) {

		if (StringUtils.isBlank(openId)) {

			return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(), ResultEnum.PARAMETER_MISSING.getDesc(),
					null);
		}

		UserThird userThird = userThirdService.queryByOpenid(openId);

		if (userThird == null) {
			return new RestResponseBean(ResultEnum.UNBOUND_OPENID.getValue(), ResultEnum.UNBOUND_OPENID.getDesc(),
					null);
		}

		User user = userService.getById(userThird.getUserId());

		return new RestResponseBean(ResultEnum.LOGIN_SUCCESS.getValue(), ResultEnum.LOGIN_SUCCESS.getDesc(),
				tokenBuild(user));

	}

	@PostMapping(value = "/bindOpenId")
	@ApiOperation(value = "绑定OpenId", tags = {"用户接口"}, notes = "绑定OpenId")
	@ApiImplicitParams({@ApiImplicitParam(name = "mobile", value = "用户手机号", dataType = "String", required = true),
			@ApiImplicitParam(name = "event", value = "事件", dataType = "String", required = true),
			@ApiImplicitParam(name = "captcha", value = "验证码", dataType = "String", required = true),
			@ApiImplicitParam(name = "openId", value = "openId", dataType = "String", required = true),
			@ApiImplicitParam(name = "platform", value = "平台类型('0':QQ,'1':微信,'2':微博)", dataType = "String", required = true)})
	public RestResponseBean bindOpenId(@RequestParam(name = "mobile") String mobile,
			@RequestParam(name = "event") String event, @RequestParam(name = "captcha") String captcha,
			@RequestParam(name = "openId") String openId, @RequestParam(name = "platform") String platform) {

		if (StringUtils.isBlank(mobile) || StringUtils.isBlank(event) || StringUtils.isBlank(captcha) || StringUtils
				.isBlank(openId) || StringUtils.isBlank(platform)) {
			return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(), ResultEnum.PARAMETER_MISSING.getDesc(),
					null);
		}

		if (!StringUtils.equals(CommonConstant.SMS_EVENT_THIRD, event)) {
			return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(), ResultEnum.SMS_CODE_ERROR.getDesc(),
					null);
		}

		switch (ismsService.check(mobile, event, captcha)) {
			case 1:
				return new RestResponseBean(ResultEnum.SMS_CODE_OVERTIME.getValue(),
						ResultEnum.SMS_CODE_OVERTIME.getDesc(), null);
			case 2:
				return new RestResponseBean(ResultEnum.SMS_CODE_ERROR.getValue(), ResultEnum.SMS_CODE_ERROR.getDesc(),
						null);
		}

		User user = userService.queryByMobile(mobile);

		UserThird userThird = userThirdService.queryByUserIdAndStatus(user.getId(), platform);
		//未绑定
		if (userThird == null) {
			//绑定失败
			if (userThirdService.bindOpenId(user.getId(), openId, platform) == 0) {
				return new RestResponseBean(ResultEnum.BINDING_FAIL.getValue(), ResultEnum.BINDING_FAIL.getDesc(),
						null);
			}
			//绑定成功
			return new RestResponseBean(ResultEnum.BINDING_SUCCESS.getValue(), ResultEnum.BINDING_SUCCESS.getDesc(),
					null);
		}
		//已绑定

		return new RestResponseBean(ResultEnum.REPEATED_BINDING.getValue(), ResultEnum.REPEATED_BINDING.getDesc(),
				null);
	}


	/**
	 * 公共方法,三方登录公共回调
	 * @param openid
	 * @param mobile
	 * @param type
	 * @return
	 */
	private RestResponseBean publicCallBack(String openid, String mobile, String type) {

		User userEntity = null;

		//返回空则表明回调异常
		if (StringUtils.isBlank(openid)) {
			return new RestResponseBean(ResultEnum.ERROR.getValue(), ResultEnum.ERROR.getDesc(), null);
		}
		//查询登录状态
		User user = userService.queryByMobileAndUserType(mobile, "0");
		//查询是否已有账户绑定该微博
		UserThird userThird = userThirdService.queryByOpenid(openid);
		if (userThird != null) {
			//查询绑定的用户信息
			userEntity = userService.getById(userThird.getUserId());
		}
		//未登录
		if (user == null) {
			//未登录,未绑定
			if (userEntity == null) {
				return new RestResponseBean(ResultEnum.UNBOUND_OPENID.getValue(), ResultEnum.UNBOUND_OPENID.getDesc(),
						null);
			}
			//未登录,已绑定,返回登录信息token等
			return new RestResponseBean(ResultEnum.LOGIN_SUCCESS.getValue(), ResultEnum.LOGIN_SUCCESS.getDesc(),
					tokenBuild(userEntity));
		}
		//已登录,未绑定
		if (userEntity == null) {

			switch (type) {
				case "0":
					//绑定QQ
					if (userService.bindingThird(openid, user.getId(), "0") == 1) {
						return new RestResponseBean(ResultEnum.BINDING_SUCCESS.getValue(),
								ResultEnum.BINDING_SUCCESS.getDesc(), null);
					}
					break;
				case "1":
					//绑定微信
					if (userService.bindingThird(openid, user.getId(), "1") == 1) {
						return new RestResponseBean(ResultEnum.BINDING_SUCCESS.getValue(),
								ResultEnum.BINDING_SUCCESS.getDesc(), null);
					}
					break;
				default:
					//绑定微博
					if (userService.bindingThird(openid, user.getId(), "2") == 1) {
						return new RestResponseBean(ResultEnum.BINDING_SUCCESS.getValue(),
								ResultEnum.BINDING_SUCCESS.getDesc(), null);
					}
					break;
			}

			//绑定失败
			return new RestResponseBean(ResultEnum.BINDING_FAIL.getValue(), ResultEnum.BINDING_FAIL.getDesc(), null);
		}
		//已登录,禁止重复绑定
		return new RestResponseBean(ResultEnum.REPEATED_BINDING.getValue(), ResultEnum.REPEATED_BINDING.getDesc(),
				null);
	}

	/**
	 * 公共方法,三方登录返回信息token
	 *
	 * @param user 根据传参查询到的实体
	 * @return
	 */
	private Map tokenBuild(User user) {

		Map<String, Object> map = new HashMap();
		//生成token
		String token = JwtUtil.signUser(user.getId(), user.getPassword());

/*		//根据用户ID模糊查询
		Set<String> keys = stringRedisTemplate.keys("*" + user.getId());
		//清除redis中存在此手机号的token记录
		stringRedisTemplate.delete(keys);*/
		//设置token到缓存中
		redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token + user.getId(), token, JwtUtil.APP_EXPIRE_TIME / 1000);
		//设置用户信息到缓存中
		redisUtil.set(CommonConstant.SIGN_PHONE_USER + user.getId(), user, JwtUtil.APP_EXPIRE_TIME / 1000);
		user.setAvatar(commonService.getLocalUrl(user.getAvatar()));

		map.put("token", token);
		map.put("user", userService.queryUserVo(user));


		return map;
	}

}
