package org.benben.modules.business.order.service.impl;

import org.benben.modules.business.order.entity.OrderDetail;
import org.benben.modules.business.order.mapper.OrderDetailMapper;
import org.benben.modules.business.order.service.IOrderDetailService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 订单详情
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

}
