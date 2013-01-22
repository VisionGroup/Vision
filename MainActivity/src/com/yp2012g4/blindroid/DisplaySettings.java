/**
 * A singleton class holding all the display preferences
 * 
 * @author Maytal
 * 
 */
package com.yp2012g4.blindroid;

import java.util.Set;

import com.yp2012g4.blindroid.R;
import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;
import android.graphics.PorterDuff.Mode;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;

import android.app.Activity;

public class DisplaySettings {
	
	private static SparseIntArray color_to_string = new SparseIntArray();
	public static float textSize = DisplaySettingsApplication.NORMAL;
  static String THEME="";
	private static int textColor = R.color.WHITE;
	private static int backgroundColor = R.color.BLACK;
	
	 /**
   * c'tor. initialize color mapping.
   * 
   */
	public DisplaySettings() {
		color_to_string.append(R.color.BLACK, Color.parseColor("#000000"));
		color_to_string.append(R.color.WHITE, Color.parseColor("#FFFFFF"));
		color_to_string.append(R.color.RED, Color.parseColor("#FF0000"));
		color_to_string.append(R.color.GREEN, Color.parseColor("#04B431"));
		color_to_string.append(R.color.BLUE, Color.parseColor("#2E9AFE"));
	}
	
  /**
   * set the text and background colors for the entire application
   * 
   * @param int1
   *          - text Color
   * @param int2
   *          - background Color
   */
	public static void setColors(int int1, int int2) {
		textColor = int1;
		backgroundColor = int2;
	}
	
  /**
   * @return text color
   * 
   */
	public static int getTextColor() {
    return textColor;
  }
	
	/**
	 * 
	 * @return bg color
	 */
	public static int getBackgroundColor() {
    return backgroundColor;
  }
	
  /**
   * set color theme to image buttons and main view
   * 
   * @param buttons
   *          - list of image buttons
   * @param v
   *          - main view of an activity
   */
	public static void applyButtonSettings (Set<View> vs, View mainView) {
	  mainView.setBackgroundColor(color_to_string.get(backgroundColor));
	  for (View v : vs) {
	    if (v instanceof TalkingImageButton) {
	      if (textColor == R.color.WHITE)
	        ((ImageView)v).setColorFilter(color_to_string.get(backgroundColor), Mode.LIGHTEN);
	      else
	        ((ImageView)v).setColorFilter(color_to_string.get(textColor), Mode.DARKEN);
	    }
	    else if (v instanceof TextView){
	      if (mainView.getId() != R.id.ColorSettingsActivity)
          ((TextView)v).setTextColor(color_to_string.get(textColor));
	      if (mainView.getId() != R.id.ThemeSettingsActivity)
	        ((TextView)v).setTextSize(textSize);
	    }
	  }
	      
	}


  /**
   * set theme to the entire application
   * 
   * @param act
   *          - current activity
   */
 public static void setThemeToActivity(Activity act)
  {
  
   if (DisplaySettings.textSize == DisplaySettingsApplication.LARGE) {
     if (backgroundColor == R.color.BLUE)
       act.setTheme(R.style.Theme_LargeWhiteBlue);
     else if (backgroundColor == R.color.GREEN)
       act.setTheme(R.style.Theme_LargeWhiteGreen);
     else if (backgroundColor == R.color.RED)
       act.setTheme(R.style.Theme_LargeWhiteRed);
     else if (textColor == R.color.WHITE)
       act.setTheme(R.style.Theme_LargeWhiteBlack);
     else if (textColor == R.color.BLUE)
       act.setTheme(R.style.Theme_LargeBlueBlack);
     else if (textColor == R.color.RED)
       act.setTheme(R.style.Theme_LargeRedBlack);
     else
       act.setTheme(R.style.Theme_LargeGreenBlack);
   }
   else if (DisplaySettings.textSize == DisplaySettingsApplication.SMALL)
     if (backgroundColor == R.color.BLUE)
       act.setTheme(R.style.Theme_SmallWhiteBlue);
     else if (backgroundColor == R.color.GREEN)
       act.setTheme(R.style.Theme_SmallWhiteGreen);
     else if (backgroundColor == R.color.RED)
       act.setTheme(R.style.Theme_SmallWhiteRed);
     else if (textColor == R.color.WHITE)
       act.setTheme(R.style.Theme_SmallWhiteBlack);
     else if (textColor == R.color.BLUE)
       act.setTheme(R.style.Theme_SmallBlueBlack);
     else if (textColor == R.color.RED)
       act.setTheme(R.style.Theme_SmallRedBlack);
     else
       act.setTheme(R.style.Theme_SmallGreenBlack);
   else
     if (backgroundColor == R.color.BLUE)
       act.setTheme(R.style.Theme_NormalWhiteBlue);
     else if (backgroundColor == R.color.GREEN)
       act.setTheme(R.style.Theme_NormalWhiteGreen);
     else if (backgroundColor == R.color.RED)
       act.setTheme(R.style.Theme_NormalWhiteRed);
     else if (textColor == R.color.WHITE)
       act.setTheme(R.style.Theme_NormalWhiteBlack);
     else if (textColor == R.color.BLUE)
       act.setTheme(R.style.Theme_NormalBlueBlack);
     else if (textColor == R.color.RED)
       act.setTheme(R.style.Theme_NormalRedBlack);
     else
       act.setTheme(R.style.Theme_NormalGreenBlack);
  }
}
