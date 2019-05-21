package org.benben.modules.business.usercoupons.mapper;


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

}
