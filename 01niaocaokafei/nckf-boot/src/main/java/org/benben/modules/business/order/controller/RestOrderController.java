package org.benben.modules.business.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.entity.OrderGoods;
import org.benben.modules.business.order.service.IOrderGoodsService;
import org.benben.modules.business.order.service.IOrderNoPayService;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.order.vo.OrderPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @Title: Controller
* @Description: 订单
* @author： jeecg-boot
* @date：   2019-04-23
* @version： V1.0
*/
@RestController
@RequestMapping("/api/order")
@Slf4j
@Api(tags = {"订单接口"})
public class RestOrderController {
   @Autowired
   private IOrderService orderService;
   @Autowired
   private IOrderGoodsService orderGoodsService;
   @Autowired
   private IOrderNoPayService orderNoPayService;



   /**
     * 分页列表查询
    * @param order
    * @return
    */
   @PostMapping(value = "/list")
   @ApiOperation(value = "订单多（单）条件查询接口 status:9:已取消 0:全部（不包括已取消） 1待付款 2收货中 3待评价 4已评价", tags = {"订单接口"}, notes = "订单多（单）条件查询接口 status:9:已取消 0:全部（不包括已取消） 1待付款 2收货中 3待评价 4已评价")
   public RestResponseBean queryList(@RequestBody Order order) {
       List<OrderPage> orderPageList = orderService.queryList(order);
       if(orderPageList != null){
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),orderPageList);
       }
       return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);

   }
    @PostMapping(value = "/rider/nopay_order")
    @ApiOperation(value = "骑手查询可接订单接口", tags = {"订单接口"}, notes = "骑手查询可接订单接口")
    public RestResponseBean queryRiderOrder(@RequestParam(name = "riderId",required = true) String riderId,
                                            @RequestParam(name = "storeId",required = true) String storeId){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("store_id",storeId).eq("status","2").eq("rider_id","");
        List<Order> list = orderService.list(wrapper);

        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),list);
    }


    @PostMapping(value = "/rider/receive_order")
    @ApiOperation(value = "骑手接单接口", tags = {"订单接口"}, notes = "骑手接单接口")
    public RestResponseBean riderOrder(@RequestParam(name = "riderId",required = true) String riderId,
                                       @RequestParam(name = "orderId",required = true) String orderId){
      return orderService.riderOrder(riderId,orderId);
    }



   @GetMapping(value = "/query_by_orderId")
   @ApiOperation(value = "用户根据订单号查询订单接口", tags = {"订单接口"}, notes = "用户根据订单号查询订单接口")
   public RestResponseBean queryByOrderId(@RequestParam(name="orderId",required=true) String orderId) {
       Order order = orderService.queryByOrderId(orderId);
       if(order!=null){
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),order);
       }
       return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
   }

   /**
     *   添加
    * @param orderPage
    * @return
    */

   @PostMapping(value = "/add")
   @ApiOperation(value = "用户新增订单接口", tags = {"订单接口"}, notes = "用户新增订单接口")
   public RestResponseBean add(@RequestBody OrderPage orderPage) {
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

   @ApiOperation(value = "取消订单接口 参数：订单id", tags = {"订单接口"}, notes = "取消订单接口 参数：订单id")
   @PostMapping(value = "/cancel")
   public RestResponseBean cancel(@RequestParam(name="id",required=true) String id) {
       boolean flag = orderService.cancel(id);
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
   @GetMapping(value = "/query_by_id")
   @ApiOperation(value = "用户查询订单（不包括商品详情）接口", tags = {"订单接口"}, notes = "用户查询订单（不包括商品详情）接口")
   public RestResponseBean queryById(@RequestParam(name="id",required=true) String id) {
       Order order = orderService.getById(id);
       if(order!=null) {
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
       }
       return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
   }
   /**
     * 通过id查询
    * @param id
    * @return
    */
   @GetMapping(value = "/query_order_goods_by_mainId")
   @ApiOperation(value = "用户查询单个订单（包括商品详情）接口", tags = {"订单接口"}, notes = "用户查询订单个（包括商品详情）接口")
   public RestResponseBean queryOrderGoodsListByMainId(@RequestParam(name="id",required=true)String id) {

       List<OrderGoods> orderGoodsList = orderGoodsService.selectByMainId(id);
       if(orderGoodsList!=null){
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),orderGoodsList);
       }
       return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);


   }
}
