package org.benben.modules.business.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.entity.OrderGoods;
import org.benben.modules.business.order.service.IOrderGoodsService;
import org.benben.modules.business.order.service.IOrderNoPayService;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.order.vo.OrderPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @PostMapping(value = "/list")
   @ApiOperation(value = "订单多（单）条件查询接口 status:9:已取消 0:全部（不包括已取消） 1待付款 2收货中 3待评价", tags = {"订单接口"}, notes = "订单多（单）条件查询接口 status:9:已取消 0:全部（不包括已取消） 1待付款 2收货中 3待评价")
   public Result<IPage<Order>> queryPageList(@RequestBody Order order,
                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                     HttpServletRequest req) {
       if("0".equals(order.getStatus())){
           Result<IPage<Order>> result = new Result<IPage<Order>>();
           QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
           queryWrapper.eq("status","1").or().eq("status","2").or().eq("status","3");
           Page<Order> page = new Page<Order>(pageNo, pageSize);
           IPage<Order> orderIPage = orderService.page(page, queryWrapper);
           List<Order> orderList = orderService.list(queryWrapper);
           result.setSuccess(true);
           result.setResult(orderIPage);
           return result;
       }else{
           Result<IPage<Order>> result = new Result<IPage<Order>>();
           QueryWrapper<Order> queryWrapper = QueryGenerator.initQueryWrapper(order, req.getParameterMap());
           Page<Order> page = new Page<Order>(pageNo, pageSize);
           IPage<Order> pageList = orderService.page(page, queryWrapper);
           result.setSuccess(true);
           result.setResult(pageList);
           return result;
       }

   }






   @GetMapping(value = "/query_by_orderId")
   @ApiOperation(value = "用户根据订单号查询订单接口", tags = {"订单接口"}, notes = "用户根据订单号查询订单接口")
   public Result<Order> queryByOrderId(@RequestParam(name="orderId",required=true) String orderId) {
       return orderService.queryByOrderId(orderId);
   }

   /**
     *   添加
    * @param orderPage
    * @return
    */

   @PostMapping(value = "/add")
   @ApiOperation(value = "用户新增订单接口", tags = {"订单接口"}, notes = "用户新增订单接口")
   public Result<Order> add(@RequestBody OrderPage orderPage) {
        return orderService.add(orderPage);
   }

   /**
     *  编辑
    * @param
    * @return
    */

   @ApiOperation(value = "取消订单接口", tags = {"订单接口"}, notes = "取消订单接口")
   @PostMapping(value = "/cancel")
   public Result<Order> cancel(@RequestParam(name="id",required=true) String id) {
       return orderService.cancel(id);
   }


    /*@ApiOperation(value = "支付完成修改订单状态接口", tags = {"订单接口"}, notes = "支付完成修改订单状态接口")
    @PostMapping(value = "/edit")
    public Result<Order> edit(@RequestParam(name="orderId",required=true) String orderId) {
        return orderService.edit(orderId);
    }*/


   /**
     * 通过id查询
    * @param id
    * @return
    */
   @GetMapping(value = "/query_by_id")
   @ApiOperation(value = "用户查询订单（不包括商品详情）接口", tags = {"订单接口"}, notes = "用户查询订单（不包括商品详情）接口")
   public Result<Order> queryById(@RequestParam(name="id",required=true) String id) {
       Result<Order> result = new Result<Order>();
       Order order = orderService.getById(id);
       if(order==null) {
           result.error500("未找到对应实体");
       }else {
           result.setResult(order);
           result.setSuccess(true);
       }
       return result;
   }
   /**
     * 通过id查询
    * @param id
    * @return
    */
   @GetMapping(value = "/query_order_goods_by_mainId")
   @ApiOperation(value = "用户查询订单（包括商品详情）接口", tags = {"订单接口"}, notes = "用户查询订单（包括商品详情）接口")
   public Result<List<OrderGoods>> queryOrderGoodsListByMainId(String id) {
       Result<List<OrderGoods>> result = new Result<List<OrderGoods>>();
       List<OrderGoods> orderGoodsList = orderGoodsService.selectByMainId(id);
       result.setResult(orderGoodsList);
       result.setSuccess(true);
       return result;
   }
}
