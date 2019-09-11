package com.rb.instagramfollowerbooster.model.rest;

public enum GeographicZone {
	
	FRANCE_SOIREE("soiree");
	
	private String tag;
	
	private GeographicZone(String tag) {
		this.tag = tag;
	}
	
	public String getLocationId() {
		return tag;
	}
}
