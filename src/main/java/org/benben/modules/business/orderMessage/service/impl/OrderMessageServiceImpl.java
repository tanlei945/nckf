package org.benben.modules.business.orderMessage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.orderMessage.entity.OrderMessage;
import org.benben.modules.business.orderMessage.mapper.OrderMessageMapper;
import org.benben.modules.business.orderMessage.service.IOrderMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 订单消息
 * @author： jeecg-boot
 * @date：   2019-07-05
 * @version： V1.0
 */
@Service
public class OrderMessageServiceImpl extends ServiceImpl<OrderMessageMapper, OrderMessage> implements IOrderMessageService {
    @Autowired
    private IOrderService orderService;
    @Override
    public Boolean addOrderMsg(String orderId) {
        try {
            Order order = orderService.getById(orderId);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("订单编号为:"+order.getOrderId());
            stringBuffer.append(",共购买"+order.getGoodsCount()+"件商品，请到个人中心->我的订单查看详情");
            OrderMessage orderMessage = new OrderMessage();
            orderMessage.setTitle("订单消息").setMsgContent(stringBuffer.toString());
            orderMessage.setUserId(order.getUserId());
            orderMessage.setOrderId(orderId);
            orderMessage.setOrderType(order.getOrderType());
            boolean save = save(orderMessage);
            return save;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }



    @Override
    public Boolean riderAddOrderMsg(String orderId,String riderId) {
        try {
            Order order = orderService.getById(orderId);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("订单编号为:"+order.getOrderId());
            stringBuffer.append(",共购买"+order.getGoodsCount()+"件商品，请到个人中心->我的订单查看详情");
            OrderMessage orderMessage = new OrderMessage();
            orderMessage.setTitle("订单消息").setMsgContent(stringBuffer.toString());
            orderMessage.setUserId(riderId);
            orderMessage.setOrderId(orderId);
            orderMessage.setOrderType(order.getOrderType());
            boolean save = save(orderMessage);
            return save;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public List<OrderMessage> queryAnnouncementCount(String userId) {
        QueryWrapper<OrderMessage> userMessageQueryWrapper = new QueryWrapper<>();
        userMessageQueryWrapper.eq("user_id", userId).eq("read_flag", "0").eq("del_flag","1");
        List<OrderMessage> list = list(userMessageQueryWrapper);
        return  list;
    }
}
