package org.benben.modules.business.usercoupons.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.benben.modules.business.usercoupons.entity.UserCoupons;
import org.benben.modules.business.usercoupons.mapper.UserCouponsMapper;
import org.benben.modules.business.usercoupons.service.IUserCouponsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 用户优惠券
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
@Service
public class UserCouponsServiceImpl extends ServiceImpl<UserCouponsMapper, UserCoupons> implements IUserCouponsService {

	@Autowired
	private UserCouponsMapper userCouponsMapper;

	/**
	 * 根据用户ID查询优惠券
	 * @param userId
	 * @return
	 */
	@Override
	public List<UserCoupons> queryByUserId(String userId) {

		QueryWrapper<UserCoupons> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id",userId);

		return userCouponsMapper.selectList(queryWrapper);
	}

	@Override
	public void updateStatus(String couponsId,String status) {
		userCouponsMapper.updateStatus(couponsId,status);
	}

	@Override
	public int getCouponsCount(String userId) {
		return userCouponsMapper.getCouponsCount(userId);
	}
}
