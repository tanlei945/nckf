package org.benben.modules.business.userMessage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.benben.modules.business.message.entity.Message;
import org.benben.modules.business.userMessage.entity.UserMessage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedList;
import java.util.List;

/**
 * @Description: 用户消息表
 * @author： jeecg-boot
 * @date：   2019-07-04
 * @version： V1.0
 */
public interface IUserMessageService extends IService<UserMessage> {
    List<UserMessage> queryMessageCount();
    IPage<UserMessage> queryPageList(Integer pageNo, Integer pageSize);
}
