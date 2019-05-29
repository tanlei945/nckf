package org.benben.modules.business.usercoupons.service;

import org.benben.modules.business.usercoupons.entity.UserCoupons;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 用户优惠券
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
public interface IUserCouponsService extends IService<UserCoupons> {

	/**
	 * 根据用户ID查询优惠券
	 * @param userId
	 * @return
	 */
	List<UserCoupons> queryByUserId(String userId);

	void updateStatus(String couponsId,String status);

}
