package com.rb.instagramfollowerbooster.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.rb.instagramfollowerbooster.model.FileIdsList;

@Component
public class FileDataFacade {
	
	@Autowired
	private IdListFileDao idListFileDao;
	
	public FileIdsList readWhiteList() throws NumberFormatException, IOException {
		FileIdsList result = idListFileDao.readFromFile(FilesInfos.WHITELIST);
		return result;
	}

	public FileIdsList readFollowedList() throws NumberFormatException, IOException {
		FileIdsList result = idListFileDao.readFromFile(FilesInfos.FOLLOWINGS);
		return result;
	}
	
	public void cleanFollowedFile() throws IOException{
		idListFileDao.cleanFile(FilesInfos.FOLLOWINGS.getPath());
	}

	public FileIdsList readUnfollowedList() throws NumberFormatException, IOException {
		FileIdsList result = idListFileDao.readFromFile(FilesInfos.UNFOLLOWED);
		return result;
	}
	
	/**
	 * 
	 * @return false if the workspace folder doesn't exist or if it is empty.
	 * 
	 */
	public boolean isWorkspaceStarted() {
		File workspaceDir = new File(FilesInfos.getBasepath());
		if(!workspaceDir.exists())
			return false;
		if(!workspaceDir.isDirectory())
			return false;
		if(workspaceDir.listFiles().length <= 0)
			return false;
		
		return true;
	}

	public void cleanWorkspace() throws FileNotFoundException {
		File workspaceDir = new File(FilesInfos.getBasepath());
		if(!workspaceDir.exists())
			throw new FileNotFoundException(String.format("The workspace folder (at expected location '%s') has not been found", FilesInfos.getBasepath()));
		else
			if(!workspaceDir.isDirectory())
				throw new FileNotFoundException(String.format("The workspace folder (at expected location '%s') has not been found", FilesInfos.getBasepath()));
				
		File[] files = workspaceDir.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            f.delete();
	        }
	    }
	}
}
