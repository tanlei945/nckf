package org.benben.modules.business.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.order.entity.Order;

/**
 * @Description: 订单
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
public interface OrderMapper extends BaseMapper<Order> {
    int edit(String id);
}
