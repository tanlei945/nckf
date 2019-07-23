package org.benben.modules.business.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.benben.modules.business.user.entity.UserThird;
import org.benben.modules.business.user.mapper.UserThirdMapper;
import org.benben.modules.business.user.service.IUserThirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: 用户三方关联
 * @author： jeecg-boot
 * @date：   2019-04-20
 * @version： V1.0
 */
@Service
public class UserThirdServiceImpl extends ServiceImpl<UserThirdMapper, UserThird> implements IUserThirdService {
	
	@Autowired
	private UserThirdMapper userThirdMapper;
	
	@Override
	public List<UserThird> selectByMainId(String mainId) {
		return userThirdMapper.selectByMainId(mainId);
	}

	/**
	 * 根据类型和用户ID查询，目的查询是否存在，处理重复绑定问题
	 * @param userId
	 * @param openType
	 * @return
	 */
	@Override
	public UserThird queryByUserIdAndStatus(String userId, String openType){

		QueryWrapper<UserThird> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
				.eq(UserThird::getUserId,userId)
				.eq(UserThird::getOpenType,openType);

		return userThirdMapper.selectOne(queryWrapper);
	}

	@Override
	@Transactional
	public int bindOpenId(String userid, String openid, String openType) {

		UserThird userThird = new UserThird();

		userThird.setOpenType(openType);
		userThird.setUserId(userid);
		userThird.setOpenId(openid);

		return userThirdMapper.insert(userThird);
	}

	/**
	 * 根据openid查询绑定信息
	 * @param openid
	 * @return
	 */
	@Override
	public UserThird queryByOpenid(String openid) {

		QueryWrapper<UserThird> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
				.eq(UserThird::getOpenId,openid)
				.eq(UserThird::getStatus,"0");

		return userThirdMapper.selectOne(queryWrapper);
	}
}
