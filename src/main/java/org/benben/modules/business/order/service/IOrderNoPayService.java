package org.benben.modules.business.order.service;

import org.benben.modules.business.order.vo.OrderNoPay;

import java.util.List;

/**
 * @Description: 订单详情
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
public interface IOrderNoPayService{

	void insert(OrderNoPay orderNoPay);
	List<OrderNoPay> selectAll();
	void removeById(String id);

}
