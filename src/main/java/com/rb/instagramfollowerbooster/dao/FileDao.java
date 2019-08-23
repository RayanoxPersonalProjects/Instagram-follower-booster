package com.rb.instagramfollowerbooster.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.logging.log4j.util.Strings;

import com.rb.instagramfollowerbooster.exceptions.NotImplementedException;

public abstract class FileDao<ContentFormatType> {
	
	private BufferedReader reader;
	private BufferedWriter writer;
	
	private String lastFilePathUsedForReading;
	private String lastFilePathUsedForWriting;
	
	public FileDao() {
		
	}
	
	/**
	 * This file writes all the content to a file without formatting it.
	 * 
	 * @param path
	 * @param content
	 * @throws IOException 
	 */
	public void writeToFileBrutContent(String path, String content) throws IOException {
		BufferedWriter writer = getWriterForPath(path);
		writer.write(content);
		writer.flush();
	}
	
	/**
	 * Clean all the content of a file
	 * 
	 * @param path
	 * @throws IOException 
	 */
	public void cleanFile(String path) throws IOException{
		this.writeToFileBrutContent(path, Strings.EMPTY);
	}
	
	/**
	 * This function returns the string of all the content of a file 
	 * 
	 * @param path
	 * @param content
	 * @return
	 */
	public String readFromFileBrutContent(String path, String content) throws NotImplementedException {
		throw new NotImplementedException();
	}
	
	public abstract ContentFormatType readFromFile(FilesInfos fileInfo) throws NumberFormatException, IOException;
	public abstract void writeToFile(String path, ContentFormatType content) throws NotImplementedException, IOException;
	
	
	protected BufferedReader getReaderForPath(String path) throws IOException {
		if(lastFilePathUsedForReading != null && lastFilePathUsedForReading.equals(path))
			return this.reader;
		
		if(this.reader != null)
			this.reader.close();
		
		FileReader fileReader = new FileReader(path);
		this.reader = new BufferedReader(fileReader);
		
		this.lastFilePathUsedForReading = path;
		return reader;
	}
	
	protected BufferedWriter getWriterForPath(String path) throws IOException {
		if(lastFilePathUsedForWriting != null && lastFilePathUsedForWriting.equals(path))
			return this.writer;
		
		if(this.writer != null)
			this.writer.close();
		
		FileWriter fileReader = new FileWriter(path);
		this.writer = new BufferedWriter(fileReader);
		
		this.lastFilePathUsedForWriting = path;
		return writer;
	}
}
