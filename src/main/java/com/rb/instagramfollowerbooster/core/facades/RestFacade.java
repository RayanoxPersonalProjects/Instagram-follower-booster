package com.rb.instagramfollowerbooster.core.facades;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rb.common.api.datafilestorage.DataStorage;
import com.rb.instagramfollowerbooster.core.rest.RestClient;
import com.rb.instagramfollowerbooster.model.rest.GeographicZone;

@Component
public class RestFacade {

	private final static String replacePattern 		= "{TAG_NAME}";
	private final static String urlGeoPeoplePattern = "https://www.instagram.com/explore/tags/" + replacePattern + "/?__a=1";
	
	private static final String KEY_TAG_SEARCHED = "tagSearched";
	
	@Autowired
	DataStorage dataStorage;
	
	@Autowired
	RestClient restClient;
	
	public Collection<String> retrieveIdsFrom(GeographicZone geoZoneInfos) throws Exception {
		String tagSearch = (String) dataStorage.getData(KEY_TAG_SEARCHED, String.class);
		if(tagSearch == null) {
			tagSearch = geoZoneInfos.getLocationId();
			dataStorage.setData(KEY_TAG_SEARCHED, tagSearch);
		}
		
		URL url = new URL(urlGeoPeoplePattern.replace(replacePattern, tagSearch));
		String jsonPath = "$..owner['id']";
		
		List<String> ids = (List<String>) this.restClient.getDatas(url, jsonPath, List.class);
				
		return ids;
	}
}
