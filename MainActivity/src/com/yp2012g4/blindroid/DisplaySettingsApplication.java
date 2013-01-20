/**
 * The Application class.
 * 
 * @author Maytal
 *
 */
package com.yp2012g4.blindroid;

import android.app.Application;


public class DisplaySettingsApplication extends Application {
	
  static final float SMALL=20;
  static final float NORMAL=26;
  static final float LARGE=32;
  
	/**
	 * singelton holding display preferences
	 */
	public DisplaySettings settings = new DisplaySettings();
}
