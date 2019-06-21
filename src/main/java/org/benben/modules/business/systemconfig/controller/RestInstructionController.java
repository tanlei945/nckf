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

	/**
	 * showdoc
	 * @catalog 个人中心接扣
	 * @title 充值说明查询
	 * @description 充值说明查询
	 * @method GET
	 * @url /nckf-boot/api/v1/rechargeConfig/queryRechargeDictionary
	 * @return {"code": 1,"data": [{"configGroup": "rechargeExplain","configName": "recharge_instruction","configType": null,"configValue": "充值说明","content": null,"createBy": null,"createTime": null,"description": null,"extend": null,"id": "66","rule": null,"title": "充值说明及优惠","updateBy": null,"updateTime": null}],"msg": "操作成功","time": "1561014987002"}
	 * @return_param code String 响应状态
	 * @return_param configGroup String 分组
	 * @return_param configName String 变量名
	 * @return_param configType String 类型(string,text,int,bool,array,datetime,date,file)
	 * @return_param configValue String 值
	 * @return_param content String 变量字典数据
	 * @return_param description String 描述
	 * @return_param extend String 拓展属性
	 * @return_param id String 配置表id
	 * @return_param rule String 验证规则
	 * @return_param title String 变量标题
	 * @return_param createBy String 创建者
	 * @return_param createTime Date 创建时间
	 * @return_param updateBy String 更新人
	 * @return_param updateTime Date 更新时间
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark 这里是备注信息
	 * @number 5
	 */
	@GetMapping(value = "/queryRechargeDictionary")
	@ApiOperation(value = "充值说明查询", tags = {"个人中心接口"}, notes = "充值说明查询")
	public RestResponseBean queryRechargeDictionary() {

		QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
		//查询条件
		queryWrapper.eq("config_group", "rechargeExplain");
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				systemConfigService.list(queryWrapper));
	}

	/**
	 * showdoc
	 * @catalog 个人中心接口
	 * @title 如何购买查询
	 * @description 如何购买查询
	 * @method GET
	 * @url /nckf-boot/api/v1/rechargeConfig/queryHowToBuy
	 * @return {"code": 1,"data": {"configGroup": "howToBuy","configName": "how_to_buy","configType": null,"configValue": "如何买买买","content": null,"createBy": null,"createTime": null,"description": "如何购买","extend": null,"id": "67", "rule": null,"title": "如何购买","updateBy": null,"updateTime": null},"msg": "操作成功","time": "1561014234178"}
	 * @return_param code String 响应状态
	 * @return_param configGroup String 分组
	 * @return_param configName String 变量名
	 * @return_param configType String 类型(string,text,int,bool,array,datetime,date,file)
	 * @return_param configValue String 值
	 * @return_param content String 变量字典数据
	 * @return_param description String 描述
	 * @return_param extend String 拓展属性
	 * @return_param id String 配置表id
	 * @return_param rule String 验证规则
	 * @return_param title String 变量标题
	 * @return_param createBy String 创建者
	 * @return_param createTime Date 创建时间
	 * @return_param updateBy String 更新人
	 * @return_param updateTime Date 更新时间
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark 这里是备注信息
	 * @number 1
	 */
	@GetMapping(value = "/queryHowToBuy")
	@ApiOperation(value = "如何购买查询", tags = {"个人中心接口"}, notes = "如何购买查询")
	public RestResponseBean queryHowToBuy() {

		QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
		//查询条件
		queryWrapper.eq("config_group", "howToBuy");
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				systemConfigService.getOne(queryWrapper));
	}

	/**
	 * showdoc
	 * @catalog 个人中心接扣
	 * @title 如何充值查询
	 * @description 如何充值查询
	 * @method GET
	 * @url /nckf-boot/api/v1/rechargeConfig/queryHowToRecharge
	 * @return {"code": 1,"data": {"configGroup": "howToRecharge","configName": "how_to_recharge","configType": null,"configValue": "如何充充充","content": null,"createBy": null,"createTime": null,"description": "如何充值","extend": null,"id": "68","rule": null,"title": "如何充值","updateBy": null,"updateTime": null},"msg": "操作成功","time": "1561013357101"}
	 * @return_param code String 响应状态
	 * @return_param configGroup String 分组
	 * @return_param configName String 变量名
	 * @return_param configType String 类型(string,text,int,bool,array,datetime,date,file)
	 * @return_param configValue String 值
	 * @return_param content String 变量字典数据
	 * @return_param description String 描述
	 * @return_param extend String 拓展属性
	 * @return_param id String 配置表id
	 * @return_param rule String 验证规则
	 * @return_param title String 变量标题
	 * @return_param createBy String 创建者
	 * @return_param createTime Date 创建时间
	 * @return_param updateBy String 更新人
	 * @return_param updateTime Date 更新时间
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark 这里是备注信息
	 * @number 2
	 */
	@GetMapping(value = "/queryHowToRecharge")
	@ApiOperation(value = "如何充值查询", tags = {"个人中心接口"}, notes = "如何充值查询")
	public RestResponseBean queryHowToRecharge() {

		QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
		//查询条件
		queryWrapper.eq("config_group", "howToRecharge");
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				systemConfigService.getOne(queryWrapper));
	}

	/**
	 * showdoc
	 * @catalog 个人中心接扣
	 * @title 用户隐私查询
	 * @description 用户隐私查询
	 * @method GET
	 * @url /nckf-boot/api/v1/rechargeConfig/queryUserPrivacy
	 * @return {"code": 1,"data": {"configGroup": "userPrivacy","configName": "user_privacy","configType": null,"configValue": "用户隐私隐私隐私","content": null,"createBy": null,"createTime": null,"description": "用户隐私","extend": null,"id": "69","rule": null,"title": "用户隐私","updateBy": null,"updateTime": null},"msg": "操作成功","time": "1561014884902"}
	 * @return_param code String 响应状态
	 * @return_param configGroup String 分组
	 * @return_param configName String 变量名
	 * @return_param configType String 类型(string,text,int,bool,array,datetime,date,file)
	 * @return_param configValue String 值
	 * @return_param content String 变量字典数据
	 * @return_param description String 描述
	 * @return_param extend String 拓展属性
	 * @return_param id String 配置表id
	 * @return_param rule String 验证规则
	 * @return_param title String 变量标题
	 * @return_param createBy String 创建者
	 * @return_param createTime Date 创建时间
	 * @return_param updateBy String 更新人
	 * @return_param updateTime Date 更新时间
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark 这里是备注信息
	 * @number 6
	 */
	@GetMapping(value = "/queryUserPrivacy")
	@ApiOperation(value = "用户隐私查询", tags = {"个人中心接口"}, notes = "用户隐私查询")
	public RestResponseBean queryUserPrivacy() {

		QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
		//查询条件
		queryWrapper.eq("config_group", "userPrivacy");
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				systemConfigService.getOne(queryWrapper));
	}

	/**
	 * showdoc
	 * @catalog 个人中心接扣
	 * @title 发票开具查询
	 * @description 发票开具查询
	 * @method GET
	 * @url /nckf-boot/api/v1/rechargeConfig/queryInvoiceIssued
	 * @return {"code": 1,"data": {"configGroup": "invoiceIssued","configName": "Invoice_issued","configType": null,"configValue": "发票开具发票开具发票开具","content": null,"createBy": null,"createTime": null,"description": "发票开具","extend": null,"id": "70","rule": null,"title": "发票开具","updateBy": null,"updateTime": null},"msg": "操作成功","time": "1561014463998"}
	 * @return_param code String 响应状态
	 * @return_param configGroup String 分组
	 * @return_param configName String 变量名
	 * @return_param configType String 类型(string,text,int,bool,array,datetime,date,file)
	 * @return_param configValue String 值
	 * @return_param content String 变量字典数据
	 * @return_param description String 描述
	 * @return_param extend String 拓展属性
	 * @return_param id String 配置表id
	 * @return_param rule String 验证规则
	 * @return_param title String 变量标题
	 * @return_param createBy String 创建者
	 * @return_param createTime Date 创建时间
	 * @return_param updateBy String 更新人
	 * @return_param updateTime Date 更新时间
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark 这里是备注信息
	 * @number 3
	 */
	@GetMapping(value = "/queryInvoiceIssued")
	@ApiOperation(value = "发票开具查询", tags = {"个人中心接口"}, notes = "发票开具查询")
	public RestResponseBean queryInvoiceIssued() {

		QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
		//查询条件
		queryWrapper.eq("config_group", "invoiceIssued");
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				systemConfigService.getOne(queryWrapper));
	}

	/**
	 * showdoc
	 * @catalog 个人中心接扣
	 * @title 其他问题查询
	 * @description 其他问题查询
	 * @method GET
	 * @url /nckf-boot/api/v1/rechargeConfig/queryOtherProblems
	 * @return {"code": 1,"data": {"configGroup": "otherProblems","configName": "other_problems","configType": null,"configValue": "其他问题其他问题其他问题其他问题","content": null,"createBy": null,"createTime": null,"description": "其他问题","extend": null,"id": "71","rule": null,"title": "其他问题","updateBy": null,"updateTime": null},"msg": "操作成功","time": "1561014673276"}
	 * @return_param code String 响应状态
	 * @return_param configGroup String 分组
	 * @return_param configName String 变量名
	 * @return_param configType String 类型(string,text,int,bool,array,datetime,date,file)
	 * @return_param configValue String 值
	 * @return_param content String 变量字典数据
	 * @return_param description String 描述
	 * @return_param extend String 拓展属性
	 * @return_param id String 配置表id
	 * @return_param rule String 验证规则
	 * @return_param title String 变量标题
	 * @return_param createBy String 创建者
	 * @return_param createTime Date 创建时间
	 * @return_param updateBy String 更新人
	 * @return_param updateTime Date 更新时间
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark 这里是备注信息
	 * @number 4
	 */
	@GetMapping(value = "/queryOtherProblems")
	@ApiOperation(value = "其他问题查询", tags = {"个人中心接口"}, notes = "其他问题查询")
	public RestResponseBean queryOtherProblems() {

		QueryWrapper<SystemConfig> queryWrapper = new QueryWrapper<>();
		//查询条件
		queryWrapper.eq("config_group", "otherProblems");
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				systemConfigService.getOne(queryWrapper));
	}

}
