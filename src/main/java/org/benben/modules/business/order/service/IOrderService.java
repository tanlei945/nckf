package org.benben.modules.business.order.service;

import org.benben.modules.business.order.entity.OrderGoods;
import org.benben.modules.business.order.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 订单
 * @author： jeecg-boot
 * @date：   2019-04-23
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

	public Order selectById(String id);
	
}
