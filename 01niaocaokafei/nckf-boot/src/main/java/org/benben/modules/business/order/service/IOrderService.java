package org.benben.modules.business.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.common.api.vo.Result;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.entity.OrderGoods;
import org.benben.modules.business.order.vo.OrderPage;
import org.springframework.web.bind.annotation.RequestBody;

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
	public Result<Order> queryByOrderID( String orderId);

	//用户新增订单
	public Result<Order> add(@RequestBody OrderPage orderPage);

	//修改订单接口
	public Result<Order> edit(@RequestBody Order order);

}
