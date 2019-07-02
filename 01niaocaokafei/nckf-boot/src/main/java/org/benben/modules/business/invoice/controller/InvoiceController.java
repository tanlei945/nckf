package org.benben.modules.business.invoice.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.invoice.entity.Invoice;
import org.benben.modules.business.invoice.service.IInvoiceService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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
import com.alibaba.fastjson.JSON;

 /**
 * @Title: Controller
 * @Description: 用户发票
 * @author： jeecg-boot
 * @date：   2019-05-06
 * @version： V1.0
 */
@RestController
@RequestMapping("/invoice/invoice")
@Slf4j
public class InvoiceController {
	@Autowired
	private IInvoiceService invoiceService;
	
	/**
	  * 分页列表查询
	 * @param invoice
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
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
	public Result<Invoice> add(@RequestBody Invoice invoice) {
		Result<Invoice> result = new Result<Invoice>();
		try {
			invoiceService.save(invoice);
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
	 * @param invoice
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Invoice> edit(@RequestBody Invoice invoice) {
		Result<Invoice> result = new Result<Invoice>();
		Invoice invoiceEntity = invoiceService.getById(invoice.getId());
		if(invoiceEntity==null) {
			result.error500("未找到对应实体");
		}else {
			invoice.setUpdateTime(new Date());
			boolean ok = invoiceService.updateById(invoice);
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

  /**
      * 导出excel
   *
   * @param request
   * @param response
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
      // Step.1 组装查询条件
      QueryWrapper<Invoice> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Invoice invoice = JSON.parseObject(deString, Invoice.class);
              queryWrapper = QueryGenerator.initQueryWrapper(invoice, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Invoice> pageList = invoiceService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "用户发票列表");
      mv.addObject(NormalExcelConstants.CLASS, Invoice.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用户发票列表数据", "导出人:Jeecg", "导出信息"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
  }

  /**
      * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<Invoice> listInvoices = ExcelImportUtil.importExcel(file.getInputStream(), Invoice.class, params);
              for (Invoice invoiceExcel : listInvoices) {
                  invoiceService.save(invoiceExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listInvoices.size());
          } catch (Exception e) {
              log.error(e.getMessage());
              return Result.error("文件导入失败！");
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
  }

}
