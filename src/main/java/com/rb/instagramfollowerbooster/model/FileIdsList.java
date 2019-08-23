package com.rb.instagramfollowerbooster.model;

import java.util.ArrayList;
import java.util.Set;

public class FileIdsList {
	
	/**
	 * A list containing the IDs of a set of instagram users
	 */
	ArrayList<Integer> userIdList;
	
	public FileIdsList() {
		this.userIdList = new ArrayList<>();
	}
	
	public FileIdsList(Set<Integer> collection) {
		this.userIdList = new ArrayList<>(collection);
	}
	
	public void addUserId(int id) {
		this.userIdList.add(id);
	}
	
	public ArrayList<Integer> getUserIdList() {
		return userIdList;
	}
	
	public int getIDsCount() {
		return userIdList.size();
	}
}
