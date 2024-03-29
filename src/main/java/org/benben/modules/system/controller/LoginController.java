package org.benben.modules.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.benben.common.api.vo.Result;
import org.benben.common.constant.CommonConstant;
import org.benben.common.system.api.ISysBaseAPI;
import org.benben.common.util.PasswordUtil;
import org.benben.common.util.RedisUtil;
import org.benben.modules.shiro.authc.util.JwtUtil;
import org.benben.modules.shiro.vo.DefContants;
import org.benben.modules.system.entity.SysUser;
import org.benben.modules.system.model.SysLoginModel;
import org.benben.modules.system.service.ISysLogService;
import org.benben.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * @author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/sys")
@Api("用户登录")
@Slf4j
public class LoginController {
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private ISysBaseAPI sysBaseAPI;
	@Autowired
	private ISysLogService logService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;


	@RequestMapping(value = "/login", method = RequestMethod.POST)
//	@ApiOperation("登录接口")
	public Result<JSONObject> login(@RequestBody SysLoginModel sysLoginModel) {
		Result<JSONObject> result = new Result<JSONObject>();
		String username = sysLoginModel.getUsername();
		String password = sysLoginModel.getPassword();
		SysUser sysUser = sysUserService.getUserByName(username);
		if(sysUser==null) {
			result.error500("该用户不存在");
			sysBaseAPI.addLog("登录失败，用户名:"+username+"不存在！", CommonConstant.LOG_TYPE_1, null);
			return result;
		}else {
			//密码验证
			String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
			String syspassword = sysUser.getPassword();
			if(!syspassword.equals(userpassword)) {
				result.error500("用户名或密码错误");
				return result;
			}

			//根据用户ID模糊查询
			Set<String> keys = stringRedisTemplate.keys("*" + sysUser.getId());
			//清除redis中存在此手机号的token记录
			stringRedisTemplate.delete(keys);

			//生成token
			String token = JwtUtil.sign(username, syspassword);
			redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token + sysUser.getId(), token);
			//设置超时时间
			redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token + sysUser.getId(), JwtUtil.EXPIRE_TIME/1000);

			JSONObject obj = new JSONObject();
			obj.put("token", token);
			obj.put("userInfo", sysUser);
			result.setResult(obj);
			result.success("登录成功");
			sysBaseAPI.addLog("用户名: "+username+",登录成功！", CommonConstant.LOG_TYPE_1, null);
		}
		return result;
	}

	/**
	 * 退出登录
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/logout")
	public Result<Object> logout(HttpServletRequest request,HttpServletResponse response) {
		//用户退出逻辑
		Subject subject = SecurityUtils.getSubject();
		SysUser sysUser = (SysUser)subject.getPrincipal();
		sysBaseAPI.addLog("用户名: "+sysUser.getRealname()+",退出成功！", CommonConstant.LOG_TYPE_1, null);
		log.info(" 用户名:  "+sysUser.getRealname()+",退出成功！ ");
		subject.logout();

		String token = request.getHeader(DefContants.X_ACCESS_TOKEN);
		//清空用户Token缓存
		redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token + sysUser.getId());
		//清空用户角色缓存
		redisUtil.del(CommonConstant.PREFIX_USER_ROLE + sysUser.getUsername());
		return Result.ok("退出登录成功！");
	}

	/**
	 * 获取访问量
	 * @return
	 */
	@GetMapping("loginfo")
	public Result<JSONObject> loginfo() {
		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();
		// 获取系统访问记录
		Long totalVisitCount = logService.findTotalVisitCount();
		obj.put("totalVisitCount", totalVisitCount);
		Long todayVisitCount = logService.findTodayVisitCount();
		obj.put("todayVisitCount", todayVisitCount);
		Long todayIp = logService.findTodayIp();
		obj.put("todayIp", todayIp);
		result.setResult(obj);
		result.success("登录成功");
		return result;
	}

}
