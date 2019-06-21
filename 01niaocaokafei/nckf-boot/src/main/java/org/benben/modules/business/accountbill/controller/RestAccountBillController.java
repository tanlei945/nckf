package org.benben.modules.business.accountbill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.modules.business.accountbill.entity.AccountBill;
import org.benben.modules.business.accountbill.service.IAccountBillService;
import org.benben.modules.business.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/accountBill")
@Slf4j
@Api(tags = {"用户接口"})
public class RestAccountBillController {
	@Autowired
	private IAccountBillService accountBillService;

	/**
	 * showdoc
	 * @catalog 用户接口
	 * @title 账单列表
	 * @description 账单列表
	 * @method GET
	 * @url /nckf-boot/api/v1/accountBill/queryAccountBill
	 * @param billType 必填 String 支出方式(1:充值 2:消费)
	 * @return {"code": 1,"data": {"current": 1,"pages": 0,"records": [],"searchCount": true,"size": 10,"total": 0},"msg": "操作成功","time": "1561014568509"}
	 * @return_param code String 响应状态
	 * @return_param data List 账单对象
	 * @return_param msg String 操作信息
	 * @return_param time Date 操作时间
	 * @remark 这里是备注信息
	 * @number 15
	 */
	@GetMapping(value = "/queryAccountBill")
	@ApiOperation(value = "账单列表", tags = {"用户接口"}, notes = "账单列表")
	@ApiImplicitParam(name = "billType", value = "1:充值 2：消费", dataType = "String", required = true)
	public RestResponseBean queryAccountBill(@RequestParam String billType,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

		User user = (User) SecurityUtils.getSubject().getPrincipal();

		if (user == null) {
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
		}

		QueryWrapper<AccountBill> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(AccountBill::getBillType, billType).eq(AccountBill::getUserId, user.getId());
		Page<AccountBill> page = new Page<AccountBill>(pageNo, pageSize);
		IPage<AccountBill> pageList = accountBillService.page(page, queryWrapper);

		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				pageList);
	}


}
