package com.yp2012g4.vision;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.tools.VisionActivity;

/**
 * 
 * @author Amir
 * @version 2 This class represents a basic calculator
 */
public class CalcActivity extends VisionActivity {
	private String calculated_number = "";
	private String lhs_number = ""; // Left-Hand-Side of a calculated number
	private String rhs_number = ""; // Right-Hand-Side of a calculated number
	private Sign sign = Sign.NO_SIGN;
	private boolean equalsPressed = false;
	private boolean lhsDone = false;
	private static Double ERROR = 0.00001; // delta for identifying a zero FP
											// number
	private boolean isBadAction = false; // checks if a wrong operation has been
											// made

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calc);
		init(0, getString(R.string.calc_screen),
				getString(R.string.calc_screen));
	}

	/**
	 * update the number of sequential buttons pressed
	 */
	@Override
	public void onShowPress(MotionEvent e) {
		super.onShowPress(e);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		super.onSingleTapUp(e);
		if (clickFlag) {
			clickFlag = false;
			return false;
		}
		if (isBadAction) { // if bad action, we do not want the tts to speak
							// also the touched button text
			isBadAction = false;
			return false;
		}
		if (e.getAction() == MotionEvent.ACTION_UP) {
			for (Map.Entry<View, Rect> entry : getView_to_rect().entrySet())
				if (isButtonType(entry.getKey())
						&& (entry.getValue().contains((int) e.getRawX(),
								(int) e.getRawY()))) {
					speakOut(textToRead(entry.getKey()));
				}
		}
		return true;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	public int getViewId() {
		return R.id.CalcScreen;
	}

	/**
	 * Overridden method which implements the action triggered by a button lift
	 */
	@Override
	public void onActionUp(View v) {
		List<Integer> digits = Arrays.asList(R.id.digit0, R.id.digit1,
				R.id.digit2, R.id.digit3, R.id.digit4, R.id.digit5,
				R.id.digit6, R.id.digit7, R.id.digit8, R.id.digit9);
		List<Integer> signs = Arrays.asList(R.id.plus, R.id.minus,
				R.id.multiplicity, R.id.div);

		// Get instance of Vibrator from current Context
		final Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		// it's a digit
		if (digits.contains(v.getId())) {
			if (!equalsPressed) {
				if (sign == Sign.NO_SIGN) { // in case no sign yet
					lhs_number += ((TalkingButton) v).getText();
				} else {
					rhs_number += ((TalkingButton) v).getText();
				}
				calculated_number += ((TalkingButton) v).getText();
			} else {
				speakOut(getString(R.string.bad_action));
				isBadAction = true;
			}
		}
		// it's a sign
		if (signs.contains(v.getId())) {
			if (lhs_number != "" && !lhs_number.endsWith(".")
					&& sign == Sign.NO_SIGN) {
				getSign(v.getId()); // updates sign to its corresponding enum
									// number
				calculated_number += ((TalkingButton) v).getText();
				equalsPressed = false;
			} else {
				speakOut(getString(R.string.bad_action));
				isBadAction = true;
			}
			lhsDone = true;
		}

		switch (v.getId()) {
		case R.id.clear:
			lhs_number = rhs_number = calculated_number = "";
			sign = Sign.NO_SIGN;
			equalsPressed = false;
			lhsDone = false;
			break;
		case R.id.dot:
			if ((lhs_number != "" && !lhs_number.contains(".")) && !lhsDone) {
				lhs_number += ((TalkingButton) v).getText();
				calculated_number += ((TalkingButton) v).getText();
			} else if ((rhs_number != "" && !rhs_number.contains("."))) {
				rhs_number += ((TalkingButton) v).getText();
				calculated_number += ((TalkingButton) v).getText();
			}

			else {
				speakOut(getString(R.string.bad_action));
				isBadAction = true;
			}
			break;
		case R.id.equals:
			if (lhs_number != "" && rhs_number != "" && sign != Sign.NO_SIGN) {
				Double res = parseResult(lhs_number, rhs_number, sign);
				if (res == null) { // in case of division by zero
					return;
				}
				if (IsIntResult(res.toString())) {
					calculated_number = ((Integer) res.intValue()).toString();
				} else {
					calculated_number = res.toString();
				}
				lhs_number = calculated_number; // now, the result of previous
												// operation is the lhs_number
				rhs_number = "";
				sign = Sign.NO_SIGN;
				equalsPressed = true;
				lhsDone = false;
				speakOut(calculated_number);
			} else {
				speakOut(getString(R.string.bad_action));
				isBadAction = true;
			}
			break;
		default:
			break;
		}

		vb.vibrate(150);
		((TalkingButton) findViewById(R.id.result)).setText(
				calculated_number.toCharArray(), 0, calculated_number.length());
		((TalkingButton) findViewById(R.id.result))
				.setReadText(calculated_number);
	}

	/**
	 * Updates sign to its corresponding enum number
	 * 
	 * @param id
	 *            The sign button id, on which we clicked
	 */
	private void getSign(int id) {
		switch (id) {
		case R.id.plus:
			sign = Sign.PLUS;
			break;
		case R.id.minus:
			sign = Sign.MINUS;
			break;
		case R.id.multiplicity:
			sign = Sign.TIMES;
			break;
		case R.id.div:
			sign = Sign.DIV;
			break;
		}
	}

	/**
	 * Parsing a calculation string and returning the result
	 * 
	 * @param lhs_number
	 *            A string representing the Left-Hand-Side number of a
	 *            calculation
	 * @param rhs_number
	 *            A string representing the Right-Hand-Side number of a
	 *            calculation
	 * @param sign
	 *            The sign between the 2 numbers
	 * @return The result (as a double) of the parsed string
	 */
	private Double parseResult(String lhs_number, String rhs_number, Sign sign) {
		Double result = null;
		Double lhs = Double.parseDouble(lhs_number);
		Double rhs = Double.parseDouble(rhs_number);
		switch (sign) {
		case PLUS:
			result = lhs + rhs;
			break;
		case MINUS:
			result = lhs - rhs;
			break;
		case TIMES:
			result = lhs * rhs;
			break;
		case DIV:
			if (Math.abs(rhs) < ERROR) {
				speakOut(getString(R.string.division_by_zero));
				break;
			}
			result = lhs / rhs;
			break;
		}
		return result;

	}

	/**
	 * Checks whether the calculated number is an integer or double
	 * 
	 * @param calculated_number
	 *            The calculated number as a string
	 * @return True if the calculated number is an integer. Else - false.
	 */
	private boolean IsIntResult(String calculated_number) {
		return (calculated_number.endsWith(".0") ? true : false);
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
