package com.francescoceliento.io;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for reading data from JSON.
 * @author @francescoceliento@github.com
  */
public class JsonIO {

	/**
	 * Returns a list of values that match the key.
	 * @author @francescoceliento@github.com
	 *
	 * @param json
	 * @param key
	 * @return List<String>
	 */
	public static List<String> readKeys(String json, String key) {
        List<String> values = new ArrayList<>();
        String searchKey = "\"" + key + "\":";
        int startIndex = 0;

        while (startIndex < json.length()) {
            int keyIndex = json.indexOf(searchKey, startIndex);

            if (keyIndex == -1) {
                break;
            }

            int valueStart = keyIndex + searchKey.length();

            int valueEnd;

            if (json.charAt(valueStart) == '"') {
                valueStart++;
                valueEnd = json.indexOf('"', valueStart);
            }
            else {
                int commaIndex = json.indexOf(',', valueStart);
                int braceIndex = json.indexOf('}', valueStart);
                int bracketIndex = json.indexOf(']', valueStart);
                
                valueEnd = json.length();
                if (commaIndex != -1) {
                    valueEnd = Math.min(valueEnd, commaIndex);
                }
                if (braceIndex != -1) {
                    valueEnd = Math.min(valueEnd, braceIndex);
                }
                if (bracketIndex != -1) {
                     valueEnd = Math.min(valueEnd, bracketIndex);
                }

                if (valueEnd == json.length()) {
                    valueEnd = json.indexOf('}', valueStart);
                    if (valueEnd == -1) valueEnd = json.length();
                }

                while (valueStart < valueEnd && Character.isWhitespace(json.charAt(valueStart))) {
                    valueStart++;
                }

            }


            if (valueEnd == -1 || valueStart >= valueEnd) {
                startIndex = keyIndex + searchKey.length();
                continue;
            }

            String value = json.substring(valueStart, valueEnd).trim();
            values.add(value);

            startIndex = valueEnd + 1;
        }

        return values;
    }
	
	/**
	 * Returns the value that match the key.
	 * @author @francescoceliento@github.com
	 * 
	 * @param json
	 * @param key
	 * @return
	 */
	public static String readKey(String json, String key) {
        List<String> values = readKeys(json, key);
        return values.size()>0 ? values.get(0) : null;
    }

}
