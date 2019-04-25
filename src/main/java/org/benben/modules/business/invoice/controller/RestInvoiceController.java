package org.benben.modules.business.invoice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.invoice.entity.Invoice;
import org.benben.modules.business.invoice.service.IInvoiceService;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
     * 分页列表查询
    * @param invoice
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @GetMapping(value = "/list")
   @ApiOperation(value = "用户发票查询接口", tags = {"发票接口"}, notes = "用户发票查询接口")
   public Result<IPage<Invoice>> queryPageList(Invoice invoice,
                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                     HttpServletRequest req) {
       Result<IPage<Invoice>> result = new Result<IPage<Invoice>>();
       QueryWrapper<Invoice> queryWrapper = QueryGenerator.initQueryWrapper(invoice, req.getParameterMap());
       Page<Invoice> page = new Page<Invoice>(pageNo, pageSize);
       IPage<Invoice> pageList = invoiceService.page(page, queryWrapper);
       result.setSuccess(true);
       result.setResult(pageList);
       return result;
   }

   /**
     *   添加
    * @param invoice
    * @return
    */
   @PostMapping(value = "/add")
   @ApiOperation(value = "用户发票提交接口", tags = {"发票接口"}, notes = "用户发票提交接口")
   public Result<Invoice> add(Invoice invoice,@RequestParam(value = "orderIdList")List<String> orderIdList) {
       //从数据库中获取用户所需的实际开票金额
       double sum = 0;
       QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
       for (String s : orderIdList) {
           Order order = orderService.getById(s);
           sum += order.getOrderMoney();
       }

       Result<Invoice> result = new Result<Invoice>();
       //判断用户提交的开票金额是否和数据库中存的实际开票金额相等
       if(invoice.getInvoiceMoney( )== sum){
           try {
               invoiceService.save(invoice);

               for (String s : orderIdList) {
                   Order order = orderService.getById(s);
                   order.setInvoiceFlag("1");
                   QueryWrapper<Order> queryWrapper1 = new QueryWrapper<>();
                   queryWrapper1.eq("id",s);
                   orderService.update(order,queryWrapper1);
               }

               result.success("添加成功！");
           } catch (Exception e) {
               e.printStackTrace();
               log.info(e.getMessage());
               result.error500("操作失败！");
           }
       }else{
           result.error500("开票金额异常！");
       }

       return result;
   }

   /**
     *  编辑
    * @param invoice
    * @return
    */
   @PostMapping(value = "/edit")
   @ApiOperation(value = "用户发票编辑接口", tags = {"发票接口"}, notes = "用户发票编辑接口")
   public Result<Invoice> edit(Invoice invoice) {
       Result<Invoice> result = new Result<Invoice>();
       Invoice invoiceEntity = invoiceService.getById(invoice.getId());
       if(invoiceEntity==null) {
           result.error500("未找到对应实体");
       }else {
           boolean ok = invoiceService.updateById(invoice);
           //TODO 返回false说明什么？
           if(ok) {
               result.success("订单取消成功!");
           }
       }

       return result;
   }

   /**
     *   通过id删除
    * @param id
    * @return
    */
   @DeleteMapping(value = "/delete")
   public Result<Invoice> delete(@RequestParam(name="id",required=true) String id) {
       Result<Invoice> result = new Result<Invoice>();
       Invoice invoice = invoiceService.getById(id);
       if(invoice==null) {
           result.error500("未找到对应实体");
       }else {
           boolean ok = invoiceService.removeById(id);
           if(ok) {
               result.success("删除成功!");
           }
       }

       return result;
   }

   /**
     *  批量删除
    * @param ids
    * @return
    */
   @DeleteMapping(value = "/deleteBatch")
   public Result<Invoice> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       Result<Invoice> result = new Result<Invoice>();
       if(ids==null || "".equals(ids.trim())) {
           result.error500("参数不识别！");
       }else {
           this.invoiceService.removeByIds(Arrays.asList(ids.split(",")));
           result.success("删除成功!");
       }
       return result;
   }

   /**
     * 通过id查询
    * @param id
    * @return
    */
   @GetMapping(value = "/queryById")
   public Result<Invoice> queryById(@RequestParam(name="id",required=true) String id) {
       Result<Invoice> result = new Result<Invoice>();
       Invoice invoice = invoiceService.getById(id);
       if(invoice==null) {
           result.error500("未找到对应实体");
       }else {
           result.setResult(invoice);
           result.setSuccess(true);
       }
       return result;
   }


}
