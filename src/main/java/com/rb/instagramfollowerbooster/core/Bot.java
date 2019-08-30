package com.rb.instagramfollowerbooster.core;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rb.common.api.logging.LogLevel;
import com.rb.common.api.logging.LogManager;
import com.rb.common.api.logging.LoggingAction;
import com.rb.instagramfollowerbooster.core.scripts.facade.ScriptFacade;
import com.rb.instagramfollowerbooster.dao.FileDataFacade;
import com.rb.instagramfollowerbooster.model.FileIdsList;
import com.rb.instagramfollowerbooster.model.UserSession;
import com.rb.instagramfollowerbooster.model.scripts.results.ErrorCode;
import com.rb.instagramfollowerbooster.model.scripts.results.ErrorCodeResult;


@Component
public class Bot {
	
	public static final int MAX_DAYS_RUNNING = 30;
	
	@Autowired
	private FileDataFacade fileDataFacade;
	
	@Autowired
	private ScriptFacade scriptFacade;
	
	@Autowired
	LogManager logger;
	
	@Autowired
	UserSession session;
	
	@Autowired
	NotificationDelegate notifDelegate;

	private int followerCount;
	private int daysRunningCurrentInstance;
	
	/**
	 *  Here is present the main algorithm of the bot
	 *  @param linkUserToStartFrom link of the account to start the massive follow from.
	 *  @param targetFollowerCount The max follower count targeted before to stop the bot (if no other criteria is reached to stop the bot)
	 *  @param forceStartANewUserInstance If true, a new empty instance will be started, cleaning all the previous datas. If false, a new running of the program will just continue where it last ended.  
	 * @throws Exception 
	 */
	public void StartBooster(String usernameToStartFrom, int targetFollowerCount, boolean forceStartANewUserInstance) throws Exception {
		String idLastUserToProcess = null, idBeforeLastUserToProcess = null;
		
		if(forceStartANewUserInstance || !fileDataFacade.isWorkspaceStarted()){
			this.logger.log("Congratulation ! A new instance of instagram follower booster is starting !", LogLevel.INFO, LoggingAction.All);
			idLastUserToProcess = scriptFacade.RunGetIdFromUsernameScript(usernameToStartFrom);
			fileDataFacade.cleanWorkspace();
			scriptFacade.RunWhitelistScript();
		}else {
			// Retrieve the last followed people from the 'followed' file
			FileIdsList followings = fileDataFacade.readFollowedList();
			idLastUserToProcess = followings.getLastId().toString();
			idBeforeLastUserToProcess = followings.getBeforeLastId().toString();
		}
		
		
		followerCount = scriptFacade.RunGetUserFollowerCount(session.getInstaUsername());
		
		while(followerCount < targetFollowerCount && daysRunningCurrentInstance <= MAX_DAYS_RUNNING) {
			
			String idToProcess; 
			if(idLastUserToProcess != null) {
				idToProcess = idLastUserToProcess;
			}else {
				if(idBeforeLastUserToProcess == null)
					throw new Exception("Either the last user followed and the before-last one are nulls, meaning that there is no users in the 'followed.txt' file");
				idToProcess = idBeforeLastUserToProcess;
			}
			
			// -----
			// Start of cycle work (loop)
			// -----
			
			ErrorCodeResult resultFollow = scriptFacade.RunFollowingScript(idToProcess);
			if(resultFollow.getErrorCode().equals(ErrorCode.Following_limit_Reached)) {
				ErrorCodeResult resultUnfollow = scriptFacade.RunUnfollowScript();
				
				if(!resultUnfollow.IsSuccess())
					this.logger.log("Unsuccess unfollowed detected !", LogLevel.ERROR, LoggingAction.All);
				
			}else if(resultFollow.getErrorCode().equals(ErrorCode.Limit_Per_Day_Reached)) {
				long millisToWaitBeforeNextDay = getMillisBeforeNextDay();
				this.logger.log(String.format("Day limit reached ! Gonna wait %l seconds before next day", millisToWaitBeforeNextDay / 1000), LogLevel.WARN, LoggingAction.File, LoggingAction.Stdout);
				Thread.sleep(millisToWaitBeforeNextDay);
			}else if(resultFollow.getErrorCode().equals(ErrorCode.Unexpected_Error)) {
				this.logger.log(String.format("Unexpected error occured after follow script execution ! \r\n\r\nScriptOutput(formatted) = %s\r\n\r\nEnd of program.", resultFollow.getFormattedOutputScript()), LogLevel.ERROR, LoggingAction.All);
				return;
			}
				
			
			// -----
			// End of cycle (loop)
			// -----
			
			FileIdsList followings = fileDataFacade.readFollowedList();
			String idLastUserToProcessTmp = idLastUserToProcess;
			idLastUserToProcess = followings.getLastId().toString();
			if(idLastUserToProcess.equals(idLastUserToProcessTmp))
				idLastUserToProcess = null;
			idBeforeLastUserToProcess = followings.getBeforeLastId().toString();
			
			followerCount = scriptFacade.RunGetUserFollowerCount(session.getInstaUsername());
			// process Notifications
			this.notifDelegate.processNotificationsIfNecessary(followerCount, targetFollowerCount);
		}
		
		notifyOfEnding(followerCount, targetFollowerCount);
		
		
	}

	private long getMillisBeforeNextDay() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime tomorrowTime = now.plusDays(1);
		LocalDate nextDay = LocalDate.of(tomorrowTime.getYear(), tomorrowTime.getMonth(), tomorrowTime.getDayOfMonth());
		return nextDay.until(now, ChronoUnit.MILLIS);
	}

	private void notifyOfEnding(int followerCount, int targetFollowerCount) throws NumberFormatException, IOException {
		int followersCountAtStarting = fileDataFacade.readWhiteList().getIDsCount();
		String resultResume = String.format("You started from %s followers and now you have %s followers (+ %d).", followersCountAtStarting, followerCount, (followersCountAtStarting - followerCount));
		String endMessage = followerCount >= targetFollowerCount ?
				"Congratulation ! You've just reached the goal of " + targetFollowerCount + " followers."
				: "The instagram bot has just reached the maximum days limit of running, which is " + MAX_DAYS_RUNNING + " days.";
		
		this.logger.log("END OF THE INSTAGRAM BOOSTER INSTANCE !   ---->   " + endMessage + "   ------   " + resultResume, LogLevel.INFO, LoggingAction.File, LoggingAction.Email);
	}

	
	
	
	
}
