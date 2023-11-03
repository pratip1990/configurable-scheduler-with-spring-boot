/**
 * 
 */
package com.ph.configurablescheduler.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import com.ph.configurablescheduler.service.SchedulerServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Slf4j
@Configuration
@EnableScheduling
public class TaskTriggerConfiguration implements SchedulingConfigurer, DisposableBean {

	private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	@Value("${default.cron:*/5 * * * * *}")
	private String existingCronExp;

	@Autowired
	private SchedulerServiceImpl schedulerService;

	@PostConstruct
	public void init() {
		log.info("Default cron expression in TaskTriggerConfiguration : {}", existingCronExp);
	}

	@Override
	public void destroy() throws Exception {
		log.info("Destroy method called");
		if (executor != null) {
			executor.shutdownNow();
		}
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.addTriggerTask(createJobTaskDynamic(), createTriggerClientWise(taskRegistrar));
	}

	private Trigger createTriggerClientWise(ScheduledTaskRegistrar taskRegistrar) {
		return triggerContext -> {
		    if (!schedulerService.getCronExpression().equalsIgnoreCase(existingCronExp)) {
		        existingCronExp = schedulerService.getCronExpression();
		        taskRegistrar.setTriggerTasksList(new ArrayList<>());
		        configureTasks(taskRegistrar);
		        taskRegistrar.destroy();
		        taskRegistrar.setScheduler(executor);
		        taskRegistrar.afterPropertiesSet();
		        return null;
		    }
		    return new CronTrigger(existingCronExp).nextExecutionTime(triggerContext);
		};
	}

	private Runnable createJobTaskDynamic() {
		return () -> log.info("Task is executing :: {}", LocalDateTime.now());
	}

}
