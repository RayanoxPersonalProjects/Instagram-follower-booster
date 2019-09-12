package com.rb.instagramfollowerbooster.model.rest;


/**
 * Les tags sont testés grace à deux liens (ids ou tags à remplacer) joués sur navigateur:
 * 		> https://www.instagram.com/explore/tags/journee/?__a=1
 * 		> https://i.instagram.com/api/v1/users/585846169/info/
 * 
 * @author rbenhmidane
 *
 */
public enum GeographicZone {
	
	FRANCE_SOIREE("soirees"), // Retourne trop de resultat de gens internationaux qui utilisent ce mot francais
	FRANCE_JOURNEE("journee"), // Qui possede le plus de resultats de francais et normaux (avec un nombre de followers normal et pas gigantesque)
	FRANCE_SOLEIL("soleil"); // Second choix, semble pas mal.
	
	private String tag;
	
	private GeographicZone(String tag) {
		this.tag = tag;
	}
	
	public String getLocationId() {
		return tag;
	}
}
