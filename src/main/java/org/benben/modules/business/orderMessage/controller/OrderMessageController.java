package org.benben.modules.business.orderMessage.controller;

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
import org.benben.modules.business.orderMessage.entity.OrderMessage;
import org.benben.modules.business.orderMessage.service.IOrderMessageService;

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
 * @Description: 订单消息
 * @author： jeecg-boot
 * @date：   2019-07-05
 * @version： V1.0
 */
@RestController
@RequestMapping("/orderMessage/orderMessage")
@Slf4j
public class OrderMessageController {
	@Autowired
	private IOrderMessageService orderMessageService;
	
	/**
	  * 分页列表查询
	 * @param orderMessage
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<OrderMessage>> queryPageList(OrderMessage orderMessage,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<OrderMessage>> result = new Result<IPage<OrderMessage>>();
		QueryWrapper<OrderMessage> queryWrapper = QueryGenerator.initQueryWrapper(orderMessage, req.getParameterMap());
		Page<OrderMessage> page = new Page<OrderMessage>(pageNo, pageSize);
		IPage<OrderMessage> pageList = orderMessageService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param orderMessage
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<OrderMessage> add(@RequestBody OrderMessage orderMessage) {
		Result<OrderMessage> result = new Result<OrderMessage>();
		try {
			orderMessageService.save(orderMessage);
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
	 * @param orderMessage
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<OrderMessage> edit(@RequestBody OrderMessage orderMessage) {
		Result<OrderMessage> result = new Result<OrderMessage>();
		OrderMessage orderMessageEntity = orderMessageService.getById(orderMessage.getId());
		if(orderMessageEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = orderMessageService.updateById(orderMessage);
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
	public Result<OrderMessage> delete(@RequestParam(name="id",required=true) String id) {
		Result<OrderMessage> result = new Result<OrderMessage>();
		OrderMessage orderMessage = orderMessageService.getById(id);
		if(orderMessage==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = orderMessageService.removeById(id);
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
	public Result<OrderMessage> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<OrderMessage> result = new Result<OrderMessage>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.orderMessageService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<OrderMessage> queryById(@RequestParam(name="id",required=true) String id) {
		Result<OrderMessage> result = new Result<OrderMessage>();
		OrderMessage orderMessage = orderMessageService.getById(id);
		if(orderMessage==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(orderMessage);
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
      QueryWrapper<OrderMessage> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              OrderMessage orderMessage = JSON.parseObject(deString, OrderMessage.class);
              queryWrapper = QueryGenerator.initQueryWrapper(orderMessage, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<OrderMessage> pageList = orderMessageService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "订单消息列表");
      mv.addObject(NormalExcelConstants.CLASS, OrderMessage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("订单消息列表数据", "导出人:Jeecg", "导出信息"));
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
              List<OrderMessage> listOrderMessages = ExcelImportUtil.importExcel(file.getInputStream(), OrderMessage.class, params);
              for (OrderMessage orderMessageExcel : listOrderMessages) {
                  orderMessageService.save(orderMessageExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listOrderMessages.size());
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
