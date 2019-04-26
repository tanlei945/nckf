package org.benben.modules.business.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.Result;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.entity.OrderGoods;
import org.benben.modules.business.order.mapper.OrderGoodsMapper;
import org.benben.modules.business.order.mapper.OrderMapper;
import org.benben.modules.business.order.service.IOrderGoodsService;
import org.benben.modules.business.order.service.IOrderNoPayService;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.order.vo.OrderNoPay;
import org.benben.modules.business.order.vo.OrderPage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 订单
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderGoodsMapper orderGoodsMapper;
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrderGoodsService orderGoodsService;
	@Autowired
	private IOrderNoPayService orderNoPayService;
	
	@Override
	@Transactional
	public void saveMain(Order order, List<OrderGoods> orderGoodsList) {
		orderMapper.insert(order);
		for(OrderGoods entity:orderGoodsList) {
			//外键设置
			entity.setOrderId(order.getId());
			orderGoodsMapper.insert(entity);
		}
	}

	@Override
	@Transactional
	public void updateMain(Order order,List<OrderGoods> orderGoodsList) {
		orderMapper.updateById(order);
		
		//1.先删除子表数据
		orderGoodsMapper.deleteByMainId(order.getId());
		
		//2.子表数据重新插入
		for(OrderGoods entity:orderGoodsList) {
			//外键设置
			entity.setOrderId(order.getId());
			orderGoodsMapper.insert(entity);
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		orderMapper.deleteById(id);
		orderGoodsMapper.deleteByMainId(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			orderMapper.deleteById(id);
			orderGoodsMapper.deleteByMainId(id.toString());
		}
	}

	@Override
	public Result<Order> queryByOrderID(String orderId) {
		Result<Order> result = new Result<Order>();
		QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("order_id",orderId);
		Order order = orderService.getOne(queryWrapper);
		if(order==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(order);
			result.setSuccess(true);
		}
		return result;
	}

	@Override
	public Result<Order> add(OrderPage orderPage) {
		//app传过来的订单金额需要与数据库中实际的商品金额做判断
		double appMoney = orderPage.getGoodsMoney();
		List<OrderGoods> orderGoodsList = orderPage.getOrderGoodsList();
		int sum=0;
		Result<Order> result = new Result<Order>();
		for (OrderGoods orderGoods : orderGoodsList) {
			sum += orderGoods.getGoodsCount()*orderGoods.getPerPrice();
		}
		if(sum==appMoney){
			OrderNoPay orderNoPay = new OrderNoPay();
			orderNoPay.setId(orderPage.getId());
			orderNoPay.setStatus(orderPage.getStatus());
			orderNoPay.setCreateBy(orderPage.getCreateBy());
			orderNoPay.setCreateTime(orderPage.getCreateTime());
			orderNoPay.setUpdateBy(orderPage.getUpdateBy());
			orderNoPay.setCreateTime(orderPage.getCreateTime());
			orderNoPayService.insert(orderNoPay);

			//订单id---->时间戳+用户id
			//orderPage.setOrderId(System.currentTimeMillis()+orderPage.getUserId());
			try {
				Order order = new Order();
				BeanUtils.copyProperties(orderPage, order);

				//订单中间表中插入一条数据
				orderNoPayService.insert(orderNoPay);

				orderService.saveMain(order, orderPage.getOrderGoodsList());

				result.success("添加成功！");
			} catch (Exception e) {
				e.printStackTrace();
				log.info(e.getMessage());
				result.error500("操作失败");
			}
			return result;
		}
		result.error500("订单金额异常");
		return result;
	}

	@Override
	public Result<Order> edit(Order order) {
		Result<Order> result = new Result<Order>();
		if(order==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = orderService.updateById(order);
			orderNoPayService.removeById(order.getId());
			result.success("取消订单成功!");
		}
		return result;
	}
}
