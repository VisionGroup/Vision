package com.yp2012g4.blindroid;

import com.yp2012g4.blindroid.R;
import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;

import android.graphics.PorterDuff.Mode;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.graphics.Color;

import android.app.Activity;

/**
 * @author Maytal
 *
 */
public class DisplaySettings {
	
	protected SparseIntArray color_to_string = new SparseIntArray();
	public static String SIZE="NORMAL";
	public static boolean settingChanged=false;
  static String THEME="";
	private int textColor = R.color.WHITE;
	private int backgroundColor = R.color.BLACK;
	
	public DisplaySettings() {
		color_to_string.append(R.color.BLACK, Color.parseColor("#000000"));
		color_to_string.append(R.color.WHITE, Color.parseColor("#FFFFFF"));
		color_to_string.append(R.color.RED, Color.parseColor("#FF0000"));
		color_to_string.append(R.color.GREEN, Color.parseColor("#04B431"));
		color_to_string.append(R.color.BLUE, Color.parseColor("#2E9AFE"));
	}
	
	public void setColors(int int1, int int2) {
		textColor = int1;
		backgroundColor = int2;
	}
	
	public void applyButtonSettings (View v) {
		
		if (v instanceof TalkingImageButton) {
			// Set the correct new color
			if (textColor == R.color.WHITE)
				((ImageView)v).setColorFilter(color_to_string.get(backgroundColor), Mode.LIGHTEN);
			else
				((ImageView)v).setColorFilter(color_to_string.get(textColor), Mode.DARKEN);
		}
		if (v instanceof TalkingButton)
		  ((TalkingButton) v).setTextColor(color_to_string.get(textColor));
		v.setBackgroundColor(color_to_string.get(backgroundColor));
	}

 public static void setThemeToActivity(Activity act)
  {
  
   try {

   if (DisplaySettings.SIZE.equalsIgnoreCase("LARGE"))
       act.setTheme(R.style.Theme_Large);
   else if (DisplaySettings.SIZE.equalsIgnoreCase("SMALL"))
       act.setTheme(R.style.Theme_Small);
   else
     act.setTheme(R.style.Theme_Normal);
  
   }
   catch (Exception e) {
  e.printStackTrace();
 }
  
  }
}
