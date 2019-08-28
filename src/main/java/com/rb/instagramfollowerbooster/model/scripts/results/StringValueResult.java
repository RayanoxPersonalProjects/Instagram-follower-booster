package com.rb.instagramfollowerbooster.model.scripts.results;

public class StringValueResult extends AbstractScriptResult<String> {

	private String value;
	
	public StringValueResult(ErrorCode errorCode, String value) {
		super(errorCode);
		
		if(value != null)
			this.value = value;
	}

	@Override
	public String getDatasResult() {
		return this.value;
	}

}
