package com.francescoceliento.api;

import com.francescoceliento.io.JsonIO;
import com.francescoceliento.network.UrlFetcher;

/**
 * Class for managing GitHub API
 * @author @francescoceliento@github.com
 */
public class GitHubAPI {
	
	//Metodi API di GitHub
	private static String BASEAPI = "https://api.github.com/repos/%s/%s";
	private static String APIRELEASES = "/releases";
	private static String APILASTRELEASE = "/latest";
				
	/**
	 * Reads the latest tag released from a repository
	 * @author @francescoceliento@github.com
	 * 
	 * @param author
	 * @param repository
	 * @return String
	 */
	public static String getLastTag(String author, String repository) {
		String apiRequest = String.format(BASEAPI+APIRELEASES+APILASTRELEASE, author, repository);
		String jsonResponse = UrlFetcher.getJsonResponse(apiRequest);
		String lastTag = JsonIO.readKey(jsonResponse, "tag_name");
		
		return lastTag;
	}

}
