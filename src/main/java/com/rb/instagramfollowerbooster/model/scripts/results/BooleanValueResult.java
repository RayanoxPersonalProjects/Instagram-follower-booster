package com.rb.instagramfollowerbooster.model.scripts.results;

public class BooleanValueResult extends AbstractScriptResult<Boolean> {

	private Boolean value;
	
	public BooleanValueResult(ErrorCode errorCode, Boolean value) {
		super(errorCode);
		
		if(value != null)
			this.value = value;
	}
	public BooleanValueResult(ErrorCode errorCode, Boolean value, String scriptOutput, String scriptOutputErr) {
		super(errorCode, scriptOutput, scriptOutputErr);
		
		if(value != null)
			this.value = value;
	}

	@Override
	public Boolean getDatasResult() {
		return this.value;
	}

}
