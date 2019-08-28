package com.rb.instagramfollowerbooster.model.scripts.results;

public abstract class AbstractScriptResult<DataResult> {
	
	private boolean isSuccess;
	private ErrorCode errorCode;
	private String scriptOutput;
	private String scriptOutputErr;
	
	/**
	 * IsSuccess of errorCode is null, false otherwise.
	 * @param errorCode
	 */
	public AbstractScriptResult(ErrorCode errorCode) {
		this.errorCode = errorCode;
		if(errorCode == null || errorCode.equals(ErrorCode.None)) {
			this.isSuccess = true;
			return;
		}
			
		this.isSuccess = false;
	}
	
	/**
	 * IsSuccess of errorCode is null, false otherwise.
	 * @param errorCode
	 */
	public AbstractScriptResult(ErrorCode errorCode, String scriptOutput, String scriptOutputErr) {
		this(errorCode);
		this.scriptOutput = scriptOutput;
		this.scriptOutputErr = scriptOutputErr;
	}
	
	
	public abstract DataResult getDatasResult();
	
	public boolean IsSuccess() {
		return this.isSuccess;
	}
	
	public ErrorCode getErrorCode() {
		return errorCode;
	}	
	
	public String getFormattedOutputScript() {
		if(this.scriptOutput == null && this.scriptOutputErr == null)
			return null;
		return this.scriptOutput == null ?
				this.scriptOutputErr
				: this.scriptOutputErr == null ?
						this.scriptOutput
						: this.scriptOutput + "\r\n\r\n (OUTPUT ERR)\r\n\r\n" + this.scriptOutputErr;
	}
}
