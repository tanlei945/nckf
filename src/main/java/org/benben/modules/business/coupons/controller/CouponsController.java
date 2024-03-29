package org.benben.modules.business.coupons.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.coupons.entity.Coupons;
import org.benben.modules.business.coupons.service.ICouponsService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.benben.modules.business.store.entity.Store;
import org.benben.modules.business.store.service.IStoreService;
import org.benben.modules.system.entity.SysUser;
import org.benben.modules.system.service.ISysUserService;
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
 * @Description: 优惠券
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@RestController
@RequestMapping("/coupons/coupons")
@Slf4j
public class CouponsController {
	@Autowired
	private ICouponsService couponsService;
	 @Autowired
	 private ISysUserService sysUserService;
	 @Autowired
	 private IStoreService storeService;
	/**
	  * 分页列表查询
	 * @param coupons
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Coupons>> queryPageList(Coupons coupons,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Coupons>> result = new Result<IPage<Coupons>>();
		QueryWrapper<Coupons> queryWrapper = QueryGenerator.initQueryWrapper(coupons, req.getParameterMap());
		Page<Coupons> page = new Page<Coupons>(pageNo, pageSize);
		IPage<Coupons> pageList = couponsService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param coupons
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<Coupons> add(@RequestBody Coupons coupons) {
		Result<Coupons> result = new Result<Coupons>();
		try {
		QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
		String s = sysUserService.querySuperAdmin();
		SysUser sysuser = (SysUser) SecurityUtils.getSubject().getPrincipal();
		if(s==null||"".equals(s)){
			queryWrapper.eq("belong_id",sysuser.getId());
		}else{
			coupons.setCommonFlag("1");
		}
		Store store = storeService.getOne(queryWrapper);
		coupons.setStoreId(store.getId());
		couponsService.save(coupons);
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
	 * @param coupons
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Coupons> edit(@RequestBody Coupons coupons) {
		Result<Coupons> result = new Result<Coupons>();
		Coupons couponsEntity = couponsService.getById(coupons.getId());
		if(couponsEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = couponsService.updateById(coupons);
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
	public Result<Coupons> delete(@RequestParam(name="id",required=true) String id) {
		Result<Coupons> result = new Result<Coupons>();
		Coupons coupons = couponsService.getById(id);
		if(coupons==null) {
			result.error500("未找到对应实体");
		}else {
			coupons.setDelFlag("1");
			boolean ok = couponsService.saveOrUpdate(coupons);
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
	public Result<Coupons> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Coupons> result = new Result<Coupons>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.couponsService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<Coupons> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Coupons> result = new Result<Coupons>();
		Coupons coupons = couponsService.getById(id);
		if(coupons==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(coupons);
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
      QueryWrapper<Coupons> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Coupons coupons = JSON.parseObject(deString, Coupons.class);
              queryWrapper = QueryGenerator.initQueryWrapper(coupons, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Coupons> pageList = couponsService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "优惠券列表");
      mv.addObject(NormalExcelConstants.CLASS, Coupons.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("优惠券列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Coupons> listCouponss = ExcelImportUtil.importExcel(file.getInputStream(), Coupons.class, params);
              for (Coupons couponsExcel : listCouponss) {
                  couponsService.save(couponsExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listCouponss.size());
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
