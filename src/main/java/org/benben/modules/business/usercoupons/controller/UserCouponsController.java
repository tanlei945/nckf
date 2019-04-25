package org.benben.modules.business.usercoupons.controller;

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
import org.benben.modules.business.usercoupons.entity.UserCoupons;
import org.benben.modules.business.usercoupons.service.IUserCouponsService;

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
 * @Description: 用户优惠券
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
@RestController
@RequestMapping("/usercoupons/userCoupons")
@Slf4j
public class UserCouponsController {
	@Autowired
	private IUserCouponsService userCouponsService;
	
	/**
	  * 分页列表查询
	 * @param userCoupons
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<UserCoupons>> queryPageList(UserCoupons userCoupons,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<UserCoupons>> result = new Result<IPage<UserCoupons>>();
		QueryWrapper<UserCoupons> queryWrapper = QueryGenerator.initQueryWrapper(userCoupons, req.getParameterMap());
		Page<UserCoupons> page = new Page<UserCoupons>(pageNo, pageSize);
		IPage<UserCoupons> pageList = userCouponsService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param userCoupons
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<UserCoupons> add(@RequestBody UserCoupons userCoupons) {
		Result<UserCoupons> result = new Result<UserCoupons>();
		try {
			userCouponsService.save(userCoupons);
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
	 * @param userCoupons
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<UserCoupons> edit(@RequestBody UserCoupons userCoupons) {
		Result<UserCoupons> result = new Result<UserCoupons>();
		UserCoupons userCouponsEntity = userCouponsService.getById(userCoupons.getId());
		if(userCouponsEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userCouponsService.updateById(userCoupons);
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
	public Result<UserCoupons> delete(@RequestParam(name="id",required=true) String id) {
		Result<UserCoupons> result = new Result<UserCoupons>();
		UserCoupons userCoupons = userCouponsService.getById(id);
		if(userCoupons==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userCouponsService.removeById(id);
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
	public Result<UserCoupons> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<UserCoupons> result = new Result<UserCoupons>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.userCouponsService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<UserCoupons> queryById(@RequestParam(name="id",required=true) String id) {
		Result<UserCoupons> result = new Result<UserCoupons>();
		UserCoupons userCoupons = userCouponsService.getById(id);
		if(userCoupons==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(userCoupons);
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
      QueryWrapper<UserCoupons> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              UserCoupons userCoupons = JSON.parseObject(deString, UserCoupons.class);
              queryWrapper = QueryGenerator.initQueryWrapper(userCoupons, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<UserCoupons> pageList = userCouponsService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "用户优惠券列表");
      mv.addObject(NormalExcelConstants.CLASS, UserCoupons.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("用户优惠券列表数据", "导出人:Jeecg", "导出信息"));
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
              List<UserCoupons> listUserCouponss = ExcelImportUtil.importExcel(file.getInputStream(), UserCoupons.class, params);
              for (UserCoupons userCouponsExcel : listUserCouponss) {
                  userCouponsService.save(userCouponsExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listUserCouponss.size());
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
