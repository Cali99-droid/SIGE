package com.tesla.frmk.util;

import java.util.Random;

public class IntegerUtil {

	/**
	 * Obtener Random
	 * @param minimum
	 * @param maximum
	 * @return
	 */
	public static int getRandom(int minimum, int maximum) {

		Random rn = new Random();
		int range = maximum - minimum + 1;
		int randomNum = rn.nextInt(range) + minimum;
		return randomNum;
	}

	/**
	 * Obtener Random
	 * @param minimum
	 * @param maximum
	 * @return
	 */
	public static int getRandom(int maximum) {

		int minimum = 1;
		Random rn = new Random();
		int range = maximum - minimum + 1;
		int randomNum = rn.nextInt(range) + minimum;
		return randomNum;
	}

}
