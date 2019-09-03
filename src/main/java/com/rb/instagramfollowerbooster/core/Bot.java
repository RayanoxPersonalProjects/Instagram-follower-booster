package com.rb.instagramfollowerbooster.core;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rb.common.api.datafilestorage.DataStorage;
import com.rb.common.api.logging.LogLevel;
import com.rb.common.api.logging.LogManager;
import com.rb.common.api.logging.LoggingAction;
import com.rb.instagramfollowerbooster.core.scripts.facade.ScriptFacade;
import com.rb.instagramfollowerbooster.dao.FileDataFacade;
import com.rb.instagramfollowerbooster.dao.FilesInfos;
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
	
	@Autowired
	DataStorage dataStorage;

	private int followerCount;
	private long daysRunningCurrentInstance = 0;
	
	/**
	 *  Here is present the main algorithm of the bot
	 *  @param linkUserToStartFrom link of the account to start the massive follow from.
	 *  @param targetFollowerCount The max follower count targeted before to stop the bot (if no other criteria is reached to stop the bot)
	 *  @param forceStartANewUserInstance If true, a new empty instance will be started, cleaning all the previous datas. If false, a new running of the program will just continue where it last ended.  
	 * @param skipWhitelistGeneration 
	 * @throws Exception 
	 */
	public void StartBooster(String usernameToStartFrom, int targetFollowerCount, boolean forceStartANewUserInstance, boolean skipWhitelistGeneration) throws Exception {
		String idLastUserToProcess = null, idBeforeLastUserToProcessRandom = null;
		
		followerCount = scriptFacade.RunGetUserFollowerCount(session.getInstaUsername());
		
		
		if(forceStartANewUserInstance || !fileDataFacade.isWorkspaceStarted()){
			this.logger.log("Congratulation ! A new instance of instagram follower booster is starting !", LogLevel.INFO, LoggingAction.All);
			idLastUserToProcess = scriptFacade.RunGetIdFromUsernameScript(usernameToStartFrom);
			if(skipWhitelistGeneration) {
				scriptFacade.RunWhitelistScript();
				fileDataFacade.cleanWorkspace(FilesInfos.WHITELIST);
			}else
				fileDataFacade.cleanWorkspace();
			dataStorage.setData(NotificationDelegate.KEY_DAILY_NOTIF_LAST_FOLLOWERS_COUNT, followerCount);
		}else {
			// Retrieve the last followed people from the 'followed' file
			FileIdsList followings = fileDataFacade.readFollowedList();
			idLastUserToProcess = followings.getLastId().toString();
			idBeforeLastUserToProcessRandom = followings.getBeforeLastId().toString();
		}
		
		
		LocalDate dayStartInstance = LocalDate.now();
		while(followerCount < targetFollowerCount && daysRunningCurrentInstance <= MAX_DAYS_RUNNING) {
			
			String idToProcess; 
			if(idLastUserToProcess != null) {
				idToProcess = idLastUserToProcess;
			}else {
				if(idBeforeLastUserToProcessRandom == null)
					throw new Exception("Either the last user followed and the before-last one are nulls, meaning that there is no users in the 'followed.txt' file");
				idToProcess = idBeforeLastUserToProcessRandom;
			}
			
			// -----
			// Start of cycle work (loop)
			// -----
			
			this.logger.log(String.format("Starting the Follow from followers of user %s !", idToProcess), LogLevel.INFO, LoggingAction.Stdout);
			ErrorCodeResult resultFollow = scriptFacade.RunFollowingScript(idToProcess);
			if(resultFollow.getErrorCode().equals(ErrorCode.Following_limit_Reached)) {
				this.logger.log("Starting the Unfollow script !", LogLevel.INFO, LoggingAction.Stdout);
				ErrorCodeResult resultUnfollow = scriptFacade.RunUnfollowScript();
				
				if(!resultUnfollow.IsSuccess())
					this.logger.log("Unsuccess unfollowed detected !", LogLevel.ERROR, LoggingAction.All);
				else
					this.logger.log("Unfollow successful !", LogLevel.INFO, LoggingAction.Stdout);
				
			}else if(resultFollow.getErrorCode().equals(ErrorCode.Limit_Per_Day_Reached)) {
				long millisToWaitBeforeNextDay = getMillisBeforeNextDay();
				this.logger.log(String.format("Day limit reached ! Gonna wait %d seconds before next day", millisToWaitBeforeNextDay / 1000), LogLevel.WARN, LoggingAction.File, LoggingAction.Stdout);
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
			idBeforeLastUserToProcessRandom = followings.getRandomUser().toString();
			
			followerCount = scriptFacade.RunGetUserFollowerCount(session.getInstaUsername());
			// process Notifications
			this.notifDelegate.processNotificationsIfNecessary(followerCount, targetFollowerCount);
			
			daysRunningCurrentInstance = dayStartInstance.until(LocalDate.now(), ChronoUnit.DAYS);
			
			this.logger.log(String.format("End of cycle. idLastUserToProcess = '%s', idBeforeLastUserToProcessRandom = '%s', followerCount = '%d', daysRunningCurrentInstance = '%d'", idLastUserToProcess, idBeforeLastUserToProcessRandom, followerCount, daysRunningCurrentInstance), LogLevel.INFO, LoggingAction.Stdout);
		}
		
		notifyOfEnding(followerCount, targetFollowerCount);
		
		
	}

	private long getMillisBeforeNextDay() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime tomorrow = now.plusDays(1).truncatedTo(ChronoUnit.DAYS);
		return now.until(tomorrow, ChronoUnit.MILLIS);
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
