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
import org.benben.modules.shiro.LoginUser;
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
    @Autowired
    private IUserMessageService userMessageService;
    @Override
    public List<UserMessage> queryAnnouncementCount() {
        User user = (User) LoginUser.getCurrentUser();
        QueryWrapper<UserMessage> userMessageQueryWrapper = new QueryWrapper<>();
        userMessageQueryWrapper.eq("user_id", user.getId()).eq("read_flag", "0");
        List<UserMessage> list = list(userMessageQueryWrapper);
        return  list;
    }

    @Override
    public IPage<UserMessage> queryPageList(String userId,Integer pageNo,Integer pageSize) {
        Page<UserMessage> page = new Page<UserMessage>(pageNo, pageSize);
        QueryWrapper<UserMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("del_flag", "1");
        IPage<UserMessage> pageList  = userMessageService.page(page,queryWrapper);
        return pageList;
    }
}
