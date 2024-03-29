package org.benben.modules.business.orderMessage.service;

import org.benben.modules.business.orderMessage.entity.OrderMessage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.userMessage.entity.UserMessage;

import java.util.List;

/**
 * @Description: 订单消息
 * @author： jeecg-boot
 * @date：   2019-07-05
 * @version： V1.0
 */
public interface IOrderMessageService extends IService<OrderMessage> {
    Boolean addOrderMsg(String orderId);
    List<OrderMessage> queryAnnouncementCount(String id);
    public Boolean riderAddOrderMsg(String orderId,String riderId);
}
