package org.benben.modules.business.rideraddress.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.rideraddress.entity.RiderAddress;
import org.benben.modules.business.rideraddress.mapper.RiderAddressMapper;
import org.benben.modules.business.rideraddress.service.IRiderAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 骑手位置表
 * @author： jeecg-boot
 * @date：   2019-04-27
 * @version： V1.0
 */
@Service
public class RiderAddressServiceImpl extends ServiceImpl<RiderAddressMapper, RiderAddress> implements IRiderAddressService {
    @Override
    public RiderAddress getRiderAddress(String riderId) {
        QueryWrapper<RiderAddress> riderAddressQueryWrapper = new QueryWrapper<>();
        riderAddressQueryWrapper.eq("rider_id",riderId);
        RiderAddress one = getOne(riderAddressQueryWrapper);
        return one;
    }
}
