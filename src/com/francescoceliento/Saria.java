package com.francescoceliento;

import com.francescoceliento.api.GitHubAPI;

/**
 * Descriptive and version check class of the library.
 * @author @francescoceliento@github.com
 *
 */
public class Saria {

	private static String version = "0.0.7-alpha";
	private static String date = "24/10/2025";
	
	private static String NAME = "Saria Toolkit";
	private static String INFO =   "Saria, Kokiri from Ocarina of Time, is Link's best friend.\n"
								 + "She gives him the ocarina and teaches him her song. She\n"
								 + "later reveals herself to be the Sage of the Forest Temple,\n"
								 + "helping Link save Hyrule.";
	private static String URL = "https://github.com/FrancescoCeliento/saria-toolkit/releases";
	private static String GITHUBAUTHOR = "FrancescoCeliento";
	private static String GITHUBREPOSITORY = "saria-toolkit";
	
	/**
	 * Print library info.
	 * @author @francescoceliento@github.com
	 *
	 */
	public static void info() {
		System.out.println(NAME + " v" + version);
		check();
		System.out.println(INFO);
	}
	
	public static String getName() {
		return NAME.toString();
	}
	/**
	 * Get library info.
	 * @author @francescoceliento@github.com
	 *
	 * @return String
	 */
	public static String getInfo() {
		return INFO.toString();
	}
	
	/**
	 * Get library url repository.
	 * @author @francescoceliento@github.com
	 *
	 * @return String
	 */
	public static String getUrl() {
		return URL.toString();
	}
	
	/**
	 * Print library version.
	 * @author @francescoceliento@github.com
	 *
	 */
	public static void version() {
		System.out.println(version);
	}
	
	/**
	 * Get library version.
	 * @author @francescoceliento@github.com
	 *
	 * @return String
	 */
	public static String getVersion() {
		return version;
	}
	
	/** Get library version date.
	 * @author @francescoceliento@github.com
	 *
	 * @return String
	 */
	public static String getDate() {
		return date;
	}
	
	/**
	 * Check for library updates.
	 * @author @francescoceliento@github.com
	 *
	 * @return boolean
	 */
	public static boolean getCheck() {
		String lastTag = GitHubAPI.getLastTag(GITHUBAUTHOR, GITHUBREPOSITORY);
				
		return lastTag!=null ? !lastTag.equals(version) : false;
	}
	
	/**
	 * Print the library update status.
	 * @author @francescoceliento@github.com
	 *
	 */
	public static void check() {
		if (getCheck())
			System.out.println("A new version is available, download it " + URL);
		else {
			System.out.println("The library has been updated to its latest version.");
		}
	}

}
