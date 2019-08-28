package com.rb.instagramfollowerbooster.model;

import org.springframework.stereotype.Component;

@Component
public class UserSession {
	private String instaUsername;
	private String instaPassword;
	
	public String getInstaPassword() {
		return instaPassword;
	}
	public String getInstaUsername() {
		return instaUsername;
	}
	public void setInstaPassword(String instaPassword) {
		this.instaPassword = instaPassword;
	}
	public void setInstaUsername(String instaUsername) {
		this.instaUsername = instaUsername;
	}
}
