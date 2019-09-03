package com.rb.instagramfollowerbooster.dao;

import java.io.File;

public enum FilesInfos {
	WHITELIST("whitelist.txt"),
	UNFOLLOWED("unfollowed.txt"),
	FOLLOWINGS("followed.txt");
		
	private String fileName;
	private String path;
	private static final String basePath = "workspace";
	
	private FilesInfos(String fileName) {
		this.path = basePath + File.separator + fileName;
		this.fileName = fileName;
	}
	
	
	public String getPath() {
		return path;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public static String getBasepath() {
		return basePath;
	}
	
	public static boolean contains(String fileName, FilesInfos... filesInfos) {
		for (FilesInfos currentFilesInfo : filesInfos) {
			if(currentFilesInfo.getFileName().equals(fileName))
				return true;
		}
		return false;
	}
}
