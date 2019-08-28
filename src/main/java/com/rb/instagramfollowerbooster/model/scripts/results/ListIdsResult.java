package com.rb.instagramfollowerbooster.model.scripts.results;

import java.util.Set;

import com.rb.instagramfollowerbooster.model.FileIdsList;

public class ListIdsResult extends AbstractScriptResult<FileIdsList> {

	private FileIdsList newFollowings;
	
	public ListIdsResult(ErrorCode errorCode, Set<Integer> newFollowings) {
		super(errorCode);		
		
		if(newFollowings != null)
			this.newFollowings = new FileIdsList(newFollowings);
	}

	@Override
	public FileIdsList getDatasResult() {
		return this.newFollowings;
	}
}
