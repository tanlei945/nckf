package org.benben.modules.shiro.authc.aop;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.benben.common.api.vo.TokenResponse;
import org.benben.common.constant.CommonConstant;
import org.benben.common.util.RedisUtil;
import org.benben.common.util.oConvertUtils;
import org.benben.config.ShiroConfig;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.benben.modules.shiro.authc.util.JwtUtil;
import org.benben.modules.shiro.vo.DefContants;
import org.benben.modules.system.entity.SysUser;
import org.benben.modules.system.service.ISysUserService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.yaml.snakeyaml.Yaml;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;


/**
 * @author Scott
 * @create 2018-07-12 15:56
 * @desc   鉴权登录拦截器
 **/
@Component
@Slf4j
@WebFilter(filterName = "apiFilter",urlPatterns = "/api/*")
public class ApiFilter implements Filter {
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private IUserService userService;
	@Autowired
	private RedisUtil redisUtil;
	@Value(value = "${server.servlet.context-path}")
	private String projectName;

	private List<String> filterlist;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
		Integer type ;
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
		httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
		httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
		// 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
		if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
			httpServletResponse.setStatus(HttpStatus.OK.value());
			return ;
		}
		HttpSession session = httpServletRequest.getSession();
		String currPath = httpServletRequest.getRequestURI();    //当前请求的URL
		currPath = currPath.replace(projectName,""); //去掉项目名，以api开头
		log.info(currPath);
		//判断接口地址是否相同
		if (filterlist.size() != 0 ) {
			for (String s : filterlist) {
				if (s.equals(currPath) ) {
					chain.doFilter(httpServletRequest, httpServletResponse);
					return;
				}
				//匹配/**
				if(s.contains("/**")){
					String path = s.replace("/**","");
					log.info(path);
					log.info(currPath);
					if(currPath.contains(path)){
						chain.doFilter(httpServletRequest, httpServletResponse);
						return;
					}
				}

			}
		}
		//假如token为空返回
		String token = httpServletRequest.getHeader(DefContants.X_ACCESS_TOKEN);
		if (StringUtils.isBlank(token)) {
			returnJson(response);
			return;
		}

		// 解密获得username，用于后台和数据库进行对比
		String username = JwtUtil.getUsername(token);
		// 解密获得userId，用于IP端和数据库进行对比
		String id = JwtUtil.getUserId(token);
		//判断是后台用户访问还是APP用户访问
		String password,sign,userId;
		SysUser sysUser = new SysUser();
		User userInfo = new User();
		if(username != null){
			//获取标识
			sign = username.substring(0, username.indexOf("@") + 1);
			username = username.substring(username.indexOf("@") + 1, username.length());
			// 查询用户信息
			sysUser = sysUserService.getUserByName(username);
			userId = sysUser.getId();
			password = sysUser.getPassword();


		}else if(id !=null){
			//获取标识
			sign = id.substring(0, id.indexOf("@") + 1);
			id = id.substring(id.indexOf("@") + 1, id.length());
			//查询用户信息
			userInfo = userService.getById(id);

			userId = userInfo.getId();
			password = userInfo.getPassword();

		} else{
			//  throw new AuthenticationException("token非法无效!");
			returnJson(response);
			return;
		}

		//校验token是否超时失效 & 或者账号密码是否错误
		if (!jwtTokenRefresh(token, userId, username, password, sign)) {
			returnJson(response);
			return;
		} else {
			chain.doFilter(httpServletRequest, httpServletResponse);
			return;
		}
		/*if (session.getAttribute("logined") != null) {
			chain.doFilter(httpServletRequest, httpServletResponse);
		} else {
			returnJson(response,json);
		}*/

	}


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//从配置文件读取添加不需要token的路径
//		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		Yaml yaml = new Yaml();
		URL url = ShiroConfig.class.getClassLoader().getResource("noneed-login.yml");
		//System.out.println(url.toString());

		Map map = null;
		try {
			map = yaml.load(new FileInputStream(url.getFile()));
			filterlist = (List)map.get("filterlist");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void destroy() {
	}

	private void returnJson(ServletResponse response) {
		String time = String.valueOf(System.currentTimeMillis());
		PrintWriter writer = null;

		TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setCode(2);
		tokenResponse.setData(null);
		tokenResponse.setMsg("token过期");
		tokenResponse.setTime(time);

		//map.put("code",2);
		//map.put("data",null);
		//map.put("msg","token过期");
		//map.put("time",time);
		/*JSONArray array = new JSONArray();
		array.put(map);*/


//		System.out.println(array);

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		try {
			JSONObject  myJson = JSONObject.fromObject(tokenResponse);
			writer = response.getWriter();
			writer.print(myJson);

		} catch (Exception e) {
			//log.error("response error",e);
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	/**
	 * JWTToken刷新生命周期 （解决用户一直在线操作，提供Token失效问题）
	 * 1、登录成功后将用户的JWT生成的Token作为k、v存储到cache缓存里面(这时候k、v值一样)
	 * 2、当该用户再次请求时，通过JWTFilter层层校验之后会进入到doGetAuthenticationInfo进行身份验证
	 * 3、当该用户这次请求JWTToken值还在生命周期内，则会通过重新PUT的方式k、v都为Token值，缓存中的token值生命周期时间重新计算(这时候k、v值一样)
	 * 4、当该用户这次请求jwt生成的token值已经超时，但该token对应cache中的k还是存在，则表示该用户一直在操作只是JWT的token失效了，程序会给token对应的k映射的v值重新生成JWTToken并覆盖v值，该缓存生命周期重新计算
	 * 5、当该用户这次请求jwt在生成的token值已经超时，并在cache中不存在对应的k，则表示该用户账户空闲超时，返回用户信息已失效，请重新登录。
	 * 6、每次当返回为true情况下，都会给Response的Header中设置Authorization，该Authorization映射的v为cache对应的v值。
	 * 7、注：当前端接收到Response的Header中的Authorization值会存储起来，作为以后请求token使用
	 * 8、sign为登录标识,0:系统用户(有效期30分钟)1:会员用户（有效期7天）
	 * 参考方案：https://blog.csdn.net/qq394829044/article/details/82763936
	 *
	 * @param userName
	 * @param passWord
	 * @return
	 */
	public boolean jwtTokenRefresh(String token,String userId, String userName, String passWord, String sign) {
		//缓冲中拿取token
		String cacheToken = String.valueOf(redisUtil.get(CommonConstant.PREFIX_USER_TOKEN + token + userId));
		//若为会员用户,重新规定有效期时长
		if (StringUtils.equals(CommonConstant.SIGN_MEMBER_USER, sign)||StringUtils.equals(CommonConstant.SIGN_RIDER_USER, sign)) {
			JwtUtil.EXPIRE_TIME = JwtUtil.APP_EXPIRE_TIME;
		}

		if (oConvertUtils.isNotEmpty(cacheToken)) {
			//校验token有效性
			if (!JwtUtil.verify(token, userName, passWord)) {
				String newAuthorization = JwtUtil.sign(sign + userName, passWord);
				redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token + userId, newAuthorization);
				//设置超时时间
				redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token + userId, JwtUtil.EXPIRE_TIME / 1000);
			} else {
				redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token + userId, cacheToken);
				//设置超时时间
				redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token + userId, JwtUtil.EXPIRE_TIME / 1000);
			}
			return true;
		}
		return false;
	}


}
