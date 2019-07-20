package org.benben.modules.business.order.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.benben.common.XXPay.service.XXPayService;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.DayUtils;
import org.benben.common.util.DistanceUtil;
import org.benben.common.util.IPUtils;
import org.benben.common.util.PageUtil;
import org.benben.modules.business.account.entity.Account;
import org.benben.modules.business.account.service.IAccountService;
import org.benben.modules.business.address.entity.Address;
import org.benben.modules.business.address.service.IAddressService;
import org.benben.modules.business.cart.entity.Cart;
import org.benben.modules.business.cart.service.ICartService;
import org.benben.modules.business.coupons.entity.Coupons;
import org.benben.modules.business.coupons.service.ICouponsService;
import org.benben.modules.business.goods.entity.Goods;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.entity.OrderGoods;
import org.benben.modules.business.order.service.IOrderGoodsService;
import org.benben.modules.business.order.service.IOrderNoPayService;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.order.vo.OrderDistanceVo;
import org.benben.modules.business.order.vo.OrderNoPay;
import org.benben.modules.business.order.vo.RiderOrder;
import org.benben.modules.business.orderMessage.entity.OrderMessage;
import org.benben.modules.business.orderMessage.service.IOrderMessageService;
import org.benben.modules.business.rideraddress.entity.RiderAddress;
import org.benben.modules.business.rideraddress.service.IRiderAddressService;
import org.benben.modules.business.store.entity.Store;
import org.benben.modules.business.store.service.IStoreService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.benben.modules.business.usercoupons.entity.UserCoupons;
import org.benben.modules.business.usercoupons.service.IUserCouponsService;
import org.benben.modules.shiro.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/api/v1/order")
@Slf4j
@Api(tags = {"订单购物车接口"})
public class RestOrderController {
   @Autowired
   private IOrderService orderService;
   @Autowired
   private IOrderGoodsService orderGoodsService;
   @Autowired
   private IOrderNoPayService orderNoPayService;
   @Autowired
   private IUserService userService;
   @Autowired
   private IStoreService storeService;
   @Autowired
   private IRiderAddressService riderAddressService;
   @Autowired
   private ICartService cartService;
   @Autowired
   private IUserCouponsService userCouponsService;
   @Autowired
   private ICouponsService couponsService;
   @Autowired
   private IAddressService addressService;
   @Autowired
   private XXPayService xxPayService;
   @Autowired
   private IOrderMessageService orderMessageService;
   @Autowired
   private IAccountService accountService;




   @PostMapping(value = "/queryOrderCount")
   @ApiOperation(value = "用户-->0全部订单数量 1待付款订单数量 2收货中订单数量 3待评价订单数量", tags = {"订单购物车接口"}, notes = "0全部订单数量 1待付款订单数量 2收货中订单数量 3待评价订单数量")
   public RestResponseBean queryOrder() {
       User user = (User) LoginUser.getCurrentUser();
       if(user==null) {
           return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
       }


       //获取所需状态的订单
       //orderPageList = orderService.page(page,queryWrapper);
       Map<String,Object> map = new HashMap<>();
       //放入map中
       //map.put("orderList",orderPageList);
       //获取各种状态的订单数量

       //代付款订单的数量
       QueryWrapper<Order> queryWrapper1 = new QueryWrapper<>();
       queryWrapper1.eq("user_id",user.getId()).eq("status","1");
       List<Order> list1 = orderService.list(queryWrapper1);
       map.put("1",list1.size());

       //收货中订单的数量
       QueryWrapper<Order> queryWrapper2 = new QueryWrapper<>();
       queryWrapper2.eq("user_id",user.getId()).eq("status","2");
       List<Order> list2 = orderService.list(queryWrapper2);
       map.put("2",list2.size());

       //待评价订单的数量
       QueryWrapper<Order> queryWrapper3 = new QueryWrapper<>();
       queryWrapper3.eq("user_id",user.getId()).eq("status","3").eq("user_del_flag","1");
       List<Order> list3 = orderService.list(queryWrapper3);
       for (Order order : list3) {

       }
       map.put("3",list3.size());

       //总订单的数量
       QueryWrapper<Order> queryWrapper0 = new QueryWrapper<>();
       queryWrapper0.eq("user_id",user.getId()).eq("status","4").eq("user_del_flag","1");
       List<Order> list0 = orderService.list(queryWrapper0);
       map.put("0",list0.size()+list1.size()+list2.size()+list3.size());

       return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),map);


   }

    /**
     * showdoc
     * @catalog 订单购物车接口
     * @title 用户订单列表-->全部订单
     * @description 用户订单列表-->全部订单
     * @method POST
     * @url /nckf-boot/api/v1/order/queryAllOrder
     * @return {"code": 1,"data": 0,"msg": "操作成功","time": "1561013429415"}
     * @return_param code String 响应状态
     * @return_param data String 订单状态
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark status:9:已取消 0:全部（不包括已取消） 1待付款 2收货中 3待评价
     * @number 10
     */
    @PostMapping(value = "/queryAllOrder")
    @ApiOperation(value = "用户订单列表-->全部订单", tags = {"订单购物车接口"}, notes = "用户订单列表-->全部订单")
    public RestResponseBean queryAllOrder(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        IPage<Order> orderPageList = null;
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getUserId,user.getId()).in(Order::getStatus,"1","2","3","4").orderByDesc(Order::getCreateTime);
        Page<Order> page = new Page<Order>(pageNo, pageSize);

        IPage<Order> pageList = orderService.page(page, queryWrapper);


        //过滤掉用户已收货订单的已删除的
        List<Order> list = pageList.getRecords();
        for (Order order : list) {
            if("3".equals(order.getStatus()) && "0".equals(order.getUserDelFlag())){
                list.remove(order);
            }

            if("4".equals(order.getStatus()) && "0".equals(order.getUserDelFlag())){
                list.remove(order);
            }

        }

        pageList.setRecords(list);

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),pageList);
    }



    /**
     * showdoc
     * @catalog 订单购物车接口
     * @title 用户订单列表-->待付款订单
     * @description 用户订单列表-->待付款订单
     * @method POST
     * @url /nckf-boot/api/v1/order/queryOrderDfk
     * @return {"code": 1,"data": 0,"msg": "操作成功","time": "1561013429415"}
     * @return_param code String 响应状态
     * @return_param data String 订单状态
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark status:9:已取消 0:全部（不包括已取消） 1待付款 2收货中 3待评价
     * @number 10
     */
    @PostMapping(value = "/queryOrderDfk")
    @ApiOperation(value = "用户订单列表-->待付款订单", tags = {"订单购物车接口"}, notes = "用户订单列表-->待付款订单")
    public RestResponseBean queryOrderDfk(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getUserId,user.getId()).eq(Order::getStatus,"1").orderByDesc(Order::getCreateTime);
        Page<Order> page = new Page<Order>(pageNo, pageSize);
        IPage<Order> pageList = orderService.page(page, queryWrapper);
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),pageList);
    }




    /**
     * showdoc
     * @catalog 订单购物车接口
     * @title 用户订单列表-->收货中订单
     * @description 用户订单列表-->收货中订单
     * @method POST
     * @url /nckf-boot/api/v1/order/queryOrderShz
     * @return {"code": 1,"data": 0,"msg": "操作成功","time": "1561013429415"}
     * @return_param code String 响应状态
     * @return_param data String 订单状态
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark status:9:已取消 0:全部（不包括已取消） 1待付款 2收货中 3待评价
     * @number 10
     */
    @PostMapping(value = "/queryOrderShz")
    @ApiOperation(value = "用户订单列表-->收货中订单", tags = {"订单购物车接口"}, notes = "用户订单列表-->收货中订单")
    public RestResponseBean queryOrderShz(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getUserId,user.getId()).eq(Order::getStatus,"2").orderByDesc(Order::getCreateTime);
        Page<Order> page = new Page<Order>(pageNo, pageSize);

        //queryWrapper.lambda().eq(Order::getUserId,user.getId()).in(Order::getStatus,);
        IPage<Order> pageList = orderService.page(page, queryWrapper);
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),pageList);
    }




    /**
     * showdoc
     * @catalog 订单购物车接口
     * @title 用户订单列表-->收货中订单
     * @description 用户订单列表-->收货中订单
     * @method POST
     * @url /nckf-boot/api/v1/order/queryOrderShz
     * @return {"code": 1,"data": 0,"msg": "操作成功","time": "1561013429415"}
     * @return_param code String 响应状态
     * @return_param data String 订单状态
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark status:9:已取消 0:全部（不包括已取消） 1待付款 2收货中 3待评价
     * @number 10
     */
    @PostMapping(value = "/queryOrderDpj")
    @ApiOperation(value = "用户订单列表-->待评价订单", tags = {"订单购物车接口"}, notes = "用户订单列表-->待评价订单")
    public RestResponseBean queryOrderDpj(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getUserId,user.getId()).eq(Order::getStatus,"3").eq(Order::getUserDelFlag,"1").orderByDesc(Order::getCreateTime);
        Page<Order> page = new Page<Order>(pageNo, pageSize);

        //queryWrapper.lambda().eq(Order::getUserId,user.getId()).in(Order::getStatus,);
        IPage<Order> pageList = orderService.page(page, queryWrapper);
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),pageList);
    }










    @PostMapping(value = "/rider/orderUserOk")
    @ApiOperation(value = "用户确认收货接口", tags = {"订单购物车接口"}, notes = "用户确认收货接口")
    public RestResponseBean orderUserOk(@RequestParam(name = "orderId",required = true) String orderId){
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        QueryWrapper<RiderAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status","3");
        Order order = new Order();
        order.setId(orderId);
        order.setStatus("3");
        order.setOverTime(new Date());
        boolean b = orderService.updateById(order);
        if(b){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
        }
        return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }





    /**
     * showdoc
     * @catalog 订单购物车接口
     * @title 清空购物车
     * @description 清空购物车
     * @method GET
     * @url /nckf-boot/api/v1/order/queryByOrderId
     * @param orderId 必填 String 订单id
     * @return {"code": 1,"data": {"createBy": "","createMinMoney": null,"createTime": 1558314475000,"deliveryMoney": null,"getTime": null,"goodsCount": 1,"goodsMoney": null,"id": "2b39208d693a2fab88ed328ef454acef","invoiceFlag": "","invoiceId": "","invoiceOpen": "","orderId": "e79f9897b098d5d199d9be47c83007201558314474898","orderMoney": 11,"orderRemark": "","orderSrc": "","orderType": "1","overTime": null,"riderId": "","riderPhone": "", "ridername": "","status": "9","storeId": "3ca3980536388ccd81a6b15eab1f703a","storename": "鸟巢咖啡谈磊店","tradeNo": "","updateBy": "","updateTime": 1558316280000,"usedPhone": "","userAddress": "","userAddressId": null,"userCouponsId": "","userId": "e79f9897b098d5d199d9be47c8300720","username": "我是骑手"},"msg": "操作成功","time": "1561013303394" }
     * @return_param code String 响应状态
     * @return_param createBy String 创建者
     * @return_param createMinMoney String
     * @return_param createTime Date 订单创建时间
     * @return_param deliveryMoney Double 配送费
     * @return_param getTime Date 骑手取餐时间
     * @return_param goodsCount Integer 商品数量
     * @return_param goodsMoney Double 商品总金额
     * @return_param id String 订单id
     * @return_param invoiceFlag String 是否需要发票(0:不开票 1:已开票)
     * @return_param invoiceId String 发票id
     * @return_param invoiceOpen String 发票是否已开
     * @return_param orderId String 订单编号
     * @return_param orderMoney Double 订单总金额
     * @return_param orderRemark String 订单备注
     * @return_param orderSrc String 订单来源（0:微信1:安卓app 2:苹果app）
     * @return_param orderType String 订单类型(0:送餐  1：店内用餐)
     * @return_param overTime Date 骑手送达时间
     * @return_param riderId String 骑手id
     * @return_param riderPhone String 骑手电话
     * @return_param ridername String 用户昵称
     * @return_param status String 订单状态（9:已取消 0:全部 1待付款 2收货中 3待评价 4已评价 ）
     * @return_param storeId String 门店id
     * @return_param storename String 门店名称
     * @return_param tradeNo String 第三方流水号
     * @return_param updateBy String 更新人
     * @return_param updateTime Date 更新时间
     * @return_param usedPhone String 用户电话
     * @return_param userAddress String 送餐地址
     * @return_param userAddressId String 用户地址id
     * @return_param userCouponsId String 用户优惠券id
     * @return_param userId String 用户id
     * @return_param username String 用户名
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 8
     */
   //@GetMapping(value = "/queryByOrderId")
   //@ApiOperation(value = "通用-->根据订单id查询订单", tags = {"订单购物车接口"}, notes = "通用-->根据订单id查询订单")
   //@ApiImplicitParam(name = "orderId", value = "订单的id",required = true )
   public RestResponseBean queryByOrderId(@RequestParam(name="orderId",required=true) String orderId) {
       User user = (User) LoginUser.getCurrentUser();
       if(user==null) {
           return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
       }
       Order order = orderService.queryByOrderId(orderId);
       return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),order);
   }


    public String getUUID(){
        //随机生成一位整数
        int random = (int) (Math.random()*9+1);
        String valueOf = String.valueOf(random);
        //生成uuid的hashCode值
        int hashCode = UUID.randomUUID().toString().hashCode();
        //可能为负数
        if(hashCode<0){
            hashCode = -hashCode;
        }
        String value = valueOf + String.format("%015d", hashCode);
        return value;
    }

    /**
     * showdoc
     * @catalog 订单购物车接口
     * @title 用户新增订单接口
     * @description 用户新增订单接口
     * @method GET
     * @url /nckf-boot/api/v1/order/queryOrderGoods
     * @return {"code": 1,"data": [],"msg": "操作成功","time": "1561013500114"}
     * @return_param code String 响应状态
     * @return_param data List 订单信息
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 11
     */
   @PostMapping(value = "/addOrder")
   @ApiOperation(value = "用户新增订单接口", tags = {"订单购物车接口"}, notes = "用户新增订单接口")
   @ApiImplicitParams({@ApiImplicitParam(name = "cartIds", value = "购物车id数组"),
                        @ApiImplicitParam(name = "userAddressId", value = "收货地址id"),
           @ApiImplicitParam(name = "orderSrc", value = "订单来源(0:微信1:安卓app 2:苹果app)"),
           @ApiImplicitParam(name = "couponseId", value = "优惠券id"),
           @ApiImplicitParam(name = "orderRemark", value = "订单备注"),
           @ApiImplicitParam(name = "orderType", value = "订单类型(1:店内用餐 0：送餐)"),
           @ApiImplicitParam(name = "deliveryMoney", value = "配送费"),
           @ApiImplicitParam(name = "appOrderMoney", value = "合计金额"),
           @ApiImplicitParam(name = "accountFlag", value = "是否使用钱包支付 1使用  0不使用"),
           @ApiImplicitParam(name = "thirdPay", value = "1:微信 2:支付宝")
   })
   public RestResponseBean addOrder(@RequestParam(name = "cartIds") String[] cartIds,
                                     String userAddressId,
                                     String couponseId,
                                    @RequestParam(name = "orderSrc") String orderSrc,
                                    @RequestParam(name = "orderRemark") String orderRemark,
                                    @RequestParam(name = "orderType") String orderType,
                                    String deliveryMoney,
                                    @RequestParam(name = "appOrderMoney") String appOrderMoney,
                                    @RequestParam(name = "accountFlag") String accountFlag,
                                     String thirdPay){
       //判断用户是否登陆
       User user = (User) LoginUser.getCurrentUser();
       if(user==null) {
           return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
       }




       List<Cart> cartList = new ArrayList<>();
       for (int i = 0; i <cartIds.length ; i++) {
           //查询选中购物车的商品详情
           //log.info(cartIds[i]);
           Cart cart = cartService.getById(cartIds[i]);
           cartList.add(cart);
       }

       //验证购物车id是否有
//        if(cartList.size()==0){
//            return new RestResponseBean(ResultEnum.CART_NULL.getValue(),ResultEnum.CART_NULL.getDesc(),null);
//        }
       double money = 0;
       int count = 0;
       List<OrderGoods> list =new ArrayList<>();


       //遍历给orderGoods值
       for (Cart cart : cartList) {
           OrderGoods orderGoods = new OrderGoods();
           orderGoods.setCreateBy(user.getRealname());
           orderGoods.setGoodsCount(cart.getGoodsCount());
           orderGoods.setSelectedPrice(cart.getSelectedPrice());
           orderGoods.setGoodsSpecValues(cart.getGoodsSpecValues());
           orderGoods.setGoodsId(cart.getGoodsId());
           orderGoods.setTotalPrice(cart.getGoodsCount()*cart.getSelectedPrice());
           orderGoods.setUserId(user.getId());
           orderGoods.setStoreId(cart.getStoreId());
           orderGoods.setGoodsCount(cart.getGoodsCount());
           orderGoods.setCreateTime(new Date());
           orderGoods.setGoodsName(cart.getGoodsName());
           list.add(orderGoods);
           money += cart.getSelectedPrice()*cart.getGoodsCount();
           count += cart.getGoodsCount();
       }



       //如果此订单使用优惠券，得到数据库查询到的订单金额减去优惠金额
       if(StringUtils.isNotBlank(couponseId)){
          Coupons coupons = couponsService.getById(couponseId);
          money -= coupons.getSaveMoney();
          QueryWrapper<UserCoupons> queryWrapper = new QueryWrapper<>();
          queryWrapper.eq("coupons_id",couponseId);
          UserCoupons userCoupons = userCouponsService.getOne(queryWrapper);
          userCoupons.setStatus("1");
          userCouponsService.updateById(userCoupons);
       }
        //log.info(deliveryMoney+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

       //得到数据库订单金额减去加上配送费
       if(StringUtils.isNotBlank(deliveryMoney)){
           money += Double.parseDouble(deliveryMoney);
       }

       //比较db金额与app传过来的金额
       if(money  != Double.parseDouble(appOrderMoney)){
           return new RestResponseBean(ResultEnum.ORDER_MONEY_FAIL.getValue(),ResultEnum.ORDER_MONEY_FAIL.getDesc(),null);
       }

       //给order值
       Order order = new Order();
       if(orderType.equals("0")){
           order.setDeliveryMoney(Double.parseDouble(deliveryMoney));
       }
        order.setAccountFlag(accountFlag);
       order.setThirdPay(thirdPay);
       order.setUsername(user.getRealname());
       order.setStatus("1");
       order.setUserId(user.getId());
       order.setInvoiceFlag("0");
       order.setOrderMoney(money);
       order.setGoodsCount(count);
       order.setOrderId(getUUID());
       order.setCreateBy(user.getRealname());
       //为了给订单列表展示订单中的“某一件商品”名称字段给上值
       order.setOneGoodsName(list.get(0).getGoodsName());
       if(userAddressId!=null && userAddressId!=""){
           //根据addressId拿到收货地址的lat，lng
           Address address = addressService.getById(userAddressId);
           if(address!=null){
               order.setUserLat(address.getLat());
               order.setUserLng(address.getLng());
           }

       }



       if(cartIds != null){
           Cart cart = cartService.getById(cartIds[0]);
           order.setStoreId(cart.getStoreId());
           order.setStorename(storeService.getById(cart.getStoreId()).getStoreName());
       }

       if(userAddressId!=null && userAddressId != ""){
           Address address = addressService.getById(userAddressId);
           if(address!=null){
               order.setUserAddress(address.getDetailedAddress());
           }
       }

       order.setUserCouponsId(couponseId);
       order.setOrderSrc(orderSrc);
       order.setOrderRemark(orderRemark);
       order.setOrderType(orderType);
       order.setUserPhone(user.getMobile());


       long currentTime = System.currentTimeMillis() + 30 * 60 * 1000;
       Date date = new Date(currentTime);


       order.setYujiTime(date);


       //保存order
       orderService.saveMain(order,list);

       //订单临时表添加数据
       OrderNoPay orderNoPay = new OrderNoPay();
       BeanUtils.copyProperties(order,orderNoPay);
       orderNoPayService.insert(orderNoPay);

       //清除生成订单的购物车记录
       for (String cartId : cartIds) {
           cartService.removeById(cartId);
       }

//       String info = "";
//       //如果不用钱包支付
//       if(accountFlag.equals("0")){
//           //调用三方支付
//           switch(thirdPay){
//               case "1":
//                   //调用微信
//                   info = xxPayService.getWxParOederStr(order.getOrderId(),order.getOrderMoney(),order.getUsername()+order.getOneGoodsName(),"");
//               case "2":
//                   //调用支付宝
//                   info = xxPayService.getWxParOederStr(order.getOrderId(),order.getOrderMoney(),order.getUsername()+order.getOneGoodsName(),"");
//
//           }
//       }else{
//           //如果使用使用钱包支付并且钱包余额大于订单余额，使用钱包付款并且更新余额
//           if(user.getUserMoney()>=Double.parseDouble(appOrderMoney)){
//               user.setUserMoney(user.getUserMoney()-Double.parseDouble(appOrderMoney));
//               userService.updateById(user);
//               order.setStatus("2");
//               orderService.updateById(order);
//               return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),order);
//           }else{
//               user.setUserMoney(user.getUserMoney()-Double.parseDouble(appOrderMoney));
//               userService.updateById(user);
//               //三方需支付金额
//               double trueMoney = Double.parseDouble(appOrderMoney)-user.getUserMoney();
//               //调用三方支付
//               switch(thirdPay){
//                   case "1":
//                       //调用微信
//                       info = xxPayService.getWxParOederStr(order.getOrderId(),order.getOrderMoney(),order.getUsername()+order.getOneGoodsName(),"");
//                   case "2":
//                       //调用支付宝
//                       info = xxPayService.getWxParOederStr(order.getOrderId(),order.getOrderMoney(),order.getUsername()+order.getOneGoodsName(),"");
//               }
//           }
//
//           orderMessageService.addOrderMsg(order.getId());
//       }

       return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),order);

   }





   /**
     *  编辑
    * @param
    * @return
    */

   @ApiOperation(value = "用户取消订单接口", tags = {"订单购物车接口"}, notes = "用户取消订单接口")
   @PostMapping(value = "/cancelOrder")
   public RestResponseBean cancelOrder(@RequestParam(name="orderId",required=true) String orderId) {
       User user = (User) LoginUser.getCurrentUser();
       if(user==null) {
           return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
       }
       boolean flag = orderService.cancel(orderId);
       if(flag){
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
       }
       return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
   }



   /**
     * 通过id查询
    * @param id
    * @return
    */
    @GetMapping(value = "/queryOrderGoods")
    @ApiOperation(value = "通用-->查询单个订单（包括商品详情）接口", tags = {"订单购物车接口"}, notes = "通用-->查询订单个（包括商品详情）接口")
    public RestResponseBean queryOrderGoods(@RequestParam(name="id",required=true)String id) {
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        Order order = orderService.getById(id);
        if(order == null){
            return  new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
        }

        //如果是普通用户查看详情
        if(user.getUserType().equals("0")){
            if("0".equals(order.getUserDelFlag())){
                return  new RestResponseBean(ResultEnum.ORDER_ALREADY_DELETE.getValue(),ResultEnum.ORDER_ALREADY_DELETE.getDesc(),null);
            }

            RiderOrder riderOrder = new RiderOrder();
            BeanUtils.copyProperties(order,riderOrder);


            String userCouponseId = order.getUserCouponsId();
            if(userCouponseId != null && userCouponseId !=""){
                UserCoupons userCoupons =  userCouponsService.getById(userCouponseId);
                if(userCoupons!=null){
                    String couponsId = userCoupons.getCouponsId();
                    Coupons coupons =couponsService.getById(couponsId);
                    riderOrder.setCouponsMoney(coupons.getSaveMoney());
                }
            }

            String riderId = order.getRiderId();
            if(StringUtils.isNotBlank(riderId)){
                User rider =userService.getById(riderId);
                riderOrder.setRiderImage(rider.getAvatar());
                riderOrder.setRiderRating(rider.getMark());
            }

            List<OrderGoods> orderGoodsList = orderGoodsService.selectByMainId(id);
            riderOrder.setOrderGoodsList(orderGoodsList);

            Store store = storeService.getById(order.getStoreId());

            String type = order.getOrderType();
            if( "0".equals(type)){

                //如果当前订单状态为骑手接单状态，给qi手经纬度赋值
                if(order.getRiderOk().equals("1") || order.getRiderOk().equals("2") || order.getRiderOk().equals("3")){
                    QueryWrapper<RiderAddress> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("rider_id",order.getRiderId());
                    ;
                }
                //得到骑手位置对象，拿到经纬度
                QueryWrapper<RiderAddress> wrapper1 = new QueryWrapper<>();
                wrapper1.eq("rider_id",user.getId());
                RiderAddress riderAddress = riderAddressService.getOne(wrapper1);

                if(riderAddress != null){
                    //得到用户下单地址的经纬度和门店的经纬度
                    double lat = order.getUserLat();
                    double lng = order.getUserLng();


                    String disRS = DistanceUtil.algorithm(lat,lng,riderAddress.getLat(),riderAddress.getLng());
                    String disRU = DistanceUtil.algorithm(store.getLat(),store.getLng(),riderAddress.getLat(),riderAddress.getLng());
                    riderOrder.setRiderAndStoreDis(Double.parseDouble(disRS));
                    riderOrder.setRiderAndUserDis(Double.parseDouble(disRU));
                }


            }

            riderOrder.setStorePhone(store.getPhone());

            riderOrder.setStoreLat(store.getLat());
            riderOrder.setStoreLng(store.getLng());

            riderOrder.setStorename(store.getStoreName());
            riderOrder.setStoreAddress(store.getAddressDesc());


            String userId = order.getUserId();
            User reciveUser = userService.getById(userId);
            riderOrder.setUserAddress(order.getUserAddress());
            riderOrder.setUserPhone(reciveUser.getMobile());

            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),riderOrder);
        }
        //如果为骑手查看订单详情
        else{
            if("0".equals(order.getRiderDelFlag())){
                return  new RestResponseBean(ResultEnum.ORDER_ALREADY_NULL.getValue(),ResultEnum.ORDER_ALREADY_NULL.getDesc(),null);
            }

            RiderOrder riderOrder = new RiderOrder();
            BeanUtils.copyProperties(order,riderOrder);


            String userCouponseId = order.getUserCouponsId();
            if(userCouponseId != null && userCouponseId !=""){
                UserCoupons userCoupons =  userCouponsService.getById(userCouponseId);
                if(userCoupons!=null){
                    String couponsId = userCoupons.getCouponsId();
                    Coupons coupons =couponsService.getById(couponsId);
                    riderOrder.setCouponsMoney(coupons.getSaveMoney());
                }
            }



            List<OrderGoods> orderGoodsList = orderGoodsService.selectByMainId(id);
            riderOrder.setOrderGoodsList(orderGoodsList);

            Store store = storeService.getById(order.getStoreId());

            String type = order.getOrderType();
            if("0".equals(type)){
                //得到骑手位置对象，拿到经纬度
                QueryWrapper<RiderAddress> wrapper1 = new QueryWrapper<>();
                wrapper1.eq("rider_id",user.getId());
                RiderAddress riderAddress = riderAddressService.getOne(wrapper1);


                //得到用户下单地址的经纬度和门店的经纬度
                double lat = order.getUserLat();
                double lng = order.getUserLng();


                String disRS = DistanceUtil.algorithm(lat,lng,riderAddress.getLat(),riderAddress.getLng());
                String disRU = DistanceUtil.algorithm(store.getLat(),store.getLng(),riderAddress.getLat(),riderAddress.getLng());
                riderOrder.setRiderAndStoreDis(Double.parseDouble(disRS));
                riderOrder.setRiderAndUserDis(Double.parseDouble(disRU));
            }


            riderOrder.setStoreLat(store.getLat());
            riderOrder.setStoreLng(store.getLng());

            riderOrder.setStorename(store.getStoreName());
            riderOrder.setStoreAddress(store.getAddressDesc());


            String userId = order.getUserId();
            User reciveUser = userService.getById(userId);
            riderOrder.setUserAddress(order.getUserAddress());
            riderOrder.setUserPhone(reciveUser.getMobile());
            riderOrder.setStoreImage(store.getImgUrl());

            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),riderOrder);
        }


    }


    /**
     * 通过id查询
     * @return
     */
    @GetMapping(value = "/queryNewOrder")
    @ApiOperation(value = "首页查询未完成订单接口", tags = {"首页"}, notes = "首页查询未完成订单接口")
    public RestResponseBean queryNewOrder() {
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId()).eq("status","2");

        List<Order> orderList = orderService.list(queryWrapper);
        if(orderList!=null){
            List<OrderGoods> orderGoodsList = new ArrayList<>();
            for (Order order : orderList) {
                orderGoodsList.add(orderGoodsService.getById(order.getId()));
            }
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),orderGoodsList);
        }
        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }





    @GetMapping(value = "/distance")
    @ApiOperation(value = "用户最近订单的骑手距离", tags = {"订单购物车接口"}, notes = "用户最近订单的骑手距离")
    public RestResponseBean queryDistance(@RequestParam(name = "userLat",required = true) String  userLat,@RequestParam(name = "userLng",required = true) String userLng){
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getUserId,user.getId()).eq(Order::getStatus,"2");
        List<Order> list = orderService.list(queryWrapper);
        if(list == null){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
        }

        Order newOrder = new Order();

        if(list.size()==1){
            newOrder = list.get(0);
            //计算距离
            QueryWrapper<RiderAddress> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(RiderAddress::getRiderId,newOrder.getRiderId());
            RiderAddress riderAddress = riderAddressService.getOne(queryWrapper1);

            OrderDistanceVo orderDistanceVo = new OrderDistanceVo();
            orderDistanceVo.setLat(riderAddress.getLat());
            orderDistanceVo.setLng(riderAddress.getLng());
            orderDistanceVo.setOrderId(newOrder.getId());


            String meter = DistanceUtil.algorithm(riderAddress.getLat(),riderAddress.getLng(),Double.parseDouble(userLat),Double.parseDouble(userLng));

            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),meter);

        }else{
            Order lastOrder = list.get(0);
            for (int i = 0; i <list.size()-1 ; i++) {
                if(list.get(i+1).getCreateTime().after(lastOrder.getCreateTime())){
                    lastOrder = list.get(i+1);
                }

            }
            //newOrder = selectLastOne(list);
            //计算距离
            QueryWrapper<RiderAddress> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(RiderAddress::getRiderId,lastOrder.getRiderId());
            RiderAddress riderAddress = riderAddressService.getOne(queryWrapper1);

            OrderDistanceVo orderDistanceVo = new OrderDistanceVo();
            orderDistanceVo.setLat(riderAddress.getLat());
            orderDistanceVo.setLng(riderAddress.getLng());
            orderDistanceVo.setOrderId(lastOrder.getId());

            String meter = DistanceUtil.algorithm(riderAddress.getLat(),riderAddress.getLng(),Double.parseDouble(userLat),Double.parseDouble(userLng));
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),meter);
        }
    }

    @GetMapping(value = "/background_list")
    public Result<IPage<Order>> queryPageList(Order order,
                                              @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                              @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                              HttpServletRequest req) {
        Result<IPage<Order>> result = new Result<IPage<Order>>();
        QueryWrapper<Order> queryWrapper = QueryGenerator.initQueryWrapper(order, req.getParameterMap());
        Page<Order> page = new Page<Order>(pageNo, pageSize);
        IPage<Order> pageList = orderService.page(page, queryWrapper);

        /*List<Order> records = pageList.getRecords();
        for (Order record : records) {
            String userId = record.getUserId();
            String riderId = record.getRiderId();
            String storeId = record.getStoreId();

            User user = userService.getById(userId);
            if(user!=null){
                record.setUsername(user.getUsername());
            }

            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("id",riderId);
            User rider = userService.getOne(wrapper);
            if(rider != null){
                record.setRidername(rider.getUsername());
            }

            Store store = storeService.getById(storeId);
            if(store != null){
                record.setStorename(store.getStoreName());
            }
        }
        pageList.setRecords(records);
*/
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }


    public Order selectLastOne(List<Order> list) {
        Order order = new Order();

        Long dates[] = new Long[list.size()];
        for (int i = 1; i <= list.size(); i++) {
            // 把date类型的时间对象转换为long类型，时间越往后，long的值就越大，
            // 所以就依靠这个原理来判断距离现在最近的时间
            dates[i - 1] = list.get(i).getCreateTime().getTime();
        }

        Long maxIndex = dates[0];// 定义最大值为该数组的第一个数
        for (int j = 0; j < dates.length; j++) {
            if (maxIndex < dates[j]) {
                maxIndex = dates[j];
                // 找到了这个j
                order = list.get(j + 1);
            }
        }
        return order;
    }




    /**
     * showdoc
     * @catalog 订单购物车接口
     * @title 骑手接单接口
     * @description 骑手接单接口
     * @method POST
     * @url /nckf-boot/api/v1/order/rider/queryRiderOrder
     * @param orderId 选填 String 订单id
     * @return {"code": 1,"data": [],"msg": "操作成功","time": "1561013848081"}
     * @return_param code String 响应状态
     * @return_param data List 订单信息
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 15
     */
    @PostMapping(value = "/rider/riderOrder")
    @ApiOperation(value = "骑手接单接口", tags = {"订单购物车接口"}, notes = "骑手接单接口")
    public RestResponseBean riderOrder(@RequestParam(name = "orderId",required = true) String orderId){
        User user = (User) LoginUser.getCurrentUser();
        if(user == null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        boolean flag = orderService.riderOrder(user.getId(),orderId);
        if(flag){

            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
        }
        return  new RestResponseBean(ResultEnum.ORDER_ALREADY_NULL.getValue(),ResultEnum.ORDER_ALREADY_NULL.getDesc(),null);
    }


    @PostMapping(value = "/rider/riderGetOrder")
    @ApiOperation(value = "骑手取货接口", tags = {"订单购物车接口"}, notes = "骑手取货接口")
    public RestResponseBean riderGetOrder(@RequestParam(name = "orderId",required = true) String orderId){
        User user = (User) LoginUser.getCurrentUser();
        if(user == null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        boolean flag = orderService.riderGetOrder(user.getId(),orderId);
        if(flag){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
        }
        return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }


    @PostMapping(value = "/rider/orderRiderOk")
    @ApiOperation(value = "骑手送达接口", tags = {"订单购物车接口"}, notes = "骑手送达接口")
    public RestResponseBean orderRiderOk(@RequestParam(name = "orderId",required = true) String orderId){
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }


        Order order = orderService.getById(orderId);
        if(order != null){
            order.setOverTime(new Date());
            order.setRiderOk("3");
            Account account = accountService.queryByUserId(user.getId());
            account.setMoney(account.getMoney()+order.getDeliveryMoney());
            //user.setUserMoney(user.getUserMoney() + order.getDeliveryMoney());
            userService.updateById(user);
            boolean b = orderService.updateById(order);
            if(b){
                return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
            }
            return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
        }
        return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);




    }


    @PostMapping(value = "/rider/queryRiderOrder")
    @ApiOperation(value = "骑手查询新任务订单接口", tags = {"订单购物车接口"}, notes = "骑手查询新任务订单接口")
    public RestResponseBean queryRiderOrder(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        List<RiderOrder> list = orderService.queryRiderOrder(pageNo,pageSize);
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),list);
    }





    @PostMapping(value = "/rider/queryOrderDqh")
    @ApiOperation(value = "骑手查询待取货订单接口", tags = {"订单购物车接口"}, notes = "骑手查询待取货订单接口")
    public RestResponseBean queryOrderDqh(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        List<RiderOrder> list = orderService.queryOrderDqh(pageNo,pageSize);



        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),list);
    }




    @PostMapping(value = "/rider/queryOrderDsd")
    @ApiOperation(value = "骑手查询待送达订单接口", tags = {"订单购物车接口"}, notes = "骑手查询待送达订单接口")
    public RestResponseBean queryOrderDsd(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        List<RiderOrder> list = orderService.queryOrderDsd(pageNo,pageSize);

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),list);
    }


    @PostMapping(value = "/rider/queryOrderYwc")
    @ApiOperation(value = "骑手查询已完成订单接口", tags = {"订单购物车接口"}, notes = "骑手查询已完成订单接口")
    public RestResponseBean queryOrderYwc(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                         @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        List<RiderOrder> list = orderService.queryOrderYwc(pageNo,pageSize);

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),list);
    }





    @PostMapping(value = "/rider/queryOrderCount")
    @ApiOperation(value = "骑手-->0新任务数量 1待取货订单数量 2待送达订单数量 3已完成订单数量", tags = {"订单购物车接口"}, notes = "骑手-->0新任务数量 1待取货订单数量 2待送达订单数量 3已完成订单数量")
    public RestResponseBean queryOrderCount() {
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        Map<String,Object> map = orderService.queryOrderCount();
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),map);

    }




    @PostMapping(value = "/rider/queryOrderToday")
    @ApiOperation(value = "骑手查询今天订单接口", tags = {"订单购物车接口"}, notes = "骑手查询今天订单接口")
    public RestResponseBean queryOrderToday(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize)throws Exception{
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        List<RiderOrder> list = orderService.queryOrderToday(pageNo,pageSize);
        if(list.size() == 0 || list == null){

            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);

        }
        //PageUtil.page(list,pageNo,pageSize);
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),list);
    }


    @PostMapping(value = "/rider/queryOrderYest")
    @ApiOperation(value = "骑手查询昨天订单接口", tags = {"订单购物车接口"}, notes = "骑手查询昨天订单接口")
    public RestResponseBean queryOrderYest(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                           @RequestParam(name="pageSize", defaultValue="10") Integer pageSize)throws Exception{
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        List<RiderOrder> list = orderService.queryOrderYest(pageNo,pageSize);
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),list);
    }


    @PostMapping(value = "/rider/queryOrderQiantian")
    @ApiOperation(value = "骑手查询前天订单接口", tags = {"订单购物车接口"}, notes = "骑手查询前天订单接口")
    public RestResponseBean queryOrderQiantian(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize)throws Exception{
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        List<RiderOrder> list = orderService.queryOrderQiantian(pageNo,pageSize);

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),list);
    }



    @PostMapping(value = "/rider/queryOrderByDate")
    @ApiOperation(value = "骑手按日期查询订单接口", tags = {"订单购物车接口"}, notes = "骑手按日期查询订单接口")
    public RestResponseBean queryOrderByDate(@RequestParam(name = "beginTime") String beginTime,
                                             @RequestParam(name = "endTime") String endTime,
                                             @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                             @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) throws Exception{
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        List<RiderOrder> list = orderService.queryOrderByDate(beginTime,endTime,pageNo,pageSize);

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),list);
    }



    @PostMapping(value = "/deleteOrderByUserId")
    @ApiOperation(value = "用户-->删除订单", tags = {"订单购物车接口"}, notes = "用户-->删除订单")
    public RestResponseBean deleteOrderByUserId(@RequestParam(name = "orderId",required = true) String orderId){
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        Order order = orderService.getById(orderId);

        if(!("4").equals(order.getStatus()) && !("3").equals(order.getStatus())){
            return new RestResponseBean(ResultEnum.ORDER_NOT_OK.getValue(),ResultEnum.ORDER_NOT_OK.getDesc(),null);
        }

        order.setUserDelFlag("0");
        boolean flag = orderService.updateById(order);

        if(flag){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);

        }
        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }


    @PostMapping(value = "/deleteOrderByRiderId")
    @ApiOperation(value = "骑手-->删除订单", tags = {"订单购物车接口"}, notes = "骑手-->删除订单")
    public RestResponseBean deleteOrderByRiderId(@RequestParam(name = "orderId",required = true) String orderId){
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        Order order = orderService.getById(orderId);


        if(!("3").equals(order.getRiderOk())){
            return new RestResponseBean(ResultEnum.ORDER_NOT_OK.getValue(),ResultEnum.ORDER_NOT_OK.getDesc(),null);
        }

        order.setRiderDelFlag("0");
        boolean flag = orderService.updateById(order);

        if(flag){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);

        }
        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }



    @ApiOperation(value = "用户钱包支付接口", tags = {"订单购物车接口"}, notes = "用户钱包支付接口")
    @PostMapping(value = "/payByAccount")
    public RestResponseBean payByAccount(@RequestParam(name="orderId",required=true) String orderId) {
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        Order order = orderService.getById(orderId);

        if(order.getOrderType().equals("0")){
            order.setStatus("3");
        }

        if(order == null){
            return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(),ResultEnum.QUERY_NOT_EXIST.getDesc(),null);
        }

        Account account = accountService.getByUid(user.getId());

        if(account.getMoney()<order.getOrderMoney()){
            return new RestResponseBean(ResultEnum.MONEY_NOT_ENOUGH.getValue(),ResultEnum.MONEY_NOT_ENOUGH.getDesc(),null);
        }

        account.setMoney(account.getMoney()-order.getOrderMoney());
        //user.setUserMoney(user.getUserMoney()-order.getOrderMoney());
        userService.updateById(user);
        order.setStatus("2");
        orderService.updateById(order);

        //把订单临时表中的数据删除
        orderNoPayService.removeById(orderId);

        //生成订单消息
        orderMessageService.addOrderMsg(orderId);
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
    }

}
