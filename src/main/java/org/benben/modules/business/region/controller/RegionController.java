package org.benben.modules.business.region.controller;

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
import org.benben.modules.business.region.entity.Region;
import org.benben.modules.business.region.service.IRegionService;

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
 * @Description: 城市列表
 * @author： jeecg-boot
 * @date：   2019-07-03
 * @version： V1.0
 */
@RestController
@RequestMapping("/region/region")
@Slf4j
public class RegionController {
	@Autowired
	private IRegionService regionService;
	
	/**
	  * 分页列表查询
	 * @param region
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Region>> queryPageList(Region region,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Region>> result = new Result<IPage<Region>>();
		QueryWrapper<Region> queryWrapper = QueryGenerator.initQueryWrapper(region, req.getParameterMap());
		Page<Region> page = new Page<Region>(pageNo, pageSize);
		IPage<Region> pageList = regionService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param region
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<Region> add(@RequestBody Region region) {
		Result<Region> result = new Result<Region>();
		try {
			regionService.save(region);
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
	 * @param region
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Region> edit(@RequestBody Region region) {
		Result<Region> result = new Result<Region>();
		Region regionEntity = regionService.getById(region.getId());
		if(regionEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = regionService.updateById(region);
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
	public Result<Region> delete(@RequestParam(name="id",required=true) String id) {
		Result<Region> result = new Result<Region>();
		Region region = regionService.getById(id);
		if(region==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = regionService.removeById(id);
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
	public Result<Region> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Region> result = new Result<Region>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.regionService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<Region> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Region> result = new Result<Region>();
		Region region = regionService.getById(id);
		if(region==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(region);
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
      QueryWrapper<Region> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Region region = JSON.parseObject(deString, Region.class);
              queryWrapper = QueryGenerator.initQueryWrapper(region, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Region> pageList = regionService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "城市列表列表");
      mv.addObject(NormalExcelConstants.CLASS, Region.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("城市列表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Region> listRegions = ExcelImportUtil.importExcel(file.getInputStream(), Region.class, params);
              for (Region regionExcel : listRegions) {
                  regionService.save(regionExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listRegions.size());
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
