package org.benben.modules.business.accountbill.controller;

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
import org.benben.modules.business.accountbill.entity.AccountBill;
import org.benben.modules.business.accountbill.service.IAccountBillService;

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
 * @Description: 账单
 * @author： jeecg-boot
 * @date：   2019-04-24
 * @version： V1.0
 */
@RestController
@RequestMapping("/accountbill/accountBill")
@Slf4j
public class AccountBillController {
	@Autowired
	private IAccountBillService accountBillService;
	
	/**
	  * 分页列表查询
	 * @param accountBill
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<AccountBill>> queryPageList(AccountBill accountBill,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<AccountBill>> result = new Result<IPage<AccountBill>>();
		QueryWrapper<AccountBill> queryWrapper = QueryGenerator.initQueryWrapper(accountBill, req.getParameterMap());
		Page<AccountBill> page = new Page<AccountBill>(pageNo, pageSize);
		IPage<AccountBill> pageList = accountBillService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param accountBill
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<AccountBill> add(@RequestBody AccountBill accountBill) {
		Result<AccountBill> result = new Result<AccountBill>();
		try {
			accountBillService.save(accountBill);
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
	 * @param accountBill
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<AccountBill> edit(@RequestBody AccountBill accountBill) {
		Result<AccountBill> result = new Result<AccountBill>();
		AccountBill accountBillEntity = accountBillService.getById(accountBill.getId());
		if(accountBillEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = accountBillService.updateById(accountBill);
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
	public Result<AccountBill> delete(@RequestParam(name="id",required=true) String id) {
		Result<AccountBill> result = new Result<AccountBill>();
		AccountBill accountBill = accountBillService.getById(id);
		if(accountBill==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = accountBillService.removeById(id);
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
	public Result<AccountBill> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<AccountBill> result = new Result<AccountBill>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.accountBillService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<AccountBill> queryById(@RequestParam(name="id",required=true) String id) {
		Result<AccountBill> result = new Result<AccountBill>();
		AccountBill accountBill = accountBillService.getById(id);
		if(accountBill==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(accountBill);
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
      QueryWrapper<AccountBill> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              AccountBill accountBill = JSON.parseObject(deString, AccountBill.class);
              queryWrapper = QueryGenerator.initQueryWrapper(accountBill, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<AccountBill> pageList = accountBillService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "账单列表");
      mv.addObject(NormalExcelConstants.CLASS, AccountBill.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("账单列表数据", "导出人:Jeecg", "导出信息"));
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
              List<AccountBill> listAccountBills = ExcelImportUtil.importExcel(file.getInputStream(), AccountBill.class, params);
              for (AccountBill accountBillExcel : listAccountBills) {
                  accountBillService.save(accountBillExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listAccountBills.size());
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
