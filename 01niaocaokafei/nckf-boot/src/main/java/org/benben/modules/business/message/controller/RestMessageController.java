package org.benben.modules.business.message.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.modules.business.message.entity.Message;
import org.benben.modules.business.message.service.IMessageService;
import org.benben.modules.business.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/message")
@Slf4j
@Api(tags = {"首页"})
public class RestMessageController {
	@Autowired
	private IMessageService messageService;

	/**
	 * 分页列表查询
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@GetMapping(value = "/queryMessage")
	@ApiOperation(value = "消息详情列表", notes = "消息详情列表", tags = {"首页"})
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pageNo", value = "当前页", dataType = "Integer", defaultValue = "1"),
			@ApiImplicitParam(name = "pageSize", value = "每页显示条数", dataType = "Integer", defaultValue = "10"),})
	public RestResponseBean queryMessage(
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
		QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(Message::getDelFlag,"1");
		Page<Message> page = new Page<Message>(pageNo, pageSize);
		IPage<Message> pageList = messageService.page(page, queryWrapper);
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
				pageList);

	}


/**
	 * 通过id查询
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryMessageById")
	//@ApiOperation(value = "通过id查询消息详情", notes = "通过id查询消息详情", tags = {"首页"})
	//@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "消息id", dataType = "String", required = true),})
	public RestResponseBean queryMessageById(@RequestParam(name = "id", required = true) String id) {
		Message message = messageService.getById(id);
		if (message == null) {
			return new RestResponseBean(ResultEnum.QUERY_NOT_EXIST.getValue(), ResultEnum.QUERY_NOT_EXIST.getDesc(),
					null);
		} else {
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),
					message);
		}

	}

	@GetMapping(value = "/queryMessageCount")
	@ApiOperation(value = "获取用户系统公告未读数量", notes = "获取用户系统公告未读数量", tags = {"首页"})
	public RestResponseBean queryAnnouncementCount() {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		if(user==null) {
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),messageService.queryCount(user.getId()));
	}




	//后台给所有用户发送系统消息的接口

}