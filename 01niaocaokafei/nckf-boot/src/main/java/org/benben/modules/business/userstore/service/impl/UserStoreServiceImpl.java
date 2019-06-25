package org.benben.modules.business.userstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.benben.modules.business.userstore.entity.UserStore;
import org.benben.modules.business.userstore.mapper.UserStoreMapper;
import org.benben.modules.business.userstore.service.IUserStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 骑手信息
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
@Service
public class UserStoreServiceImpl extends ServiceImpl<UserStoreMapper, UserStore> implements IUserStoreService {

	@Autowired
	private UserStoreMapper userStoreMapper;

	@Override
	public UserStore queryByUserId(String userId) {

		QueryWrapper<UserStore> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id",userId);

		return userStoreMapper.selectOne(queryWrapper);
	}
}
