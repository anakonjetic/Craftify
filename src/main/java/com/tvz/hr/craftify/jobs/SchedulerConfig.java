package com.tvz.hr.craftify.jobs;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfig {
    private static final String USERS_PRINT_JOB_IDENTITY = "usersPrintJob";
    private static final String USERS_PRINT_TRIGGER = "usersPrintTrigger";

    @Bean
    public JobDetail projectPrintJobDetail(){
        return JobBuilder.newJob(LoggedUsersPrintJob.class).withIdentity(USERS_PRINT_JOB_IDENTITY)
                .storeDurably().build();
    }

    @Bean
    public SimpleTrigger projectPrintTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(15).repeatForever();
        return TriggerBuilder.newTrigger().forJob(projectPrintJobDetail())
                .withIdentity(USERS_PRINT_TRIGGER).withSchedule(scheduleBuilder).build();
    }
}
