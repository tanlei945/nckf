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
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.usercoupons.entity.UserCoupons;
import org.benben.modules.business.usercoupons.service.IUserCouponsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/userCoupons")
@Slf4j
@Api(tags = "用户接口")
public class RestUserCouponsController {
	@Autowired
	private IUserCouponsService userCouponsService;

	/**
	 * 个人优惠券查询
	 * @param status
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@GetMapping(value = "/list")
	@ApiOperation(value = "个人优惠券查询", notes = "个人优惠券查询", tags = "首页")
	@ApiImplicitParams({@ApiImplicitParam(name = "pageNo", value = "当前页", dataType = "Integer", defaultValue = "1"),
			@ApiImplicitParam(name = "pageSize", value = "每页显示条数", dataType = "Integer", defaultValue = "10"),
			@ApiImplicitParam(name = "status", value = "状态：-1已过期 0 未使用 1已使用", dataType = "String")})
	public RestResponseBean list(@RequestParam(name = "status") String status,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

		if(StringUtils.isBlank(status)){
			return new RestResponseBean(ResultEnum.PARAMETER_MISSING.getValue(),ResultEnum.PARAMETER_MISSING.getDesc(),null);
		}

		User user = (User) SecurityUtils.getSubject().getPrincipal();
		if(user == null){
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}

		QueryWrapper<UserCoupons> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(UserCoupons::getUserId,user.getId()).eq(UserCoupons::getStatus,status);
		Page<UserCoupons> page = new Page<UserCoupons>(pageNo, pageSize);
		IPage<UserCoupons> pageList = userCouponsService.page(page, queryWrapper);
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), pageList);
	}

	@PostMapping(value = "/getCoupons")
	@ApiOperation(value = "用户领取优惠券", notes = "用户领取优惠券",tags = {"首页"})
	@ApiImplicitParams({
			@ApiImplicitParam(name="couponsId",value = "优惠券id")
	})
	public RestResponseBean getCoupons(@RequestParam(value = "couponsId",required = true) String couponsId){
		User user = (User) SecurityUtils.getSubject().getPrincipal();
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

}
