package org.benben.modules.system.service;

import java.util.Map;

import org.benben.modules.system.entity.SysUserRole;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author scott
 * @since 2018-12-21
 */
public interface ISysUserRoleService extends IService<SysUserRole> {
	
	/**
	 * 查询所有的用户角色信息
	 * @return
	 */
	Map<String,String> queryUserRole();

	Map<String,String> queryGeneralUserRole();
}
