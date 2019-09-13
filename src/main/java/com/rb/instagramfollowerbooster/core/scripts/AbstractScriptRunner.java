package com.rb.instagramfollowerbooster.core.scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import com.rb.common.api.datafilestorage.DataStorage;
import com.rb.common.api.logging.LogLevel;
import com.rb.common.api.logging.LogManager;
import com.rb.common.api.logging.LoggingAction;
import com.rb.instagramfollowerbooster.InstagramFollowerBoosterApplication;
import com.rb.instagramfollowerbooster.model.UserSession;
import com.rb.instagramfollowerbooster.model.scripts.ScriptsInfos;
import com.rb.instagramfollowerbooster.model.scripts.inputs.ScriptInputDto;
import com.rb.instagramfollowerbooster.utils.Globals;

public abstract class AbstractScriptRunner<ResultType> {
			
	private static final long TIMEOUT_FOLLOW_PROCESS = 1; // in hours

	
	@Autowired
	DataStorage dataStorage;
	
	@Autowired
	UserSession userSession;
	
	@Autowired
	LogManager logger;
	
	public ResultType processScript(ScriptsInfos scriptInfos, ScriptInputDto inputDto) throws Exception {
		
		if(userSession == null || userSession.getInstaUsername() == null || userSession.getInstaUsername().isEmpty() || userSession.getInstaPassword() == null || userSession.getInstaPassword().isEmpty())
			throw new Exception("The login and/or the password (of instagram account) is missing to run the script.");
		
		String pythonDirPath = (String) dataStorage.getData(InstagramFollowerBoosterApplication.ARG_NAME_PYTHON_PATH, String.class);
		
		String command = Globals.isWindowsEnvironment() ? "python" : "python3";
		if(pythonDirPath != null && !pythonDirPath.isEmpty())
			command = pythonDirPath + File.separator + command;
		
		String scriptPath = ".." + File.separator + scriptInfos.getPath();
			
		String [] pythonArgs = new String [] {"-u", userSession.getInstaUsername(), "-p", userSession.getInstaPassword()};
		pythonArgs = addArrayValuesToArray(pythonArgs, getPythonAdditionnalParameters(inputDto));
		
		
		String[] commandWithParameters;
        if(Globals.isWindowsEnvironment())
        	commandWithParameters = new String[] { "cmd", "/C", command, scriptPath};
        else
        	commandWithParameters = new String[] { command, scriptPath};
		commandWithParameters = addArrayValuesToArray(commandWithParameters, pythonArgs);
		File workspace = new File("workspace");
		if(!workspace.exists())
			workspace.mkdirs();
		
		
		
		ProcessBuilder builderProc = new ProcessBuilder(commandWithParameters);
		builderProc.directory(workspace);
		builderProc.redirectErrorStream(true);
		Process process = builderProc.start();
		
//		if(!process.waitFor(TIMEOUT_FOLLOW_PROCESS, TimeUnit.HOURS))
//			process.destroyForcibly();
		
		
		
		
		String output = readContentOfOutputStream(process.getInputStream());
//		String outputErr = readContentOfOutputStream(process.getErrorStream());
		String outputErr = output;
		if(outputErr.contains("ERR") || outputErr.contains("Error:"))
			logger.log("Error logs have been detected at the (err) output of the script " + scriptInfos.name() + ". More details: " + outputErr, LogLevel.ERROR, new LoggingAction[] {LoggingAction.File});
		
		return getResult(output, outputErr);
	}
	
	private String[] addArrayValuesToArray(String[] pythonArgs, String[] pythonAdditionnalParameters) {
		if(pythonAdditionnalParameters == null || pythonAdditionnalParameters.length == 0)
			return pythonArgs;
		
		String [] result = new String[pythonArgs.length + pythonAdditionnalParameters.length];
		for(int i = 0; i < pythonArgs.length; i++) {
			result[i] = pythonArgs[i];
		}
		for(int i = pythonArgs.length; i < result.length; i++) {
			result[i] = pythonAdditionnalParameters[i - pythonArgs.length];
		}
		return result;
	}

	protected abstract String[] getPythonAdditionnalParameters(ScriptInputDto inputDto) throws Exception;
	protected abstract ResultType getResult(String outputOfScript, String outputErr) throws Exception;
	
	
	/*
	 *  Privates methods
	 */
	
	private String readContentOfOutputStream(InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String result = "";
		String line;
		
		while( (line = reader.readLine()) != null) {
			result += line + "\r\n";
		}
		
		return result;
	}
	
	protected String extractScriptOutputValue(String output) throws Exception {
		Pattern pattern = Pattern.compile("\\[\\[\\[(.*)\\]\\]\\]");
		Matcher matcher = pattern.matcher(output);
		if(!matcher.find())
			throw new Exception("The result has not been found inside the ouput of the script. Output = " + output);
		String result = matcher.group(1);
		return result;
	}
	
}
