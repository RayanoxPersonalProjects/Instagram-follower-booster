package com.rb.instagramfollowerbooster.core.scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.rb.instagramfollowerbooster.model.scripts.ScriptsInfos;
import com.rb.instagramfollowerbooster.model.scripts.results.AbstractScriptResult;

public abstract class AbstractScriptRunner<ResultType> {
			
	
	
	public ResultType processScript(ScriptsInfos scriptInfos) throws IOException {
		
		String command = "python .." + File.separator + scriptInfos.getPath();
		File workspace = new File("workspace");
		
		Process process = Runtime.getRuntime().exec(command, null, workspace);
		String output = readContentOfOutputStream(process.getInputStream());
		
		return getResult(output);
	}
	
	protected abstract ResultType getResult(String outputOfScript);
	
	
	/*
	 *  Privates methods
	 */
	
	private String readContentOfOutputStream(InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String result = "";
		String line;
		
		while( (line = reader.readLine()) != null) {
			result += line;
		}
		
		return result;
	}
	
}
