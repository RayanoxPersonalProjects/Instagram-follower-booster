package com.rb.instagramfollowerbooster.core;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.rb.common.api.datafilestorage.DataStorage;
import com.rb.common.api.logging.LogLevel;
import com.rb.common.api.logging.LogManager;
import com.rb.common.api.logging.LoggingAction;

@Component
public class NotificationDelegate {
	
	private static final int PERIODIC_POURCENT_PROGRESSION_NOTIFICATION = 20; // Every 20 % of progression, a mail is sent to notify the advancement.

	private static final String KEY_DAILY_NOTIF_LAST_DAY = "last_day_dailyNotif";
	static final String KEY_DAILY_NOTIF_LAST_FOLLOWERS_COUNT = "last_follower_count";
	private static final String KEY_DAILY_NOTIF_LAST_POURCENT_STEP_PROGRESSION = "last_pourcent_progression";
	
	public static final String KEY_STARTING_FOLLOWERS_COUNT = "starting_folowers_count";

	@Autowired
	DataStorage dataStorage;
	
//	@Autowired
//	LogManager loggerImportantNotifs;
	
	@Autowired
	LogManager logger;
	
	public void processNotificationsIfNecessary(int followerCount, int targetFollowerCount) throws Exception {
		
		processPeriodicImportantNotifs(followerCount, targetFollowerCount);
		processDailyNotifs(followerCount);
	}

	private void processDailyNotifs(int followerCount) throws Exception {
		LocalDate lastDateNotif = (LocalDate) this.dataStorage.getData(KEY_DAILY_NOTIF_LAST_DAY, LocalDate.class);
		LocalDate today = LocalDate.now();
		
		if(lastDateNotif == null) {
			this.dataStorage.setData(KEY_DAILY_NOTIF_LAST_DAY, today); //To avoid sending notif for the first day
			return;
		}
		if(lastDateNotif.isEqual(today))
			return;
		
		
		Integer lastFollowerCount = (Integer) dataStorage.getData(KEY_DAILY_NOTIF_LAST_FOLLOWERS_COUNT, Integer.class);
		Integer followerEarnSinceLastTime = followerCount - lastFollowerCount;
		
		logger.log(String.format("Daily notification: NB followers = " + followerCount + " (+%d).", followerEarnSinceLastTime), LogLevel.INFO, LoggingAction.Email, LoggingAction.File);
		
		// Store datas for next iteration
		dataStorage.setData(KEY_DAILY_NOTIF_LAST_FOLLOWERS_COUNT, followerCount);
		this.dataStorage.setData(KEY_DAILY_NOTIF_LAST_DAY, today);
	}
	
	private void processPeriodicImportantNotifs(int followerCount, int targetFollowerCount) throws Exception {
		Integer lastPourcentStepProgression = (Integer) dataStorage.getData(KEY_DAILY_NOTIF_LAST_POURCENT_STEP_PROGRESSION, Integer.class, 0);
		Integer startingFollowerCount = (Integer) dataStorage.getData(KEY_STARTING_FOLLOWERS_COUNT, Integer.class);

		int followerCountFromStart = startingFollowerCount - followerCount;
		Integer currentPourcentProgression = followerCountFromStart * 100 / targetFollowerCount;
		
		Integer newStep = lastPourcentStepProgression + PERIODIC_POURCENT_PROGRESSION_NOTIFICATION;
		if(currentPourcentProgression > newStep) {
			LocalDate startingDate = (LocalDate) dataStorage.getData(Bot.KEY_STARTING_DATE, LocalDate.class);
			LocalDate endingDate = predictEndingDate(startingDate, targetFollowerCount, followerCountFromStart);
			logger.log(String.format("Periodic step reached !  -> Step of %d %% (current followers count = %d / %d total followers goal)! Estimation date of the end = %s", newStep, followerCountFromStart, targetFollowerCount, endingDate.toString()), LogLevel.INFO, LoggingAction.Email, LoggingAction.File, LoggingAction.Stdout);
			this.dataStorage.setData(KEY_DAILY_NOTIF_LAST_POURCENT_STEP_PROGRESSION, newStep);
		}
	}
	
	private LocalDate predictEndingDate(LocalDate startingDate, int followersTarget, int currentNbFollowers) {
		long daysDone = startingDate.until(LocalDate.now(), ChronoUnit.DAYS);
		long totalDaysEstimation = (daysDone * followersTarget) / currentNbFollowers;
		return startingDate.plus(totalDaysEstimation, ChronoUnit.DAYS);
	}
}
