package org.benben.modules.business.userMessage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.message.entity.Message;
import org.benben.modules.business.message.service.IMessageService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.userMessage.entity.UserMessage;
import org.benben.modules.business.userMessage.mapper.UserMessageMapper;
import org.benben.modules.business.userMessage.service.IUserMessageService;
import org.benben.modules.shiro.LoginUser;
import org.benben.modules.system.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.LinkedList;
import java.util.List;

/**
 * @Description: 用户消息表
 * @author： jeecg-boot
 * @date：   2019-07-04
 * @version： V1.0
 */
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements IUserMessageService {

    @Autowired
    private IMessageService messageService;
    @Override
    public List<UserMessage> queryAnnouncementCount() {
        User user = (User) LoginUser.getCurrentUser();
        QueryWrapper<UserMessage> userMessageQueryWrapper = new QueryWrapper<>();
        userMessageQueryWrapper.eq("user_id", user.getId()).eq("read_flag", "0");
        List<UserMessage> list = list(userMessageQueryWrapper);
        return  list;
    }

    @Override
    public LinkedList<Message> queryPageList(String userId) {
        UserMessage userMessage = new UserMessage();
        QueryWrapper<UserMessage> queryWrapper = QueryGenerator.initQueryWrapper(userMessage, null);
        queryWrapper.eq("user_id", userId).eq("del_flag", "0");
        List<UserMessage> list = list(queryWrapper);
        LinkedList<Message> messages = new LinkedList<>();
        list.forEach(msg -> {
            messages.add(messageService.getById(msg.getMessageId()));
        });
        return messages;
    }
}
