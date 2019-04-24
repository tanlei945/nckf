package org.benben.modules.business.store.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.jdbc.Null;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.store.entity.Store;
import org.benben.modules.business.store.service.IStoreService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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
 * @Description: 店面
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@RestController
@RequestMapping("/store/store")
@Slf4j
@Api(tags = {"门店管理接口"})
public class StoreController {
	@Autowired
	private IStoreService storeService;
	
	/**
	  * 分页列表查询
	 * @param store
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Store>> queryPageList(Store store,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Store>> result = new Result<IPage<Store>>();
		QueryWrapper<Store> queryWrapper = QueryGenerator.initQueryWrapper(store, req.getParameterMap());
		Page<Store> page = new Page<Store>(pageNo, pageSize);
		IPage<Store> pageList = storeService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param store
	 * @return
	 */
	@PostMapping(value = "/add")
	@ApiOperation("添加门店")
	public Result<Store> add(@RequestBody Store store) {
		Result<Store> result = new Result<Store>();
		try {
			storeService.save(store);
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
	 * @param store
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Store> edit(@RequestBody Store store) {
		Result<Store> result = new Result<Store>();
		Store storeEntity = storeService.getById(store.getId());
		if(storeEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = storeService.updateById(store);
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
	public Result<Store> delete(@RequestParam(name="id",required=true) String id) {
		Result<Store> result = new Result<Store>();
		Store store = storeService.getById(id);
		if(store==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = storeService.removeById(id);
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
	public Result<Store> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Store> result = new Result<Store>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.storeService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<Store> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Store> result = new Result<Store>();
		Store store = storeService.getById(id);
		if(store==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(store);
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
      QueryWrapper<Store> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Store store = JSON.parseObject(deString, Store.class);
              queryWrapper = QueryGenerator.initQueryWrapper(store, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Store> pageList = storeService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "店面列表");
      mv.addObject(NormalExcelConstants.CLASS, Store.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("店面列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Store> listStores = ExcelImportUtil.importExcel(file.getInputStream(), Store.class, params);
              for (Store storeExcel : listStores) {
                  storeService.save(storeExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listStores.size());
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

  @RequestMapping(value = "/query_By_Distance",method = RequestMethod.GET)
  @ApiOperation("查询用户离店铺距离")
	public RestResponseBean queryByDistance(double lng, double lat){
	  List<Store> storeList = null;
	  try {
	  		storeList = storeService.queryByDistance(lng, lat);
		  return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), storeList);
	  } catch (Exception e) {
		  e.printStackTrace();
		  return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
	  }

  }

	 @RequestMapping(value = "/queryScope_By_id",method = RequestMethod.GET)
	 @ApiOperation("查询收货地址距离是否超过限制")
	 public RestResponseBean queryScopeById(String storeId, double lng, double lat){
		 Boolean aBoolean = null;
		 try {
			 aBoolean = storeService.queryScopeById(storeId,lng,lat);
			 return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), aBoolean);
		 } catch (Exception e) {
			 e.printStackTrace();
			 return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
		 }

	 }
}
