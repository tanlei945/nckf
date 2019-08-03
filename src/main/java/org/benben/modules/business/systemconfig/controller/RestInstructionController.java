package org.benben.modules.business.systemconfig.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.api.vo.Result;
import org.benben.common.menu.ResultEnum;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.systemconfig.entity.SystemConfig;
import org.benben.modules.business.systemconfig.service.ISystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/rechargeConfig")
@Slf4j
@Api(tags = {"个人中心接口"})
public class RestInstructionController {

	@Autowired
	private ISystemConfigService systemConfigService;


	@GetMapping(value = "/queryRechargeDictionary")
	@ApiOperation(value = "充值说明查询", tags = {"个人中心接口"}, notes = "充值说明查询")
	public RestResponseBean queryRechargeDictionary() {

		QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
		//查询条件
		queryWrapper.eq("config_group", "rechargeExplain");
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				systemConfigService.list(queryWrapper));
	}


	@GetMapping(value = "/queryHowToBuy")
	@ApiOperation(value = "如何购买查询", tags = {"个人中心接口"}, notes = "如何购买查询")
	public RestResponseBean queryHowToBuy() {

		QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
		//查询条件
		queryWrapper.eq("config_group", "howToBuy");
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				systemConfigService.getOne(queryWrapper));
	}


	@GetMapping(value = "/queryHowToRecharge")
	@ApiOperation(value = "如何充值查询", tags = {"个人中心接口"}, notes = "如何充值查询")
	public RestResponseBean queryHowToRecharge() {

		QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
		//查询条件
		queryWrapper.eq("config_group", "howToRecharge");
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				systemConfigService.getOne(queryWrapper));
	}


	@GetMapping(value = "/queryUserPrivacy")
	@ApiOperation(value = "用户隐私查询", tags = {"个人中心接口"}, notes = "用户隐私查询")
	public RestResponseBean queryUserPrivacy() {

		QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
		//查询条件
		queryWrapper.eq("config_group", "userPrivacy");
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				systemConfigService.getOne(queryWrapper));
	}


	@GetMapping(value = "/queryInvoiceIssued")
	@ApiOperation(value = "发票开具查询", tags = {"个人中心接口"}, notes = "发票开具查询")
	public RestResponseBean queryInvoiceIssued() {

		QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
		//查询条件
		queryWrapper.eq("config_group", "invoiceIssued");
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				systemConfigService.getOne(queryWrapper));
	}


	@GetMapping(value = "/za")
	@ApiOperation(value = "其他问题查询", tags = {"个人中心接口"}, notes = "其他问题查询")
	public RestResponseBean queryOtherProblems() {

		QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
		//查询条件
		queryWrapper.eq("config_group", "otherProblems");
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				systemConfigService.getOne(queryWrapper));
	}


	@GetMapping(value = "/yhxy")
	@ApiOperation(value = "用户协议", tags = {"个人中心接口"}, notes = "用户协议")
	public RestResponseBean yhxy() {

		QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
		//查询条件
		queryWrapper.eq("config_group", "yhxy");
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				systemConfigService.getOne(queryWrapper));
	}


	@GetMapping(value = "/zfxy")
	@ApiOperation(value = "支付协议", tags = {"个人中心接口"}, notes = "支付协议")
	public RestResponseBean zfxy() {

		QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
		//查询条件
		queryWrapper.eq("config_group", "zfxy");
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				systemConfigService.getOne(queryWrapper));
	}
}
