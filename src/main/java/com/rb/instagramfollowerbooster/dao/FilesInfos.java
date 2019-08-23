package com.rb.instagramfollowerbooster.dao;

import java.io.File;

public enum FilesInfos {
	WHITELIST("whitelist.txt"),
	UNFOLLOWED("unfollowed.txt"),
	FOLLOWINGS("followed.txt");
		
	private String path;
	private static final String basePath = "workspace";
	
	private FilesInfos(String fileName) {
		this.path = basePath + File.separator + fileName;
	}
	
	
	public String getPath() {
		return path;
	}
	
	public static String getBasepath() {
		return basePath;
	}
}
