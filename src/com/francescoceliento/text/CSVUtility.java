package com.francescoceliento.text;

/**
 * Manages CSV.
 * @author @francescoceliento@github.com
 *
 */
public class CSVUtility {
	
	/**
	 * Create a CSV row from a set of fields.
	 * @author @francescoceliento@github.com
	 *
	 * @param separator
	 * @param fields
	 * @return String
	 */
	public static String makeRow(String separator, String...fields) {
		String newRow = "";
		
		for (String value : fields) {
			newRow = value + fields + separator;
		}
		
		return newRow;
	}

}
