package com.sige.spring.service;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class Schedules {

	//@Scheduled(cron="0 1 1 * * *", zone="Europe/Istanbul")
	//@Scheduled(cron="0 0/1 08,09 * * ?")
	@Scheduled(cron="0 0/1 * * * ?")
	public void scheduleFixedDelayTask() {
	}
}
