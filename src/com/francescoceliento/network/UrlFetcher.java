package com.francescoceliento.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.francescoceliento.validator.JsonValidator;

/**
 * This class deals with retrieving data from URL.
 * @author @francescoceliento@github.com
 */
public class UrlFetcher {
	
	/**
	 * 
	 * @author @francescoceliento@github.com
	 *
	 * @param targetUrl
	 * @return
	 */
	public static String readUrl(String targetUrl) {
		StringBuilder result = new StringBuilder();
        
        try {
            URL url = new URL(targetUrl);
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
	}
	
	/**
	 * Calls a target URL and receives the response in JSON format.
	 * @author @francescoceliento@github.com
	 * 
	 * @param targetUrl
	 * @return String
	 */
	public static String getJsonResponse(String targetUrl) {
        String json = readUrl(targetUrl);
        if (JsonValidator.isValid(json))
        	return json;
        else
        	return "{}";
    }

}
