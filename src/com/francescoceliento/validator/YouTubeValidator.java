package com.francescoceliento.validator;

public class YouTubeValidator {
	
	private static String youtubeRegex = "^(https?://)?(www\\.)?" +
			"(youtube\\.com/watch\\?v=|" +
			"youtu\\.be/|" +
			"youtube\\.com/embed/|" +
			"youtube\\.com/v/|" +
			"youtube\\.com/user/.+/.+|" +
			"youtube\\.com/.+/|" +
			"youtube\\.com/playlist\\?list=|" +
			"youtube\\.com/c/.+/.+)" +
			"([a-zA-Z0-9_-]+)" +
			"(&.*)?$";
	
	public static boolean isValidUrl(String url) {
	    if (url == null || url.trim().isEmpty()) {
	        return false;
	    }

	    return url.matches(youtubeRegex);
	}

}
