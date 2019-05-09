package org.benben.modules.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.system.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author scott
 * @since 2018-12-20
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
	/**
	  * 通过用户账号查询用户信息
	 * @param username
	 * @return
	 */
	public SysUser getUserByName(@Param("username") String username);

	@Select("SELECT user_id FROM sys_user_role where role_id in (select id from sys_role where role_code = 'superadmin')")
	public List<String> querySuperAdmin();

	@Select("select * from sys_user where id in (select sur.user_id from sys_user_role sur where role_id not in (select id from sys_role where role_code = 'superadmin')) ")
	public List<SysUser> queryGeneralUser();
}
