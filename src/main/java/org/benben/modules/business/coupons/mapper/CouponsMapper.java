package org.benben.modules.business.coupons.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.benben.modules.business.coupons.entity.Coupons;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 优惠券
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
public interface CouponsMapper extends BaseMapper<Coupons> {
    int getCouponsCount(String userId);
}
