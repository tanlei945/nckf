package org.benben.modules.business.usercoupons.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.address.entity.Address;
import org.benben.modules.business.coupons.entity.Coupons;
import org.benben.modules.business.coupons.service.ICouponsService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.usercoupons.entity.UserCoupons;
import org.benben.modules.business.usercoupons.service.IUserCouponsService;
import org.benben.modules.shiro.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/userCoupons")
@Slf4j
@Api(tags = "优惠券")
public class RestUserCouponsController {
	@Autowired
	private IUserCouponsService userCouponsService;
	@Autowired
	private ICouponsService couponsService;

	/**
	 * showdoc
	 * @catalog 用户接口
	 * @title 个人优惠券查询
	 * @description 个人优惠券查询
	 * @method GET
	 * @url /nckf-boot/api/v1/userCoupons/list
	 * @param status 必填 String 优惠券状态(-1已过期 0 未使用 1已使用)
	 * @return {"code": 1,"data": {"current": 1,"pages": 0,"records": [],"searchCount": true,"size": 10,"total": 0},"msg": "操作成功","time": "1561014704334"}
	 * @return_param code String 响应状态
	 * @return_param data List 优惠券信息
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark 这里是备注信息
	 * @number 21
	 */
	@GetMapping(value = "/list")
	@ApiOperation(value = "个人优惠券列表", notes = "个人优惠券列表", tags = "优惠券")
	@ApiImplicitParams({@ApiImplicitParam(name = "pageNo", value = "当前页", dataType = "Integer", defaultValue = "1"),
						@ApiImplicitParam(name = "pageSize", value = "每页显示条数", dataType = "Integer", defaultValue = "10"),
						@ApiImplicitParam(name = "type", value = "0:失效  1:可用  2:已使用", dataType = "String", defaultValue = "10"),

	})
	public RestResponseBean list(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
								 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
								 @RequestParam(name = "type") String type) {

		User user = (User) LoginUser.getCurrentUser();
		if(user == null){
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

		switch(type){
			//可使用优惠券
			case "1" :
				QueryWrapper<UserCoupons> queryWrapper1 = new QueryWrapper<>();
				//未使用
				queryWrapper1.lambda().eq(UserCoupons::getUserId,user.getId()).eq(UserCoupons::getStatus,"0");
				Page<UserCoupons> page = new Page<UserCoupons>(pageNo, pageSize);
				IPage<UserCoupons> pageList = userCouponsService.page(page, queryWrapper1);
				List<UserCoupons> list1 = pageList.getRecords();
				//剔除未使用但已过期的优惠券
				if(list1!=null){
					for (UserCoupons userCoupons : list1) {
						Coupons coupons =couponsService.getById(userCoupons.getCouponsId());
						if(coupons != null){
							if(coupons.getUseEndTime().before(new Date())){
								list1.remove(userCoupons);
							}
						}
					}
				}
				//拿到couponsId去查询优惠券
				List<Coupons> couponsList1 = new ArrayList<>();
//				if(couponsList1 == null){
//					return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);
//				}
				for (UserCoupons userCoupons : list1) {
					Coupons coupons =couponsService.getById(userCoupons.getCouponsId());
					couponsList1.add(coupons);
				}

				IPage<Coupons> pageList1 = new Page<>(pageNo,pageSize);
				pageList1.setRecords(couponsList1);
				pageList1.setTotal((long)couponsList1.size());
				pageList1.setSize(pageList.getSize());
				pageList1.setCurrent(pageList.getCurrent());

				return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), pageList1);
			//已使用优惠券
			case "2" :
				QueryWrapper<UserCoupons> queryWrapper2 = new QueryWrapper<>();
				queryWrapper2.lambda().eq(UserCoupons::getUserId,user.getId()).eq(UserCoupons::getStatus,"1");
				Page<UserCoupons> page2 = new Page<UserCoupons>(pageNo, pageSize);
				IPage<UserCoupons> pageList22 = userCouponsService.page(page2, queryWrapper2);
				List<UserCoupons> list2 = pageList22.getRecords();
				//拿到couponsId去查询优惠券
				List<Coupons> couponsList2 = new ArrayList<>();
				if(couponsList2 == null){
					return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);
				}
				for (UserCoupons userCoupons : list2) {
					Coupons coupons =couponsService.getById(userCoupons.getCouponsId());
					couponsList2.add(coupons);
				}

				IPage<Coupons> pageList2 = new Page<>(pageNo,pageSize);
				pageList2.setRecords(couponsList2);
				pageList2.setTotal((long)couponsList2.size());
				pageList2.setCurrent(pageList22.getCurrent());
				pageList2.setSize(pageList22.getSize());

				return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), pageList2);
			//已过期优惠券
			case "0":
				QueryWrapper<UserCoupons> queryWrapper0 = new QueryWrapper<>();
				//未使用
				queryWrapper0.lambda().eq(UserCoupons::getUserId, user.getId()).eq(UserCoupons::getStatus, "0");
				Page<UserCoupons> page0 = new Page<UserCoupons>(pageNo, pageSize);
				IPage<UserCoupons> pageList00 = userCouponsService.page(page0, queryWrapper0);
				List<UserCoupons> list0 = pageList00.getRecords();

				//剔除未使用也未过期的优惠券
				if (list0 == null || list0.size() == 0) {
					return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);

				}

				List<UserCoupons> removeList = new ArrayList<>();

				for (UserCoupons userCoupons : list0) {
					Coupons coupons = couponsService.getById(userCoupons.getCouponsId());
					if (coupons != null) {
						if (coupons.getUseEndTime().after(new Date())) {
							//list0.remove(userCoupons);
							removeList.add(userCoupons);
						}
					}
				}

				list0.removeAll(removeList);

				//拿到couponsId去查询优惠券
				List<Coupons> couponsList0 = new ArrayList<>();

				for (UserCoupons userCoupons : list0) {
					Coupons coupons =couponsService.getById(userCoupons.getCouponsId());
					couponsList0.add(coupons);
				}

				IPage<Coupons> pageList0 = new Page<>(pageNo,pageSize);
				pageList0.setRecords(couponsList0);
				pageList0.setSize(pageList00.getSize());
				pageList0.setCurrent(pageList00.getCurrent());
				pageList0.setTotal((long)couponsList0.size());
				return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), pageList0);
		}

		return new RestResponseBean(ResultEnum.PARAM_TYPE_FAIL.getValue(),ResultEnum.PARAM_TYPE_FAIL.getDesc(),null);
	}




	/**
	 * showdoc
	 * @catalog 首页
	 * @title 用户优惠券数量
	 * @description 用户优惠券数量
	 * @method POST
	 * @url /nckf-boot/api/v1/coupons/getCouponsCount
	 * @return {"code": 1,"data": 0,"msg": "操作成功","time": "1561014430794"}
	 * @return_param code String 响应状态
	 * @return_param data Integer 优惠券数量
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark 这里是备注信息
	 * @number 4
	 */
	@PostMapping(value = "/getCouponsCount")
	@ApiOperation(value = "用户优惠券数量", notes = "用户优惠券数量",tags = {"优惠券"})
	public RestResponseBean getCouponsCount(){
		User user = (User) LoginUser.getCurrentUser();
		if(user == null){
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

		QueryWrapper<UserCoupons> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(UserCoupons::getUserId,user.getId()).eq(UserCoupons::getStatus,"0");
		List<UserCoupons> list = userCouponsService.list(queryWrapper);
		List<UserCoupons> removeList = new ArrayList<>();
		//剔除未使用但已过期的优惠券
		if(list!=null){
			for (UserCoupons userCoupons : list) {
				Coupons coupons =couponsService.getById(userCoupons.getCouponsId());
				if(coupons != null){
					if(new Date().after(coupons.getUseEndTime())){
						removeList.add(userCoupons);
					}
				}
			}
		}
		list.removeAll(removeList);

		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), list.size());
	}




	/**
	 * showdoc
	 * @catalog 用户接口
	 * @title 用户领取优惠券
	 * @description 用户领取优惠券
	 * @method POST
	 * @url /nckf-boot/api/v1/userCoupons/getCoupons
	 * @param couponsId 必填 String 优惠券id
	 * @return {"code": 1,"data": null,"msg": "操作成功","time": "1561014660668"}
	 * @return_param code String 响应状态
	 * @return_param data String NULL
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark 这里是备注信息
	 * @number 22
	 */
	@PostMapping(value = "/getCoupons")
	@ApiOperation(value = "用户领取优惠券", notes = "用户领取优惠券",tags = {"优惠券"})
	@ApiImplicitParams({
			@ApiImplicitParam(name="couponsId",value = "优惠券id")
	})
	public RestResponseBean getCoupons(@RequestParam(value = "couponsId",required = true) String couponsId){
		User user = (User) LoginUser.getCurrentUser();
		if (user == null) {
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
		}
		UserCoupons userCoupons = new UserCoupons();
		userCoupons.setUserId(user.getId());
		userCoupons.setStatus("0");
		userCoupons.setCouponsId(couponsId);
		userCoupons.setGetTime(new Date());
		userCoupons.setCreateBy(user.getUsername());
		userCoupons.setCreateTime(new Date());

		boolean flag = userCouponsService.save(userCoupons);
		if(flag){
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);
		}
		return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(),ResultEnum.OPERATION_FAIL.getDesc(),null);
	}


	@GetMapping(value = "/queryByStore")
	@ApiOperation(value = "生成订单查询可用优惠券", notes = "生成订单查询可用优惠券", tags = "优惠券")
	@ApiImplicitParams({@ApiImplicitParam(name = "pageNo", value = "当前页", dataType = "Integer", defaultValue = "1"),
			@ApiImplicitParam(name = "pageSize", value = "每页显示条数", dataType = "Integer", defaultValue = "10"),
			@ApiImplicitParam(name = "storeId", value = "门店id", dataType = "String")})
	public RestResponseBean queryByStore(@RequestParam(name = "storeId") String storeId,
								 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
								 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
								 @RequestParam(name = "appOrderMoney")String appOrderMoney ) {

		if(StringUtils.isBlank(storeId)){
			return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(),ResultEnum.PARAMETER_MISSING.getDesc(),null);
		}

		User user = (User) LoginUser.getCurrentUser();
		if(user == null){
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

		QueryWrapper<UserCoupons> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(UserCoupons::getUserId,user.getId()).eq(UserCoupons::getStatus,"0");

		Page<UserCoupons> page0 = new Page<UserCoupons>(pageNo, pageSize);
		IPage<UserCoupons> pageList = userCouponsService.page(page0, queryWrapper);

		List<UserCoupons> list = userCouponsService.list( queryWrapper);

		if(list == null){
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);
		}


		//List<UserCoupons> userCouponsList = new ArrayList<>();
		List<UserCoupons> removeList = new ArrayList<>();
		for (UserCoupons userCoupons : list) {
			Coupons coupons =couponsService.getById(userCoupons.getCouponsId());
			if(coupons != null){
				if(coupons.getUseEndTime().before(new Date()) || coupons.getUseCondition() > Double.parseDouble(appOrderMoney)){
					removeList.add(userCoupons);
				}
			}
		}

		list.removeAll(removeList);
		List<Coupons> couponsList = new ArrayList<>();
		for (UserCoupons userCoupons : list) {
			Coupons coupons =couponsService.getById(userCoupons.getCouponsId());
			if(coupons.getStoreId().equals(storeId) || "1".equals(coupons.getCommonFlag())){
				couponsList.add(coupons);
			}
		}

		//拿到couponsId去查询优惠券


		IPage<Coupons> pageList0 = new Page<>(pageNo,pageSize);
		pageList0.setRecords(couponsList);
		pageList0.setTotal((long)couponsList.size());
		pageList0.setCurrent(pageNo);
		pageList0.setSize(pageSize);

		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), pageList0);
	}



	@GetMapping(value = "/queryCouponsWdl")
	@ApiOperation(value = "首页展示可领取优惠券-->未登录", notes = "首页展示可领取优惠券-->未登录",tags = {"优惠券"})
	@ApiImplicitParams({
			@ApiImplicitParam(name="pageNo",value = "当前页",dataType = "Integer",defaultValue = "1"),
			@ApiImplicitParam(name="pageSize",value = "每页显示条数",dataType = "Integer",defaultValue = "10"),
	})
	public RestResponseBean queryCouponsWdl(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {


		QueryWrapper<Coupons> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(Coupons::getDelFlag,"1");
		Page<Coupons> page = new Page<Coupons>(pageNo, pageSize);
		IPage<Coupons> pageList = couponsService.page(page, queryWrapper);

		List<Coupons> list = pageList.getRecords();
		for (Coupons coupons : list) {
			if (coupons.getUseEndTime().before(new Date())){
				list.remove(coupons);
			}
		}

		//查询用户现在已经拥有的优惠券id
		//userCouponsService

		pageList.setRecords(list);

		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),pageList);
	}



	@GetMapping(value = "/queryCouponsYdl")
	@ApiOperation(value = "首页展示可领取优惠券-->已登录", notes = "首页展示可领取优惠券-->已登录",tags = {"优惠券"})
	@ApiImplicitParams({
			@ApiImplicitParam(name="pageNo",value = "当前页",dataType = "Integer",defaultValue = "1"),
			@ApiImplicitParam(name="pageSize",value = "每页显示条数",dataType = "Integer",defaultValue = "10"),
	})
	public RestResponseBean queryCouponsYdl(@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
										    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {

		User user = (User) LoginUser.getCurrentUser();

		QueryWrapper<Coupons> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(Coupons::getDelFlag,"1");
		IPage<Coupons> page = new Page<Coupons>(pageNo, pageSize);
		IPage<Coupons> pageList = couponsService.page(page, queryWrapper);

		List<Coupons> list = pageList.getRecords();
		for (Coupons coupons : list) {
			if (coupons.getUseEndTime().before(new Date())){
				list.remove(coupons);
			}
		}

		//查询用户现在已经拥有的优惠券id

		QueryWrapper<UserCoupons> userQueryWrapper = new QueryWrapper<>();

		//标记要移除的元素
		List<Coupons> listRemove = new ArrayList<>();

		userQueryWrapper.eq("user_id",user.getId());
		List<UserCoupons> userCoupons = userCouponsService.list(userQueryWrapper);
		for (Coupons coupons : list) {
			if (coupons.getUseEndTime().before(new Date())){
				list.remove(coupons);
			}
			for (int i = 0; i < userCoupons.size(); i++) {
				if(coupons.getId().equals(userCoupons.get(i).getCouponsId())){
					listRemove.add(coupons);
				}
			}
		}
		list.removeAll(listRemove);
		//查询用户现在已经拥有的优惠券id
		//userCouponsService
		pageList.setRecords(list);
		pageList.setTotal((long)list.size());
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(),ResultEnum.OPERATION_SUCCESS.getDesc(),pageList);
	}
}
