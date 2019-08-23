package com.rb.instagramfollowerbooster.model.scripts.results;

public abstract class AbstractScriptResult<DataResult> {
	
	private boolean isSuccess;
	
	public AbstractScriptResult(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	public abstract DataResult getDatasResult();
	
	public boolean IsSuccess() {
		return this.isSuccess;
	}
	
}
