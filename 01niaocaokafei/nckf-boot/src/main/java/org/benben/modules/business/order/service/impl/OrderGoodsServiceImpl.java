package org.benben.modules.business.order.service.impl;

import org.benben.modules.business.order.entity.OrderGoods;
import org.benben.modules.business.order.mapper.OrderGoodsMapper;
import org.benben.modules.business.order.service.IOrderGoodsService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 订单详情
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
@Service
public class OrderGoodsServiceImpl extends ServiceImpl<OrderGoodsMapper, OrderGoods> implements IOrderGoodsService {
	
	@Autowired
	private OrderGoodsMapper orderGoodsMapper;
	
	@Override
	public List<OrderGoods> selectByMainId(String mainId) {
		return orderGoodsMapper.selectByMainId(mainId);
	}
}
