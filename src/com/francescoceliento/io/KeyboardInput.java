package com.francescoceliento.io;

import java.text.ParseException;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.francescoceliento.converter.DateConverter;

/**
 * Simplify keyboard input usage.
 * @author @francescoceliento@github.com
 */
public class KeyboardInput {
		
	/**
	 * Pause the application until the ENTER button is pressed.
	 * @author @francescoceliento@github.com
	 */
	public static void waitEnter() {
		Scanner scanner = new Scanner(System.in);
		try {
			scanner.nextLine();
		} catch (NoSuchElementException e) {
			// nothing
		}
        scanner.close();
	}
	
	/**
	 * Read input text.
	 * @author @francescoceliento@github.com
	 * 
	 * @return String
	 */
	public static String getInputStringFromConsole() {
		Scanner scanner = new Scanner(System.in);
		try {
			String inputTesto = scanner.nextLine();
			scanner.close();
			return inputTesto;			
		} catch (NoSuchElementException e) {
			scanner.close();
			return null;
		}
		
	}
	
	/**
	 * Read input Integer.
	 * @author @francescoceliento@github.com
	 * 
	 * @return Integer
	 */
	public static Integer getInputIntFromConsole() {
		Scanner scanner = new Scanner(System.in);

		try {
			int numero = scanner.nextInt();
			scanner.close();
			return numero;
		} catch (InputMismatchException e) {
			scanner.close();
			return null;
		}
	}
	
	/**
	 * Read input Double.
	 * @author @francescoceliento@github.com
	 *
	 * @return Double
	 */
	public static Double getInputDoubleFromConsole() {
        try {
        	String input = getInputStringFromConsole();
        	String inputPulito = input.trim().replace(',', '.');
        	Double inputDouble = Double.parseDouble(inputPulito);        	
        	return inputDouble;
		} catch (InputMismatchException e) {
			return null;
		}
    }
	
	public static Date getInputDateFromConsole(String pattern) {
		try {
			String input = getInputStringFromConsole();
			String inputPulito = input.trim();
			Date inputDate = DateConverter.stringToDateWithThrow(inputPulito, pattern);
			return inputDate;
		} catch (ParseException e) {
			return null;
		}
	}

}
