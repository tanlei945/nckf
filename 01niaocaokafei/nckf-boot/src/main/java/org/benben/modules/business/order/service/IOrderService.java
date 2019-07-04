package org.benben.modules.business.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.entity.OrderGoods;
import org.benben.modules.business.order.vo.OrderPage;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 订单
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
public interface IOrderService extends IService<Order> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(Order order,List<OrderGoods> orderGoodsList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(Order order,List<OrderGoods> orderGoodsList);
	
	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);


	//根据订单id查询订单
	public Order queryByOrderId( String orderId);

	//用户新增订单
	//public Order add(OrderPage orderPage);

	//取消订单
	public boolean cancel(String id);

	//支付完成修改订单状态
	public boolean edit(String id,String tradeNo);

	//开发票后修改订单状态
	public boolean invoiceOk(List<String> orderIdList);

	//骑手确认接单
	public boolean riderOrder(String riderId, String orderId);
	//骑手取货
	public boolean riderGetOrder (String riderId, String orderId);


}
