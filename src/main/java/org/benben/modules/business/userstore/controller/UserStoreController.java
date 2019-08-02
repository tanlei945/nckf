package org.benben.modules.business.userstore.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.benben.common.api.vo.Result;
import org.benben.common.system.query.QueryGenerator;
import org.benben.common.util.PageUtil;
import org.benben.common.util.oConvertUtils;
import org.benben.modules.business.store.service.IStoreService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.benben.modules.business.userstore.entity.UserStore;
import org.benben.modules.business.userstore.service.IUserStoreService;
import org.benben.modules.business.userstore.vo.UserStoreVo;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

 /**
 * @Title: Controller
 * @Description: 骑手信息
 * @author： jeecg-boot
 * @date：   2019-04-25
 * @version： V1.0
 */
@RestController
@RequestMapping("/userStore/userStore")
@Slf4j
public class UserStoreController {
	@Autowired
	private IUserStoreService userStoreService;
	@Autowired
	private IStoreService storeService;
	
	/**
	  * 分页列表查询
	 * @param idCard
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<UserStoreVo>> queryPageList(@RequestParam String idCard,
													@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
													@RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
		Result<IPage<UserStoreVo>> result = new Result<IPage<UserStoreVo>>();
		//QueryWrapper<UserStore> queryWrapper = QueryGenerator.initQueryWrapper(userStore, req.getParameterMap());
		QueryWrapper<UserStore> queryWrapper = new QueryWrapper<>();
		if(StringUtils.isNotBlank(idCard)){
			queryWrapper.like("id_card",idCard);
		}
		queryWrapper.orderByAsc("complete_flag");
		Page<UserStore> page = new Page<UserStore>(pageNo, pageSize);
		IPage<UserStore> pageList = userStoreService.page(page, queryWrapper);
		List<UserStore> records = pageList.getRecords();
		List<UserStoreVo> userStoreVoList = new ArrayList<>();
		for (UserStore record : records) {
			UserStoreVo userStoreVo = new UserStoreVo();
			BeanUtils.copyProperties(record,userStoreVo);
			userStoreVo.setMobile(userService.getById(record.getUserId()).getMobile());
			userStoreVo.setStoreName(storeService.getById(record.getStoreId()).getStoreName());
			userStoreVoList.add(userStoreVo);
		}
		List list = PageUtil.startPage(userStoreVoList,pageNo,pageSize);

		IPage<UserStoreVo> iPage = new Page<>();
		iPage.setRecords(list);
		iPage.setSize(list.size());
		//iPage.setCurrent();


		result.setSuccess(true);
		result.setResult(iPage);
		return result;
	}
	
	/**
	  *   添加
	 * @param userStore
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<UserStore> add(@RequestBody UserStore userStore) {
		Result<UserStore> result = new Result<UserStore>();
		try {
			userStoreService.save(userStore);
			result.success("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			result.error500("操作失败");
		}
		return result;
	}

	@Autowired
	private IUserService userService;
	/**
	  *  编辑
	 * @param userStore
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<UserStore> edit(@RequestBody UserStore userStore) {
		Result<UserStore> result = new Result<UserStore>();
		UserStore userStoreEntity = userStoreService.getById(userStore.getId());
		String userId = userStore.getUserId();
		User user = userService.getById(userId);
		user.setStoreId(userStore.getStoreId());
		userService.saveOrUpdate(user);
		if(userStoreEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userStoreService.updateById(userStore);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}



	 //修改审核状态为成功接口
	 @GetMapping(value = "/changeRiderStatusOk")
	 public Result<UserStore> changeRiderStatus(@RequestParam(name = "riderId") String riderId){
		 Result<UserStore> result = new Result<UserStore>();
		 QueryWrapper<UserStore> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("user_id",riderId);
		 UserStore userStore = new UserStore();
		 userStore.setCompleteFlag("2");
		 boolean ok = userStoreService.update(userStore,queryWrapper);
		 if(ok){
			 result.success("修改成功");
		 }else{
		 	result.error500("修改失败");
		 }

		 return result;
	 }




	 //修改审核状态为未通过审核接口
	 @GetMapping(value = "/changeRiderStatusNo")
	 public Result<UserStore> changeRiderStatusNo(@RequestParam(name = "riderId") String riderId){
		 Result<UserStore> result = new Result<UserStore>();
		 QueryWrapper<UserStore> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("user_id",riderId);
		 UserStore userStore = new UserStore();
		 userStore.setCompleteFlag("1");
		 boolean ok = userStoreService.update(userStore,queryWrapper);
		 if(ok){
			 result.success("修改成功");
		 }else{
			 result.error500("修改失败");
		 }

		 return result;
	 }
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<UserStore> delete(@RequestParam(name="id",required=true) String id) {
		Result<UserStore> result = new Result<UserStore>();
		UserStore userStore = userStoreService.getById(id);
		if(userStore==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = userStoreService.removeById(id);
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
	public Result<UserStore> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<UserStore> result = new Result<UserStore>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.userStoreService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<UserStore> queryById(@RequestParam(name="id",required=true) String id) {
		Result<UserStore> result = new Result<UserStore>();
		UserStore userStore = userStoreService.getById(id);
		if(userStore==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(userStore);
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
      QueryWrapper<UserStore> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              UserStore userStore = JSON.parseObject(deString, UserStore.class);
              queryWrapper = QueryGenerator.initQueryWrapper(userStore, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<UserStore> pageList = userStoreService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "骑手信息列表");
      mv.addObject(NormalExcelConstants.CLASS, UserStore.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("骑手信息列表数据", "导出人:Jeecg", "导出信息"));
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
              List<UserStore> listUserStores = ExcelImportUtil.importExcel(file.getInputStream(), UserStore.class, params);
              for (UserStore userStoreExcel : listUserStores) {
                  userStoreService.save(userStoreExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listUserStores.size());
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
