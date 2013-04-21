package com.yp2012g4.vision;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.tools.VisionActivity;

public class CalcActivity extends VisionActivity {
	private String calculated_number = "";
	private String lhs_number = "";
	private String rhs_number = "";
	private Sign sign = Sign.NO_SIGN;
	private String sign_to_read = "";
	private boolean equalsPressed = false;
	private boolean divisionByZero = true;
	private boolean lhsDone = false;
	private int buttonPressed = 0;

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
		++buttonPressed;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		super.onSingleTapUp(e);
		if (clickFlag) {
			clickFlag = false;
			return false;
		}
		if (e.getAction() == MotionEvent.ACTION_UP) {
			for (Map.Entry<View, Rect> entry : getView_to_rect().entrySet())
				if (isButtonType(entry.getKey())
						&& (entry.getValue().contains((int) e.getRawX(),
								(int) e.getRawY()))
						&& (last_button_view != entry.getKey() || buttonPressed == 0)) {
					speakOut(textToRead(entry.getKey()));
				}
			buttonPressed = 0;

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
				} else if (sign == Sign.DIV && v.getId() == R.id.digit0
						&& divisionByZero) {
					speakOut(getString(R.string.division_by_zero));
					return;
				} else {
					rhs_number += ((TalkingButton) v).getText();
					divisionByZero = false;

				}
				calculated_number += ((TalkingButton) v).getText();
			} else {
				speakOut(getString(R.string.bad_action));
			}
		}
		// it's a sign
		if (signs.contains(v.getId())) {
			if (lhs_number != "" && sign == Sign.NO_SIGN) {
				getSign(v.getId()); // updates sign to its corresponding enum
									// number
				calculated_number += ((TalkingButton) v).getText();
				equalsPressed = false;
			} else {
				speakOut(getString(R.string.bad_action));
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
			}
			break;
		case R.id.equals:
			if (lhs_number != "" && rhs_number != "" && sign != Sign.NO_SIGN) {
				Log.i("MyLog", lhs_number + "   " + rhs_number);
				Double res = parseResult(lhs_number, rhs_number, sign);
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
				divisionByZero = true;
				lhsDone = false;
				speakOut(calculated_number);
			} else {
				speakOut(getString(R.string.bad_action));
			}
			break;
		case R.id.result:
			String s = (sign != Sign.NO_SIGN ? (lhs_number + " " + sign_to_read
					+ " " + rhs_number) : (lhs_number));
			speakOut(s);
			break;
		default:
			break;
		}

		vb.vibrate(150);
		((TalkingButton) findViewById(R.id.result)).setText(
				calculated_number.toCharArray(), 0, calculated_number.length());
	}

	private void getSign(int id) {
		switch (id) {
		case R.id.plus:
			sign = Sign.PLUS;
			sign_to_read = getString(R.string.ReadPlus);
			break;
		case R.id.minus:
			sign = Sign.MINUS;
			sign_to_read = getString(R.string.ReadMinus);
			break;
		case R.id.multiplicity:
			sign = Sign.TIMES;
			sign_to_read = getString(R.string.ReadTimes);
			break;
		case R.id.div:
			sign = Sign.DIV;
			sign_to_read = getString(R.string.ReadDiv);
			break;
		}
	}

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
			result = lhs / rhs;
			break;
		}
		return result;

	}

	private boolean IsIntResult(String calculated_number) {
		return (calculated_number.endsWith(".0") ? true : false);
	}

	public enum Sign {
		NO_SIGN, PLUS, MINUS, TIMES, DIV;

	}
}
