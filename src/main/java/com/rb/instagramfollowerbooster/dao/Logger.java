package com.rb.instagramfollowerbooster.dao;

import java.io.BufferedWriter;
import java.io.IOException;
import org.springframework.stereotype.Component;
import com.rb.instagramfollowerbooster.exceptions.NotImplementedException;

@Component
public class Logger extends FileDao<String> {
	
	public static final String pathLogger = "Logs.txt"; 
		
	private void log(String message) {
		try {
			writeToFile(pathLogger, message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void info(String message) {
		this.log("[INFO] " + message);
	}
	
	public void error(String message) {
		this.log("[ERROR] " + message);
	}


	@Override
	public void writeToFile(String path, String content) throws IOException {
		BufferedWriter writer = this.getWriterForPath(path);
		writer.append(content);
		writer.flush();
	}
	
	@Override
	public String readFromFile(FilesInfos fileInfo) throws NumberFormatException, IOException {
		try {
			throw new NotImplementedException();
		} catch (NotImplementedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
