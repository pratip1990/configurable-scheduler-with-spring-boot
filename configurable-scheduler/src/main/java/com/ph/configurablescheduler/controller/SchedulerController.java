/**
 * 
 */
package com.ph.configurablescheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ph.configurablescheduler.service.SchedulerServiceImpl;

/**
 * 
 */

@RestController
@RequestMapping("/scheduler")
public class SchedulerController {

	@Autowired
	private SchedulerServiceImpl schedulerService;

	@PostMapping(value = "/set-cron", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> setScheduler(@RequestParam("cron") String cronExp) {
		return ResponseEntity.ok(schedulerService.setCronExpression(cronExp.trim()));
	}

	@GetMapping(value = "/get-cron", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getScheduler() {
		return ResponseEntity.ok("{\"current-corn\":\"" + schedulerService.getCronExpression() + "\"}");
	}
}
