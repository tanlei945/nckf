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
import org.benben.modules.business.announcement.entity.Announcement;
import org.benben.modules.business.announcement.service.IAnnouncementService;
import org.benben.modules.business.message.entity.Message;
import org.benben.modules.business.message.service.IMessageService;
import org.benben.modules.business.message.vo.MessageVo;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.shiro.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/v1/message")
@Slf4j
@Api(tags = {"首页"})
public class RestMessageController {
	@Autowired
	private IMessageService messageService;
	@Autowired
	private IAnnouncementService announcementService;

	/**
	 * 分页列表查询
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@GetMapping(value = "/queryMessage")
	@ApiOperation(value = "用户消息列表", notes = "用户消息列表", tags = {"首页"})
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

		IPage<Announcement> pageListAnno = null;

		List<Message> records = pageList.getRecords();
		for (Message record : records) {
			Page<Announcement> pageAnno = new Page<Announcement>(pageNo, pageSize);
			pageListAnno = announcementService.page(pageAnno,null);

		}




		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), pageListAnno);

	}




	/**
	 *
	 * @return
	 */
	@GetMapping(value = "/changeMessageStatus")
	@ApiOperation(value = "用户阅读系统消息（改变状态为已读）", notes = "用户阅读系统消息（改变状态为已读）", tags = {"首页"})
	public RestResponseBean changeMessageStatus(@RequestParam(name = "messageId",required = true)String messageId){

		Message message = new Message();
		message.setId(messageId);
		message.setReadTime(new Date());
		message.setReadFlag("1");
		boolean ok = messageService.updateById(message);
		if(ok){
			return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);
		}

		return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(),null);

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
		message.setReadTime(new Date());
		message	.setReadFlag("1");
		messageService.updateById(message);
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
		User user = (User) LoginUser.getCurrentUser();
		if(user==null) {
			return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
		}
		return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(),messageService.queryCount(user.getId()));
	}




	//后台给所有用户发送系统消息的接口

}
