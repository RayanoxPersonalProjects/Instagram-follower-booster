package com.rb.instagramfollowerbooster.model;

import java.util.ArrayList;

public class FileIdsList {
	
	/**
	 * A list containing the IDs of a set of instagram users
	 */
	ArrayList<Integer> userIdList;
	
	public FileIdsList() {
		this.userIdList = new ArrayList<>();
	}
	
	public void addUserId(int id) {
		this.userIdList.add(id);
	}
	
	public ArrayList<Integer> getUserIdList() {
		return userIdList;
	}
}
