package org.benben.modules.business.order.quatz;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.service.IOrderService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
@EnableScheduling
public class OrderTask extends QuartzJobBean {
    @Autowired
    private IOrderService orderService;
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("status","1");
        List<Order> orderList = orderService.list(wrapper);
       // List<Order> outtimeList=new ArrayList<>();
        int count=0;
        for (Order order : orderList) {
            Date createTime = order.getCreateTime();
            long timeCreate = createTime.getTime();
            long timeNow=new Date().getTime();
            if(timeNow-timeCreate>=1000*60){
                order.setStatus("-1");
                QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id",order.getId());
                orderService.update(order,queryWrapper);
                count++;
            }
        }

        log.info("本次取消的订单数为"+count+"条");
    }
}
