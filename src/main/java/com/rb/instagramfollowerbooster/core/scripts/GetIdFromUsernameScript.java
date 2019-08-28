package com.rb.instagramfollowerbooster.core.scripts;

import org.springframework.stereotype.Component;

import com.rb.instagramfollowerbooster.model.scripts.inputs.ScriptInputDto;
import com.rb.instagramfollowerbooster.model.scripts.results.StringValueResult;

@Component
public class GetIdFromUsernameScript extends AbstractScriptRunner<StringValueResult> {

	@Override
	public StringValueResult getResult(String outputOfScript, String outputErr) throws Exception {
		return new StringValueResult(null, extractScriptOutputValue(outputOfScript));
	}

	@Override
	protected String[] getPythonAdditionnalParameters(ScriptInputDto inputDto) throws Exception {
		if(inputDto.usernameToGetId == null || inputDto.usernameToGetId.isEmpty())
			throw new Exception("You must provide a inputDto.usernameToGetId to call the script 'GetIdFromUsernameScript'");
		
		return new String [] { "-usernameTarget", inputDto.usernameToGetId };
	}

}
