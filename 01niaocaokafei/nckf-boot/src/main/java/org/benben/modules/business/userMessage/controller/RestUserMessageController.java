package org.benben.modules.business.userMessage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.userMessage.entity.UserMessage;
import org.benben.modules.business.userMessage.service.IUserMessageService;
import org.benben.modules.shiro.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * 分页列表查询
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/list")
    @ApiOperation(value = "通用-->查询系统消息", tags = {"首页系统消息"}, notes = "通用-->查询系统消息")
    public RestResponseBean queryPageList(
                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }
        IPage<Message> pageList1 = new Page<>(pageNo,pageSize);
        try {
            Page<UserMessage> page = new Page<UserMessage>(pageNo, pageSize);
            QueryWrapper<UserMessage> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(UserMessage::getUserId,user.getId()).eq(UserMessage::getDelFlag,"1").eq(UserMessage::getReadFlag,"0");
            IPage<UserMessage> pageList = userMessageService.page(page, queryWrapper);
            List<UserMessage> records = pageList.getRecords();
            LinkedList<Message> messages = new LinkedList<>();
            records.forEach(msg->{
                Message message = messageService.getById(msg.getMessageId());
                messages.add(message);
            });
            pageList1.setTotal(pageList.getTotal());
            pageList1.setRecords(messages);
            pageList1.setCurrent(pageList.getCurrent());
            pageList1.setPages(pageList.getPages());
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
        }
        return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), pageList1);
    }


    /**
     * 编辑
     *
     * @return
     */
    @PostMapping(value = "/changeMessageStatus")
    @ApiOperation(value = "通用-->修改系统消息为已读", tags = {"首页系统消息"}, notes = "通用-->修改系统消息为已读")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "messageId", value = "消息id", dataType = "String", required = true),
    })
    public RestResponseBean edit(@RequestParam(name = "messageId") String messageId) {

        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        UserMessage userMessage = null;
        try {
            userMessage = userMessageService.getById(messageId);
            userMessage.setReadFlag("1");
            userMessageService.updateById(userMessage);
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
    @ApiOperation(value = "通用-->删除单个系统信息", notes = "通用-->删除单个系统信息", tags = {"首页系统消息"})
    public RestResponseBean delete(@RequestParam(name = "id", required = true) String id) {
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }


        try {
            UserMessage userMessage = userMessageService.getById(id);
            userMessage.setReadFlag("1").setDelFlag("0");
            userMessageService.updateById(userMessage);
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), null);

        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);
        }
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryMessageById")
    @ApiOperation(value = "通用-->查询某个系统信息", notes = "通用-->查询某个系统信息", tags = {"首页系统消息"})
    public RestResponseBean queryById(@RequestParam(name = "id", required = true) String id) {

        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        try {
           // UserMessage userMessage = userMessageService.getById(id);
            Message message = messageService.getById(id);
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), message);

        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseBean(ResultEnum.OPERATION_FAIL.getValue(), ResultEnum.OPERATION_FAIL.getDesc(), null);

        }
    }

    @GetMapping(value = "/queryMessageCount")
    @ApiOperation(value = "通用-->取系统消息未读数量", notes = "通用-->取系统消息未读数量", tags = {"首页系统消息"})
    public RestResponseBean queryAnnouncementCount() {
        User user = (User) LoginUser.getCurrentUser();
        if(user==null) {
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(),ResultEnum.TOKEN_OVERDUE.getDesc(),null);
        }

        try {
            List<UserMessage> userMessages = userMessageService.queryMessageCount();
            return new RestResponseBean(ResultEnum.OPERATION_SUCCESS.getValue(), ResultEnum.OPERATION_SUCCESS.getDesc(), userMessages.size());
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResponseBean(ResultEnum.TOKEN_OVERDUE.getValue(), ResultEnum.TOKEN_OVERDUE.getDesc(), null);
        }
    }
}
