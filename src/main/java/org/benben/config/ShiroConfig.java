package org.benben.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import javax.servlet.Filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.benben.modules.shiro.authc.MyRealm;
import org.benben.modules.shiro.authc.aop.JwtFilter;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.yaml.snakeyaml.Yaml;

/**
 * @author: Scott
 * @date: 2018/2/7
 * @description: shiro 配置类
 */

@Configuration
@Slf4j
public class ShiroConfig {

	/**
	 * Filter Chain定义说明 
	 * 
	 * 1、一个URL可以配置多个Filter，使用逗号分隔
	 * 2、当设置多个过滤器时，全部验证通过，才视为通过
	 * 3、部分过滤器可指定参数，如perms，roles
	 */
	@Bean("shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 拦截器
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 配置不会被拦截的链接 顺序判断
		filterChainDefinitionMap.put("/sys/login", "anon"); //登录接口排除
		filterChainDefinitionMap.put("/auth/2step-code", "anon");//登录验证码
		filterChainDefinitionMap.put("/test/jeecgDemo/**", "anon"); //测试接口
		filterChainDefinitionMap.put("/test/jeecgOrderMain/**", "anon"); //测试接口
		filterChainDefinitionMap.put("/**/exportXls", "anon"); //导出接口
		filterChainDefinitionMap.put("/**/importExcel", "anon"); //导入接口
		filterChainDefinitionMap.put("/sys/common/view/**", "anon");//图片预览不限制token
		filterChainDefinitionMap.put("/", "anon");
		filterChainDefinitionMap.put("/**/*.js", "anon");
		filterChainDefinitionMap.put("/**/*.css", "anon");
		filterChainDefinitionMap.put("/**/*.html", "anon");
		filterChainDefinitionMap.put("/**/*.svg", "anon");
		filterChainDefinitionMap.put("/**/*.jpg", "anon");
		filterChainDefinitionMap.put("/**/*.png", "anon");
		filterChainDefinitionMap.put("/**/*.ico", "anon");
		filterChainDefinitionMap.put("/druid/**", "anon");
		filterChainDefinitionMap.put("/doc.html", "anon");
		filterChainDefinitionMap.put("/swagger**/**", "anon");
		filterChainDefinitionMap.put("/webjars/**", "anon");
		filterChainDefinitionMap.put("/v2/**", "anon");
		filterChainDefinitionMap.put("/api/**", "anon");
		filterChainDefinitionMap.put("/h5/**", "anon");


		//暂时的
/*		filterChainDefinitionMap.put("/api/v1/user/mobileLogin", "anon");
		filterChainDefinitionMap.put("/api/v1/user/qqLoginCallback", "anon");
		filterChainDefinitionMap.put("/api/v1/user/wxLoginCallBack", "anon");
		filterChainDefinitionMap.put("/api/v1/user/wbLoginCallback", "anon");
		filterChainDefinitionMap.put("/api/v1/user/userRegister", "anon");
		filterChainDefinitionMap.put("/api/v1/user/riderRegister", "anon");
		filterChainDefinitionMap.put("/api/v1/user/login", "anon");
		filterChainDefinitionMap.put("/api/v1/user/forgetPassword", "anon");
		filterChainDefinitionMap.put("/api/v1/user/isExistMobile", "anon");
		//filterChainDefinitionMap.put("/api/v1/sms/**", "anon");
		filterChainDefinitionMap.put("/api/v1/login/third", "anon");
		filterChainDefinitionMap.put("/api/v1/validate/**", "anon");
		filterChainDefinitionMap.put("/api/v1/announcement/**", "anon");
		filterChainDefinitionMap.put("/api/v1/banner/**", "anon");
		filterChainDefinitionMap.put("/api/v1/rechargeConfig/queryRechargeDictionary", "anon");
		filterChainDefinitionMap.put("/api/v1/coupons/queryCoupons", "anon");
		filterChainDefinitionMap.put("/api/v1/announcement/queryAnnouncementTitle", "anon");
		filterChainDefinitionMap.put("/api/v1/user/riderlogin", "anon");
		filterChainDefinitionMap.put("/api/v1/user/userlogin", "anon");
		filterChainDefinitionMap.put("/api/v1/common/uploadImageLocal", "anon");
		filterChainDefinitionMap.put("/api/v1/systemConfig/queryByTime", "anon");
		filterChainDefinitionMap.put("/api/v1/store/query_all_store", "anon");
		filterChainDefinitionMap.put("/api/v1/category/query_all_store/getCategory", "anon");
		filterChainDefinitionMap.put("/api/v1/goods/queryGoodsByCategory", "anon");
		filterChainDefinitionMap.put("/api/v1/category/getCategory", "anon");
		filterChainDefinitionMap.put("/api/v1/rechargeConfig/queryHowToBuy", "anon");
		filterChainDefinitionMap.put("/api/v1/rechargeConfig/queryHowToRecharge", "anon");
		filterChainDefinitionMap.put("/api/v1/rechargeConfig/queryInvoiceIssued", "anon");
		filterChainDefinitionMap.put("/api/v1/rechargeConfig/queryOtherProblems", "anon");
		filterChainDefinitionMap.put("/api/v1/rechargeConfig/queryRechargeDictionary", "anon");
		filterChainDefinitionMap.put("/api/v1/feedback/queryFeedBackList", "anon");
		filterChainDefinitionMap.put("/api/v1/common/uploadImageLocal", "anon");
		filterChainDefinitionMap.put("/api/v1/category/getCategory", "anon");
		filterChainDefinitionMap.put("/api/v1/goods/queryGoodsByCategory", "anon");
		filterChainDefinitionMap.put("/api/v1/goods/queryGoodsSpec", "anon");
		filterChainDefinitionMap.put("/api/v1/store/queryStoreByDistance", "anon");
		filterChainDefinitionMap.put("/api/v1/store/queryStoreById", "anon");
		filterChainDefinitionMap.put("/api/v1/store/query_all_store", "anon");
		filterChainDefinitionMap.put("/api/v1/announcement/queryAnnouncementById", "anon");
		filterChainDefinitionMap.put("/api/v1/announcement/queryAnnouncementTitle", "anon");
		filterChainDefinitionMap.put("/api/v1/coupons/queryCoupons", "anon");
		filterChainDefinitionMap.put("/api/v1/systemConfig/queryByTime", "anon");
		filterChainDefinitionMap.put("/api/v1/announcement/queryAnnouncementTitle", "anon");
		filterChainDefinitionMap.put("/api/v1/region/list", "anon");
		filterChainDefinitionMap.put("/api/v1/store/queryStoreByName", "anon");
		filterChainDefinitionMap.put("/api/v1/store/queryScopeById", "anon");
		filterChainDefinitionMap.put("/api/v1/store/queryStoreByDistance", "anon");
		filterChainDefinitionMap.put("/api/v1/store/queryStoreById", "anon");
		filterChainDefinitionMap.put("/api/v1/store/query_all_store", "anon");
		filterChainDefinitionMap.put("/api/v1/evaluate/queryEvaluateList", "anon");*/
		//性能监控
		filterChainDefinitionMap.put("/actuator/metrics/**", "anon");
		filterChainDefinitionMap.put("/actuator/httptrace/**", "anon");
		filterChainDefinitionMap.put("/redis/**", "anon");

		//TODO 排除Online请求
		filterChainDefinitionMap.put("/auto/cgform/**", "anon");
		filterChainDefinitionMap.put("/online/cgreport/api/exportXls/**", "anon");

		filterChainDefinitionMap.put("/api/v1/user/login", "anon");
		//从配置文件读取添加不需要token的路径
		//从配置文件读取添加不需要token的路径
/*		Yaml yaml = new Yaml();
		try {
			InputStream inputStream = ShiroConfig.class.getClassLoader().getResourceAsStream("noneed-login.yml");
			HashMap hashMap = yaml.loadAs(inputStream, HashMap.class);
			if(hashMap!=null){
				List<String> filterlist = (List)hashMap.get("filterlist");
				filterlist.forEach(value->filterChainDefinitionMap.put(value,"anon"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		// 添加自己的过滤器并且取名为jwt
		Map<String, Filter> filterMap = new HashMap<String, Filter>(1);
		filterMap.put("jwt", new JwtFilter());
		shiroFilterFactoryBean.setFilters(filterMap);
		// <!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边
		filterChainDefinitionMap.put("/**", "jwt");

		// 未授权界面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	@Bean("securityManager")
	public DefaultWebSecurityManager securityManager(MyRealm myRealm) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(myRealm);

		/*
		 * 关闭shiro自带的session，详情见文档
		 * http://shiro.apache.org/session-management.html#SessionManagement-
		 * StatelessApplications%28Sessionless%29
		 */
		DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
		DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
		defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
		subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
		securityManager.setSubjectDAO(subjectDAO);

		return securityManager;
	}

	/**
	 * 下面的代码是添加注解支持
	 * @return
	 */
	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
	}

	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}

}
