package com.rb.instagramfollowerbooster.utils;

public class Globals {
	
	public static boolean isWindowsEnvironment() {
		String OS = System.getProperty("os.name").toLowerCase();
		return (OS.indexOf("win") >= 0);
	}
}
