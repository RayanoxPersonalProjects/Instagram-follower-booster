package com.rb.instagramfollowerbooster.core.facades;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rb.instagramfollowerbooster.core.scripts.FollowScript;
import com.rb.instagramfollowerbooster.core.scripts.GetIdFromUsernameScript;
import com.rb.instagramfollowerbooster.core.scripts.GetUserFollowerCount;
import com.rb.instagramfollowerbooster.core.scripts.UnfollowScript;
import com.rb.instagramfollowerbooster.core.scripts.WhiteListScript;
import com.rb.instagramfollowerbooster.model.scripts.ScriptsInfos;
import com.rb.instagramfollowerbooster.model.scripts.inputs.ScriptInputDto;
import com.rb.instagramfollowerbooster.model.scripts.results.ErrorCodeResult;

@Service
public class ScriptFacade {
	
	@Autowired
	private FollowScript followRunner;
	@Autowired
	private UnfollowScript unfollowRunner;
	@Autowired
	private WhiteListScript whiteListRunner;
	@Autowired
	private GetIdFromUsernameScript getIdScript;
	@Autowired
	private GetUserFollowerCount getUserFollowerCount;

	/**
	 * 
	 * @param userId The ID of the user you want to follow his followers.
	 * @return
	 * @throws Exception
	 */
	public ErrorCodeResult RunFollowingScript(String userId) throws Exception {
		ScriptInputDto input = new ScriptInputDto();
		input.userIdToFollowHisFollowers = userId;
		return this.followRunner.processScript(ScriptsInfos.FOLLOW_USER_FOLLOWERS, input);
	}
	
	public ErrorCodeResult RunUnfollowScript() throws Exception {
		return this.unfollowRunner.processScript(ScriptsInfos.UNFOLLOW_EVERYONE, null);
	}
	
	public Boolean RunWhitelistScript() throws Exception {
		return this.whiteListRunner.processScript(ScriptsInfos.WHITE_LIST_GENERATORE_FROM_CURRENT_FOLLOWINGS, null).getDatasResult();
	}

	// OK
	public String RunGetIdFromUsernameScript(String usernameToGetId) throws Exception {
		ScriptInputDto input = new ScriptInputDto();
		input.usernameToGetId = usernameToGetId;
		return this.getIdScript.processScript(ScriptsInfos.GET_USER_ID_FROM_USERNAME, input).getDatasResult();
	}
	
	public int RunGetUserFollowerCount(String usernameToGetFollowerCount) throws Exception {
		ScriptInputDto input = new ScriptInputDto();
		input.usernameToGetFollowerCount= usernameToGetFollowerCount;
		return this.getUserFollowerCount.processScript(ScriptsInfos.GET_USER_FOLLOWERS_COUNT, input).getDatasResult();
	}
	
}
