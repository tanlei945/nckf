package org.benben.modules.business.invoice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.modules.business.invoice.entity.Invoice;
import org.benben.modules.business.invoice.service.IInvoiceService;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
* @Title: Controller
* @Description: 用户发票
* @author： jeecg-boot
* @date：   2019-04-23
* @version： V1.0
*/
@RestController
@RequestMapping("/api/invoice")
@Slf4j
@Api(tags = {"发票接口"})
public class RestInvoiceController {
    @Autowired
    private IInvoiceService invoiceService;
    @Autowired
    private IOrderService orderService;


   /**
     * 查询
    * @param userId
    * @return
    */
   @GetMapping(value = "/list")
   @ApiOperation(value = "用户发票查询接口", tags = {"发票接口"}, notes = "用户发票查询接口")
   @ApiImplicitParam(name = "userId", value = "用户id",required = true)
   public RestResponseBean queryPageList(@RequestParam(name = "userId",required = true) String userId) {
       QueryWrapper<Invoice> queryWrapper = new QueryWrapper<>();
       queryWrapper.eq("userId",userId);
       List<Invoice> invoiceList = invoiceService.list(queryWrapper);
       if(invoiceList != null){
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),invoiceList);
       }
       return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
   }

   /**
    *   添加
    * @param invoice
    * @return
    */
   @PostMapping(value = "/add")
   @ApiOperation(value = "用户发票提交接口", tags = {"发票接口"}, notes = "用户发票提交接口")
   @ApiImplicitParams({
           @ApiImplicitParam(name = "invoice", value = "发票实体"),
           @ApiImplicitParam(name = "orderIdList", value = "选中订单的id")
   })
   public RestResponseBean add(Invoice invoice,@RequestParam(value = "orderIdList",required = true)List<String> orderIdList) {
       //从数据库中获取用户所需的实际开票金额
       double sum = 0;
       QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
       for (String s : orderIdList) {
           Order order = orderService.getById(s);
           if(order.getOrderMoney()!=0){
               sum += order.getOrderMoney();
           }
       }
       //判断用户提交的开票金额是否和数据库中存的实际开票金额相等
       if(invoice != null){
           if(invoice.getInvoiceMoney( ) == sum){
               try {
                   invoiceService.save(invoice);
                   for (String s : orderIdList) {
                       Order order = orderService.getById(s);
                       order.setInvoiceFlag("1");
                       QueryWrapper<Order> queryWrapper1 = new QueryWrapper<>();
                       queryWrapper.eq("id",s);
                       orderService.update(order,queryWrapper1);
                   }
                   orderService.invoiceOk(orderIdList);
                   return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
               } catch (Exception e) {
                   log.info(e.getMessage());
                   return  new RestResponseBean(ResultEnum.INVOICE_MONEY_ERROR.getValue(),ResultEnum.INVOICE_MONEY_ERROR.getDesc(),null);
               }
           }else{
               return  new RestResponseBean(ResultEnum.INVOICE_MONEY_ERROR.getValue(),ResultEnum.INVOICE_MONEY_ERROR.getDesc(),null);
           }
       }else{
           return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
       }
   }

   /**
     *  编辑
    * @param invoice
    * @return
    */
   @PostMapping(value = "/edit")
   @ApiOperation(value = "用户发票编辑接口", tags = {"发票接口"}, notes = "用户发票编辑接口")
   @ApiImplicitParam(name = "invoice", value = "发票实体")
   public RestResponseBean edit(Invoice invoice) {
       Invoice invoiceEntity = invoiceService.getById(invoice.getId());
       if(invoiceEntity==null) {
           return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
       }else {
           boolean ok = invoiceService.updateById(invoice);
           if(ok) {
               return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
           }else{
               return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
           }
       }
   }

   /**
     *   通过id删除
    * @param id
    * @return
    */
   @DeleteMapping(value = "/delete")
   @ApiImplicitParam(name = "id", value = "发票id",required = true )
   public RestResponseBean delete(@RequestParam(name="id",required=true) String id) {
       Invoice invoice = invoiceService.getById(id);
       if(invoice==null) {
           return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
       }else {
           boolean ok = invoiceService.removeById(id);
           if(ok) {
               return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
           }else{
               return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
       }
       }
   }

   /**
     *  批量删除
    * @param ids
    * @return
    */
   @DeleteMapping(value = "/deleteBatch")
   @ApiImplicitParam(name = "ids", value = "选中发票的id",required = true )
   public RestResponseBean deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       if(ids==null || "".equals(ids.trim())) {
           return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
       }else {
           this.invoiceService.removeByIds(Arrays.asList(ids.split(",")));
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
       }
   }

   /**
     * 通过id查询
    * @param id
    * @return
    */
   @GetMapping(value = "/queryById")
   @ApiImplicitParam(name = "id", value = "发票id",required = true )
   public RestResponseBean queryById(@RequestParam(name="id",required=true) String id) {
       Result<Invoice> result = new Result<Invoice>();
       Invoice invoice = invoiceService.getById(id);
       if(invoice==null) {
           return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
       }else {
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),invoice);
       }
   }


}
