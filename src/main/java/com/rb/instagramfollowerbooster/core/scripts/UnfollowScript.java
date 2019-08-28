package com.rb.instagramfollowerbooster.core.scripts;

import org.springframework.stereotype.Component;
import com.rb.instagramfollowerbooster.model.scripts.inputs.ScriptInputDto;
import com.rb.instagramfollowerbooster.model.scripts.results.BooleanValueResult;
import com.rb.instagramfollowerbooster.model.scripts.results.ErrorCode;
import com.rb.instagramfollowerbooster.model.scripts.results.ErrorCodeResult;

@Component
public class UnfollowScript extends AbstractScriptRunner<ErrorCodeResult> {

	@Override
	public ErrorCodeResult getResult(String outputOfScript, String outputErr) {
		if(outputErr.contains("ERR") || outputErr.contains("Error:"))
			return new ErrorCodeResult(ErrorCode.Unexpected_Error, outputOfScript, outputErr);
		return new ErrorCodeResult(ErrorCode.None, outputOfScript, outputErr);
	}

	@Override
	protected String[] getPythonAdditionnalParameters(ScriptInputDto inputDto) throws Exception {
		return null;
	}

}
