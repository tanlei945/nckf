package org.benben.modules.business.usercoupons.mapper;


import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.usercoupons.entity.UserCoupons;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Description: 用户优惠券
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
public interface UserCouponsMapper extends BaseMapper<UserCoupons> {

	@Select("select * from bb_user_coupons where user_id = #{userId}")
	List<UserCoupons> queryByUserId(String userId);

	@Select("update bb_user_coupons set status = #{status} where id = #{userCouponsId}")
	void updateStatus(@Param("userCouponsId") String userCouponsId, @Param("status") String status);


	@Select("select count(*) from bb_user_coupons where #{status} ='1'")
	int getCouponsCount(String userId);




}
