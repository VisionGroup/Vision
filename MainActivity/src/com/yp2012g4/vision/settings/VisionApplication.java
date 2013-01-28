/**
 * The Application class.
 * 
 * @author Maytal
 * 
 */
package com.yp2012g4.vision.settings;

import java.util.Set;

import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.util.SparseIntArray;
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
  private static SparseIntArray color_to_string = new SparseIntArray();
  public static float textSize = VisionApplication.NORMAL;
  private static int textColor = R.color.WHITE;
  private static int backgroundColor = R.color.BLACK;
  
  /**
   * c'tor. initialize color mapping.
   * 
   */
  public VisionApplication() {
    color_to_string.append(R.color.BLACK, Color.parseColor("#000000"));
    color_to_string.append(R.color.WHITE, Color.parseColor("#FFFFFF"));
    color_to_string.append(R.color.RED, Color.parseColor("#B40404"));
    color_to_string.append(R.color.GREEN, Color.parseColor("#04B45F"));
    color_to_string.append(R.color.BLUE, Color.parseColor("#0489B1"));
    color_to_string.append(R.color.LIGHT_PURPLE, Color.parseColor("#A901DB"));
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
  public static void applyButtonSettings(Set<View> vs, View mainView) {
    mainView.setBackgroundColor(color_to_string.get(backgroundColor));
    for (View v : vs) {
      if (v instanceof TalkingImageButton) {
        if (textColor == R.color.WHITE)
          ((ImageView) v).setColorFilter(color_to_string.get(backgroundColor), Mode.LIGHTEN);
        else
          ((ImageView) v).setColorFilter(color_to_string.get(textColor), Mode.DARKEN);
        ((TalkingImageButton) v).setBackgroundColor(color_to_string.get(backgroundColor));
      }
    }
  }
  
  public static void visualFeedback(View v) {
    if (v instanceof TalkingImageButton) {
      if (textColor == R.color.WHITE) {
        ((ImageView) v).setColorFilter(color_to_string.get(R.color.LIGHT_PURPLE), Mode.LIGHTEN);
        ((TalkingImageButton) v).setBackgroundColor(color_to_string.get(R.color.LIGHT_PURPLE));
      } else
        ((ImageView) v).setColorFilter(color_to_string.get(R.color.LIGHT_PURPLE), Mode.DARKEN);
    } else if (v instanceof TextView) {
      ((TextView) v).setBackgroundColor(color_to_string.get(R.color.LIGHT_PURPLE));
    }
  }
  
  public static void restoreColors(View v) {
    if (v instanceof TalkingImageButton) {
      if (textColor == R.color.WHITE) {
        ((ImageView) v).setColorFilter(color_to_string.get(backgroundColor), Mode.LIGHTEN);
        ((TalkingImageButton) v).setBackgroundColor(color_to_string.get(backgroundColor));
      } else
        ((ImageView) v).setColorFilter(color_to_string.get(textColor), Mode.DARKEN);
    } else if (v instanceof TextView) {
      int bg = backgroundColor;
      if (v.getId() == R.id.WhiteRed)
        bg = R.color.RED;
      else if (v.getId() == R.id.WhiteGreen)
        bg = R.color.GREEN;
      else if (v.getId() == R.id.WhiteBlue)
        bg = R.color.BLUE;
      else if (((View) (v.getParent().getParent())).getId() == R.id.ColorSettingsActivity)
        bg = R.color.BLACK;
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
    if (textSize == VisionApplication.LARGE) {
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
    } else if (textSize == VisionApplication.SMALL)
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
    else if (backgroundColor == R.color.BLUE)
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
