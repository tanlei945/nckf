package org.benben.modules.business.order.quatz;

import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

//    @Bean
//    public JobDetail uploadTaskDetail(){
//        return JobBuilder.newJob(OrderTask.class).withIdentity("orderTask").storeDurably().build();
//    }
//
//    @Bean
//    public Trigger uploadTaskTrigger(){
//        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("*/5 * * * * ?");
//        return TriggerBuilder.newTrigger()
//                .forJob(uploadTaskDetail())
//                .withIdentity("orderTask")
//                .withSchedule(scheduleBuilder)
//                .build();
//    }


}
