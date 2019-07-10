package org.benben.modules.business.order.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.DateUtils;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.entity.OrderGoods;
import org.benben.modules.business.order.service.IOrderGoodsService;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.order.vo.OrderPage;
import org.benben.modules.system.service.ISysUserService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
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
import java.text.ParseException;
import java.util.*;

/**
 * @Title: Controller
 * @Description: 订单
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
@RestController
@RequestMapping("/order/order")
@Slf4j
public class OrderController {
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrderGoodsService orderGoodsService;
	@Autowired
	private ISysUserService sysUserService;
	
	/**
	  * 分页列表查询
	 * @param order
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Order>> queryPageList(Order order,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		String storeId = sysUserService.queryStoreId();
		log.info("门店id---------->"+storeId);
		if(storeId == null){
			Result<IPage<Order>> result = new Result<IPage<Order>>();
			QueryWrapper<Order> queryWrapper = QueryGenerator.initQueryWrapper(order, req.getParameterMap());
			Page<Order> page = new Page<Order>(pageNo, pageSize);
			IPage<Order> pageList = orderService.page(page, queryWrapper);
			result.setSuccess(true);
			result.setResult(pageList);
			return result;
		}else{
			Result<IPage<Order>> result = new Result<IPage<Order>>();
			/*QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("store_id",storeId);*/
			order.setStoreId(storeId);
			QueryWrapper<Order> queryWrapper = QueryGenerator.initQueryWrapper(order, req.getParameterMap());
			Page<Order> page = new Page<Order>(pageNo, pageSize);
			IPage<Order> pageList = orderService.page(page, queryWrapper);
			result.setSuccess(true);
			result.setResult(pageList);
			return result;
		}


	}
	
	/**
	  *   添加
	 * @param orderPage
	 * @return
	 */
	@PostMapping(value = "/add")
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
	@PutMapping(value = "/edit")
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
			result.success("修改成功!");
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<Order> delete(@RequestParam(name="id",required=true) String id) {
		Result<Order> result = new Result<Order>();
		Order order = orderService.getById(id);
		if(order==null) {
			result.error500("未找到对应实体");
		}else {
			orderService.delMain(id);
			result.success("删除成功!");
		}
		
		return result;
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<Order> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Order> result = new Result<Order>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.orderService.delBatchMain(Arrays.asList(ids.split(",")));
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
	public Result<List<OrderGoods>> queryOrderGoodsListByMainId(@RequestParam(name="id",required=true) String id) {
		Result<List<OrderGoods>> result = new Result<List<OrderGoods>>();
		List<OrderGoods> orderGoodsList = orderGoodsService.selectByMainId(id);
		result.setResult(orderGoodsList);
		result.setSuccess(true);
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
      QueryWrapper<Order> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Order order = JSON.parseObject(deString, Order.class);
              queryWrapper = QueryGenerator.initQueryWrapper(order, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<OrderPage> pageList = new ArrayList<OrderPage>();
      List<Order> orderList = orderService.list(queryWrapper);
      for (Order order : orderList) {
          OrderPage vo = new OrderPage();
          BeanUtils.copyProperties(order, vo);
          List<OrderGoods> orderGoodsList = orderGoodsService.selectByMainId(order.getId());
          vo.setOrderGoodsList(orderGoodsList);
          pageList.add(vo);
      }
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "订单列表");
      mv.addObject(NormalExcelConstants.CLASS, OrderPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("订单列表数据", "导出人:Jeecg", "导出信息"));
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
              List<OrderPage> list = ExcelImportUtil.importExcel(file.getInputStream(), OrderPage.class, params);
              for (OrderPage page : list) {
                  Order po = new Order();
                  BeanUtils.copyProperties(page, po);
                  orderService.saveMain(po, page.getOrderGoodsList());
              }
              return Result.ok("文件导入成功！数据行数：" + list.size());
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

  @GetMapping(value="/moneyCount")
  public Result<Double> moneyCount(){
	  Result<Double> result = new Result<Double>();
	  Double i = orderService.countMoney();
	  result.setResult(i);
	  return result;
  }
	@GetMapping(value="/DiffDayMoney")
  public Result<Double> diffDayMoney(){
	  Result<Double> result = new Result<Double>();
	  Double aDouble = orderService.DiffDayMoney();
	  result.setResult(aDouble);
	  return result;
  }

	@GetMapping(value="/orderCount")
	public Result<Integer> orderCount(){
		Result<Integer> result = null;
		try {
			result = new Result<Integer>();
			Integer integer = orderService.countOrder();
			result.setResult(integer);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.error500("系统错误");
		}
		return result;
	}
}
