package com.rb.instagramfollowerbooster.core.scripts;

import org.springframework.stereotype.Component;
import com.rb.instagramfollowerbooster.model.scripts.inputs.ScriptInputDto;
import com.rb.instagramfollowerbooster.model.scripts.results.ErrorCode;
import com.rb.instagramfollowerbooster.model.scripts.results.ErrorCodeResult;

@Component
public class FollowScript extends AbstractScriptRunner<ErrorCodeResult> {

	@Override
	protected ErrorCodeResult getResult(String outputOfScript, String outputErr) {
//		if(isLimitDetectedAtEndOfScript(outputErr))
//			return new BooleanValueResult(ErrorCode.Following_limit_Reached, false, outputOfScript, outputErr);
		
		if(outputErr.contains("Out of ") && outputErr.contains("for today."))
			return new ErrorCodeResult(ErrorCode.Limit_Per_Day_Reached, outputOfScript, outputErr);
		
		if(outputErr.contains("ERR") || outputErr.contains("Error:"))
			return new ErrorCodeResult(ErrorCode.Unexpected_Error, outputOfScript, outputErr);
		
		return new ErrorCodeResult(ErrorCode.None, outputOfScript, outputErr);
	}


	@Override
	protected String[] getPythonAdditionnalParameters(ScriptInputDto inputDto) throws Exception {
		if(inputDto.userIdToFollowHisFollowers == null || inputDto.userIdToFollowHisFollowers.isEmpty())
			throw new Exception("You must provide a inputDto.userIdToFollowHisFollowers to call the script 'FollowScript'");
		
		return new String [] {"-users" , inputDto.userIdToFollowHisFollowers};
	}

	/*
	 *  Privates methods
	 */
	
//	private boolean isLimitDetectedAtEndOfScript(String outputErr) {
//		Pattern pattern = Pattern.compile("INFO - Total follows: (5/5)");
//		Matcher matcher = pattern.matcher(outputErr);
//		if(!matcher.find()) {
//			this.logger.log("The total follow information was not displayed a the end of the follow script", LogLevel.WARN, new LoggingAction[] {LoggingAction.File});
//			return false;
//		}
//		
//		String ratioString = matcher.group(1);
//		String [] ratioValues = ratioString.split("/");
//		
//		return ratioValues[0].equals(ratioValues[1]);
//	}
}
