package org.benben.modules.business.config.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.config.entity.Config;
import org.benben.modules.business.config.service.IConfigService;

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
 * @Description: 功能向导
 * @author： jeecg-boot
 * @date：   2019-07-02
 * @version： V1.0
 */
@RestController
@RequestMapping("/config/config")
@Slf4j
public class ConfigController {
	@Autowired
	private IConfigService configService;
	
	/**
	  * 分页列表查询
	 * @param config
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Config>> queryPageList(Config config,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Config>> result = new Result<IPage<Config>>();
		QueryWrapper<Config> queryWrapper = QueryGenerator.initQueryWrapper(config, req.getParameterMap());
		Page<Config> page = new Page<Config>(pageNo, pageSize);
		IPage<Config> pageList = configService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param config
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<Config> add(@RequestBody Config config) {
		Result<Config> result = new Result<Config>();
		try {
			QueryWrapper<Config> configQueryWrapper = new QueryWrapper<>();
			configQueryWrapper.eq("config_type",config.getConfigType());
			int count = configService.count(configQueryWrapper);
			if(count!=0){
				result.error500("操作失败,已有该类型的向导内容，请勿重复添加");
				return result;
			}
			configService.save(config);
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
	 * @param config
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Config> edit(@RequestBody Config config) {
		Result<Config> result = new Result<Config>();
		Config configEntity = configService.getById(config.getId());
		if(configEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = configService.updateById(config);
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
	public Result<Config> delete(@RequestParam(name="id",required=true) String id) {
		Result<Config> result = new Result<Config>();
		Config config = configService.getById(id);
		if(config==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = configService.removeById(id);
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
	public Result<Config> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Config> result = new Result<Config>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.configService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<Config> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Config> result = new Result<Config>();
		Config config = configService.getById(id);
		if(config==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(config);
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
      QueryWrapper<Config> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Config config = JSON.parseObject(deString, Config.class);
              queryWrapper = QueryGenerator.initQueryWrapper(config, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Config> pageList = configService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "功能向导列表");
      mv.addObject(NormalExcelConstants.CLASS, Config.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("功能向导列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Config> listConfigs = ExcelImportUtil.importExcel(file.getInputStream(), Config.class, params);
              for (Config configExcel : listConfigs) {
                  configService.save(configExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listConfigs.size());
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



	 /**
	  * @return
	  */
	 @GetMapping(value = "/queryVersion")
	 public Result<IPage<Config>> checkVersion( @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												@RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		 Result<IPage<Config>> result = new Result<IPage<Config>>();
		 Page<Config> page = new Page<Config>(pageNo, pageSize);
		 QueryWrapper<Config> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("config_name","version");
		 IPage<Config> pageList = configService.page(page,queryWrapper);
		 result.setSuccess(true);
		 result.setResult(pageList);
		 return result;
	 }


	 /**
	  * @return
	  */
	 @GetMapping(value = "/editVersion")
	 public Result<Config> editVersion() {
		 Result<Config> result = new Result<Config>();

		 QueryWrapper<Config> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("config_name","version");
		 Config config = configService.getOne(queryWrapper);
		 result.setSuccess(true);
		 result.setResult(config);
		 return result;
	 }
}
