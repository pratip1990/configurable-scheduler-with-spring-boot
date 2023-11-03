/**
 * 
 */
package com.ph.configurablescheduler.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Slf4j
@Service
public class SchedulerServiceImpl {
	@Value("${default.cron:*/5 * * * * *}")
	private String cronExpression;

	@PostConstruct
	public void init() {
		log.info("Default cron expression at system startup {}", cronExpression);
	}

	public Map<String, String> setCronExpression(String newCronExpression) {
		Map<String, String> response = new HashMap<>();
		response.put("new-corn", newCronExpression);
		response.put("current-corn", this.cronExpression);
		this.cronExpression = newCronExpression;
		return response;
	}

	public String getCronExpression() {
		return this.cronExpression;
	}

}
