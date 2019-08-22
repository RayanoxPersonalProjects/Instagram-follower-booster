package com.rb.instagramfollowerbooster.dao;

import java.io.BufferedReader;
import java.io.IOException;

import com.rb.instagramfollowerbooster.exceptions.NotImplementedException;
import com.rb.instagramfollowerbooster.model.FileIdsList;

public class IdListFileDao extends FileDao<FileIdsList> {

	@Override
	public FileIdsList readFromFile(String path) throws NumberFormatException, IOException {
		BufferedReader reader = getReaderForPath(path);
		FileIdsList result = new FileIdsList();
		
		String currentLineRead;
		while((currentLineRead = reader.readLine()) != null) {
			int id = Integer.parseInt(currentLineRead);
			result.addUserId(id);
		}
		
		return result;
	}

	@Override
	public void writeToFile(String path, FileIdsList content) throws NotImplementedException {
		throw new NotImplementedException("The method has not been implemented due to a useless state.");
	}
}
