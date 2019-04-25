package org.benben.modules.business.order.service.impl;

import org.benben.modules.business.order.mapper.OrderNoPayMapper;
import org.benben.modules.business.order.service.IOrderNoPayService;
import org.benben.modules.business.order.vo.OrderNoPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 订单详情
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
@Service
public class OrderNoPayServiceImpl  implements IOrderNoPayService {
	
	@Autowired
	private OrderNoPayMapper orderNoPayMapper;

	@Override
	public void insert(OrderNoPay orderNoPay) {
		orderNoPayMapper.insert(orderNoPay);
	}

	@Override
	public List<OrderNoPay> selectAll() {
		return orderNoPayMapper.selectAll();
	}

	@Override
	public void removeById(String id) {
		orderNoPayMapper.removeById(id);
	}
}
