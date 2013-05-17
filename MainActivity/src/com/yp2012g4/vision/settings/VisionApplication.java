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
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.customUI.TalkingEditText;
import com.yp2012g4.vision.customUI.TalkingImageButton;

public class VisionApplication extends Application {
  public static final float SMALL_FONT_SIZE = 20;
  public static final float NORMAL_FONT_SIZE = 25;
  public static final float LARGE_FONT_SIZE = 30;
  public static final long DEFUALT_DELAY_TIME = 1000;
  private static HashMap<String, Integer> _colorToString = new HashMap<String, Integer>();
  public static String _textSize = "NORMAL";
  private static String _textColor = "WHITE";
  private static String _backgroundColor = "BLACK";
  
  /**
   * c'tor. initialize color mapping.
   * 
   */
  public VisionApplication() {
    _colorToString.put("BLACK", Integer.valueOf(Color.parseColor("#000000")));
    _colorToString.put("WHITE", Integer.valueOf(Color.parseColor("#FFFFFF")));
    _colorToString.put("RED", Integer.valueOf(Color.parseColor("#B40404")));
    _colorToString.put("GREEN", Integer.valueOf(Color.parseColor("#04B45F")));
    _colorToString.put("BLUE", Integer.valueOf(Color.parseColor("#0489B1")));
    _colorToString.put("LIGHT_PURPLE", Integer.valueOf(Color.parseColor("#A901DB")));
  }
  
  /**
   * load the text size, text color and bg color from xml file
   * 
   * @param a
   *          - current activity
   * 
   */
  public static void loadPrefs(final Activity a) {
    final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(a.getApplicationContext());
    _textSize = sp.getString("TEXT SIZE", _textSize);
    _textColor = sp.getString("TEXT COLOR", _textColor);
    _backgroundColor = sp.getString("BG COLOR", _backgroundColor);
  }
  
  /**
   * save a certain preference to the xml file
   * 
   * @param key
   *          - preference name
   * @param val
   *          - value to be saved
   * @param a
   *          - current activity
   * 
   */
  public static void savePrefs(final String key, final String val, final Activity a) {
    final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(a.getApplicationContext());
    sp.edit().putString(key, val).commit();
  }
  
  /**
   * set the text and background colors for the entire application
   * 
   * @param int1
   *          - text Color
   * @param int2
   *          - background Color
   */
  public static void setColors(final String txtC, final String backg) {
    _textColor = txtC;
    _backgroundColor = backg;
  }
  
  /**
   * @return text color
   * 
   */
  public static String getTextColor() {
    return _textColor;
  }
  
  /**
   * 
   * @return background color
   */
  public static String getBackgroundColor() {
    return _backgroundColor;
  }
  
  /**
   * Apply color theme to image buttons and main view
   * 
   * @param buttons
   *          - list of image buttons
   * @param v
   *          - main view of an activity
   */
  public static void applyButtonSettings(final Set<View> vs, final View mainView, final Activity act) {
    loadPrefs(act);
    mainView.setBackgroundColor(_colorToString.get(_backgroundColor).intValue());
    for (final View v : vs)
      if (v instanceof TalkingImageButton) {
        if (_textColor.equals("WHITE"))
          ((ImageView) v).setColorFilter(_colorToString.get(_backgroundColor).intValue(), Mode.LIGHTEN);
        else
          ((ImageView) v).setColorFilter(_colorToString.get(_textColor).intValue(), Mode.DARKEN);
        ((TalkingImageButton) v).setBackgroundColor(_colorToString.get(_backgroundColor).intValue());
      }
  }
  
  /**
   * Highlighting buttons when pressed
   * 
   * @param v
   *          - current view being pressed
   */
  public static void visualFeedback(final View v, final Activity act) {
    loadPrefs(act);
    if (v instanceof TalkingImageButton) {
      final TalkingImageButton b = (TalkingImageButton) v;
      if (_textColor.equals("WHITE")) {
        b.setColorFilter(_colorToString.get("LIGHT_PURPLE").intValue(), Mode.LIGHTEN);
        b.setBackgroundColor(_colorToString.get("LIGHT_PURPLE").intValue());
        return;
      }
      b.setColorFilter(_colorToString.get("LIGHT_PURPLE").intValue(), Mode.DARKEN);
      return;
    }
    if (v instanceof TextView)
      ((TextView) v).setBackgroundColor(_colorToString.get("LIGHT_PURPLE").intValue());
  }
  
  /**
   * Remove button highlight when the button is no longer pressed
   * 
   * @param v
   *          - last view pressed
   */
  public static void restoreColors(final View v, final Activity act) {
    loadPrefs(act);
    if (v instanceof TalkingImageButton) {
      final TalkingImageButton b = (TalkingImageButton) v;
      if (_textColor.equals("WHITE")) {
        b.setColorFilter(_colorToString.get(_backgroundColor).intValue(), Mode.LIGHTEN);
        b.setBackgroundColor(_colorToString.get(_backgroundColor).intValue());
        return;
      }
      b.setColorFilter(_colorToString.get(_textColor).intValue(), Mode.DARKEN);
      return;
    }
    if (v instanceof TextView) {
      final View viewType = v instanceof TalkingButton ? (TalkingButton) v : (TalkingEditText) v;
      String bg = _backgroundColor;
      final int viewId = viewType.getId();
      if (viewId == R.id.WhiteRed)
        bg = "RED";
      else if (viewId == R.id.WhiteGreen)
        bg = "GREEN";
      else if (viewId == R.id.WhiteBlue)
        bg = "BLUE";
      else if (((View) viewType.getParent().getParent()).getId() == R.id.ColorSettingsActivity)
        bg = "BLACK";
      viewType.setBackgroundColor(_colorToString.get(bg).intValue());
    }
  }
  
  /**
   * set theme to the entire application
   * 
   * @param act
   *          - current activity
   */
  public static void setThemeToActivity(final Activity act) {
    loadPrefs(act);
    if (_textSize.equals("LARGE"))
      setLargeTheme(act);
    else if (_textSize.equals("SMALL"))
      setSmallTheme(act);
    else
      // NORMAL
      setNormalTheme(act);
  }
  
  /**
   * @param act
   */
  private static void setNormalTheme(final Activity act) {
    if (_backgroundColor.equals("BLUE"))
      act.setTheme(R.style.Theme_NormalWhiteBlue);
    else if (_backgroundColor.equals("GREEN"))
      act.setTheme(R.style.Theme_NormalWhiteGreen);
    else if (_backgroundColor.equals("RED"))
      act.setTheme(R.style.Theme_NormalWhiteRed);
    else if (_textColor.equals("WHITE"))
      act.setTheme(R.style.Theme_NormalWhiteBlack);
    else if (_textColor.equals("BLUE"))
      act.setTheme(R.style.Theme_NormalBlueBlack);
    else if (_textColor.equals("RED"))
      act.setTheme(R.style.Theme_NormalRedBlack);
    else
      act.setTheme(R.style.Theme_NormalGreenBlack);
  }
  
  /**
   * @param act
   */
  private static void setSmallTheme(final Activity act) {
    if (_backgroundColor.equals("BLUE"))
      act.setTheme(R.style.Theme_SmallWhiteBlue);
    else if (_backgroundColor.equals("GREEN"))
      act.setTheme(R.style.Theme_SmallWhiteGreen);
    else if (_backgroundColor.equals("RED"))
      act.setTheme(R.style.Theme_SmallWhiteRed);
    else if (_textColor.equals("WHITE"))
      act.setTheme(R.style.Theme_SmallWhiteBlack);
    else if (_textColor.equals("BLUE"))
      act.setTheme(R.style.Theme_SmallBlueBlack);
    else if (_textColor.equals("RED"))
      act.setTheme(R.style.Theme_SmallRedBlack);
    else
      act.setTheme(R.style.Theme_SmallGreenBlack);
  }
  
  /**
   * @param act
   */
  private static void setLargeTheme(final Activity act) {
    if (_backgroundColor.equals("BLUE"))
      act.setTheme(R.style.Theme_LargeWhiteBlue);
    else if (_backgroundColor.equals("GREEN"))
      act.setTheme(R.style.Theme_LargeWhiteGreen);
    else if (_backgroundColor.equals("RED"))
      act.setTheme(R.style.Theme_LargeWhiteRed);
    else if (_textColor.equals("WHITE"))
      act.setTheme(R.style.Theme_LargeWhiteBlack);
    else if (_textColor.equals("BLUE"))
      act.setTheme(R.style.Theme_LargeBlueBlack);
    else if (_textColor.equals("RED"))
      act.setTheme(R.style.Theme_LargeRedBlack);
    else
      act.setTheme(R.style.Theme_LargeGreenBlack);
  }
}
