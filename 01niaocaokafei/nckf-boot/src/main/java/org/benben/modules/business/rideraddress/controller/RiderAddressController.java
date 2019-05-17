package org.benben.modules.business.rideraddress.controller;

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
import org.benben.modules.business.rideraddress.entity.RiderAddress;
import org.benben.modules.business.rideraddress.service.IRiderAddressService;

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
 * @Description: 骑手位置表
 * @author： jeecg-boot
 * @date：   2019-04-27
 * @version： V1.0
 */
@RestController
@RequestMapping("/rideraddress/riderAddress")
@Slf4j
public class RiderAddressController {
	@Autowired
	private IRiderAddressService riderAddressService;
	
	/**
	  * 分页列表查询
	 * @param riderAddress
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/queryiderAddress")
	public Result<IPage<RiderAddress>> queryiderAddress(RiderAddress riderAddress,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<RiderAddress>> result = new Result<IPage<RiderAddress>>();
		QueryWrapper<RiderAddress> queryWrapper = QueryGenerator.initQueryWrapper(riderAddress, req.getParameterMap());
		Page<RiderAddress> page = new Page<RiderAddress>(pageNo, pageSize);
		IPage<RiderAddress> pageList = riderAddressService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param riderAddress
	 * @return
	 */
	@PostMapping(value = "/addRiderAddress")
	public Result<RiderAddress> addRiderAddress(@RequestBody RiderAddress riderAddress) {
		Result<RiderAddress> result = new Result<RiderAddress>();
		try {
			riderAddressService.save(riderAddress);
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
	 * @param riderAddress
	 * @return
	 */
	@PutMapping(value = "/editRiderAddress")
	public Result<RiderAddress> editRiderAddress(@RequestBody RiderAddress riderAddress) {
		Result<RiderAddress> result = new Result<RiderAddress>();
		RiderAddress riderAddressEntity = riderAddressService.getById(riderAddress.getId());
		if(riderAddressEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = riderAddressService.updateById(riderAddress);
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
	@DeleteMapping(value = "/deleteRiderAddress")
	public Result<RiderAddress> deleteRiderAddress(@RequestParam(name="id",required=true) String id) {
		Result<RiderAddress> result = new Result<RiderAddress>();
		RiderAddress riderAddress = riderAddressService.getById(id);
		if(riderAddress==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = riderAddressService.removeById(id);
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
	@DeleteMapping(value = "/deleteBatchRiderAddress")
	public Result<RiderAddress> deleteBatchRiderAddress(@RequestParam(name="ids",required=true) String ids) {
		Result<RiderAddress> result = new Result<RiderAddress>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.riderAddressService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryRiderAddressById")
	public Result<RiderAddress> queryRiderAddressById(@RequestParam(name="id",required=true) String id) {
		Result<RiderAddress> result = new Result<RiderAddress>();
		RiderAddress riderAddress = riderAddressService.getById(id);
		if(riderAddress==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(riderAddress);
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
      QueryWrapper<RiderAddress> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              RiderAddress riderAddress = JSON.parseObject(deString, RiderAddress.class);
              queryWrapper = QueryGenerator.initQueryWrapper(riderAddress, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<RiderAddress> pageList = riderAddressService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "骑手位置表列表");
      mv.addObject(NormalExcelConstants.CLASS, RiderAddress.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("骑手位置表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<RiderAddress> listRiderAddresss = ExcelImportUtil.importExcel(file.getInputStream(), RiderAddress.class, params);
              for (RiderAddress riderAddressExcel : listRiderAddresss) {
                  riderAddressService.save(riderAddressExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listRiderAddresss.size());
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
