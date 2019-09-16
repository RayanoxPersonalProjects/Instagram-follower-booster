package com.rb.instagramfollowerbooster.model.scripts;

import java.io.File;

public enum ScriptsInfos {

	GET_USER_FOLLOWERS_COUNT("getUserFollowersCount.py"),
	GET_USER_FOLLOWING_COUNT("getUserFollowingsCount.py"),
	GET_USER_ID_FROM_USERNAME("get_user_id_from_username.py"),
	FOLLOW_USER_FOLLOWERS("follow_user_followers.py"),
	UNFOLLOW_EVERYONE("unfollow_everyone.py"), // Everyone but the whitelist
	UNFOLLOW_NON_FOLLOWERS("unfollow_non_followers.py"),
	WHITE_LIST_GENERATORE_FROM_CURRENT_FOLLOWINGS("whitelist_generator_automatic_all_followings.py");
	
	
	private String path;
	private static final String basePath = "Scripts";
	
	private ScriptsInfos(String fileName) {
		this.path = basePath + File.separator + fileName;
	}
	
	
	public String getPath() {
		return path;
	}
}
