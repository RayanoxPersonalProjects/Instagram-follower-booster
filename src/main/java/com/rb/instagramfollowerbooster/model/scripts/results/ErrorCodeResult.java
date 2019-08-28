package com.rb.instagramfollowerbooster.model.scripts.results;

public class ErrorCodeResult extends AbstractScriptResult<ErrorCode> {

	public ErrorCodeResult(ErrorCode errorCode, String scriptOutput, String scriptOutputErr) {
		super(errorCode, scriptOutput, scriptOutputErr);
	}

	@Override
	public ErrorCode getDatasResult() {
		return this.getErrorCode();
	}

}
