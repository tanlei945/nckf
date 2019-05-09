package org.benben.modules.business.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
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
import org.benben.modules.business.store.entity.Store;
import org.benben.modules.business.store.service.IStoreService;
import org.benben.modules.business.user.entity.User;
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
	@Autowired
	private IStoreService storeService;
	
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
	public Order queryByOrderId(String orderId) {
		QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("order_id",orderId);
		Order order = orderService.getOne(queryWrapper);
		return order;
	}


	@Override
	public List<OrderPage> queryList(Order order) {
		if(order!=null){
			if("0".equals(order.getStatus())){
				//把状态为0的查询全部 分开为查询1 2 3 最后再合并为一个List
				//状态为1的查询
				order.setStatus("1");
				List<OrderPage> orderPageList1 = orderService.queryList(order);
				//状态为2的查询
				order.setStatus("2");
				List<OrderPage> orderPageList2 = orderService.queryList(order);
				//状态为3的查询
				order.setStatus("3");
				List<OrderPage> orderPageList3 = orderService.queryList(order);
				//合并
				List<OrderPage> orderListAll = new ArrayList<>();

				for (OrderPage orderPage : orderPageList1) {
					orderListAll.add(orderPage);
				}
				for (OrderPage orderPage : orderPageList2) {
					orderListAll.add(orderPage);
				}
				for (OrderPage orderPage : orderPageList3) {
					orderListAll.add(orderPage);
				}
				return orderListAll;
			}else{
				List<OrderPage> orderPageList = orderService.queryList(order);
				return orderPageList;
			}
		}
		return null;
	}


	@Override
	public Order add(OrderPage orderPage) {
		//app端传过来的订单金额需要与数据库中实际的商品金额做判断
		double appMoney = orderPage.getOrderMoney();
		List<OrderGoods> orderGoodsList = orderPage.getOrderGoodsList();
		int sum = 0;
		Result<Order> result = new Result<Order>();
		for (OrderGoods orderGoods : orderGoodsList) {
			sum += orderGoods.getGoodsCount()*orderGoods.getPerPrice();
		}
		Order order = new Order();
		if(sum==appMoney){
			//订单id---->时间戳+用户id
			String orderId = orderPage.getUserId()+System.currentTimeMillis();
			orderPage.setOrderId(orderId);
			User user = (User) SecurityUtils.getSubject().getPrincipal();
			orderPage.setUsername(user.getUsername());
			String storeId = orderPage.getStoreId();
			Store store = storeService.getById(storeId);
			orderPage.setStorename(store.getStoreName());
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
	@Transactional
	public boolean cancel(String id) {
		Order order = orderMapper.selectById(id);
		if(order==null) {
			log.info("未找到对应实体");
			return false;
		}else {
			//设置订单状态为取消状态
			order.setStatus("9");
			try{
				orderService.updateById(order);
				orderNoPayService.removeById(order.getId());
				return true;
			}catch(Exception e){
				log.error(e.getMessage());
				return false;
			}
		}
	}

	//支付完成修改订单状态
	@Override
	@Transactional
	public boolean edit(String id,String tradeNo) {
		Order order = new Order();
		order.setId(id);
		order.setStatus("3");
		order.setTradeNo(tradeNo);
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
	@Override
	public boolean invoiceOk(List<String> orderIdList){
		Order order = new Order();
		order.setInvoiceFlag("1");
		Map<String,String> map = new HashMap<>();
		try{
			for (String id : orderIdList) {
				order.setId(id);
				orderService.updateById(order);
			}
			return true;
		}catch(Exception e){
			log.info(e.getMessage());
			return false;
		}
	}

	@Override
	@Transactional
	//骑手接单
	public synchronized boolean riderOrder(String riderId, String orderId) {
		Order order = orderService.getById(orderId);
		if(order!=null&& StringUtils.isBlank(order.getOrderId())){
			order.setRiderId(riderId);
			order.setUpdateTime(new Date());
			if(orderService.updateById(order)){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
}
