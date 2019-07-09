package org.benben.modules.business.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.message.entity.Message;

/**
 * @Description: 消息
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
public interface IMessageService extends IService<Message> {
    int queryCount(String userId);
}
