package org.benben.modules.business.message.service.impl;

import org.benben.modules.business.message.entity.Message;
import org.benben.modules.business.message.mapper.MessageMapper;
import org.benben.modules.business.message.service.IMessageService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 消息
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

}
