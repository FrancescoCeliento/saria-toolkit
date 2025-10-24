package com.francescoceliento.validator;

/**
 * Manages JSON format validations.
 * @author @francescoceliento@github.com
 *
 */
public class JsonValidator {
	
	/**
	 * Checks that the JSON is correct.
	 * @author @francescoceliento@github.com
	 * 
	 * @param json
	 * @return
	 */
	public static boolean isValid(String json) {
	    // Regex per verificare la validità del JSON
	    String regex = "(?(DEFINE)\n" + 
	    		"    (?<number> -? (?: 0 | [1-9]\\d* ) (?:\\.\\d+)? (?:[eE][+-]?\\d+)? )\n" + 
	    		"    (?<boolean> true | false | null )\n" + 
	    		"    (?<string> \" (?: [^\"\\\\\\x00-\\x1f] | \\\\ [[\"\\\\/bfnrt] | \\\\ u [0-9A-Fa-f]{4} )* \" )\n" + 
	    		"    (?<pair> (?&string) \\s* : \\s* (?&value) )\n" + 
	    		"    (?<array> \\[ \\s* (?: (?&value) \\s* (?:, \\s* (?&value) \\s*)* )? \\] )\n" + 
	    		"    (?<object> \\{ \\s* (?: (?&pair) \\s* (?:, \\s* (?&pair) \\s*)* )? \\} )\n" + 
	    		"    (?<value> (?&number) | (?&boolean) | (?&string) | (?&array) | (?&object) )\n" + 
	    		")";

	    try {
	    	return json != null && json.matches(regex);	    	
	    } catch (Exception e) {
	    	return false;
	    }
	}

}
