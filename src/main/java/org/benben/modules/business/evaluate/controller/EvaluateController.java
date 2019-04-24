package org.benben.modules.business.evaluate.controller;

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
import org.benben.modules.business.evaluate.entity.Evaluate;
import org.benben.modules.business.evaluate.service.IEvaluateService;

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
 * @Description: 评价表
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@RestController
@RequestMapping("/evaluate/evaluate")
@Slf4j
public class EvaluateController {
	@Autowired
	private IEvaluateService evaluateService;
	
	/**
	  * 分页列表查询
	 * @param evaluate
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Evaluate>> queryPageList(Evaluate evaluate,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Evaluate>> result = new Result<IPage<Evaluate>>();
		QueryWrapper<Evaluate> queryWrapper = QueryGenerator.initQueryWrapper(evaluate, req.getParameterMap());
		Page<Evaluate> page = new Page<Evaluate>(pageNo, pageSize);
		IPage<Evaluate> pageList = evaluateService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param evaluate
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<Evaluate> add(@RequestBody Evaluate evaluate) {
		Result<Evaluate> result = new Result<Evaluate>();
		try {
			evaluateService.save(evaluate);
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
	 * @param evaluate
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Evaluate> edit(@RequestBody Evaluate evaluate) {
		Result<Evaluate> result = new Result<Evaluate>();
		Evaluate evaluateEntity = evaluateService.getById(evaluate.getId());
		if(evaluateEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = evaluateService.updateById(evaluate);
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
	public Result<Evaluate> delete(@RequestParam(name="id",required=true) String id) {
		Result<Evaluate> result = new Result<Evaluate>();
		Evaluate evaluate = evaluateService.getById(id);
		if(evaluate==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = evaluateService.removeById(id);
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
	public Result<Evaluate> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Evaluate> result = new Result<Evaluate>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.evaluateService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<Evaluate> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Evaluate> result = new Result<Evaluate>();
		Evaluate evaluate = evaluateService.getById(id);
		if(evaluate==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(evaluate);
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
      QueryWrapper<Evaluate> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Evaluate evaluate = JSON.parseObject(deString, Evaluate.class);
              queryWrapper = QueryGenerator.initQueryWrapper(evaluate, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Evaluate> pageList = evaluateService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "评价表列表");
      mv.addObject(NormalExcelConstants.CLASS, Evaluate.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("评价表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Evaluate> listEvaluates = ExcelImportUtil.importExcel(file.getInputStream(), Evaluate.class, params);
              for (Evaluate evaluateExcel : listEvaluates) {
                  evaluateService.save(evaluateExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listEvaluates.size());
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
