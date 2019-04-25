package org.benben.modules.business.withdraw.controller;

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
import org.benben.modules.business.withdraw.entity.Withdraw;
import org.benben.modules.business.withdraw.service.IWithdrawService;

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
 * @Description: 提现
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
@RestController
@RequestMapping("/withdraw/withdraw")
@Slf4j
public class WithdrawController {
	@Autowired
	private IWithdrawService withdrawService;
	
	/**
	  * 分页列表查询
	 * @param withdraw
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Withdraw>> queryPageList(Withdraw withdraw,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Withdraw>> result = new Result<IPage<Withdraw>>();
		QueryWrapper<Withdraw> queryWrapper = QueryGenerator.initQueryWrapper(withdraw, req.getParameterMap());
		Page<Withdraw> page = new Page<Withdraw>(pageNo, pageSize);
		IPage<Withdraw> pageList = withdrawService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param withdraw
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<Withdraw> add(@RequestBody Withdraw withdraw) {
		Result<Withdraw> result = new Result<Withdraw>();
		try {
			withdrawService.save(withdraw);
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
	 * @param withdraw
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Withdraw> edit(@RequestBody Withdraw withdraw) {
		Result<Withdraw> result = new Result<Withdraw>();
		Withdraw withdrawEntity = withdrawService.getById(withdraw.getId());
		if(withdrawEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = withdrawService.updateById(withdraw);
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
	public Result<Withdraw> delete(@RequestParam(name="id",required=true) String id) {
		Result<Withdraw> result = new Result<Withdraw>();
		Withdraw withdraw = withdrawService.getById(id);
		if(withdraw==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = withdrawService.removeById(id);
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
	public Result<Withdraw> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Withdraw> result = new Result<Withdraw>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.withdrawService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<Withdraw> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Withdraw> result = new Result<Withdraw>();
		Withdraw withdraw = withdrawService.getById(id);
		if(withdraw==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(withdraw);
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
      QueryWrapper<Withdraw> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Withdraw withdraw = JSON.parseObject(deString, Withdraw.class);
              queryWrapper = QueryGenerator.initQueryWrapper(withdraw, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Withdraw> pageList = withdrawService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "提现列表");
      mv.addObject(NormalExcelConstants.CLASS, Withdraw.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("提现列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Withdraw> listWithdraws = ExcelImportUtil.importExcel(file.getInputStream(), Withdraw.class, params);
              for (Withdraw withdrawExcel : listWithdraws) {
                  withdrawService.save(withdrawExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listWithdraws.size());
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
