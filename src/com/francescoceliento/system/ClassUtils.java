package com.francescoceliento.system;

/**
 * 
 * @author @francescoceliento@github.com
 *
 */
public class ClassUtils {
	
	/**
	 * 
	 * @author @francescoceliento@github.com
	 *
	 * @return
	 */
	public static String getClassName() {
        return new Exception().getStackTrace()[1].getClassName();
	}
}
