package org.benben.modules.shiro;

import org.apache.commons.lang3.StringUtils;
import org.benben.common.constant.CommonConstant;
import org.benben.common.util.RedisUtil;
import org.benben.modules.business.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取当前登录会员用户
 */
@Component
public class LoginUser {

	private static RedisUtil redisUtil;

	@Autowired
	public void setRedisUtil(RedisUtil redisUtil) {
		LoginUser.redisUtil = redisUtil;
	}

	public static String getCurrentUserId(){

		//获取到当前线程绑定的请求对象
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		return (String) request.getAttribute(CommonConstant.SIGN_PHONE_USER);

	}

	public static Object getCurrentUser(){

		User entity = new User();

		//获取到当前线程绑定的请求对象
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		String id = (String) request.getAttribute(CommonConstant.SIGN_PHONE_USER);

		if(StringUtils.isNotBlank(id)){

			entity = (User) redisUtil.get(CommonConstant.SIGN_PHONE_USER + id);

		}

		return entity;

	}


}
