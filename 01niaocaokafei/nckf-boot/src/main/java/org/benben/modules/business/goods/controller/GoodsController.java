package org.benben.modules.business.goods.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.goods.entity.Goods;
import org.benben.modules.business.goods.entity.SpecDict;
import org.benben.modules.business.goods.service.IGoodsService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

 /**
 * @Title: Controller
 * @Description: 商品列表
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@RestController
@RequestMapping("/goods/goods")
@Slf4j
public class GoodsController {
	@Autowired
	private IGoodsService goodsService;

	@Autowired
	private ISysUserService sysUserService;

	/**
	  * 分页列表查询
	 * @param goods
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Goods>> queryGoodsPageList(Goods goods,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Goods>> result = new Result<IPage<Goods>>();
			QueryWrapper<Goods> queryWrapper = QueryGenerator.initQueryWrapper(goods, req.getParameterMap());
			String s = sysUserService.querySuperAdmin();
			if(s!=null&&!"".equals(s)){
				queryWrapper.eq("belong_id",s);
		}
		Page<Goods> page = new Page<Goods>(pageNo, pageSize);
		IPage<Goods> pageList = goodsService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 *
	  *   添加
	 * @param goods
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<Goods> add(@RequestBody Goods goods) {
		Result<Goods> result = new Result<Goods>();
		SysUser sysuser = (SysUser) SecurityUtils.getSubject().getPrincipal();
		try {
			goods.setBelongId(sysuser.getId());
			goodsService.save(goods);
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
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<Goods> edit(@RequestBody JSONObject jsonObject) {
		Object selectedRole = jsonObject.get("selectedroles");
		System.out.println(selectedRole.toString());
		List<String> selectedRole1 = (List<String>) selectedRole;
		Goods goods = JSON.parseObject(jsonObject.toJSONString(), Goods.class);
		System.out.println(goods);
		goodsService.editGoodsWithSpec(selectedRole1,goods.getId());

		Result<Goods> result = new Result<Goods>();
		Goods goodsEntity = goodsService.getById(goods.getId());
		if(goodsEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = goodsService.updateById(goods);
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
	public Result<Goods> delete(@RequestParam(name="id",required=true) String id) {
		Result<Goods> result = new Result<Goods>();
		Goods goods = goodsService.getById(id);
		if(goods==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = goodsService.removeById(id);
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
	public Result<Goods> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Goods> result = new Result<Goods>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.goodsService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<Goods> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Goods> result = new Result<Goods>();
		Goods goods = goodsService.getById(id);
		if(goods==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(goods);
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
      QueryWrapper<Goods> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Goods goods = JSON.parseObject(deString, Goods.class);
              queryWrapper = QueryGenerator.initQueryWrapper(goods, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Goods> pageList = goodsService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "商品列表列表");
      mv.addObject(NormalExcelConstants.CLASS, Goods.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("商品列表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Goods> listGoodss = ExcelImportUtil.importExcel(file.getInputStream(), Goods.class, params);
              for (Goods goodsExcel : listGoodss) {
                  goodsService.save(goodsExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listGoodss.size());
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
	 @GetMapping("query_all_spec")
	 @ApiOperation(value="查询所有商品规格",tags = {"门店管理接口"})
	 public RestResponseBean queryallspec(){
		 try {
			 List<SpecDict> queryallspec = goodsService.queryallspec();
			 return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), queryallspec);
		 } catch (Exception e) {
			 e.printStackTrace();
			 return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
		 }
	 }
}
