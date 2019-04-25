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
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.order.vo.OrderPage;
import org.springframework.beans.BeanUtils;
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

   /**
     * 分页列表查询
    * @param order
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @GetMapping(value = "/list")
   @ApiOperation(value = "订单查询接口 status:9:已取消 0:全部 1待付款 2收货中 3待评价 9未支付", tags = {"订单接口"}, notes = "订单查询接口 status:9:已取消 0:全部 1待付款 2收货中 3待评价 9未支付")
   public Result<IPage<Order>> queryPageList(Order order,
                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                     HttpServletRequest req) {
       Result<IPage<Order>> result = new Result<IPage<Order>>();
       QueryWrapper<Order> queryWrapper = QueryGenerator.initQueryWrapper(order, req.getParameterMap());
       Page<Order> page = new Page<Order>(pageNo, pageSize);
       IPage<Order> pageList = orderService.page(page, queryWrapper);
       result.setSuccess(true);
       result.setResult(pageList);
       return result;
   }

   /**
     *   添加
    * @param orderPage
    * @return
    */

   @PostMapping(value = "/add")
   @ApiOperation(value = "用户新增订单接口", tags = {"订单接口"}, notes = "用户新增订单接口")
   public Result<Order> add(@RequestBody OrderPage orderPage) {
       Result<Order> result = new Result<Order>();
       try {
           Order order = new Order();
           BeanUtils.copyProperties(orderPage, order);

           orderService.saveMain(order, orderPage.getOrderGoodsList());
           result.success("添加成功！");
       } catch (Exception e) {
           e.printStackTrace();
           log.info(e.getMessage());
           result.error500("操作失败");
       }
       return result;
   }

   /**
     *  编辑
    * @param orderPage
    * @return
    */

   @ApiOperation(value = "用户取消订单接口", tags = {"订单接口"}, notes = "用户取消订单接口")
   @PostMapping(value = "/cancel")
   public Result<Order> edit(@RequestBody OrderPage orderPage) {
       Result<Order> result = new Result<Order>();
       Order order = new Order();
       BeanUtils.copyProperties(orderPage, order);
       Order orderEntity = orderService.getById(order.getId());
       if(orderEntity==null) {
           result.error500("未找到对应实体");
       }else {
           boolean ok = orderService.updateById(order);
           orderService.updateMain(order, orderPage.getOrderGoodsList());
           result.success("取消订单成功!");
       }
       return result;
   }

   /**
     * 通过id查询
    * @param id
    * @return
    */
   @GetMapping(value = "/queryById")
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
   @GetMapping(value = "/queryOrderGoodsByMainId")
   @ApiOperation(value = "用户查询订单（包括商品详情）接口", tags = {"订单接口"}, notes = "用户查询订单（包括商品详情）接口")
   public Result<List<OrderGoods>> queryOrderGoodsListByMainId(String id) {
       Result<List<OrderGoods>> result = new Result<List<OrderGoods>>();
       List<OrderGoods> orderGoodsList = orderGoodsService.selectByMainId(id);
       result.setResult(orderGoodsList);
       result.setSuccess(true);
       return result;
   }
}
