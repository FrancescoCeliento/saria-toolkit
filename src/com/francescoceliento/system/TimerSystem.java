package com.francescoceliento.system;

/**
 * Manages timers
 * @author @francescoceliento@github.com
 */
public class TimerSystem {
	
	/**
	 * Executes Thread.sleep with internal exception handling.
	 * @author @francescoceliento@github.com
	 * 
	 * @param millisecond
	 */
	public static void sleep(int millisecond) {
		try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * Sleep cycle for the desired seconds.
	 * @author @francescoceliento@github.com
	 *
	 * @param seconds
	 */
	public static void waitForSeconds(int seconds) {
		sleep(seconds*1000);
	}
	
	/**
	 * Sleep cycle for the desired minutes.
	 * @author @francescoceliento@github.com
	 *
	 * @param minutes
	 */
	public static void waitForMinutes(int minutes) {
		sleep(minutes*60*1000);
	}

}
