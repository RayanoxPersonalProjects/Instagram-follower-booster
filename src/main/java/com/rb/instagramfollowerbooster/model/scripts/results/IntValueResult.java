package com.rb.instagramfollowerbooster.model.scripts.results;

public class IntValueResult extends AbstractScriptResult<Integer> {

	private Integer value;
	
	public IntValueResult(ErrorCode errorCode, String value) {
		super(errorCode);
		
		if(value != null)
			this.value = Integer.parseInt(value);
	}
	
	public IntValueResult(ErrorCode errorCode, Integer value) {
		super(errorCode);
		
		if(value != null)
			this.value = value;
	}

	@Override
	public Integer getDatasResult() {
		return this.value;
	}

}
