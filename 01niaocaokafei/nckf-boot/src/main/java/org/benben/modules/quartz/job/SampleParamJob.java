package org.benben.modules.quartz.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.benben.common.util.DateUtils;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.service.IOrderNoPayService;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.order.vo.OrderNoPay;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 示例带参定时任务
 * 
 * @author Scott
 */
@Slf4j
@Component
public class SampleParamJob implements Job {
	@Autowired
	private IOrderNoPayService noPayService;

	@Autowired
	private IOrderService orderService;



	/**
	 * 若参数变量名修改 QuartzJobController中也需对应修改
	 */
	private String parameter;

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		QueryWrapper<Order> wrapper = new QueryWrapper<>();
		List<OrderNoPay> orderList = noPayService.selectAll();
		// List<Order> outtimeList=new ArrayList<>();
		int count=0;
		for (OrderNoPay order : orderList) {
			Date createTime = order.getCreateTime();
			long timeCreate = createTime.getTime();
			if(createTime!=null){
				long timeNow=System.currentTimeMillis();
				if(timeNow-timeCreate>=1000*60){
					order.setStatus("9");
					noPayService.removeById(order.getId());
					orderService.updateById(orderService.getById(order.getId()));
					count++;
				}
			}else{
				log.error("订单暂存表数据异常！");
			}

		}

		log.info("本次取消的订单数为"+count+"条");
		log.info(String.format("welcome %s! Jeecg-Boot 带参数定时任务 SampleParamJob !   时间:" + DateUtils.now(), this.parameter));

		//log.info(String.format("welcome %s! Jeecg-Boot 带参数定时任务 SampleParamJob !   时间:" + DateUtils.now(), this.parameter));
	}
}
