package com.marvin.circleime;

//import com.google.tts.TTSEarcon;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.inputmethodservice.KeyboardView;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.yp2012g4.vision.utils.TTS;

/**
 * A transparent overlay which catches all touch events and uses a call back to
 * return the gesture that the user performed.
 * 
 * @author clchen@google.com (Charles L. Chen)
 * 
 *         Significant changes made by the Vision project: Changed the TTS
 *         engine, Changed names of characters, added language support.
 * 
 ** 
 **         Copyright 2008, Eyes-Free
 ** 
 **         Licensed under the Apache License, Version 2.0 (the "License"); you
 *         may not use this file except in compliance with the License. You may
 *         obtain a copy of the License at
 ** 
 **         http://www.apache.org/licenses/LICENSE-2.0
 ** 
 **         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *         implied. See the License for the specific language governing
 *         permissions and limitations under the License.
 */

public class CircleGestureView extends KeyboardView {
    private static final String abc[][][] = {
	    {
		    { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
			    "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
			    "v", "w", "x", "y", "z", ",", ".", " ", "?", "!",
			    "" },
		    { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
			    "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
			    "v", "w", "x", "y", "z", "comma", "dot", "space",
			    "Question Mark", "Exclamation Mark", "Backspace" },
		    { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
			    "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
			    "v", "w", "x", "y", "z", ",", ".", "Sp", "?", "!",
			    "<-" } },
	    {

		    { "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט", "י", "כ",
			    "ל", "מ", "נ", "ס", "ע", "פ", "צ", "ק", "ר", "ש",
			    "ת", "ך", "ן", "ף", "ץ", ",", ".", " ", "?", "!",
			    "" },
		    { "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט", "י", "כ",
			    "ל", "מ", "נ", "ס", "ע", "פ", "צ", "ק", "ר", "ש",
			    "ת", "ך", "ן", "ף", "ץ", "פסיק", "נקודה", "רווח",
			    "סימן שאלה", "סימן קריאה", "מחק" },
		    { "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט", "י", "כ",
			    "ל", "מ", "נ", "ס", "ע", "פ", "צ", "ק", "ר", "ש",
			    "ת", "ך", "ן", "ף", "ץ", ",", ".", "רווח", "?",
			    "!", "<-" } } };

    public enum languages {
	EN, HE
    }

    private enum ABCType {
	WRITTEN, READ, DISPLAYED
    }

    private static final String TAG = "vision:CircleIME";

    private static final long[] PATTERN = { 0, 1, 40, 41 };

    private static final int AE = 0;

    private static final int IM = 1;

    private static final int QU = 2;

    private static final int Y = 4;

    private static final int NONE = 5;

    private static final int NUM0 = 6;

    private static final int NUM1 = 7;

    private final double left = 0;

    private final double upleft = Math.PI * .25;

    private final double up = Math.PI * .5;

    private final double upright = Math.PI * .75;

    private final double downright = -Math.PI * .75;

    private final double down = -Math.PI * .5;

    private final double downleft = -Math.PI * .25;

    private final double right = Math.PI;

    private final double rightWrap = -Math.PI;

    private double downX;

    private double downY;

    private int currentWheel = 5;

    private String currentCharacter = "";// Using the READ abc

    private String currentString = "";

    private double lastX;

    private double lastY;

    private int currentValue;

    private boolean screenIsBeingTouched;

    private Vibrator vibe;

    private SoftKeyboard parent;

    private TTS _tts;

    private int lang = languages.EN.ordinal();

    public CircleGestureView(Context context, AttributeSet attrs) {
	super(context, attrs);
	parent = (SoftKeyboard) context;
	vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	_tts = new TTS(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
	final int action = event.getAction();
	final float x = event.getX();
	final float y = event.getY();
	if (action == MotionEvent.ACTION_DOWN) {
	    initiateMotion(x, y);
	    return true;
	} else if (action == MotionEvent.ACTION_UP) {
	    confirmEntry();
	    return true;
	} else {
	    screenIsBeingTouched = true;
	    lastX = x;
	    lastY = y;
	    final int prevVal = currentValue;
	    currentValue = evalMotion(x, y);
	    // Do nothing since we want a deadzone here;
	    // restore the state to the previous value.
	    if (currentValue == -1) {
		currentValue = prevVal;
		return true;
	    }
	    // There is a wheel that is active
	    if (currentValue != 5) {
		if (currentWheel == NONE)
		    currentWheel = getWheel(currentValue);
		currentCharacter = getCharacter(currentWheel, currentValue,
			ABCType.READ);
	    } else
		currentCharacter = "";
	    invalidate();
	    if (prevVal != currentValue) {
		// parent.mTts.playEarcon("[tock]", 2, null);
		if (currentCharacter.equals("")) {
		    // parent.tts.playEarcon(TTSEarcon.TOCK.toString(), 0,
		    // null);
		}// else String[] params = new String[1];
		 // params[0] = TTSParams.VOICE_FEMALE.toString();
		else
		    _tts.speak(currentCharacter);

		vibe.vibrate(PATTERN, -1);
	    }
	}
	return true;
    }

    public int getWheel(int value) {
	switch (value) {
	case 1:
	    return AE;
	case 2:
	    return IM;
	case 3:
	    return QU;
	case 4:
	    return Y;
	case 5:
	    return NONE;
	case 6:
	    return Y;
	case 7:
	    return QU;
	case 8:
	    return IM;
	case 9:
	    return AE;
	default:
	    return NONE;
	}
    }

    public String getCharacter(int wheel, int value, ABCType type) {
	final int t = type.ordinal();
	switch (wheel) {
	case AE:
	    switch (value) {
	    case 1:
		return abc[lang][t][0];
	    case 2:
		return abc[lang][t][1];
	    case 3:
		return abc[lang][t][2];
	    case 4:
		return abc[lang][t][7];
	    case 5:
		return "";
	    case 6:
		return abc[lang][t][3];
	    case 7:
		return abc[lang][t][6];
	    case 8:
		return abc[lang][t][5];
	    case 9:
		return abc[lang][t][4];
	    default:
		return "";
	    }
	case IM:
	    switch (value) {
	    case 1:
		return abc[lang][t][15];
	    case 2:
		return abc[lang][t][8];
	    case 3:
		return abc[lang][t][9];
	    case 4:
		return abc[lang][t][14];
	    case 5:
		return "";
	    case 6:
		return abc[lang][t][10];
	    case 7:
		return abc[lang][t][13];
	    case 8:
		return abc[lang][t][12];
	    case 9:
		return abc[lang][t][11];
	    default:
		return "";
	    }
	case QU:
	    switch (value) {
	    case 1:
		return abc[lang][t][22];
	    case 2:
		return abc[lang][t][23];
	    case 3:
		return abc[lang][t][16];
	    case 4:
		return abc[lang][t][21];
	    case 5:
		return "";
	    case 6:
		return abc[lang][t][17];
	    case 7:
		return abc[lang][t][20];
	    case 8:
		return abc[lang][t][19];
	    case 9:
		return abc[lang][t][18];
	    default:
		return "";
	    }
	case Y:
	    switch (value) {
	    case 1:
		return abc[lang][t][26];
	    case 2:
		return abc[lang][t][30];
	    case 3:
		return abc[lang][t][28]; // return "MODE";
	    case 4:
		return abc[lang][t][31];
	    case 5:
		return "";
	    case 6:
		return abc[lang][t][24];
	    case 7:
		return abc[lang][t][27];
	    case 8:
		return abc[lang][t][29];
	    case 9:
		return abc[lang][t][25];
	    default:
		return "";
	    }
	case NUM0:
	    switch (value) {
	    case 1:
		return "0";
	    case 2:
		return "1";
	    case 3:
		return "2"; // return "MODE";
	    case 4:
		return "3";
	    case 5:
		return "";
	    case 6:
		return "4";
	    case 7:
		return "5";
	    case 8:
		return "6";
	    case 9:
		return "7";
	    default:
		return "";
	    }
	case NUM1:
	    switch (value) {
	    case 1:
		return "8";
	    case 2:
		return "9";
	    case 3:
		return "Backspace";
	    case 4:
		return "Asteriks";
	    case 5:
		return "";
	    case 6:
		return "At";
	    case 7:
		return "And";
	    case 8:
		return "Pound";
	    case 9:
		return "Dollar";
	    default:
		return "Percent";
	    }
	default:
	    return "";
	}
    }

    private void confirmEntry() {
	screenIsBeingTouched = false;
	final int prevVal = currentValue;
	currentValue = evalMotion(lastX, lastY);
	// Do some correction if the user lifts up on deadspace
	if (currentValue == -1)
	    currentValue = prevVal;
	// The user never got a number that wasn't deadspace,
	// so assume 5.
	if (currentValue == -1)
	    currentValue = 5;
	String c = getCharacter(currentWheel, currentValue, ABCType.WRITTEN);
	Log.i(TAG, "Character entered: " + c);
	if (c.equals(abc[lang][ABCType.WRITTEN.ordinal()][31])) {
	    // BackSpace
	    c = "";
	    backspace();

	} else {
	    currentString = currentString + c;
	    // parent.tts.speak(currentCharacter, 0, null);
	    if (c.length() > 0) {
		parent.sendKeyChar(c.toLowerCase().charAt(0));
		Log.i(TAG, "Character sent: " + c);
	    }
	}
	invalidate();
	initiateMotion(lastX, lastY);
    }

    private void initiateMotion(double x, double y) {
	downX = x;
	downY = y;
	lastX = x;
	lastY = y;
	currentValue = -1;
	currentWheel = NONE;
	currentCharacter = "";
    }

    @Override
    public void onDraw(Canvas canvas) {
	// super.onDraw(canvas);

	setBackgroundColor(Color.TRANSPARENT);

	// Draw an indication that the IME is up - doing a border for now
	final Paint imeStatusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	imeStatusPaint.setColor(Color.RED);
	imeStatusPaint.setTextSize(14);
	imeStatusPaint.setTypeface(Typeface.DEFAULT_BOLD);
	final Paint imeBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	imeBgPaint.setColor(Color.BLACK);
	final int fudgeFactor = 15;
	final int startY = 0;

	canvas.drawRect(0, startY, getWidth(), startY + fudgeFactor, imeBgPaint);
	canvas.drawText("IME Active", 10, startY + 13, imeStatusPaint);

	final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	paint.setColor(Color.WHITE);
	paint.setTextAlign(Paint.Align.CENTER);
	paint.setTypeface(Typeface.DEFAULT_BOLD);

	int x = 5;
	int y = 50;
	paint.setTextSize(50);
	paint.setTextAlign(Paint.Align.LEFT);
	y -= paint.ascent() / 2;

	// canvas.drawText(currentString, x, y, paint);

	if (!screenIsBeingTouched) {
	    x = 5;
	    y = getHeight() - 40;
	    paint.setTextSize(20);
	    paint.setTextAlign(Paint.Align.LEFT);
	    y -= paint.ascent() / 2;
	    // canvas.drawText("Scroll apps with trackball.", x, y, paint);

	    x = 5;
	    y = getHeight() - 20;
	    paint.setTextSize(20);
	    paint.setTextAlign(Paint.Align.LEFT);
	    y -= paint.ascent() / 2;
	    // canvas.drawText("Press CALL to launch app.", x, y, paint);
	} else {
	    final int offset = 90;

	    final int x1 = (int) downX - offset;
	    int y1 = (int) downY - offset;
	    final int x2 = (int) downX;
	    int y2 = (int) downY - offset;
	    final int x3 = (int) downX + offset;
	    int y3 = (int) downY - offset;
	    final int x4 = (int) downX - offset;
	    int y4 = (int) downY;
	    final int x6 = (int) downX + offset;
	    int y6 = (int) downY;
	    final int x7 = (int) downX - offset;
	    int y7 = (int) downY + offset;
	    final int x8 = (int) downX;
	    int y8 = (int) downY + offset;
	    final int x9 = (int) downX + offset;
	    int y9 = (int) downY + offset;

	    y1 -= paint.ascent() / 2;
	    y2 -= paint.ascent() / 2;
	    y3 -= paint.ascent() / 2;
	    y4 -= paint.ascent() / 2;
	    y6 -= paint.ascent() / 2;
	    y7 -= paint.ascent() / 2;
	    y8 -= paint.ascent() / 2;
	    y9 -= paint.ascent() / 2;

	    switch (currentWheel) {
	    case AE:
		paint.setColor(Color.RED);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][0], x1,
			y1, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][0].toUpperCase()));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][1], x2,
			y2, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][1].toUpperCase()));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][2], x3,
			y3, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][2].toUpperCase()));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][7], x4,
			y4, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][7].toUpperCase()));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][3], x6,
			y6, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][3].toUpperCase()));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][6], x7,
			y7, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][6].toUpperCase()));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][5], x8,
			y8, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][5].toUpperCase()));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][4], x9,
			y9, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][4].toUpperCase()));
		break;
	    case IM:
		paint.setColor(Color.BLUE);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][15], x1,
			y1, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][15].toUpperCase()));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][8], x2,
			y2, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][8].toUpperCase()));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][9], x3,
			y3, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][9].toUpperCase()));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][14], x4,
			y4, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][14].toUpperCase()));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][10], x6,
			y6, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][10].toUpperCase()));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][13], x7,
			y7, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][13].toUpperCase()));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][12], x8,
			y8, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][12].toUpperCase()));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][11], x9,
			y9, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][11].toUpperCase()));
		break;
	    case QU:
		paint.setColor(Color.GREEN);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][22], x1,
			y1, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][22].toUpperCase()));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][23], x2,
			y2, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][23].toUpperCase()));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][16], x3,
			y3, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][16].toUpperCase()));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][21], x4,
			y4, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][21].toUpperCase()));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][17], x6,
			y6, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][17].toUpperCase()));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][20], x7,
			y7, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][20].toUpperCase()));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][19], x8,
			y8, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][19].toUpperCase()));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][18], x9,
			y9, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][18].toUpperCase()));
		break;
	    case Y:
		paint.setColor(Color.YELLOW);
		drawCharacter(",", x1, y1, canvas, paint,
			currentCharacter.equals(","));
		drawCharacter("!", x2, y2, canvas, paint,
			currentCharacter.equals("!"));
		drawCharacter("SPACE", x3, y3, canvas, paint,
			currentCharacter.equals("SPACE"));
		drawCharacter("<-", x4, y4, canvas, paint,
			currentCharacter.equals("<-"));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][24], x6,
			y6, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][24].toUpperCase()));
		drawCharacter(".", x7, y7, canvas, paint,
			currentCharacter.equals("."));
		drawCharacter("?", x8, y8, canvas, paint,
			currentCharacter.equals("?"));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][25], x9,
			y9, canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.DISPLAYED
				.ordinal()][25].toUpperCase()));
		break;
	    default:
		paint.setColor(Color.RED);
		canvas.drawText(abc[lang][ABCType.DISPLAYED.ordinal()][0], x1,
			y1, paint);
		canvas.drawText(abc[lang][ABCType.DISPLAYED.ordinal()][4], x9,
			y9, paint);
		paint.setColor(Color.BLUE);
		canvas.drawText(abc[lang][ABCType.DISPLAYED.ordinal()][8], x2,
			y2, paint);
		canvas.drawText(abc[lang][ABCType.DISPLAYED.ordinal()][12], x8,
			y8, paint);
		paint.setColor(Color.GREEN);
		canvas.drawText(abc[lang][ABCType.DISPLAYED.ordinal()][16], x3,
			y3, paint);
		canvas.drawText(abc[lang][ABCType.DISPLAYED.ordinal()][20], x7,
			y7, paint);
		paint.setColor(Color.YELLOW);
		canvas.drawText(abc[lang][ABCType.DISPLAYED.ordinal()][24], x6,
			y6, paint);
		canvas.drawText("<-", x4, y4, paint);
		break;
	    }
	}

    }

    public void backspace() {
	parent.handleBackspace();
	String deletedCharacter = "";
	if (currentString.length() > 0) {
	    deletedCharacter = ""
		    + currentString.charAt(currentString.length() - 1);
	    currentString = currentString.substring(0,
		    currentString.length() - 1);
	}
	if (!deletedCharacter.equals(""))
	    _tts.speak(deletedCharacter + " deleted.");
	else {
	    // parent.tts.playEarcon(TTSEarcon.TOCK.toString(), 0, null);
	    // parent.tts.playEarcon(TTSEarcon.TOCK.toString(), 1, null);
	}
	invalidate();
    }

    private void drawCharacter(String character, int x, int y, Canvas canvas,
	    Paint paint, boolean isSelected) {
	final int regSize = 50;
	final int selectedSize = regSize * 2;
	if (isSelected)
	    paint.setTextSize(selectedSize);
	else
	    paint.setTextSize(regSize);
	canvas.drawText(character, x, y, paint);
    }

    public int evalMotion(double x, double y) {
	final float rTolerance = 25;
	final double thetaTolerance = (Math.PI / 16);

	final double r = Math.sqrt(((downX - x) * (downX - x))
		+ ((downY - y) * (downY - y)));

	if (r < rTolerance)
	    return 5;

	final double theta = Math.atan2(downY - y, downX - x);

	if (Math.abs(theta - left) < thetaTolerance)
	    return 4;
	else if (Math.abs(theta - upleft) < thetaTolerance)
	    return 1;
	else if (Math.abs(theta - up) < thetaTolerance)
	    return 2;
	else if (Math.abs(theta - upright) < thetaTolerance)
	    return 3;
	else if (Math.abs(theta - downright) < thetaTolerance)
	    return 9;
	else if (Math.abs(theta - down) < thetaTolerance)
	    return 8;
	else if (Math.abs(theta - downleft) < thetaTolerance)
	    return 7;
	else if ((theta > right - thetaTolerance)
		|| (theta < rightWrap + thetaTolerance))
	    return 6;

	// Off by more than the threshold, so it doesn't count
	return -1;
    }

    private int keyboardMode = 0;

    private void toggleKeyboardMode() {
	if (keyboardMode == 0) {
	    keyboardMode = 1;
	    _tts.speak("Numbers");
	} else {
	    keyboardMode = 0;
	    _tts.speak("Alpha");
	}
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	final String input = "";
	Log.i(TAG, "Keycode_Menu.");
	switch (keyCode) {
	case KeyEvent.KEYCODE_MENU:
	    toggleKeyboardMode();
	    Log.i(TAG, "Keycode_Menu.");
	    return false;
	default:
	    break;
	}
	return false;
    }

    /**
     * Change the language of the input.
     * 
     * @param l
     */
    public void changeLang() {
	if (lang == languages.EN.ordinal())
	    lang = languages.HE.ordinal();
	else
	    lang = languages.EN.ordinal();
	invalidate();
    }

}
