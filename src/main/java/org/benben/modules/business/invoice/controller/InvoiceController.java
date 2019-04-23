package org.benben.modules.business.invoice.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.invoice.entity.Invoice;
import org.benben.modules.business.invoice.service.IInvoiceService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.entity.OrderGoods;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.user.entity.User;
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
 * @date：   2019-04-23
 * @version： V1.0
 */
@RestController
@RequestMapping("/invoice/invoice")
@Slf4j
@Api(tags = "{发票接口}")
public class InvoiceController {
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
	@ApiOperation(value = "用户发票查询接口", tags = "{发票接口}", notes = "用户发票查询接口")
	public Result<IPage<Invoice>> queryPageList(Invoice invoice,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Invoice>> result = new Result<>();
		QueryWrapper<Invoice> queryWrapper = QueryGenerator.initQueryWrapper(invoice, req.getParameterMap());
		queryWrapper.eq("user_id",invoice.getId());
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
	@ApiOperation(value = "用户发票申请接口", tags = "{发票接口}", notes = "用户发票申请接口")
	public Result<Invoice> add(@RequestBody Invoice invoice,List<String> orderIdList) {

//		List<Order> orderList = new ArrayList<>(orderService.listByIds(orderIdList));
//		double sum = 0;
//
//		for (Order order : orderList) {
//			sum +=order.getOrderMoney();
//		}
//
//		Result<Invoice> result = new Result<Invoice>();
//
//		if(invoice.getInvoiceMoney()==sum){
//			try {
//				invoiceService.save(invoice);
//				result.success("添加成功！");
//			} catch (Exception e) {
//				e.printStackTrace();
//				log.info(e.getMessage());
//				result.error500("操作失败！");
//			}
//		}else{
//			result.error500("开票金额异常！");
//		}
//

		return null;
	}

}
