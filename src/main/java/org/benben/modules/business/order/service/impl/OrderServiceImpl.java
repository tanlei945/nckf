package org.benben.modules.business.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.util.DateUtils;
import org.benben.common.util.DayUtils;
import org.benben.common.util.DistanceUtil;
import org.benben.common.util.UUIDGenerator;
import org.benben.modules.business.goods.entity.Goods;
import org.benben.modules.business.goods.service.IGoodsService;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.entity.OrderGoods;
import org.benben.modules.business.order.mapper.OrderGoodsMapper;
import org.benben.modules.business.order.mapper.OrderMapper;
import org.benben.modules.business.order.service.IOrderNoPayService;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.order.vo.OrderNoPay;
import org.benben.modules.business.order.vo.OrderPage;
import org.benben.modules.business.order.vo.RiderOrder;
import org.benben.modules.business.rideraddress.entity.RiderAddress;
import org.benben.modules.business.rideraddress.service.IRiderAddressService;
import org.benben.modules.business.store.entity.Store;
import org.benben.modules.business.store.service.IStoreService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.benben.modules.business.usercoupons.service.IUserCouponsService;
import org.benben.modules.shiro.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	private IUserCouponsService userCouponsService;
	@Autowired
	private IOrderNoPayService orderNoPayService;
	@Autowired
	private IStoreService storeService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IGoodsService goodsService;
	@Autowired
	private IRiderAddressService riderAddressService;
	
	@Override

	public void saveMain(Order order, List<OrderGoods> orderGoodsList) {
		orderMapper.insert(order);
		log.info(order.toString());
		for(OrderGoods entity:orderGoodsList) {
			//外键设置
			entity.setOrderId(order.getId());
			entity.setId(UUIDGenerator.generate());
			log.info(entity.toString());
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
		queryWrapper.eq("id",orderId);
		Order order = orderService.getOne(queryWrapper);
		return order;
	}



	/*@Override
	public Order add(OrderPage orderPage) {
		//给订单的商品数量赋值
		int count = 0;
		List<OrderGoods> goodsList = orderPage.getOrderGoodsList();
		for (OrderGoods orderGoods : goodsList) {
			count += orderGoods.getGoodsCount();
		}
		orderPage.setGoodsCount(count);

		//app端传过来的订单金额需要与数据库中实际的商品金额做判断
		double appMoney = orderPage.getOrderMoney();
		List<OrderGoods> orderGoodsList = orderPage.getOrderGoodsList();
		int sum = 0;
		Result<Order> result = new Result<Order>();
		for (OrderGoods orderGoods : orderGoodsList) {
			sum += orderGoods.getGoodsCount()*orderGoods.getPerPrice();
		}
		Order order = new Order();
		if(sum == appMoney){
			//开始给order赋值
			User user = (User) LoginUser.getCurrentUser();
			//订单id---->用户id+时间戳
			String orderId = user.getId()+System.currentTimeMillis();
			orderPage.setOrderId(orderId);
			//下单用户名
			orderPage.setUsername(user.getUsername());
			orderPage.setUserId(user.getId());
			//获取订单商品的id
			String goodsId = orderPage.getOrderGoodsList().get(0).getGoodsId();
			//查出商品信息
			Goods goods = goodsService.getById(goodsId);
			//利用商品id获取下单商家的id
			Store store = storeService.getById(goods.getBelongId());
			//给订单的商家id和商家名称赋值
			orderPage.setStoreId(store.getId());
			orderPage.setStorename(store.getStoreName());

			BeanUtils.copyProperties(orderPage, order);
			//log.info("订单信息"+order);
			//log.info("订单信息"+orderPage);
			orderService.saveMain(order, orderPage.getOrderGoodsList());
			//修改优惠券使用的状态
			String userCouponsId = order.getUserCouponsId();
			if(userCouponsId != null){
				userCouponsService.updateStatus(userCouponsId,"1");
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

	}*/

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
				//设置优惠券状态为未使用,如果使用优惠券的话
				if(order.getUserCouponsId() != null){
				userCouponsService.updateStatus(order.getUserCouponsId(),"0");
				}
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
	public boolean invoiceOk(String orderIdList){
		Order order = new Order();
		order.setInvoiceFlag("1");
		Map<String,String> map = new HashMap<>();
		try{
			for (String id : orderIdList.split(",")) {
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
		if(order!=null){
			//给订单的骑手id赋上值
			order.setRiderId(riderId);
			//给订单的rider  name 赋上值
			User user = userService.getById(riderId);
			order.setRidername(user.getUsername());
			order.setRiderOk("1");
			//order.setGetTime(new Date());
			order.setRiderPhone(user.getMobile());
			order.setJiedanTime(new Date());
			//订单修改时间
			order.setUpdateTime(new Date());
			if(orderService.updateById(order)){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}


	@Override
	public boolean riderGetOrder(String riderId, String orderId) {
		Order order = orderService.getById(orderId);
		order.setRiderOk("2");
		order.setGetTime(new Date());
		if(orderService.updateById(order)){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Double countMoney() {
		return orderMapper.OrderSumMoney();
	}

	@Override
	public Integer countOrder() {
		return orderMapper.OrderCount();
	}
	@Override
	public Double DiffDayMoney() {
		Double diffmoney =0.00;
		try {
			Calendar calendar = DateUtils.getCalendar();
			Date date = DateUtils.parseDate("2019-07-05", "yyyy-MM-dd");
			calendar.setTime(date);
			int d = DateUtils.dateDiff('d',DateUtils.getCalendar(), calendar);
			Double aDouble = orderService.countMoney();
			if(d!=0){
				diffmoney = aDouble / d;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return 0.00;
		}
		return diffmoney;
	}


	@Override
	public Page<RiderOrder> queryOrderByDate(String beginTime, String endTime, Integer pageNo, Integer pageSize) throws Exception{
		User user = (User) LoginUser.getCurrentUser();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDate = formatter.parse(beginTime);
		Date endDate = formatter.parse(endTime);

		QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("rider_id",user.getId()).eq("rider_ok","3").eq("rider_del_flag","1");
		List<Order> list = orderService.list(queryWrapper);
		List<Order> orderList = new ArrayList<>();
		for (Order order : list) {
			if(!beginDate.after(order.getOverTime()) && !order.getOverTime().after(endDate)){
				orderList.add(order);
			}
		}

		List<RiderOrder> riderOrderList = query(user,orderList);

		Page<RiderOrder> pageList = new Page<>(pageNo,pageSize);
		pageList.setRecords(riderOrderList);

		return pageList;


	}

	@Override
	public Page<RiderOrder> queryOrderToday(Integer pageNo, Integer pageSize)  throws  Exception{

		User user = (User) LoginUser.getCurrentUser();

		QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("rider_id",user.getId()).eq("rider_ok","3").eq("rider_del_flag","1");
		List<Order> list = orderService.list(queryWrapper);
		List<Order> orderList = new ArrayList<>();
		for (Order order : list) {
			if(DayUtils.isYesterday(order.getOverTime())){
				orderList.add(order);
			}
		}


		List<RiderOrder> riderOrderList = query(user,orderList);

		Page<RiderOrder> pageList = new Page<>(pageNo,pageSize);
		pageList.setRecords(riderOrderList);

		return pageList;
	}

	@Override
	public Page<RiderOrder> queryOrderYest(Integer pageNo, Integer pageSize)  throws  Exception{
		User user = (User) LoginUser.getCurrentUser();

		QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("rider_id",user.getId()).eq("rider_ok","3").eq("rider_del_flag","1");
		List<Order> list = orderService.list(queryWrapper);
		List<Order> orderList = new ArrayList<>();
		for (Order order : list) {
			if(DayUtils.isYesterday(order.getOverTime())){
				orderList.add(order);
			}
		}
		List<RiderOrder> riderOrderList = query(user,orderList);


		Page<RiderOrder> pageList = new Page<>(pageNo,pageSize);
		pageList.setRecords(riderOrderList);

		return pageList;
	}

	@Override
	public Page<RiderOrder> queryOrderQiantian(Integer pageNo, Integer pageSize)  throws  Exception{
		User user = (User) LoginUser.getCurrentUser();

		QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("rider_id",user.getId()).eq("rider_ok","3").eq("rider_del_flag","1");
		List<Order> list = orderService.list(queryWrapper);
		List<Order> orderList = new ArrayList<>();
		for (Order order : list) {
			if(DayUtils.isQiantian(order.getOverTime())){
				orderList.add(order);
			}
		}
		List<RiderOrder> riderOrderList = query(user,orderList);


		Page<RiderOrder> pageList = new Page<>(pageNo,pageSize);
		pageList.setRecords(riderOrderList);

		return pageList;
	}



	@Override
	public Map<String, Object> queryOrderCount() {

		User user = (User) LoginUser.getCurrentUser();
		//获取所需状态的订单
		//orderPageList = orderService.page(page,queryWrapper);
		Map<String,Object> map = new HashMap<>();
		//放入map中
		//map.put("orderList",orderPageList);
		//获取各种状态的订单数量

		//新任务订单的数量
		QueryWrapper<Order> queryWrapper0 = new QueryWrapper<>();

		queryWrapper0.eq("store_id",user.getStoreId()).eq("status","2").eq("rider_ok","0");

		List<Order> list0 = orderService.list(queryWrapper0);
		map.put("0",list0.size());

		//待取货订单的数量
		QueryWrapper<Order> queryWrapper1 = new QueryWrapper<>();
		queryWrapper1.lambda().eq(Order::getRiderId,user.getId()).eq(Order::getRiderOk,"1").eq(Order::getStoreId,user.getStoreId());
		List<Order> list1 = orderService.list(queryWrapper1);
		map.put("1",list1.size());

		//待送达订单的数量
		QueryWrapper<Order> queryWrapper2 = new QueryWrapper<>();
		queryWrapper2.lambda().eq(Order::getRiderId,user.getId()).eq(Order::getRiderOk,"2").eq(Order::getStoreId,user.getStoreId());
		List<Order> list2 = orderService.list(queryWrapper2);
		map.put("2",list2.size());

		//已送达订单的数量
		QueryWrapper<Order> queryWrappe3 = new QueryWrapper<>();
		queryWrappe3.lambda().eq(Order::getRiderId,user.getId()).eq(Order::getRiderOk,"3").eq(Order::getStoreId,user.getStoreId()).eq(Order::getRiderDelFlag,"1");
		List<Order> list3 = orderService.list(queryWrappe3);
		map.put("3",list3.size());

		return map;
	}

	@Override
	public Page<RiderOrder> queryOrderYwc(Integer pageNo, Integer pageSize) {
		User user = (User) LoginUser.getCurrentUser();

		QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("rider_id",user.getId()).eq("rider_ok","3").eq("rider_del_flag","1");
		List<Order> list = orderService.list(queryWrapper);

		List<RiderOrder> riderOrderList = query(user,list);

		Page<RiderOrder> pageList = new Page<>(pageNo,pageSize);
		pageList.setRecords(riderOrderList);

		return  pageList;
	}

	@Override
	public Page<RiderOrder> queryOrderDsd(Integer pageNo, Integer pageSize) {
		User user = (User) LoginUser.getCurrentUser();
		QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("rider_id",user.getId()).eq("rider_ok","2");
		List<Order> list = orderService.list(queryWrapper);

		List<RiderOrder> riderOrderList = query(user,list);

		Page<RiderOrder> pageList = new Page<>(pageNo,pageSize);
		pageList.setRecords(riderOrderList);

		return  pageList;
	}

	@Override
	public Page<RiderOrder> queryOrderDqh(Integer pageNo, Integer pageSize) {
		User user = (User) LoginUser.getCurrentUser();

		QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("rider_id",user.getId()).eq("rider_ok","1");
		List<Order> list = orderService.list(queryWrapper);

		List<RiderOrder> riderOrderList = query(user,list);

		Page<RiderOrder> pageList = new Page<>(pageNo,pageSize);
		pageList.setRecords(riderOrderList);

		return  pageList;
	}

	@Override
	public Page<RiderOrder> queryRiderOrder(Integer pageNo, Integer pageSize) {
		User user = (User) LoginUser.getCurrentUser();

		QueryWrapper<Order> wrapper = new QueryWrapper<>();
		wrapper.eq("store_id",user.getStoreId()).eq("status","2").eq("rider_ok","0");
		List<Order> list = orderService.list(wrapper);

		List<RiderOrder> riderOrderList = query(user,list);

		Page<RiderOrder> pageList = new Page<>(pageNo,pageSize);
		pageList.setRecords(riderOrderList);

		return  pageList;
	}


	//@赵永刚
	private List<RiderOrder> query(User user,List<Order> list){
		//得到骑手位置对象，拿到经纬度
		QueryWrapper<RiderAddress> wrapper1 = new QueryWrapper<>();
		wrapper1.eq("rider_id",user.getId());
		RiderAddress riderAddress = riderAddressService.getOne(wrapper1);
		//创建RiderAddress对象
		List<RiderOrder> riderOrderList = new ArrayList<>();

		if(list == null){
			return new ArrayList<RiderOrder>();
		}
		//遍历订单，得到用户下单地址的经纬度和门店的经纬度
		for (Order order : list) {
			RiderOrder riderOrder = new RiderOrder();
			BeanUtils.copyProperties(order,riderOrder);
			double lat = order.getUserLat();
			double lng = order.getUserLng();

			Store store = storeService.getById(order.getStoreId());

			String disRS = DistanceUtil.algorithm(lat,lng,riderAddress.getLat(),riderAddress.getLng());
			String disRU = DistanceUtil.algorithm(store.getLat(),store.getLng(),riderAddress.getLat(),riderAddress.getLng());

			riderOrder.setRiderAndStoreDis(Double.parseDouble(disRS));
			riderOrder.setRiderAndUserDis(Double.parseDouble(disRU));

			riderOrder.setStoreLat(store.getLat());
			riderOrder.setStoreLng(store.getLng());

			riderOrder.setStorename(store.getStoreName());
			riderOrder.setStoreAddress(store.getAddressDesc());


			String userId = order.getUserId();
			User reciveUser = userService.getById(userId);
			riderOrder.setUserAddress(order.getUserAddress());
			riderOrder.setUserPhone(reciveUser.getMobile());

			riderOrderList.add(riderOrder);
		}

		return riderOrderList;
	}
}
