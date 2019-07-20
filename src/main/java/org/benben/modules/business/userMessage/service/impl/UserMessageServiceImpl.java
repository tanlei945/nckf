package org.benben.modules.business.userMessage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.message.entity.Message;
import org.benben.modules.business.message.service.IMessageService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.userMessage.entity.UserMessage;
import org.benben.modules.business.userMessage.mapper.UserMessageMapper;
import org.benben.modules.business.userMessage.service.IUserMessageService;
import org.benben.modules.business.withdraw.entity.Withdraw;
import org.benben.modules.shiro.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
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
    @Autowired
    private IUserMessageService userMessageService;
    @Override
    public List<UserMessage> queryMessageCount() {
        User user = (User) LoginUser.getCurrentUser();
        QueryWrapper<UserMessage> userMessageQueryWrapper = new QueryWrapper<>();
        userMessageQueryWrapper.eq("user_id", user.getId()).eq("read_flag", "0").eq("del_flag","1");
        List<UserMessage> list = list(userMessageQueryWrapper);
        return  list;
    }

    @Override
    public IPage<UserMessage> queryPageList(Integer pageNo,Integer pageSize) {
        User user = (User) LoginUser.getCurrentUser();
        IPage<UserMessage> page = new Page<UserMessage>(pageNo, pageSize);
        //List<Message> messageList = new ArrayList<>();

        QueryWrapper<UserMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserMessage::getUserId,user.getId()).eq(UserMessage::getDelFlag, "1").orderByDesc(UserMessage::getCreateTime);
        IPage<UserMessage> page1 = userMessageService.page(page,queryWrapper);

        /*for (UserMessage userMessage : userMessageList) {
            Message message = messageService.getById(userMessage.getMessageId());
            messageList.add(message);
        }
        page.setRecords(messageList);
        page.setTotal((long)messageList.size());*/
        return page1;
    }
}
