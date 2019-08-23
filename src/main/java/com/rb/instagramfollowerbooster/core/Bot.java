package com.rb.instagramfollowerbooster.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.rb.instagramfollowerbooster.core.scripts.facade.ScriptFacade;
import com.rb.instagramfollowerbooster.dao.FileDataFacade;

@Component
public class Bot {
	
	public static final int MAX_DAYS_RUNNING = 30;
	
	@Autowired
	private FileDataFacade fileDataFacade;
	
	@Autowired
	private ScriptFacade scriptFacade;
	
	@Autowired
	Logger logger;

	private int followerCount;
	private int daysRunningCurrentInstance;
	
	/**
	 *  Here is present the main algorithm of the bot
	 *  @param linkUserToStartFrom link of the account to start the massive follow from.
	 *  @param targetFollowerCount The max follower count targeted before to stop the bot (if no other criteria is reached to stop the bot)
	 *  @param forceStartANewUserInstance If true, a new empty instance will be started, cleaning all the previous datas. If false, a new running of the program will just continue where it last ended.  
	 * @throws Exception 
	 */
	public void StartBooster(int userIdToStartFrom, int targetFollowerCount, boolean forceStartANewUserInstance) throws Exception {
			
		if(forceStartANewUserInstance || !fileDataFacade.isWorkspaceStarted()){
			fileDataFacade.cleanWorkspace();
			scriptFacade.RunWhitelistScript();
		}
		
		//followerCount = scriptFacade.RunGetFollowerCount();
		
		while(followerCount < targetFollowerCount && daysRunningCurrentInstance <= MAX_DAYS_RUNNING) {
			//TODO Gerer le cas ou un quota journalier est atteint dans un script (attente jusqu'au jour suivant normalement)
			
		}
		
		
		// Check if the whitelist file is not empty, otherwise call the appropriate script to fill it.

		// Get from parameters an ID of a user I want to start the browse (or the url of the user, and I will retrieve the ID)
		
		// Loop (while follower count < targetFollowerCount or if MAX_DAYS_RUNNING has been reached)
			// Check that the "following" file is empty, otherwise empty it.
			// Call the "follow" script
			// Retrieve all the following IDs from the "following" file
			// Store (append) the IDs to a storage file (own file)
			// Get the old total account data stored and add the new following count
			// If a following limit has been reached, then process the "unfollow" script after getting last ID of the followings file
		
			// One time a day, Retrieve the new amount of followers and store it to a log file (specific to the follower count). => Statistics logger
		
		// Log some informations like the reason why the program stopped and a small recap of the statistics results 
	}

}
