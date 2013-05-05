/**
 * The Application class.
 * 
 * @author Maytal
 * 
 */
package com.yp2012g4.vision.settings;

import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.customUI.TalkingImageButton;

public class VisionApplication extends Application {
  public static final float SMALL = 20;
  public static final float NORMAL = 25;
  public static final float LARGE = 30;
  public static HashMap<String, Integer> color_to_string = new HashMap<String, Integer>();
  public static String textSize = "NORMAL";
  private static String textColor = "WHITE";
  private static String backgroundColor = "BLACK";
  
  /**
   * c'tor. initialize color mapping.
   * 
   */
  public VisionApplication() {
    color_to_string.put("BLACK", Color.parseColor("#000000"));
    color_to_string.put("WHITE", Color.parseColor("#FFFFFF"));
    color_to_string.put("RED", Color.parseColor("#B40404"));
    color_to_string.put("GREEN", Color.parseColor("#04B45F"));
    color_to_string.put("BLUE", Color.parseColor("#0489B1"));
    color_to_string.put("LIGHT_PURPLE", Color.parseColor("#A901DB"));
  }
  
  /**
   * load the text size, text color and bg color from xml file
   * 
   * @param act
   *          - current activity
   * 
   */
  public static void loadPrefs(Activity act) {
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(act.getApplicationContext());
    textSize = sp.getString("TEXT SIZE", textSize);
    textColor = sp.getString("TEXT COLOR", textColor);
    backgroundColor = sp.getString("BG COLOR", backgroundColor);
  }
  
  /**
   * save a certain preference to the xml file
   * 
   * @param key
   *          - preference name
   * @param value
   *          - value to be saved
   * @param act
   *          - current activity
   * 
   */
  public static void savePrefs(String key, String value, Activity act) {
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(act.getApplicationContext());
    Editor edit = sp.edit();
    edit.putString(key, value);
    edit.commit();
  }
  
  /**
   * set the text and background colors for the entire application
   * 
   * @param int1
   *          - text Color
   * @param int2
   *          - background Color
   */
  public static void setColors(String s1, String s2) {
    textColor = s1;
    backgroundColor = s2;
  }
  
  /**
   * @return text color
   * 
   */
  public static String getTextColor() {
    return textColor;
  }
  
  /**
   * 
   * @return bg color
   */
  public static String getBackgroundColor() {
    return backgroundColor;
  }
  
  /**
   * Apply color theme to image buttons and main view
   * 
   * @param buttons
   *          - list of image buttons
   * @param v
   *          - main view of an activity
   */
  public static void applyButtonSettings(Set<View> vs, View mainView, Activity act) {
    loadPrefs(act);
    mainView.setBackgroundColor(color_to_string.get(backgroundColor));
    for (View v : vs)
      if (v instanceof TalkingImageButton) {
        if (textColor.equals("WHITE"))
          ((ImageView) v).setColorFilter(color_to_string.get(backgroundColor), Mode.LIGHTEN);
        else
          ((ImageView) v).setColorFilter(color_to_string.get(textColor), Mode.DARKEN);
        ((TalkingImageButton) v).setBackgroundColor(color_to_string.get(backgroundColor));
      }
  }
  
  /**
   * Highlighting buttons when pressed
   * 
   * @param v
   *          - current view being pressed
   */
  public static void visualFeedback(View v, Activity act) {
    loadPrefs(act);
    if (v instanceof TalkingImageButton) {
      if (textColor.equals("WHITE")) {
        ((ImageView) v).setColorFilter(color_to_string.get("LIGHT_PURPLE"), Mode.LIGHTEN);
        ((TalkingImageButton) v).setBackgroundColor(color_to_string.get("LIGHT_PURPLE"));
      } else
        ((ImageView) v).setColorFilter(color_to_string.get("LIGHT_PURPLE"), Mode.DARKEN);
    } else if (v instanceof TextView)
      ((TextView) v).setBackgroundColor(color_to_string.get("LIGHT_PURPLE"));
  }
  
  /**
   * Remove button highlight when the button is no longer pressed
   * 
   * @param v
   *          - last view pressed
   */
  public static void restoreColors(View v, Activity act) {
    loadPrefs(act);
    if (v instanceof TalkingImageButton) {
      if (textColor.equals("WHITE")) {
        ((ImageView) v).setColorFilter(color_to_string.get(backgroundColor), Mode.LIGHTEN);
        ((TalkingImageButton) v).setBackgroundColor(color_to_string.get(backgroundColor));
      } else
        ((ImageView) v).setColorFilter(color_to_string.get(textColor), Mode.DARKEN);
    } else if (v instanceof TextView) {
      String bg = backgroundColor;
      // TODO sparta
      if (v.getId() == R.id.WhiteRed)
        bg = "RED";
      else if (v.getId() == R.id.WhiteGreen)
        bg = "GREEN";
      else if (v.getId() == R.id.WhiteBlue)
        bg = "BLUE";
      else if (((View) v.getParent().getParent()).getId() == R.id.ColorSettingsActivity)
        bg = "BLACK";
      ((Button) v).setBackgroundColor(color_to_string.get(bg));
    }
  }
  
  /**
   * set theme to the entire application
   * 
   * @param act
   *          - current activity
   */
  public static void setThemeToActivity(Activity act) {
    loadPrefs(act);
    if (textSize.equals("LARGE")) {
      if (backgroundColor.equals("BLUE"))
        act.setTheme(R.style.Theme_LargeWhiteBlue);
      else if (backgroundColor.equals("GREEN"))
        act.setTheme(R.style.Theme_LargeWhiteGreen);
      else if (backgroundColor.equals("RED"))
        act.setTheme(R.style.Theme_LargeWhiteRed);
      else if (textColor.equals("WHITE"))
        act.setTheme(R.style.Theme_LargeWhiteBlack);
      else if (textColor.equals("BLUE"))
        act.setTheme(R.style.Theme_LargeBlueBlack);
      else if (textColor.equals("RED"))
        act.setTheme(R.style.Theme_LargeRedBlack);
      else
        act.setTheme(R.style.Theme_LargeGreenBlack);
    } else if (textSize.equals("SMALL"))
      if (backgroundColor.equals("BLUE"))
        act.setTheme(R.style.Theme_SmallWhiteBlue);
      else if (backgroundColor.equals("GREEN"))
        act.setTheme(R.style.Theme_SmallWhiteGreen);
      else if (backgroundColor.equals("RED"))
        act.setTheme(R.style.Theme_SmallWhiteRed);
      else if (textColor.equals("WHITE"))
        act.setTheme(R.style.Theme_SmallWhiteBlack);
      else if (textColor.equals("BLUE"))
        act.setTheme(R.style.Theme_SmallBlueBlack);
      else if (textColor.equals("RED"))
        act.setTheme(R.style.Theme_SmallRedBlack);
      else
        act.setTheme(R.style.Theme_SmallGreenBlack);
    else if (backgroundColor.equals("BLUE"))
      act.setTheme(R.style.Theme_NormalWhiteBlue);
    else if (backgroundColor.equals("GREEN"))
      act.setTheme(R.style.Theme_NormalWhiteGreen);
    else if (backgroundColor.equals("RED"))
      act.setTheme(R.style.Theme_NormalWhiteRed);
    else if (textColor.equals("WHITE"))
      act.setTheme(R.style.Theme_NormalWhiteBlack);
    else if (textColor.equals("BLUE"))
      act.setTheme(R.style.Theme_NormalBlueBlack);
    else if (textColor.equals("RED"))
      act.setTheme(R.style.Theme_NormalRedBlack);
    else
      act.setTheme(R.style.Theme_NormalGreenBlack);
  }
}
