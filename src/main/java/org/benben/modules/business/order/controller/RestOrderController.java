package org.benben.modules.business.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.DistanceUtil;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.entity.OrderGoods;
import org.benben.modules.business.order.service.IOrderGoodsService;
import org.benben.modules.business.order.service.IOrderNoPayService;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.order.vo.OrderDistanceVo;
import org.benben.modules.business.order.vo.OrderPage;
import org.benben.modules.business.rideraddress.entity.RiderAddress;
import org.benben.modules.business.rideraddress.service.IRiderAddressService;
import org.benben.modules.business.store.entity.Store;
import org.benben.modules.business.store.service.IStoreService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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





   @PostMapping(value = "/queryOrder")
   @ApiOperation(value = "订单多（单）条件查询接口 status:9:已取消 0:全部（不包括已取消） 1待付款 2收货中 3待评价", tags = {"订单购物车接口"}, notes = "订单多（单）条件查询接口 status:9:已取消 0:全部（不包括已取消） 1待付款 2收货中 3待评价")
   public RestResponseBean queryOrder(@RequestParam(required = true) String status,
                                      @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                      @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
       User user = (User) SecurityUtils.getSubject().getPrincipal();
       if(user==null) {
           return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
       }

       IPage<Order> orderPageList = null;
       QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
       Page<Order> page = new Page<Order>(pageNo, pageSize);
       //查询当前用户所有不包括已取消订单
       if("0".equals(status)){
           queryWrapper.lambda().eq(Order::getUserId,user.getId()).ne(Order::getStatus,"9");
           orderPageList = orderService.page(page,queryWrapper);
       }

       queryWrapper.eq("user_id",user.getId()).eq("status",status);

       //获取所需状态的订单
       orderPageList = orderService.page(page,queryWrapper);
       Map<String,Object> map = new HashMap<>();
       //放入map中
       map.put("orderList",orderPageList);
       //获取各种状态的订单数量

       //代付款订单的数量
       QueryWrapper<Order> queryWrapper1 = new QueryWrapper<>();
       queryWrapper1.lambda().eq(Order::getUserId,user.getId()).eq(Order::getStatus,"1");
       List<Order> list1 = orderService.list(queryWrapper1);
       map.put("1",list1.size());

       //收货中订单的数量
       QueryWrapper<Order> queryWrapper2 = new QueryWrapper<>();
       queryWrapper1.lambda().eq(Order::getUserId,user.getId()).eq(Order::getStatus,"2");
       List<Order> list2 = orderService.list(queryWrapper2);
       map.put("2",list2.size());

       //待评价订单的数量
       QueryWrapper<Order> queryWrapper3 = new QueryWrapper<>();
       queryWrapper1.lambda().eq(Order::getUserId,user.getId()).eq(Order::getStatus,"3");
       List<Order> list3 = orderService.list(queryWrapper3);
       map.put("3",list3.size());

       return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),map);


   }

    /**
     * showdoc
     * @catalog 订单购物车接口
     * @title 订单多（单）条件查询接口
     * @description 订单多（单）条件查询接口
     * @method POST
     * @url /nckf-boot/api/v1/order/queryOrderCount
     * @param status 必填 String 订单状态
     * @return {"code": 1,"data": 0,"msg": "操作成功","time": "1561013429415"}
     * @return_param code String 响应状态
     * @return_param data String 订单状态
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark status:9:已取消 0:全部（不包括已取消） 1待付款 2收货中 3待评价
     * @number 10
     */
    @PostMapping(value = "/queryOrderCount")
    @ApiOperation(value = "订单多（单）条件查询接口 status:9:已取消 0:全部（不包括已取消） 1待付款 2收货中 3待评价", tags = {"订单购物车接口"}, notes = "订单多（单）条件查询接口 status:9:已取消 0:全部（不包括已取消） 1待付款 2收货中 3待评价")
    public RestResponseBean queryOrderCount(@RequestParam(required = true) String status) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getUserId,user.getId()).eq(Order::getStatus,status);
        List<Order> list = orderService.list(queryWrapper);
        if(list != null){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),list.size());
        }
        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);

    }

    @PostMapping(value = "/rider/queryRiderOrder")
    @ApiOperation(value = "骑手查询可接订单接口", tags = {"订单购物车接口"}, notes = "骑手查询可接订单接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "riderId", value = "骑手的id"),
            @ApiImplicitParam(name = "storeId", value = "商店的id")
    })
    public RestResponseBean queryRiderOrder(@RequestParam(name = "riderId",required = true) String riderId,
                                            @RequestParam(name = "storeId",required = true) String storeId){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("store_id",storeId).eq("status","2").eq("rider_id",riderId);
        List<Order> list = orderService.list(wrapper);

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),list);
    }

    /**
     * showdoc
     * @catalog 订单购物车接口
     * @title 骑手接单接口
     * @description 骑手接单接口
     * @method POST
     * @url /nckf-boot/api/v1/order/rider/queryRiderOrder
     * @param riderId 选填 String 骑手id
     * @param storeId 选填 String 商店id
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
    public RestResponseBean riderOrder(@RequestParam(name = "riderId",required = true) String riderId,
                                       @RequestParam(name = "orderId",required = true) String orderId){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
       boolean flag = orderService.riderOrder(riderId,orderId);
        if(flag){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
        }
        return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }

    @PostMapping(value = "/rider/orderUserOk")
    @ApiOperation(value = "用户确认收货接口", tags = {"订单购物车接口"}, notes = "用户确认收货接口")
    public RestResponseBean orderUserOk(@RequestParam(name = "orderId",required = true) String orderId){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
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

    @PostMapping(value = "/rider/orderRiderOk")
    @ApiOperation(value = "骑手送达接口", tags = {"订单购物车接口"}, notes = "骑手送达接口")
    public RestResponseBean orderRiderOk(@RequestParam(name = "orderId",required = true) String orderId){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
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
   @GetMapping(value = "/queryByOrderId")
   @ApiOperation(value = "用户根据订单号查询订单接口", tags = {"订单购物车接口"}, notes = "用户根据订单号查询订单接口")
   @ApiImplicitParam(name = "orderId", value = "订单的id",required = true )
   public RestResponseBean queryByOrderId(@RequestParam(name="orderId",required=true) String orderId) {
       User user = (User) SecurityUtils.getSubject().getPrincipal();
       if(user==null) {
           return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
       }
       Order order = orderService.queryByOrderId(orderId);
       if(order!=null){
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),order);
       }
       return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
   }

    /**
     * showdoc
     * @catalog 订单购物车接口
     * @title 用户新增订单接口
     * @description 用户新增订单接口
     * @method GET
     * @url /nckf-boot/api/v1/order/queryOrderGoods
     * @param id 必填 String 订单id
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
   public RestResponseBean addOrder(OrderPage orderPage) {
       User user = (User) SecurityUtils.getSubject().getPrincipal();
       if(user==null) {
           return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
       }
       Order order = orderService.add(orderPage);
       if(order!=null){
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),order);
       }
       return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
   }

   /**
     *  编辑
    * @param
    * @return
    */

   @ApiOperation(value = "取消订单接口 参数：订单id", tags = {"订单购物车接口"}, notes = "取消订单接口 参数：订单id")
   @PostMapping(value = "/cancelOrder")
   public RestResponseBean cancelOrder(@RequestParam(name="id",required=true) String id) {
       User user = (User) SecurityUtils.getSubject().getPrincipal();
       if(user==null) {
           return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
       }
       boolean flag = orderService.cancel(id);
       if(flag){
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
       }
       return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
   }


    /**
     * showdoc   (~~~~~~~~~~~~有错~~~~~~~~~~~~)
     * @catalog 订单购物车接口
     * @title 用户查询订单（不包括商品详情）接口
     * @description 用户查询订单（不包括商品详情）接口
     * @method GET
     * @url /nckf-boot/api/v1/order/queryOrderUserById
     * @param id 必填 String 订单id
     * @return {"code": 1,"data": [],"msg": "操作成功","time": "1561013500114"}
     * @return_param code String 响应状态
     * @return_param data List 订单信息
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 12
     */
   @GetMapping(value = "/queryOrderUserById")
   @ApiOperation(value = "用户查询订单（不包括商品详情）接口", tags = {"订单购物车接口"}, notes = "用户查询订单（不包括商品详情）接口")
   public RestResponseBean queryOrderUserById(@RequestParam(name="id",required=true) String id) {
       User user = (User) SecurityUtils.getSubject().getPrincipal();
       if(user==null) {
           return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
       }
       Order order = orderService.getById(id);
       if(order!=null) {
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),order);
       }
       return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
   }
   /**
     * 通过id查询
    * @param id
    * @return
    */
    @GetMapping(value = "/queryOrderGoods")
    @ApiOperation(value = "用户查询单个订单（包括商品详情）接口", tags = {"订单购物车接口"}, notes = "用户查询订单个（包括商品详情）接口")
    public RestResponseBean queryOrderGoods(@RequestParam(name="id",required=true)String id) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        List<OrderGoods> orderGoodsList = orderGoodsService.selectByMainId(id);
        if(orderGoodsList!=null){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),orderGoodsList);
        }
        return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
    }

    @GetMapping(value = "/distance")
    @ApiOperation(value = "用户最近订单的骑手距离", tags = {"订单购物车接口"}, notes = "用户最近订单的骑手距离")
    public RestResponseBean queryDistance(double riderLng,double riderLat){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
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

            String meter = DistanceUtil.algorithm(riderAddress.getLat(),riderAddress.getLng(),riderLng,riderLat);

            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),meter);

        }else{
            newOrder = selectLastOne(list);
            //计算距离
            QueryWrapper<RiderAddress> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(RiderAddress::getRiderId,newOrder.getRiderId());
            RiderAddress riderAddress = riderAddressService.getOne(queryWrapper1);

            OrderDistanceVo orderDistanceVo = new OrderDistanceVo();
            orderDistanceVo.setLat(riderAddress.getLat());
            orderDistanceVo.setLng(riderAddress.getLng());
            orderDistanceVo.setOrderId(newOrder.getId());

            String meter = DistanceUtil.algorithm(riderAddress.getLat(),riderAddress.getLng(),riderLng,riderLat);
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

}
