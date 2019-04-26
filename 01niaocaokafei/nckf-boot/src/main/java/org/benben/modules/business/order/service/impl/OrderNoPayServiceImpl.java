package org.benben.modules.business.order.service.impl;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class OrderNoPayServiceImpl  implements IOrderNoPayService {
	
	@Autowired
	private OrderNoPayMapper orderNoPayMapper;

	@Override
	public boolean insert(OrderNoPay orderNoPay) {
		try{
			orderNoPayMapper.insert(orderNoPay);
			return true;
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return false;

	}

	@Override
	public List<OrderNoPay> selectAll() {
		return orderNoPayMapper.selectAll();
	}

	@Override
	public boolean removeById(String id) {
		try{
			orderNoPayMapper.removeById(id);
			return true;
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return false;
	}
}
