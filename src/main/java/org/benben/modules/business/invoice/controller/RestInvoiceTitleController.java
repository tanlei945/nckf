package org.benben.modules.business.invoice.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.invoice.entity.InvoiceTitle;
import org.benben.modules.business.invoice.service.IInvoiceTitleService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
* @Title: Controller
* @Description: 用户发票抬头
* @author： jeecg-boot
* @date：   2019-04-23
* @version： V1.0
*/
@RestController
@RequestMapping("/invoice_title")
@Slf4j
@Api("{发票抬头接口}")
public class RestInvoiceTitleController {
   @Autowired
   private IInvoiceTitleService invoiceTitleService;

   /**
     * 分页列表查询
    * @param invoiceTitle
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @GetMapping(value = "/list")
   @ApiOperation(value = "用户发票抬头（多条件）查询接口", tags = "{发票抬头接口}", notes = "用户发票抬头（多条件）查询接口")
   public Result<IPage<InvoiceTitle>> queryPageList(InvoiceTitle invoiceTitle,
                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                     HttpServletRequest req) {
       Result<IPage<InvoiceTitle>> result = new Result<IPage<InvoiceTitle>>();
       QueryWrapper<InvoiceTitle> queryWrapper = QueryGenerator.initQueryWrapper(invoiceTitle, req.getParameterMap());
       Page<InvoiceTitle> page = new Page<InvoiceTitle>(pageNo, pageSize);
       IPage<InvoiceTitle> pageList = invoiceTitleService.page(page, queryWrapper);
       result.setSuccess(true);
       result.setResult(pageList);
       return result;
   }

   /**
     *   添加
    * @param invoiceTitle
    * @return
    */
   @PostMapping(value = "/add")
   @ApiOperation(value = "用户发票头提交接口", tags = "{发票抬头接口}", notes = "用户发票头提交接口")
   public Result<InvoiceTitle> add(InvoiceTitle invoiceTitle) {
       Result<InvoiceTitle> result = new Result<InvoiceTitle>();
       try {
           invoiceTitleService.save(invoiceTitle);
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
    * @param invoiceTitle
    * @return
    */
   @PutMapping(value = "/edit")
   @ApiOperation(value = "用户发票头编辑接口", tags = "{发票抬头接口}", notes = "用户发票头编辑接口")
   public Result<InvoiceTitle> edit(@RequestBody InvoiceTitle invoiceTitle) {
       Result<InvoiceTitle> result = new Result<InvoiceTitle>();
       InvoiceTitle invoiceTitleEntity = invoiceTitleService.getById(invoiceTitle.getId());
       if(invoiceTitleEntity==null) {
           result.error500("未找到对应实体");
       }else {
           boolean ok = invoiceTitleService.updateById(invoiceTitle);
           //TODO 返回false说明什么？
           if(ok) {
               result.success("修改成功!");
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
   @ApiOperation(value = "用户发票头根据id删除接口", tags = "{发票抬头接口}", notes = "用户发票头根据id删除接口")
   public Result<InvoiceTitle> delete(@RequestParam(name="id",required=true) String id) {
       Result<InvoiceTitle> result = new Result<InvoiceTitle>();
       InvoiceTitle invoiceTitle = invoiceTitleService.getById(id);
       if(invoiceTitle==null) {
           result.error500("未找到对应实体");
       }else {
           boolean ok = invoiceTitleService.removeById(id);
           if(ok) {
               result.success("删除成功!");
           }
       }

       return result;
   }
}
