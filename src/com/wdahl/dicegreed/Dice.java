/**
 * 
 */
package com.wdahl.dicegreed;

import java.util.Random;

/**
 * @author Will Dahl
 * Dice - represents a single die.
 * This can randomly return a single dice roll from 1-6
 * 
 * The dice images are the work of April Russo:
 *
	Copyright (C) 2012 April Russo. Some rights reserved.
	These images are licensed under a Creative Commons Attribution 3.0 license.
	<http://creativecommons.org/licenses/by/3.0/>
	
	Credit link: http://russograffix.blogspot.com
	
	I'm unavailable for custom design work. But your suggestions are always welcome!
	<mailto:apps.freeware.apps@gmail.com>
 */

public class Dice {
	
	private int mResult;
	private void Roll() {
		
		Random randomGenerator = new Random();
		mResult = randomGenerator.nextInt(6) + 1; 
		// Get random number from 0 to 5, then add 1
		
	}
	public int getLastResult() {
		// Return the result without rolling
		return mResult;
	}
	public int getNewResult() {
		// Roll the dice, then return the result
		this.Roll();
		return mResult;
	}
}

