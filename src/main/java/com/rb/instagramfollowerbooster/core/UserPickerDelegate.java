package com.rb.instagramfollowerbooster.core;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.rb.common.api.datafilestorage.DataStorage;
import com.rb.common.api.logging.LogLevel;
import com.rb.common.api.logging.LogManager;
import com.rb.common.api.logging.LoggingAction;
import com.rb.instagramfollowerbooster.core.facades.RestFacade;
import com.rb.instagramfollowerbooster.core.facades.ScriptFacade;
import com.rb.instagramfollowerbooster.model.rest.GeographicZone;

@Component
public class UserPickerDelegate {
	
	private static final String KEY_ALREADY_PROCESSED_IDS = "alreadyProcessedIds";
	private static final String KEY_FILTERED_IDS = "filteredIds";
	private static final int FILTER_NB_FOLLOWERS_MAX = 2000; //To try to avoid the international people (who probably have too many followers)
	
	@Autowired
	DataStorage dataStorage;
	
	@Autowired
	ScriptFacade scriptFacade;
	
	@Autowired
	RestFacade restFacade;
	
	@Autowired
	LogManager logger;
	
	/**
	 * 
	 * @return the ID of the next user to process the "follow_user_followers" script
	 * @throws Exception
	 */
	public String processUserPicking() throws Exception {
		Collection<String> idList = this.restFacade.retrieveIdsFrom(GeographicZone.FRANCE_JOURNEE);
		idList = reverseListOrder(idList); //TODO To comment
		ArrayList<String> alreadyProcessedIds = (ArrayList<String>) dataStorage.getData(KEY_ALREADY_PROCESSED_IDS, ArrayList.class, new ArrayList<>());
		ArrayList<String> filteredIds = (ArrayList<String>) dataStorage.getData(KEY_FILTERED_IDS, ArrayList.class, new ArrayList<>());
		
		//Returns the first ID which has not already been processed, or null if all the retrieved IDs have already been processed
		for (String idRetrieved : idList) {
			if(!alreadyProcessedIds.contains(idRetrieved) && !filteredIds.contains(idRetrieved)) {
				int nbFollowerCount = scriptFacade.RunGetUserFollowerCount(idRetrieved);
				if(nbFollowerCount <= FILTER_NB_FOLLOWERS_MAX) {
					alreadyProcessedIds.add(idRetrieved);
					this.dataStorage.setData(KEY_ALREADY_PROCESSED_IDS, alreadyProcessedIds);
					return idRetrieved;
				}else {
					filteredIds.add(idRetrieved);
					this.dataStorage.setData(KEY_FILTERED_IDS, filteredIds);
					Thread.sleep(2*1000);
				}
			}
		}
		this.logger.log(String.format("No User has been found in the picking process (total amount of userIds retrieved = %d)", idList.size()), LogLevel.WARN, LoggingAction.All);
		return null;
	}

	/**
	 * An own choice to reverse the order to process firstly the oldest people that published with the hashtag.
	 * @param idList
	 * @return
	 */
	private Collection<String> reverseListOrder(Collection<String> idList) {
		ArrayList<String> result = new ArrayList<>();
		idList.forEach(id -> {
			result.add(0, id);
		});
		
		return result;
	}
	
	//Useless
//	public void confirmUserProcessed(String userId) throws NotImplementedException {
//		throw new NotImplementedException("TODO: Add the userID to the list of already processed IDs");
//	}
}
