package com.rb.instagramfollowerbooster.core.scripts;

import org.springframework.stereotype.Component;

import com.rb.instagramfollowerbooster.model.scripts.inputs.ScriptInputDto;
import com.rb.instagramfollowerbooster.model.scripts.results.BooleanValueResult;

@Component
public class WhiteListScript extends AbstractScriptRunner<BooleanValueResult> {

	@Override
	public BooleanValueResult getResult(String outputOfScript, String outputErr) {
		if(outputErr.contains("ERR"))
			return new BooleanValueResult(null, false);
		return new BooleanValueResult(null, true);
	}

	@Override
	protected String[] getPythonAdditionnalParameters(ScriptInputDto inputDto) throws Exception {
		return null;
	}

}
