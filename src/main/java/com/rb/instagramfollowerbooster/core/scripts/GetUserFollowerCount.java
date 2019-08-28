package com.rb.instagramfollowerbooster.core.scripts;

import org.springframework.stereotype.Component;

import com.rb.instagramfollowerbooster.model.scripts.inputs.ScriptInputDto;
import com.rb.instagramfollowerbooster.model.scripts.results.IntValueResult;

@Component
public class GetUserFollowerCount extends AbstractScriptRunner<IntValueResult> {

	@Override
	public IntValueResult getResult(String outputOfScript, String outputErr) throws Exception {
		return new IntValueResult(null, extractScriptOutputValue(outputOfScript));
	}

	@Override
	protected String[] getPythonAdditionnalParameters(ScriptInputDto inputDto) throws Exception {
		if(inputDto.usernameToGetFollowerCount == null || inputDto.usernameToGetFollowerCount.isEmpty())
			throw new Exception("You must provide a inputDto.usernameToGetFollowerCount to call the script 'GetUserFollowerCount'");
		
		return new String [] { "-usernameTarget", inputDto.usernameToGetFollowerCount };
	}

}
