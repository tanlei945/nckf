package org.benben.modules.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.store.entity.Store;
import org.benben.modules.business.store.service.IStoreService;
import org.benben.modules.system.entity.SysDepart;
import org.benben.modules.system.entity.SysUser;
import org.benben.modules.system.entity.SysUserRole;
import org.benben.modules.system.mapper.SysDepartMapper;
import org.benben.modules.system.mapper.SysUserMapper;
import org.benben.modules.system.mapper.SysUserRoleMapper;
import org.benben.modules.system.model.SysUserCacheInfo;
import org.benben.modules.system.service.ISysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author scott
 * @since 2018-12-20
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
	
	@Autowired
	private SysUserMapper userMapper;
	
	@Autowired
	private SysUserRoleMapper sysUserRoleMapper;
	
	@Autowired
	private SysDepartMapper sysDepartMapper;

	@Autowired
	private IStoreService storeService;
	
	@Override
	public SysUser getUserByName(String username) {
		return userMapper.getUserByName(username);
	}
	
	
	@Override
	public void addUserWithRole(SysUser user, String roles) {
		this.save(user);
		if(oConvertUtils.isNotEmpty(roles)) {
			String[] arr = roles.split(",");
			for (String roleId : arr) {
				SysUserRole userRole = new SysUserRole(user.getId(), roleId);
				sysUserRoleMapper.insert(userRole);
			}
		}
	}

	@Override
	public void editUserWithRole(SysUser user, String roles) {
		this.updateById(user);
		//先删后加
		sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId, user.getId()));
		if(oConvertUtils.isNotEmpty(roles)) {
			String[] arr = roles.split(",");
			for (String roleId : arr) {
				SysUserRole userRole = new SysUserRole(user.getId(), roleId);
				sysUserRoleMapper.insert(userRole);
			}
		}
	}


	@Override
	public List<String> getRole(String username) {
		return sysUserRoleMapper.getRoleByUserName(username);
	}


	@Override
	public SysUserCacheInfo getCacheUser(String username) {
		SysUserCacheInfo info = new SysUserCacheInfo();
		info.setOneDepart(true);
		SysUser user = userMapper.getUserByName(username);
		info.setSysUserCode(user.getUsername());
		info.setSysUserName(user.getRealname());
		
		List<SysDepart> list = sysDepartMapper.queryUserDeparts(user.getId());
		List<String> sysOrgCode = new ArrayList<String>();
		if(list==null || list.size()==0) {
			//当前用户无部门
			sysOrgCode.add("0");
		}else if(list.size()==1) {
			sysOrgCode.add(list.get(0).getOrgCode());
		}else {
			info.setOneDepart(false);
			for (SysDepart dpt : list) {
				sysOrgCode.add(dpt.getOrgCode());
			}
		}
		info.setSysOrgCode(sysOrgCode);
		// TODO companycode 没有处理
		return info;
	}

	@Override
	public String querySuperAdmin() {
		SysUser sysuser = (SysUser) SecurityUtils.getSubject().getPrincipal();
		List<String> strings = userMapper.querySuperAdmin();
		if(strings.contains(sysuser.getId())){
			return null;
		}else{
			return sysuser.getId();
		}
	}

	@Override
	public List<SysUser> queryGeneralUser() {
		return userMapper.queryGeneralUser();
	}

	@Override
	public String queryStoreId() {
		List<String> strings = userMapper.queryIsAdmin();
		SysUser sysuser = (SysUser) SecurityUtils.getSubject().getPrincipal();
		log.info("当前登录人的信息-------------->"+sysuser);
		if(strings.contains(sysuser.getId())){
			return null;
		}else{
			String belongId = sysuser.getId();
			log.info("当前管理员的id------------->"+belongId);
			QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("belong_id",belongId);
			Store store = storeService.getOne(queryWrapper);
			log.info("门店信息------------->"+store);
			if(store != null){
				return store.getId();
			}
			return null;
		}
	}
}
