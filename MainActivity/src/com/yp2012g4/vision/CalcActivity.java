package com.yp2012g4.vision;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.tools.VisionActivity;

/**
 * 
 * @author Amir Mizrachi
 * @version 2
 * 
 *          This class represents a basic calculator
 */
public class CalcActivity extends VisionActivity {
  private String calculated_number = "";
  private String lhs_number = ""; // Left-Hand-Side of a calculated number
  private String rhs_number = ""; // Right-Hand-Side of a calculated number
  private Sign sign = Sign.NO_SIGN;
  private boolean equalsPressed = false;
  private boolean lhsDone = false;
  final private static double EPSILON = 0.00001; // epsilon for identifying a
                                                 // zero FP
  // number
  private boolean isBadAction = false; // checks if a wrong operation has been
  // id of views
  @SuppressWarnings("boxing") final List<Integer> digits = Arrays.asList(R.id.digit0, R.id.digit1, R.id.digit2, R.id.digit3,
      R.id.digit4, R.id.digit5, R.id.digit6, R.id.digit7, R.id.digit8, R.id.digit9);
  
  // made
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calc);
    init(0, getString(R.string.calc_screen), getString(R.string.calc_screen));
  }
  
  @Override public boolean onSingleTapUp(MotionEvent e) {
    super.onSingleTapUp(e);
    if (_navigationBar)
      return _navigationBar = false;
    if (isBadAction)
      // also the touched button text
      return isBadAction = false;
    if (e.getAction() == MotionEvent.ACTION_UP)
      for (Map.Entry<View, Rect> entry : getView_to_rect().entrySet())
        if (isButtonType(entry.getKey()) && entry.getValue().contains((int) e.getRawX(), (int) e.getRawY()))
          speakOutAsync(textToRead(entry.getKey()));
    return true;
  }
  
  @Override public int getViewId() {
    return R.id.CalcScreen;
  }
  
  /**
   * Overridden method which implements the action triggered by a button lift
   */
  @Override public void onActionUp(View v) {
    final CharSequence buttonText = ((TalkingButton) v).getText();
    final int buttonId = v.getId();
    // it's a digit
    if (digits.contains(Integer.valueOf(buttonId)))
      if (!equalsPressed) {
        if (sign == Sign.NO_SIGN)
          lhs_number += buttonText;
        else
          rhs_number += buttonText;
        calculated_number += buttonText;
      } else
        badAction();
    // it's a sign
    else if (getSignFromId(buttonId) != Sign.NO_SIGN)
      pressOnSignButton(buttonText, buttonId);
    else
      // not a digit, not a sign
      switch (buttonId) {
        case R.id.clear:
          clearCalcFields();
          break;
        case R.id.dot:
          pressOnDotButton(buttonText);
          break;
        case R.id.equals:
          if (lhs_number != "" && rhs_number != "" && sign != Sign.NO_SIGN) {
            Double res = Double.valueOf(parseResult(lhs_number, rhs_number, sign));
            if (res.doubleValue() == Double.NaN)
              return;
            updateCalculatedNumber(res);
          } else
            badAction();
          break;
        default:
          break;
      }
    vibrator.vibrate(150);
    final TalkingButton resultButton = (TalkingButton) findViewById(R.id.result);
    resultButton.setText(calculated_number.toCharArray(), 0, calculated_number.length());
    resultButton.setReadText(calculated_number);
  }
  
  private void badAction() {
    speakOutAsync(getString(R.string.bad_action));
    isBadAction = true;
  }
  
  private void pressOnSignButton(final CharSequence buttonText, final int buttonId) {
    lhsDone = true;
    if (lhs_number != "" && !lhs_number.endsWith(".") && sign == Sign.NO_SIGN) {
      // updates sign to its corresponding enum
      sign = getSignFromId(buttonId);
      // number
      calculated_number += buttonText;
      equalsPressed = false;
      return;
    }
    badAction();
  }
  
  private void updateCalculatedNumber(Double res) {
    lhs_number = calculated_number = Integer.valueOf((int) Math.floor(res.doubleValue())).toString();
    // operation is the lhs_number
    rhs_number = "";
    sign = Sign.NO_SIGN;
    equalsPressed = true;
    lhsDone = false;
    speakOutAsync(calculated_number);
  }
  
  private void pressOnDotButton(CharSequence text) {
    if (lhs_number != "" && !lhs_number.contains(".") && !lhsDone) {
      lhs_number += text;
      calculated_number += text;
      return;
    }
    if (rhs_number != "" && !rhs_number.contains(".")) {
      rhs_number += text;
      calculated_number += text;
      return;
    }
    badAction();
  }
  
  private void clearCalcFields() {
    lhs_number = rhs_number = calculated_number = "";
    sign = Sign.NO_SIGN;
    equalsPressed = false;
    lhsDone = false;
  }
  
  /**
   * Updates sign to its corresponding enum number
   * 
   * @param id
   *          The sign button id, on which we clicked
   * @return the corresponding sign
   */
  private static Sign getSignFromId(int id) {
    switch (id) {
      case R.id.plus:
        return Sign.PLUS;
      case R.id.minus:
        return Sign.MINUS;
      case R.id.multiplicity:
        return Sign.TIMES;
      case R.id.div:
        return Sign.DIV;
      default:
        return Sign.NO_SIGN;
    }
  }
  
  /**
   * Parsing a calculation string and returning the result
   * 
   * @param lhs_number
   *          A string representing the Left-Hand-Side number of a calculation
   * @param rhs_number
   *          A string representing the Right-Hand-Side number of a calculation
   * @param sign
   *          The sign between the 2 numbers
   * @return The result (as a double) of the parsed string
   */
  private double parseResult(String lhs_num, String rhs_num, Sign s) {
    double lhs = Double.parseDouble(lhs_num);
    double rhs = Double.parseDouble(rhs_num);
    switch (s) {
      case PLUS:
        return lhs + rhs;
      case MINUS:
        return lhs - rhs;
      case TIMES:
        return lhs * rhs;
      case DIV:
        if (Math.abs(rhs) < EPSILON) {
          speakOutAsync(getString(R.string.division_by_zero));
          return Double.NaN;
        }
        return lhs / rhs;
      default:
        return Double.NaN;
    }
  }
  
  /**
   * 
   * Enum class representing the different signs in a calculation
   * 
   */
  public enum Sign {
    NO_SIGN, PLUS, MINUS, TIMES, DIV;
  }
}
