package org.benben.modules.business.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
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
import java.util.*;

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
	public Result<Order> queryByOrderId(String orderId) {
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
	public Order add(OrderPage orderPage) {
		//app传过来的订单金额需要与数据库中实际的商品金额做判断
		double appMoney = orderPage.getOrderMoney();
		List<OrderGoods> orderGoodsList = orderPage.getOrderGoodsList();
		int sum=0;
		Result<Order> result = new Result<Order>();
		for (OrderGoods orderGoods : orderGoodsList) {
			sum += orderGoods.getGoodsCount()*orderGoods.getPerPrice();
		}
		Order order = new Order();
		if(sum==appMoney){
			//订单id---->时间戳+用户id
			String orderId = System.currentTimeMillis()+orderPage.getUserId();
			orderPage.setOrderId(orderId);
			try {
				BeanUtils.copyProperties(orderPage, order);
				orderService.saveMain(order, orderPage.getOrderGoodsList());
			} catch (Exception e) {
				log.info(e.getMessage());
			}
			QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("order_id",orderId);
			order = orderService.getOne(queryWrapper);

			OrderNoPay orderNoPay = new OrderNoPay();
			orderNoPay.setId(order.getId());
			orderNoPay.setStatus(orderPage.getStatus());
			orderNoPay.setCreateBy(orderPage.getCreateBy());
			orderNoPay.setCreateTime(orderPage.getCreateTime());
			orderNoPay.setUpdateBy(orderPage.getUpdateBy());
			orderNoPay.setCreateTime(orderPage.getCreateTime());
			orderNoPayService.insert(orderNoPay);
			//根据orderId查询Order
			QueryWrapper<Order> wrapper = new QueryWrapper<>();
			wrapper.eq("order_id",orderId);
			order = orderService.getOne(wrapper);
			if(order != null){
				return order;
			}
			return null;
		}
		return null;

	}

	@Override
	public Result<Order> cancel(String id) {
		Result<Order> result = new Result<Order>();
		Order order = orderMapper.selectById(id);
		if(order==null) {
			result.error500("未找到对应实体");
		}else {
			//设置订单状态为取消状态
			order.setStatus("9");
			boolean ok = orderService.updateById(order);
			boolean flag = orderNoPayService.removeById(order.getId());
			if(ok && flag){
				result.success("取消订单成功!");
			}else{
				result.error500("订单取消异常");
			}
		}
		return result;
	}

	//支付完成修改订单状态
	@Override
	public boolean edit(String id) {
		Order order = new Order();
		order.setId(id);
		order.setStatus("3");
		int i = orderMapper.updateById(order);
		return 1==i?true:false;
	}
	//骑手送完单修改订单状态
	public boolean change(String id) {
		Order order = new Order();
		order.setId(id);
		order.setStatus("3");
		int i = orderMapper.updateById(order);
		return 1==i?true:false;
	}
	//

	//用户开发票后修改订单状态
	public Map invoiceOk(List<String> orderIdList){
		Order order = new Order();
		order.setInvoiceFlag("1");
		Map<String,String> map = new HashMap<>();
		try{
			for (String id : orderIdList) {
				order.setId(id);
				orderService.updateById(order);
			}
			map.put("change_flag","更改成功");
		}catch(Exception e){
			log.info(e.getMessage());
			map.put("change_flag","更改失败");
		}
		return map;
	}

	@Override
	@Transactional
	//骑手接单
	public RestResponseBean riderOrder(String riderId, String orderId) {
		/* QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id",orderId);*/
		Order order = orderService.getById(orderId);
		if(order!=null){
			order.setRiderId(riderId);
			order.setUpdateTime(new Date());
			if(orderService.updateById(order)){
				return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),order);
			}else{
				return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.ERROR.getDesc(),null);
			}
		}
		return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.ERROR.getDesc(),null);
	}
}
