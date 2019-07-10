package org.benben.modules.business.userMessage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.api.vo.RestResponseBean;
import org.benben.common.menu.ResultEnum;
import org.benben.modules.business.message.entity.Message;
import org.benben.modules.business.message.service.IMessageService;
import org.benben.modules.business.orderMessage.entity.OrderMessage;
import org.benben.modules.business.orderMessage.service.IOrderMessageService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.userMessage.entity.UserMessage;
import org.benben.modules.business.userMessage.service.IUserMessageService;
import org.benben.modules.shiro.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
* @Title: Controller
* @Description: 用户消息表
* @author： jeecg-boot
* @date：   2019-07-04
* @version： V1.0
*/
@RestController
@RequestMapping("/api/v1/userMessage")
@Slf4j
public class RestUserMessageController {
    @Autowired
    private IUserMessageService userMessageService;
    @Autowired
    private IMessageService messageService;
    @Autowired
    private IOrderMessageService orderMessageService;

    /**
     * 分页列表查询
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "通用-->查询系统消息", tags = {"首页"}, notes = "通用-->查询系统消息")
    public RestResponseBean queryPageList(
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        IPage<Message> pageList = null;
        LinkedList<Message> messages = null;
        try {
            Page<Message> page = new Page<Message>(pageNo, pageSize);
            pageList = messageService.page(page);
            messages = userMessageService.queryPageList(user.getId());
            pageList.setRecords(messages);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
        }
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), pageList);
    }


    /**
     * 编辑
     *
     * @return
     */
    @PostMapping(value = "/changeMessageStatus")
    @ApiOperation(value = "通用-->修改系统消息为已读", tags = {"首页"}, notes = "通用-->修改系统消息为已读")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "messageId", value = "消息id", dataType = "String", required = true),
    })
    public RestResponseBean edit(@RequestParam(name = "messageId") String messageId) {
        UserMessage byId = null;
        try {
            byId = userMessageService.getById(messageId);
            byId.setReadFlag("1");
            userMessageService.saveOrUpdate(byId);
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
        }
    }

    /**
     * @param id
     * @return
     */
    @GetMapping(value = "/delete")
    @ApiOperation(value = "通用-->删除单个系统信息", notes = "通用-->删除单个系统信息", tags = {"首页"})
    public RestResponseBean delete(@RequestParam(name = "id", required = true) String id) {
        try {
            UserMessage userMessage = userMessageService.getById(id);
            userMessage.setReadFlag("1").setDelFlag("0");
            userMessageService.saveOrUpdate(userMessage);
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);
        }
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryMessageById")
    @ApiOperation(value = "通用-->查询系统信息详情", notes = "通用-->查询系统信息详情", tags = {"首页"})
    public RestResponseBean queryById(@RequestParam(name = "id", required = true) String id) {
        try {
            UserMessage userMessage = userMessageService.getById(id);
            Message byId = messageService.getById(userMessage.getMessageId());
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), byId);
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);
        }
    }

    @GetMapping(value = "/queryMessageCount")
    @ApiOperation(value = "通用-->获取系统公告未读数量", notes = "通用-->获取系统公告未读数量", tags = {"首页"})
    public RestResponseBean queryAnnouncementCount() {
        try {
            List<UserMessage> userMessages = userMessageService.queryAnnouncementCount();
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), userMessages.size());
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
        }
    }



    @GetMapping(value = "/queryCount")
    @ApiOperation(value = "通用-->首页查询所有未读消息数量", tags = {"首页"}, notes = "通用-->首页查询所有未读消息数量")
    public RestResponseBean queryCount() {

        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        List<UserMessage> userMessages = new ArrayList<>();
        List<OrderMessage> orderMessages = new ArrayList<>();
        userMessages = userMessageService.queryAnnouncementCount();
        orderMessages = orderMessageService.queryAnnouncementCount(user.getId());
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), userMessages.size()+orderMessages.size());
    }
}
