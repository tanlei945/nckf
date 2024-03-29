package org.benben.modules.business.category.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.category.entity.Category;
import org.benben.modules.business.category.service.ICategoryService;
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


@RestController
@RequestMapping("/category/category")
@Slf4j
public class CategoryController {
	@Autowired
	private ICategoryService categoryService;
	

	@GetMapping(value = "/list")
	public Result<IPage<Category>> queryPageList(Category category,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<Category>> result = new Result<IPage<Category>>();
		QueryWrapper<Category> queryWrapper = QueryGenerator.initQueryWrapper(category, req.getParameterMap());
		Page<Category> page = new Page<Category>(pageNo, pageSize);
		IPage<Category> pageList = categoryService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	@ApiOperation(value="查询商品种类",tags = {"门店管理接口"})
	@GetMapping("/getCategory")
	public RestResponseBean getCategory(){
		try {
			List<Category> category = categoryService.getCategory();
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), category);
		} catch (Exception e) {
			e.printStackTrace();
			return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
		}
	}

	@PostMapping(value = "/addCateGory")
	public Result<Category> addCateGory(@RequestBody Category category) {
		Result<Category> result = new Result<Category>();
		try {
			categoryService.save(category);
			result.success("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			result.error500("操作失败");
		}
		return result;
	}
	

	@PutMapping(value = "/edit")
	public Result<Category> edit(@RequestBody Category category) {
		Result<Category> result = new Result<Category>();
		Category categoryEntity = categoryService.getById(category.getId());
		if(categoryEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = categoryService.updateById(category);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}
	

	@DeleteMapping(value = "/delete")
	public Result<Category> delete(@RequestParam(name="id",required=true) String id) {
		Result<Category> result = new Result<Category>();
		Category category = categoryService.getById(id);
		if(category==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = categoryService.removeById(id);
			if(ok) {
				result.success("删除成功!");
			}
		}
		
		return result;
	}
	

	@DeleteMapping(value = "/deleteBatch")
	public Result<Category> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Category> result = new Result<Category>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.categoryService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	

	@GetMapping(value = "/queryById")
	public Result<Category> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Category> result = new Result<Category>();
		Category category = categoryService.getById(id);
		if(category==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(category);
			result.setSuccess(true);
		}
		return result;
	}


  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
      // Step.1 组装查询条件
      QueryWrapper<Category> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Category category = JSON.parseObject(deString, Category.class);
              queryWrapper = QueryGenerator.initQueryWrapper(category, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Category> pageList = categoryService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "商品种类列表列表");
      mv.addObject(NormalExcelConstants.CLASS, Category.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("商品种类列表列表数据", "导出人:Jeecg", "导出信息"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
  }


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
              List<Category> listCategorys = ExcelImportUtil.importExcel(file.getInputStream(), Category.class, params);
              for (Category categoryExcel : listCategorys) {
                  categoryService.save(categoryExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listCategorys.size());
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

/*	 @ApiOperation("查询多级菜单")
	 @GetMapping("/find_category_byParentId")
	 public RestResponseBean findMenuByParentId(String parentid){
		 try {
			 List<Category> menuByParentId = categoryService.findMenuByParentId(parentid);
			 return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), menuByParentId);
		 } catch (Exception e) {
			 e.printStackTrace();
			 return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
		 }
	 }
	 @ApiOperation("查询菜单树")
	 @RequestMapping("/findtree")
	 public Result<List<CategoryTreeModel>> queryTreeList() {
		 List<CategoryTreeModel> list = null;
		 Result<List<CategoryTreeModel>> result = new Result<>();
		 try {
		 	list = categoryService.queryTreeList();
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 result.setResult(list);
		 return result;
  		//return  new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),  list);
	 }*/

}
