package com.rb.instagramfollowerbooster.model.scripts;

import java.io.File;

public enum ScriptsInfos {
	
	FOLLOW_USER_FOLLOWERS("follow_user_followers.py"),
	UNFOLLOW_EVERYONE("unfollow_everyone.py"), // Everyone but the whitelist
	WHITE_LIST_GENERATORE_FROM_CURRENT_FOLLOWINGS("whitelist_generator_automatic_all_followings.py"),
	TEST("test.py");
	
	
	private String path;
	private static final String basePath = "Scripts";
	
	private ScriptsInfos(String fileName) {
		this.path = basePath + File.separator + fileName;
	}
	
	
	public String getPath() {
		return path;
	}
}
