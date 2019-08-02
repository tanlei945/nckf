package org.benben.modules.business.order.controller;

import java.io.File;
import java.util.Arrays;
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
import org.benben.modules.business.order.entity.OrderDetail;
import org.benben.modules.business.order.service.IOrderDetailService;

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
 * @Description: 订单详情
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@RestController
@RequestMapping("/order/orderDetail")
@Slf4j
public class OrderDetailController {
	@Autowired
	private IOrderDetailService orderDetailService;
	
	/**
	  * 分页列表查询
	 * @param orderDetail
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<OrderDetail>> queryPageList(OrderDetail orderDetail,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<OrderDetail>> result = new Result<IPage<OrderDetail>>();
		QueryWrapper<OrderDetail> queryWrapper = QueryGenerator.initQueryWrapper(orderDetail, req.getParameterMap());
		Page<OrderDetail> page = new Page<OrderDetail>(pageNo, pageSize);
		IPage<OrderDetail> pageList = orderDetailService.page(page, queryWrapper);

		List<OrderDetail> records = pageList.getRecords();
		for (OrderDetail record : records) {
			String goodsSpecValues = record.getGoodsSpecValues();
			if(goodsSpecValues.startsWith(File.separator)){
				goodsSpecValues.replace(File.separator," ");
			}
		}
		pageList.setRecords(records);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param orderDetail
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<OrderDetail> add(@RequestBody OrderDetail orderDetail) {
		Result<OrderDetail> result = new Result<OrderDetail>();
		try {
			orderDetailService.save(orderDetail);
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
	 * @param orderDetail
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<OrderDetail> edit(@RequestBody OrderDetail orderDetail) {
		Result<OrderDetail> result = new Result<OrderDetail>();
		OrderDetail orderDetailEntity = orderDetailService.getById(orderDetail.getId());
		if(orderDetailEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = orderDetailService.updateById(orderDetail);
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
	public Result<OrderDetail> delete(@RequestParam(name="id",required=true) String id) {
		Result<OrderDetail> result = new Result<OrderDetail>();
		OrderDetail orderDetail = orderDetailService.getById(id);
		if(orderDetail==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = orderDetailService.removeById(id);
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
	public Result<OrderDetail> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<OrderDetail> result = new Result<OrderDetail>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.orderDetailService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<OrderDetail> queryById(@RequestParam(name="id",required=true) String id) {
		Result<OrderDetail> result = new Result<OrderDetail>();
		OrderDetail orderDetail = orderDetailService.getById(id);
		if(orderDetail==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(orderDetail);
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
      QueryWrapper<OrderDetail> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              OrderDetail orderDetail = JSON.parseObject(deString, OrderDetail.class);
              queryWrapper = QueryGenerator.initQueryWrapper(orderDetail, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<OrderDetail> pageList = orderDetailService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "订单详情列表");
      mv.addObject(NormalExcelConstants.CLASS, OrderDetail.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("订单详情列表数据", "导出人:Jeecg", "导出信息"));
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
              List<OrderDetail> listOrderDetails = ExcelImportUtil.importExcel(file.getInputStream(), OrderDetail.class, params);
              for (OrderDetail orderDetailExcel : listOrderDetails) {
                  orderDetailService.save(orderDetailExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listOrderDetails.size());
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
