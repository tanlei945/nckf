package org.benben.modules.business.invoice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.invoice.entity.Invoice;
import org.benben.modules.business.invoice.service.IInvoiceService;
import org.benben.modules.business.order.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
