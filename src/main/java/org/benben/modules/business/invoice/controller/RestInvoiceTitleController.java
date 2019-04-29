package org.benben.modules.business.invoice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.modules.business.invoice.entity.InvoiceTitle;
import org.benben.modules.business.invoice.service.IInvoiceTitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
* @Title: Controller
* @Description: 用户发票抬头
* @author： jeecg-boot
* @date：   2019-04-23
* @version： V1.0
*/
@RestController
@RequestMapping("/api/invoice/title")
@Slf4j
@Api(tags = {"发票抬头接口"})
public class RestInvoiceTitleController {
   @Autowired
   private IInvoiceTitleService invoiceTitleService;

   /**
     * 分页列表查询
    * @param userId
    * @return
    */
   @GetMapping(value = "/list")
   @ApiOperation(value = "用户发票抬头查询接口", tags = {"发票抬头接口"}, notes = "用户发票抬头查询接口")
   @ApiImplicitParam(name = "userId", value = "用户id")
   public RestResponseBean queryList(@RequestParam(name = "userId",required = true) String userId) {
       QueryWrapper<InvoiceTitle> queryWrapper = new QueryWrapper<>();
       queryWrapper.eq("user_id", userId);
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
   @PostMapping(value = "/add")
   @ApiOperation(value = "用户发票头提交接口", tags = {"发票抬头接口"}, notes = "用户发票头提交接口")
   @ApiImplicitParam(name = "invoiceTitle", value = "发票头实体")
   public RestResponseBean add(@RequestBody InvoiceTitle invoiceTitle) {
       if(invoiceTitle.getUserId() == null && invoiceTitle.getUserId()==""){
           return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
       }else{
           QueryWrapper<InvoiceTitle> queryWrapper = new QueryWrapper<>();
           queryWrapper.eq("user_id", invoiceTitle.getUserId());
           InvoiceTitle invoiceTitlebyDB = invoiceTitleService.getOne(queryWrapper);
           if(invoiceTitlebyDB == null){
               boolean flag = invoiceTitleService.save(invoiceTitle);
               if(flag){
                   return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
               }else{
                   return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
               }
           }else{
               QueryWrapper<InvoiceTitle> wrapper = new QueryWrapper<>();
               wrapper.eq("user_id", invoiceTitle.getUserId());
               boolean flag = invoiceTitleService.update(invoiceTitle,wrapper);
               if(flag){
                   return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),null);
               }else{
                   return  new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
               }
           }
       }
   }

   /**
     *   通过id删除
    * @param id
    * @return
    */
   @PostMapping(value = "/delete")
   @ApiOperation(value = "用户发票头根据id删除接口", tags = {"发票抬头接口"}, notes = "用户发票头根据id删除接口")
   @ApiImplicitParam(name = "id", value = "发票头id",required = true )
   public RestResponseBean delete(@RequestParam(name="id",required=true) String id) {
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
}
