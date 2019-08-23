package com.rb.instagramfollowerbooster.core.scripts;

import java.util.Collections;

import org.springframework.stereotype.Component;

import com.rb.instagramfollowerbooster.exceptions.NotImplementedException;
import com.rb.instagramfollowerbooster.model.scripts.results.ListIdsResult;

@Component
public class WhiteListScript extends AbstractScriptRunner<ListIdsResult> {

	@Override
	public ListIdsResult getResult(String outputOfScript) {
		try {
			throw new NotImplementedException();
		} catch (NotImplementedException e) {
			e.printStackTrace();
		}
		
		return new ListIdsResult(false, Collections.emptySet());
	}

}
