package org.benben.modules.business.invoice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
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
import org.benben.modules.business.invoice.vo.InvoiceTitleVo;
import org.benben.modules.business.invoice.vo.InvoiceVo;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.benben.modules.shiro.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/invoice")
@Slf4j
@Api(tags = {"用户发票"})
public class RestInvoiceController {
    @Autowired
    private IInvoiceService invoiceService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IInvoiceTitleService invoiceTitleService;
    @Autowired
    private IUserService userService;



    @GetMapping(value = "/queryInvoiceOrder")
    @ApiOperation(value = "用户可开发票订单查询接口", tags = {"用户发票"}, notes = "用户可开发票订单查询接口")
    public RestResponseBean queryInvoiceOrder(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo, @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        Result<IPage<Order>> result = new Result<IPage<Order>>();
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        Page<Order> page = new Page<Order>(pageNo, pageSize);
        IPage<Order> pageList = orderService.page(page, queryWrapper);
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),pageList);

    }



    /**
     * showdoc
     * @catalog 用户接口
     * @title 用户发票查询接口
     * @description 用户发票查询接口
     * @method GET
     * @url /nckf-boot/api/v1/invoice/queryInvoice
     * @return {"code": 1,"data": {"current": 1,"pages": 0,"records": [],"searchCount": true,"size": 10,"total": 0},"msg": "操作成功","time": "1561013874531"}
     * @return_param code String 响应状态
     * @return_param data List 发票信息
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 12
     */
   @GetMapping(value = "/queryInvoice")
   @ApiOperation(value = "用户发票查询接口", tags = {"用户发票"}, notes = "用户发票查询接口")
   public RestResponseBean queryInvoice(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo, @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
       User user = (User) LoginUser.getCurrentUser();
       if(user==null) {
           return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
       }
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
     * showdoc
     * @catalog 用户接口
     * @title 用户发票提交接口
     * @description 用户发票提交接口
     * @method POST
     * @url /nckf-boot/api/v1/invoice/addInvoice
     * @param invoice 必填 Object 发票对象
     * @param id 必填 String 发票id
     * @param invoiceContent 必填 String 发票内容
     * @param invoiceMoney 必填 Double 开票金额
     * @param invoiceTitileId 必填 String 纳税人名称
     * @param invoiceType 必填 String 发票类型(0:个人 1:公司)
     * @param mailingAddress String 邮寄地址
     * @param orderIdList 必填 String 选中订单的id
     * @param paperFlag 必填 String 纸质发票(0:不是 1:是)
     * @param phone 必填 String 电话
     * @json_param status 必填 String 是否开票成功(0:失败 1:成功)
     * @json_param userId 必填 String 用户id
     * @json_param username 必填 String 用户名
     * @json_param email 必填 String 邮箱
     * @json_param createBy 必填 String 创建者
     * @json_param createTime 必填 String 创建时间
     * @json_param updateBy 必填 String 更新人
     * @json_param updateTime 必填 String 更新时间
     * @return {"code": 0,"data": null,"msg": "申请发票金额与订单金额不符","time": "1561086735092"}
     * @return_param code String 响应状态
     * @return_param data String null
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 12
     */
   @PostMapping(value = "/addInvoice")
   @ApiOperation(value = "用户发票提交接口", tags = {"用户发票"}, notes = "用户发票提交接口")
  /* @ApiImplicitParams({
   })*/
   public RestResponseBean addInvoice(@RequestParam(name = "content") String content,
                                      @RequestParam(name = "email") String email,
                                      @RequestParam(name = "invoiceMoney") String invoiceMoney,
                                      @RequestParam(name = "invoiceType") String invoiceType,
                                      @RequestParam(name = "mailingAddress") String mailingAddress,
                                      @RequestParam(name = "orderIdList") String orderIdList,
                                      @RequestParam(name = "taxName") String taxName,
                                      @RequestParam(name = "taxNo") String taxNo,
                                      @RequestParam(name = "titleType") String titleType) {
       User user = (User) LoginUser.getCurrentUser();
       if(user==null) {
           return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
       }

       InvoiceVo invoiceVo = new InvoiceVo();
       invoiceVo.setContent(content);
       invoiceVo.setEmail(email);
       invoiceVo.setInvoiceMoney(Double.parseDouble(invoiceMoney));
       invoiceVo.setInvoiceType(invoiceType);
       invoiceVo.setMailingAddress(mailingAddress);
       invoiceVo.setOrderIdList(orderIdList);
       invoiceVo.setTaxName(taxName);
       invoiceVo.setTaxNo(taxNo);
       invoiceVo.setTitleType(titleType);



       //从数据库中获取用户所需的实际开票金额
       double sum = 0;
       for (String s : invoiceVo.getOrderIdList().split(",")) {
           Order order = orderService.getById(s);
           if(order == null){
               return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
           }
           if(order.getOrderMoney()!=0){
               sum += order.getOrderMoney();
           }
       }
       Invoice invoice = new Invoice();
	   BeanUtils.copyProperties(invoiceVo,invoice);
       //判断用户提交的开票金额是否和数据库中存的实际开票金额相等
       if(invoiceVo != null){
           if(invoiceVo.getInvoiceMoney( ) == sum){
               try {
                   invoice.setRealname(user.getRealname());
				   invoice.setUserId(user.getId());
				   invoice.setCreateBy(user.getRealname());
				   invoice.setCreateTime(new Date());
				   invoice.setStatus("0");
                   invoiceService.save(invoice);
                   for (String s : invoiceVo.getOrderIdList().split(",")) {
                       Order order = orderService.getById(s);
                       order.setInvoiceFlag("1");
                       order.setInvoiceId(invoice.getId());

                       QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
                       queryWrapper.eq("id",s);
                       orderService.update(order,queryWrapper);
                   }
                   orderService.invoiceOk(invoiceVo.getOrderIdList());
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
       User user = (User) LoginUser.getCurrentUser();
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
       User user = (User) LoginUser.getCurrentUser();
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
       User user = (User) LoginUser.getCurrentUser();
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
   @ApiOperation(value = "用户查询发票详情接口", tags = {"用户发票"}, notes = "用户查询发票详情接口")
   public RestResponseBean queryInvoiceById(@RequestParam(name="id",required=true) String id) {
       User user = (User) LoginUser.getCurrentUser();
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
     * showdoc
     * @catalog 用户接口
     * @title 用户发票抬头查询接口
     * @description 用户发票抬头查询接口
     * @method GET
     * @url /nckf-boot/api/v1/invoice/title/queryInvoiceTitle
     * @json_param userId 必填 String 用户id
     * @return {"code": 1,"data": {"bankAccount": "1","companyAddress": "1","companyBank": "1","createBy": "1","createTime": 0,"id": "c73ee7f3d95a74f9970eaac804548f78","invoiceType": "1","status": "1","taxName": "1","taxNo": "1","telephone": "1","updateBy": "1","updateTime": 0,"userId": "c73ee7f3d95a74f9970eaac804548f78"},"msg": "操作成功","time": "1561017271069"}
     * @return_param code String 响应状态
     * @return_param bankAccount String 银行账号
     * @return_param companyAddress String 公司地址
     * @return_param companyBank String 开户行名称
     * @return_param createBy String 创建者
     * @return_param createTime Date 创建时间
     * @return_param id String 发票抬头id
     * @return_param invoiceType String 发票类型(0:个人 1:公司)
     * @return_param status String 发票状态(0:失败 1:成功)
     * @return_param taxName String 纳税人名称
     * @return_param taxNo String 税号
     * @return_param telephone String 电话号码
     * @return_param updateBy String 更新人
     * @return_param updateTime Date 更新时间
     * @return_param userId String 用户id
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 13
     */
    @GetMapping(value = "/title/queryInvoiceTitle")
    @ApiOperation(value = "用户发票抬头查询接口", tags = {"用户发票"}, notes = "用户发票抬头查询接口")
    public RestResponseBean queryInvoiceTitle() {
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        QueryWrapper<InvoiceTitle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        InvoiceTitle invoiceTitle = invoiceTitleService.getOne(queryWrapper);
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),invoiceTitle);


    }

    /**
     * showdoc
     * @catalog 用户接口
     * @title 用户发票头提交接口
     * @description 用户发票头提交接口
     * @method POST
     * @url /nckf-boot/api/v1/invoice/title/addInvoiceTitle
     * @json_param {"messageConstellation": "string","messageHight": 0,"messageWeight": 0,"nickName": "string","sex":0}
     * @return {"code": 1,"data": null,"msg": "操作成功","time": "1561017237369"}
     * @return_param code String 响应状态
     * @return_param data String null
     * @return_param msg String 操作信息
     * @return_param time Date 操作时间
     * @remark 这里是备注信息
     * @number 14
     */
    @PostMapping(value = "/title/addInvoiceTitle")
    @ApiOperation(value = "用户发票头提交接口", tags = {"用户发票"}, notes = "用户发票头提交接口")
    public RestResponseBean addInvoiceTitle(@RequestBody InvoiceTitleVo invoiceTitleVo) {
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        QueryWrapper<InvoiceTitle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        InvoiceTitle invoiceTitlebyDB = invoiceTitleService.getOne(queryWrapper);
        if (invoiceTitlebyDB == null) {
            InvoiceTitle invoiceTitle = new InvoiceTitle();
            BeanUtils.copyProperties(invoiceTitleVo,invoiceTitle);
            invoiceTitle.setCreateBy(user.getRealname());
            invoiceTitle.setCreateTime(new Date());
            invoiceTitle.setUserId(user.getId());
            boolean flag = invoiceTitleService.save(invoiceTitle);
            if (flag) {
                return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);
            } else {
                return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
            }
        } else {
            QueryWrapper<InvoiceTitle> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", user.getId());
            InvoiceTitle invoiceTitle = new InvoiceTitle();
            BeanUtils.copyProperties(invoiceTitleVo,invoiceTitle);
            invoiceTitle.setUpdateBy(user.getRealname());
            invoiceTitle.setUpdateTime(new Date());
            invoiceTitle.setId(invoiceTitlebyDB.getId());
            boolean flag = invoiceTitleService.updateById(invoiceTitle);
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
    //@ApiImplicitParam(name = "id", value = "发票头id",required = true )
    public RestResponseBean deleteInvoiceTitle(@RequestParam(name="id",required=true) String id) {
        User user = (User) LoginUser.getCurrentUser();
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
                record.setRealname(user.getRealname());
            }
        }
        pageList.setRecords(records);

        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }


}
