package com.rb.instagramfollowerbooster.model.scripts.results;

import java.util.Set;

import com.rb.instagramfollowerbooster.model.FileIdsList;

public class ListIdsResult extends AbstractScriptResult<FileIdsList> {

	private FileIdsList newFollowings;
	
	public ListIdsResult(boolean isSuccess, Set<Integer> newFollowings) {
		super(isSuccess);
		this.newFollowings = new FileIdsList(newFollowings);
	}

	@Override
	public FileIdsList getDatasResult() {
		return this.newFollowings;
	}
}
