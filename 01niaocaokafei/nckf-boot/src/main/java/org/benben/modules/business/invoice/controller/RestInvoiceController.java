package org.benben.modules.business.invoice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.feedback.entity.FeedBack;
import org.benben.modules.business.invoice.entity.Invoice;
import org.benben.modules.business.invoice.entity.InvoiceTitle;
import org.benben.modules.business.invoice.service.IInvoiceService;
import org.benben.modules.business.invoice.service.IInvoiceTitleService;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
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
@RequestMapping("/api/v1/invoice")
@Slf4j
@Api(tags = {"用户接口"})
public class RestInvoiceController {
    @Autowired
    private IInvoiceService invoiceService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IInvoiceTitleService invoiceTitleService;
    @Autowired
    private IUserService userService;


   /**
     * 查询
    * @return
    */
   @GetMapping(value = "/queryInvoice")
   @ApiOperation(value = "用户发票查询接口", tags = {"用户接口"}, notes = "用户发票查询接口")
   public RestResponseBean queryInvoice(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo, @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
       User user = (User) SecurityUtils.getSubject().getPrincipal();
       if(user==null) {
           return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
       }
       Result<IPage<Invoice>> result = new Result<IPage<Invoice>>();
       QueryWrapper<Invoice> queryWrapper = new QueryWrapper<>();
       queryWrapper.eq("user_id",user.getId());
       Page<Invoice> page = new Page<Invoice>(pageNo, pageSize);
       IPage<Invoice> pageList = invoiceService.page(page, queryWrapper);
       if(pageList != null){
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),pageList);
       }
       return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
   }

   /**
    *   添加
    * @param invoice
    * @return
    */
   @PostMapping(value = "/addInvoice")
   @ApiOperation(value = "用户发票提交接口", tags = {"用户接口"}, notes = "用户发票提交接口")
   @ApiImplicitParams({
           @ApiImplicitParam(name = "invoice", value = "发票实体"),
           @ApiImplicitParam(name = "orderIdList", value = "选中订单的id")
   })
   public RestResponseBean addInvoice(Invoice invoice,@RequestParam(value = "orderIdList",required = true)List<String> orderIdList) {
       User user = (User) SecurityUtils.getSubject().getPrincipal();
       if(user==null) {
           return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
       }
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
                   invoice.setUsername(user.getUsername());
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
   @PostMapping(value = "/editInvoice")
   /*@ApiOperation(value = "用户发票编辑接口", tags = {"用户接口"}, notes = "用户发票编辑接口")
   @ApiImplicitParam(name = "invoice", value = "发票实体")*/
   public RestResponseBean editInvoice(Invoice invoice) {
       User user = (User) SecurityUtils.getSubject().getPrincipal();
       if(user==null) {
           return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
       }
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
   @DeleteMapping(value = "/deleteInvoiceById")
   /*@ApiImplicitParam(name = "id", value = "发票id",required = true )*/
   public RestResponseBean deleteInvoiceById(@RequestParam(name="id",required=true) String id) {
       User user = (User) SecurityUtils.getSubject().getPrincipal();
       if(user==null) {
           return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
       }
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
   @DeleteMapping(value = "/deleteBatchInvoice")
   /*@ApiImplicitParam(name = "ids", value = "选中发票的id",required = true )*/
   public RestResponseBean deleteBatchInvoice(@RequestParam(name="ids",required=true) String ids) {
       User user = (User) SecurityUtils.getSubject().getPrincipal();
       if(user==null) {
           return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
       }
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
   @GetMapping(value = "/queryInvoiceById")
   @ApiImplicitParam(name = "id", value = "发票id",required = true )
   public RestResponseBean queryInvoiceById(@RequestParam(name="id",required=true) String id) {
       User user = (User) SecurityUtils.getSubject().getPrincipal();
       if(user==null) {
           return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
       }
       Result<Invoice> result = new Result<Invoice>();
       Invoice invoice = invoiceService.getById(id);
       if(invoice==null) {
           return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
       }else {
           return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),invoice);
       }
   }


    /**
     * 分页列表查询
     * @return
     */
    @GetMapping(value = "/title/queryInvoiceTitle")
    @ApiOperation(value = "用户发票抬头查询接口", tags = {"用户接口"}, notes = "用户发票抬头查询接口")
    @ApiImplicitParam(name = "userId", value = "用户id")
    public RestResponseBean queryInvoiceTitle() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        QueryWrapper<InvoiceTitle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        InvoiceTitle invoiceTitle = invoiceTitleService.getOne(queryWrapper);
        if(invoiceTitle != null){
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),invoiceTitle);
        }
        return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);

    }

    /**
     *   添加
     * @param invoiceTitle
     * @return
     */
    @PostMapping(value = "/title/addInvoiceTitle")
    @ApiOperation(value = "用户发票头提交接口", tags = {"用户接口"}, notes = "用户发票头提交接口")
    public RestResponseBean addInvoiceTitle(@RequestBody InvoiceTitle invoiceTitle) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        QueryWrapper<InvoiceTitle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", invoiceTitle.getUserId());
        InvoiceTitle invoiceTitlebyDB = invoiceTitleService.getOne(queryWrapper);
        if (invoiceTitlebyDB == null) {
            boolean flag = invoiceTitleService.save(invoiceTitle);
            if (flag) {
                return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);
            } else {
                return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
            }
        } else {
            QueryWrapper<InvoiceTitle> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", invoiceTitle.getUserId());
            boolean flag = invoiceTitleService.update(invoiceTitle, wrapper);
            if (flag) {
                return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);
            } else {
                return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
            }
        }
    }

    /**
     *   通过id删除
     * @param id
     * @return
     */
    @PostMapping(value = "/title/deleteInvoiceTitle")
   /* @ApiOperation(value = "用户发票头根据id删除接口", tags = {"用户接口"}, notes = "用户发票头根据id删除接口")*/
    @ApiImplicitParam(name = "id", value = "发票头id",required = true )
    public RestResponseBean deleteInvoiceTitle(@RequestParam(name="id",required=true) String id) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        Result<InvoiceTitle> result = new Result<InvoiceTitle>();
        InvoiceTitle invoiceTitle = invoiceTitleService.getById(id);
        if(invoiceTitle==null) {
            return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
        }else {
            boolean ok = invoiceTitleService.removeById(id);
            if(ok) {
                return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
            }
            return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
        }
    }


    @GetMapping(value = "/background_list")
    public Result<IPage<Invoice>> queryPageList(Invoice invoice,
                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {
        Result<IPage<Invoice>> result = new Result<IPage<Invoice>>();
        QueryWrapper<Invoice> queryWrapper = QueryGenerator.initQueryWrapper(invoice, req.getParameterMap());
        Page<Invoice> page = new Page<Invoice>(pageNo, pageSize);
        IPage<Invoice> pageList = invoiceService.page(page, queryWrapper);

        List<Invoice> records = pageList.getRecords();
        for (Invoice record : records) {
            String userId = record.getUserId();
            User user = userService.getById(userId);
            if(user != null){
                record.setUsername(user.getUsername());
            }
        }
        pageList.setRecords(records);

        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }


}