package org.benben.modules.business.coupons.service.impl;

import org.benben.common.aspect.annotation.AutoLog;
import org.benben.modules.business.coupons.entity.Coupons;
import org.benben.modules.business.coupons.mapper.CouponsMapper;
import org.benben.modules.business.coupons.service.ICouponsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 优惠券
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Service
public class CouponsServiceImpl extends ServiceImpl<CouponsMapper, Coupons> implements ICouponsService {
    @Autowired
    private CouponsMapper couponsMapper;
    @Override
    public int getCouponsCount(String userId) {
        return couponsMapper.getCouponsCount(userId);
    }
}
