package com.rb.instagramfollowerbooster.core;

import org.springframework.beans.factory.annotation.Autowired;

import com.rb.instagramfollowerbooster.dao.IdListFileDao;

public class Bot {
	
	public static final int MAX_DAYS_RUNNING = 30;
	
	@Autowired
	IdListFileDao idListFileDao;
	
	
	/**
	 *  Here is present the main algorithm of the bot
	 */
	public void StartBooster(String linkUserToStartFrom, int targetFollowerCount) {
		System.out.println("idListFileDao = " + idListFileDao);
		
		// Check if the whitelist file is not empty, otherwise call the appropriate script to fill it.

		// Get from parameters an ID of a user I want to start the browse (or the url of the user, and I will retrieve the ID)
		
		// Loop (while follower count < targetFollowerCount or if MAX_DAYS_RUNNING has been reached)
			// Check that the "following" file is empty, otherwise empty it.
			// Call the "follow" script
			// Retrieve all the following IDs from the "following" file
			// Store (append) the IDs to a storage file (own file)
			// Get the old total account data stored and add the new following count
			// If a following limit has been reached, then process the "unfollow" script after getting last ID of the followings file
		
			// One time a day, Retrieve the new amount of followers and store it to a log file (specific to the follower count).
		
		// Log some informations like the reason why the program stopped and a small recap of the statistics results 
	}
	
	
	
	public void setIdListFileDao(IdListFileDao idListFileDao) {
		this.idListFileDao = idListFileDao;
	}
}
