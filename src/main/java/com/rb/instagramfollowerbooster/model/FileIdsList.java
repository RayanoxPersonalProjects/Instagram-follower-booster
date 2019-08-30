package com.rb.instagramfollowerbooster.model;

import java.util.ArrayList;
import java.util.Set;

public class FileIdsList {
	
	/**
	 * A list containing the IDs of a set of instagram users
	 */
	ArrayList<Long> userIdList;
	
	public FileIdsList() {
		this.userIdList = new ArrayList<>();
	}
	
	public FileIdsList(Set<Long> collection) {
		this.userIdList = new ArrayList<>(collection);
	}
	
	public void addUserId(long id) {
		this.userIdList.add(id);
	}
	
	public ArrayList<Long> getUserIdList() {
		return userIdList;
	}
	
	public int getIDsCount() {
		return userIdList.size();
	}
	
	public Long getLastId() {
		if(this.getIDsCount() <= 0)
			return null;
		
		return this.userIdList.get(getIDsCount()-1);
	}
	public Long getBeforeLastId() {
		if(this.getIDsCount() <= 1)
			return null;
		
		return this.userIdList.get(getIDsCount()-2);
	}
}
