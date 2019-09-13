package com.rb.instagramfollowerbooster.core;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rb.common.api.logging.LogLevel;
import com.rb.common.api.logging.LogManager;
import com.rb.common.api.logging.LoggingAction;

@Component
public class WaiterDelegate {

	@Autowired
	LogManager logger;
	
	/**
	 * Algorithm of waiting before unfollowing depending on the current time.
	 * 
	 * @throws InterruptedException
	 */
	public void waitBeforeUnfollow() throws InterruptedException {
		LocalDateTime now = LocalDateTime.now();
		int currentHour = now.get(ChronoField.HOUR_OF_DAY);
		
		if(currentHour >= 0 && currentHour < 9) {
			LocalDateTime nextMidday = now.truncatedTo(ChronoUnit.DAYS).withHour(12);
			logger.log(String.format("Wait until %s.", nextMidday.toString()), LogLevel.INFO, LoggingAction.Stdout, LoggingAction.File);
			this.waitUntil(nextMidday);
		}else {
			int hoursToWait = 3;
			logger.log(String.format("Wait for %d hours.", hoursToWait), LogLevel.INFO, LoggingAction.Stdout, LoggingAction.File);
			this.waitForHours(hoursToWait);
		}
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 */
	public void waitUntilNextDay() throws InterruptedException {
		long millisBeforeDate = this.getMillisBeforeNextDay();
		Thread.sleep(millisBeforeDate);
	}
	
	/**
	 * 
	 * @param dateUntil
	 * @throws InterruptedException
	 */
	public void waitUntil(LocalDateTime dateUntil) throws InterruptedException {
		long millisBeforeDate = this.getMillisBefore(dateUntil);
		Thread.sleep(millisBeforeDate);
	}
	
	/**
	 * 
	 * @param hours
	 * @throws InterruptedException
	 */
	public void waitForHours(int hours) throws InterruptedException {
		long millisToWait = hours*3600*1000;
		Thread.sleep(millisToWait);
	}
	
	/**
	 * 
	 * @param minutes
	 * @throws InterruptedException
	 */
	public void waitForMinutes(int minutes) throws InterruptedException {
		long millisToWait = minutes*60*1000;
		Thread.sleep(millisToWait);
	}
	
	
	/*
	 *  Privates methods
	 */
	
	public long getMillisBeforeNextDay() {
		LocalDateTime tomorrow = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.DAYS);
		return getMillisBefore(tomorrow);
	}
	
	private long getMillisBefore(LocalDateTime dateTime) {
		LocalDateTime now = LocalDateTime.now();
		return now.until(dateTime, ChronoUnit.MILLIS);
	}

	
	
}
