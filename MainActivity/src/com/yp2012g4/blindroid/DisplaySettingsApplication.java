/**
 * The Application class.
 * 
 * @author Maytal
 *
 */
package com.yp2012g4.blindroid;

import android.app.Application;


public class DisplaySettingsApplication extends Application {
	
	/**
	 * singelton holding display preferences
	 */
	public DisplaySettings settings = new DisplaySettings();
}
