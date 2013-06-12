package com.marvin.circleime;

//import com.google.tts.TTSEarcon;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.inputmethodservice.KeyboardView;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
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
			    "BS", "0", "1", "2", "3", "4", "5", "6", "7", "8",
			    "9", "*", "@", "&", "#", "$", "%" },
		    { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
			    "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
			    "v", "w", "x", "y", "z", "comma", "dot", "space",
			    "Question Mark", "Exclamation Mark", "Backspace",
			    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			    "*", "At", "And", "Pound", "Dollar", "Percent" },
		    { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
			    "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			    "V", "W", "X", "Y", "Z", ",", ".", "Sp", "?", "!",
			    "<-", "0", "1", "2", "3", "4", "5", "6", "7", "8",
			    "9", "*", "@", "&", "#", "$", "%" } },
	    {

		    { "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט", "י", "כ",
			    "ל", "מ", "נ", "ס", "ע", "פ", "צ", "ק", "ר", "ש",
			    "ת", "ך", "ן", "ף", "ץ", ",", ".", " ", "?", "!",
			    "BS", "0", "1", "2", "3", "4", "5", "6", "7", "8",
			    "9", "*", "@", "&", "#", "$", "%" },
		    { "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט", "י", "כ",
			    "ל", "מ", "נ", "ס", "ע", "פ", "צ", "ק", "ר", "ש",
			    "ת", "ך", "ן", "ף", "ץ", "פסיק", "נקודה", "רווח",
			    "סימן שאלה", "סימן קריאה", "מחק", "0", "1", "2",
			    "3", "4", "5", "6", "7", "8", "9", "*", "כרוכית",
			    "וגם", "סולמית", "דולר", "אחוז" },
		    { "א", "ב", "ג", "ד", "ה", "ו", "ז", "ח", "ט", "י", "כ",
			    "ל", "מ", "נ", "ס", "ע", "פ", "צ", "ק", "ר", "ש",
			    "ת", "ך", "ן", "ף", "ץ", ",", ".", "רווח", "?",
			    "!", "<-", "0", "1", "2", "3", "4", "5", "6", "7",
			    "8", "9", "*", "@", "&", "#", "$", "%" } } };

    public enum languages {
	EN, HE
    }

    private enum ABCType {
	WRITTEN, READ, DISPLAYED
    }

    private enum KeyboardMode {
	ALPHA_MODE, NUMERIC_MODE
    }

    private enum Dir {
	NW, N, NE, E, SE, S, SW, W, C, NONE
    }

    private static class Loc {
	public int x;
	public int y;

	Loc(int _x, int _y) {
	    x = _x;
	    y = _y;
	}

	@Override
	public String toString() {
	    return "X: " + x + " Y: " + y;
	}
    }

    private static final int numOfDir = 9;
    // private static final int X = 0;
    // private static final int Y = 1;

    private static final String TAG = "vision:CircleIME";

    private static final long[] PATTERN = { 0, 1, 40, 41 }; // For Vibration

    private static final int AE = 0;

    private static final int IM = 1;

    private static final int QU = 2;

    private static final int BSY = 4;

    private static final int ALPHA = 5;
    private static final int NUMERIC = 8;

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

    private int currentWheel = ALPHA;

    private String currentCharacter = "";// Using the READ abc

    // private int currentCharOrdinal = -1;

    private String currentString = "";

    private double lastX;

    private double lastY;

    private Dir currentDir;

    private boolean screenIsBeingTouched;

    private Vibrator vibe;

    private SoftKeyboard parent;

    private TTS _tts;

    private int lang = languages.EN.ordinal();

    private KeyboardMode keyboardMode = KeyboardMode.ALPHA_MODE; // Alpha or
								 // Numeric

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
	    final Dir prevVal = currentDir;
	    currentDir = evalMotion(x, y);
	    // Do nothing since we want a deadzone here;
	    // restore the state to the previous value.
	    if (currentDir == Dir.NONE) {
		currentDir = prevVal;
		return true;
	    }
	    // There is a wheel that is active
	    if (currentDir != Dir.C) {
		if ((currentWheel == ALPHA) || (currentWheel == NUMERIC))
		    currentWheel = getWheel(currentDir, keyboardMode);
		currentCharacter = getCharacter(currentWheel, currentDir,
			ABCType.READ);
	    } else
		currentCharacter = "";
	    invalidate();
	    if (prevVal != currentDir) {
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

    public static int getWheel(Dir value, KeyboardMode km) {
	if (km == KeyboardMode.ALPHA_MODE)
	    switch (value) {
	    case NW:
		return AE;
	    case N:
		return IM;
	    case NE:
		return QU;
	    case W:
		return BSY;
	    case C:
		return ALPHA;
	    case E:
		return BSY;
	    case SW:
		return QU;
	    case S:
		return IM;
	    case SE:
		return AE;
	    default:
		return ALPHA;
	    }
	switch (value) {
	case NW:
	    return NUM0;
	case N:
	    return NUM0;
	case NE:
	    return NUM0;
	case W:
	    return NUM1;
	case C:
	    return NUMERIC;
	case E:
	    return NUM0;
	case SW:
	    return NUM1;
	case S:
	    return NUM1;
	case SE:
	    return NUM1;
	default:
	    return NUMERIC;
	}
    }

    public String getCharacter(int wheel, Dir value, ABCType type) {
	final int t = type.ordinal();
	switch (wheel) {
	case AE:
	    switch (value) {
	    case NW:
		return abc[lang][t][0];
	    case N:
		return abc[lang][t][1];
	    case NE:
		return abc[lang][t][2];
	    case W:
		return abc[lang][t][7];
	    case C:
		return "";
	    case E:
		return abc[lang][t][3];
	    case SW:
		return abc[lang][t][6];
	    case S:
		return abc[lang][t][5];
	    case SE:
		return abc[lang][t][4];
	    default:
		return "";
	    }
	case IM:
	    switch (value) {
	    case NW:
		return abc[lang][t][15];
	    case N:
		return abc[lang][t][8];
	    case NE:
		return abc[lang][t][9];
	    case W:
		return abc[lang][t][14];
	    case C:
		return "";
	    case E:
		return abc[lang][t][10];
	    case SW:
		return abc[lang][t][13];
	    case S:
		return abc[lang][t][12];
	    case SE:
		return abc[lang][t][11];
	    default:
		return "";
	    }
	case QU:
	    switch (value) {
	    case NW:
		return abc[lang][t][22];
	    case N:
		return abc[lang][t][23];
	    case NE:
		return abc[lang][t][16];
	    case W:
		return abc[lang][t][21];
	    case C:
		return "";
	    case E:
		return abc[lang][t][17];
	    case SW:
		return abc[lang][t][20];
	    case S:
		return abc[lang][t][19];
	    case SE:
		return abc[lang][t][18];
	    default:
		return "";
	    }
	case BSY:
	    switch (value) {
	    case NW:
		return abc[lang][t][26];
	    case N:
		return abc[lang][t][30];
	    case NE:
		return abc[lang][t][28]; // return "MODE";
	    case W:
		return abc[lang][t][31];
	    case C:
		return "";
	    case E:
		return abc[lang][t][24];
	    case SW:
		return abc[lang][t][27];
	    case S:
		return abc[lang][t][29];
	    case SE:
		return abc[lang][t][25];
	    default:
		return "";
	    }
	case NUM0:
	    switch (value) {
	    case NW:
		return abc[lang][t][32];
	    case N:
		return abc[lang][t][33];
	    case NE:
		return abc[lang][t][34]; // return "MODE";
	    case E:
		return abc[lang][t][35];
	    case C:
		return "";
	    case SE:
		return abc[lang][t][36];
	    case S:
		return abc[lang][t][37];
	    case SW:
		return abc[lang][t][38];
	    case W:
		return abc[lang][t][39];
	    default:
		return "";
	    }
	case NUM1:
	    switch (value) {
	    case NW:
		return abc[lang][t][40];
	    case N:
		return abc[lang][t][41];
	    case NE:
		return abc[lang][t][42];
	    case E:
		return abc[lang][t][45];
	    case C:
		return "";
	    case SE:
		return abc[lang][t][44];
	    case S:
		return abc[lang][t][43];
	    case SW:
		return abc[lang][t][47];
	    case W:
		return abc[lang][t][31];
	    default:
		return "";
	    }
	default:
	    return "";
	}
    }

    private void confirmEntry() {
	screenIsBeingTouched = false;
	final Dir prevVal = currentDir;
	currentDir = evalMotion(lastX, lastY);
	// Do some correction if the user lifts up on deadspace
	if (currentDir == Dir.NONE)
	    currentDir = prevVal;
	// The user never got a number that wasn't deadspace,
	// so assume 5.
	if (currentDir == Dir.NONE)
	    currentDir = Dir.C;
	String c = getCharacter(currentWheel, currentDir, ABCType.WRITTEN);
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
	currentDir = Dir.NONE;
	currentWheel = (keyboardMode == KeyboardMode.ALPHA_MODE) ? ALPHA
		: NUMERIC;
	currentCharacter = "";
    }

    @SuppressLint("DrawAllocation")
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
	canvas.drawText("CircleIME Active", 10, startY + 13, imeStatusPaint);

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

	    x = 5;
	    y = getHeight() - 20;
	    paint.setTextSize(20);
	    paint.setTextAlign(Paint.Align.LEFT);
	    y -= paint.ascent() / 2;
	} else {
	    final int offset = 90; // TODO : CHange According to screen size
	    final Loc[] xy = new Loc[numOfDir];
	    xy[Dir.NW.ordinal()] = new Loc((int) downX - offset, (int) (downY
		    - offset - (paint.ascent() / 2)));

	    xy[Dir.N.ordinal()] = new Loc((int) downX,
		    (int) (downY - offset - (paint.ascent() / 2)));

	    xy[Dir.NE.ordinal()] = new Loc((int) downX + offset, (int) (downY
		    - offset - (paint.ascent() / 2)));

	    xy[Dir.W.ordinal()] = new Loc((int) downX - offset,
		    (int) (downY - (paint.ascent() / 2)));

	    xy[Dir.E.ordinal()] = new Loc((int) downX + offset,
		    (int) (downY - (paint.ascent() / 2)));

	    xy[Dir.SW.ordinal()] = new Loc((int) downX - offset, (int) (downY
		    + offset - (paint.ascent() / 2)));

	    xy[Dir.S.ordinal()] = new Loc((int) downX,
		    (int) (downY + offset - (paint.ascent() / 2)));

	    xy[Dir.SE.ordinal()] = new Loc((int) downX + offset, (int) (downY
		    + offset - (paint.ascent() / 2)));

	    switch (currentWheel) {
	    case AE:
		paint.setColor(Color.RED);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][0],
			xy[Dir.NW.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][0]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][1],
			xy[Dir.N.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][1]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][2],
			xy[Dir.NE.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][2]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][7],
			xy[Dir.W.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][7]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][3],
			xy[Dir.E.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][3]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][6],
			xy[Dir.SW.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][6]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][5],
			xy[Dir.S.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][5]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][4],
			xy[Dir.SE.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][4]));
		break;
	    case IM:
		paint.setColor(Color.BLUE);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][15],
			xy[Dir.NW.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][15]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][8],
			xy[Dir.N.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][8]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][9],
			xy[Dir.NE.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][9]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][14],
			xy[Dir.W.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][14]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][10],
			xy[Dir.E.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][10]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][13],
			xy[Dir.SW.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][13]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][12],
			xy[Dir.S.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][12]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][11],
			xy[Dir.SE.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][11]));
		break;
	    case QU:
		paint.setColor(Color.GREEN);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][22],
			xy[Dir.NW.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][22]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][23],
			xy[Dir.N.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][23]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][16],
			xy[Dir.NE.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][16]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][21],
			xy[Dir.W.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][21]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][17],
			xy[Dir.E.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][17]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][20],
			xy[Dir.SW.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][20]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][19],
			xy[Dir.S.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][19]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][18],
			xy[Dir.SE.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][18]));
		break;
	    case BSY:
		paint.setColor(Color.YELLOW);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][26],
			xy[Dir.NW.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][26]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][30],
			xy[Dir.N.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][30]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][28],
			xy[Dir.NE.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][28]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][31],
			xy[Dir.W.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][31]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][24],
			xy[Dir.E.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][24]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][27],
			xy[Dir.SW.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][27]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][29],
			xy[Dir.S.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][29]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][25],
			xy[Dir.SE.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][25]));
		break;
	    case NUM0:
		paint.setColor(Color.YELLOW);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][32],
			xy[Dir.NW.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][32]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][33],
			xy[Dir.N.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][33]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][34],
			xy[Dir.NE.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][34]));

		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][39],
			xy[Dir.W.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][39]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][35],
			xy[Dir.E.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][35]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][38],
			xy[Dir.SW.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][38]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][37],
			xy[Dir.S.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][37]));

		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][36],
			xy[Dir.SE.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][36]));
		break;
	    case NUM1:
		paint.setColor(Color.BLUE);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][40],
			xy[Dir.NW.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][40]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][41],
			xy[Dir.N.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][41]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][42],
			xy[Dir.NE.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][42]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][45],
			xy[Dir.E.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][45]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][44],
			xy[Dir.SE.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][44]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][43],
			xy[Dir.S.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][43]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][47],
			xy[Dir.SW.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][47]));
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][31],
			xy[Dir.W.ordinal()], canvas, paint,
			currentCharacter.equals(abc[lang][ABCType.READ
				.ordinal()][31]));

		break;
	    case ALPHA:
		paint.setColor(Color.RED);

		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][0],
			xy[Dir.NW.ordinal()], canvas, paint, false);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][4],
			xy[Dir.SE.ordinal()], canvas, paint, false);
		paint.setColor(Color.BLUE);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][8],
			xy[Dir.N.ordinal()], canvas, paint, false);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][12],
			xy[Dir.S.ordinal()], canvas, paint, false);
		paint.setColor(Color.GREEN);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][16],
			xy[Dir.NE.ordinal()], canvas, paint, false);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][20],
			xy[Dir.SW.ordinal()], canvas, paint, false);
		paint.setColor(Color.YELLOW);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][24],
			xy[Dir.W.ordinal()], canvas, paint, false);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][31],
			xy[Dir.E.ordinal()], canvas, paint, false);
		break;
	    case NUMERIC:
		paint.setColor(Color.YELLOW);

		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][32],
			xy[Dir.NW.ordinal()], canvas, paint, false);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][33],
			xy[Dir.N.ordinal()], canvas, paint, false);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][34],
			xy[Dir.NE.ordinal()], canvas, paint, false);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][35],
			xy[Dir.E.ordinal()], canvas, paint, false);

		paint.setColor(Color.BLUE);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][44],
			xy[Dir.SE.ordinal()], canvas, paint, false);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][43],
			xy[Dir.S.ordinal()], canvas, paint, false);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][47],
			xy[Dir.SW.ordinal()], canvas, paint, false);
		drawCharacter(abc[lang][ABCType.DISPLAYED.ordinal()][31],
			xy[Dir.W.ordinal()], canvas, paint, false);

		break;
	    default:
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

    @SuppressWarnings("static-method")
    private void drawCharacter(String character, Loc xy, Canvas canvas,
	    Paint paint, boolean isSelected) {
	final int regSize = 50;
	final int selectedSize = regSize * 2;
	if (isSelected)
	    paint.setTextSize(selectedSize);
	else
	    paint.setTextSize(regSize);
	canvas.drawText(character, xy.x, xy.y, paint);
    }

    public Dir evalMotion(double x, double y) {
	final float rTolerance = 25;
	final double thetaTolerance = (Math.PI / 16);

	final double r = Math.sqrt(((downX - x) * (downX - x))
		+ ((downY - y) * (downY - y)));

	if (r < rTolerance)
	    return Dir.C;

	final double theta = Math.atan2(downY - y, downX - x);

	if (Math.abs(theta - left) < thetaTolerance)
	    return Dir.W;
	else if (Math.abs(theta - upleft) < thetaTolerance)
	    return Dir.NW;
	else if (Math.abs(theta - up) < thetaTolerance)
	    return Dir.N;
	else if (Math.abs(theta - upright) < thetaTolerance)
	    return Dir.NE;
	else if (Math.abs(theta - downright) < thetaTolerance)
	    return Dir.SE;
	else if (Math.abs(theta - down) < thetaTolerance)
	    return Dir.S;
	else if (Math.abs(theta - downleft) < thetaTolerance)
	    return Dir.SW;
	else if ((theta > right - thetaTolerance)
		|| (theta < rightWrap + thetaTolerance))
	    return Dir.E;

	// Off by more than the threshold, so it doesn't count
	return Dir.NONE;
    }

    /**
     * Changes between Alpha and numeric layouts.
     */
    public void toggleKeyboardMode() {
	if (keyboardMode == KeyboardMode.ALPHA_MODE) {
	    keyboardMode = KeyboardMode.NUMERIC_MODE;
	    currentWheel = NUMERIC;
	    _tts.speak("Numbers");
	} else {
	    keyboardMode = KeyboardMode.ALPHA_MODE;
	    currentWheel = ALPHA;
	    _tts.speak("Alpha");
	}
    }

    // @Override
    // public boolean onKeyDown(int keyCode, KeyEvent event) {
    // final String input = "";
    // Log.i(TAG, "Keycode_Menu.");
    // switch (keyCode) {
    // case KeyEvent.KEYCODE_MENU:
    // toggleKeyboardMode();
    // Log.i(TAG, "Keycode_Menu.");
    // return false;
    // default:
    // break;
    // }
    // return false;
    // }

    /**
     * Change the language of the input.
     * 
     * @param l
     */
    public void changeLang() {
	if (lang == languages.EN.ordinal()) {
	    lang = languages.HE.ordinal();
	    _tts.speak("עברית");
	} else {
	    lang = languages.EN.ordinal();
	    _tts.speak("english");
	}
	invalidate();
    }

}
