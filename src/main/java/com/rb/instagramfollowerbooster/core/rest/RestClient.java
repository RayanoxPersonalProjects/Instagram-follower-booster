package com.rb.instagramfollowerbooster.core.rest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.stereotype.Component;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@Component
public class RestClient {
	
	public static <T> T getDatas(URL url, String jsonPath, Class<T> classResult) throws MalformedURLException, IOException {
		DocumentContext jsonContext = JsonPath.parse(url);
		
		T result = jsonContext.read(jsonPath, classResult);
		return result;
	}
}
