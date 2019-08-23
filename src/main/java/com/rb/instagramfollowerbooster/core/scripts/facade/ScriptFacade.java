package com.rb.instagramfollowerbooster.core.scripts.facade;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rb.instagramfollowerbooster.core.scripts.FollowScript;
import com.rb.instagramfollowerbooster.core.scripts.UnfollowScript;
import com.rb.instagramfollowerbooster.core.scripts.WhiteListScript;
import com.rb.instagramfollowerbooster.model.FileIdsList;
import com.rb.instagramfollowerbooster.model.scripts.ScriptsInfos;

@Service
public class ScriptFacade {
	
	@Autowired
	private FollowScript followRunner;
	@Autowired
	private UnfollowScript unfollowRunner;
	@Autowired
	private WhiteListScript whiteListRunner;
	
	public ScriptFacade() {
		System.out.println("");
	}
	
	public FileIdsList RunFollowingScript() throws IOException {
		return this.followRunner.processScript(ScriptsInfos.FOLLOW_USER_FOLLOWERS).getDatasResult();
	}
	
	public FileIdsList RunUnfollowScript() throws IOException {
		return this.unfollowRunner.processScript(ScriptsInfos.UNFOLLOW_EVERYONE).getDatasResult();
	}
	
	public FileIdsList RunWhitelistScript() throws IOException {
		return this.whiteListRunner.processScript(ScriptsInfos.WHITE_LIST_GENERATORE_FROM_CURRENT_FOLLOWINGS).getDatasResult();
	}
	
	
	
	
	/*
	 *  Setters
	 */
	
	
//	public void setFollowRunner(FollowScript followRunner) {
//		this.followRunner = followRunner;
//	}
//	public void setUnfollowRunner(UnfollowScript unfollowRunner) {
//		this.unfollowRunner = unfollowRunner;
//	}
//	public void setWhiteListRunner(WhiteListScript whiteListRunner) {
//		this.whiteListRunner = whiteListRunner;
//	}
	
}
