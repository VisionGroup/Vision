package com.yp2012g4.vision.tools;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AnalogClock;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.customUI.TalkingImageButton;
import com.yp2012g4.vision.settings.VisionApplication;

/**
 * This super class is handling on touch events with text-to-speech feedback for
 * the (blind) user
 * 
 * @author Amir
 * @version 1.1
 */
public abstract class VisionGestureDetector extends Activity implements
		OnClickListener, TextToSpeech.OnInitListener, OnGestureListener,
		OnTouchListener {
	private static final String TAG = "vision:VisionGestureDetector";
	/**
	 * for multitouch gesture detection
	 */
	private static final int TIMEOUT = ViewConfiguration.getDoubleTapTimeout() + 100;
	private long mFirstDownTime = 0;
	private boolean mSeparateTouches = false;
	private byte mTwoFingerTapCount = 0;
	/**
	 * Stores the dimensions of a button
	 */
	protected Rect rect;
	/**
	 * For supporting Text-To-Speech
	 */
	public TTS _t;
	/**
	 * Stores the last view
	 */
	protected View last_button_view = null;
	protected View curr_view;
	/**
	 * For inserting delays...
	 */
	public Handler mHandler;
	/**
	 * Stores the gestures
	 */
	protected GestureDetector gestureDetector;
	/**
	 * Flag indicating a click on control bar button
	 */
	protected boolean clickFlag;
	/**
	 * Mapping from views to their locations on screen
	 */
	private final Map<View, Rect> view_to_rect = new HashMap<View, Rect>();

	protected Locale myLocale;

	// ==================================================================
	// ===========================METHODS================================
	// ==================================================================
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onDown(MotionEvent e) {
		Log.i(TAG, "onDown");
		last_button_view = getView(e.getRawX(), e.getRawY()); // updating
																// curr_view
																// (inside
																// getView())
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.i(TAG, "onFling");
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		Log.i(TAG, "onLongPress");
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		Log.i(TAG, "onShowPress");
		if (isButtonType(last_button_view)) {
			hapticFeedback(last_button_view);
			speakOut(textToRead(last_button_view));
		}
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		Log.i(TAG, "onSingleTapUp");
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			Log.i("MyLog", "onTouch: ACTION_DOWN");
			// resetting to false when touching screen
			// updating curr_view (inside getView())
			last_button_view = getView(event.getRawX(), event.getRawY());
			break;
		case MotionEvent.ACTION_MOVE:
			// viewArray.clear(); // finger is scrolling so can clear the array
			for (final Map.Entry<View, Rect> entry : view_to_rect.entrySet())
				if (isButtonType(entry.getKey()))
					if (entry.getValue().contains((int) event.getRawX(),
							(int) event.getRawY()))
						if (last_button_view != entry.getKey()) {
							VisionApplication.restoreColors(last_button_view);
							hapticFeedback(entry.getKey());
							speakOut(textToRead(entry.getKey()));
							last_button_view = entry.getKey();
						} else
							last_button_view = getView(event.getRawX(),
									event.getRawY());
			break;
		case MotionEvent.ACTION_UP:
			Log.i(TAG, "ACTION UP");
			VisionApplication.restoreColors(last_button_view);
			onActionUp(last_button_view);
			break;
		}
		gestureDetector.setIsLongpressEnabled(false);
		onTwoFingerDoubleTap(event);
		return gestureDetector.onTouchEvent(event);
	}

	private void reset(long time) {
		mFirstDownTime = time;
		mSeparateTouches = false;
		mTwoFingerTapCount = 0;
	}

	public boolean onTwoFingerDoubleTap(MotionEvent event) {
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			if (mFirstDownTime == 0
					|| event.getEventTime() - mFirstDownTime > TIMEOUT)
				reset(event.getDownTime());
			break;
		case MotionEvent.ACTION_POINTER_UP:
			if (event.getPointerCount() == 2)
				mTwoFingerTapCount++;
			else
				mFirstDownTime = 0;
			break;
		case MotionEvent.ACTION_UP:
			if (!mSeparateTouches)
				mSeparateTouches = true;
			else if (mTwoFingerTapCount == 2
					&& event.getEventTime() - mFirstDownTime < TIMEOUT) {
				// open back door to tools.
				final Intent intent = new Intent(Settings.ACTION_SETTINGS);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				startActivity(intent);
				mFirstDownTime = 0;
				return true;
			}
			break;
		default:
			break;
		}
		return false;
	}

	public Map<View, Rect> getView_to_rect() {
		return view_to_rect;
	}

	/**
	 * In this overridden function we gather the buttons positions of the
	 * current activity and make them all listen to onTouch and onClick.
	 * 
	 * @param hasFocus
	 *            indicates whether a window has the focus
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		Log.i("MyLog", "onWindowFocusChanged");
		super.onWindowFocusChanged(hasFocus);
		final ViewGroup mainView = (ViewGroup) findViewById(getViewId());
		getButtonsPosition(mainView);
		setLocaleToActivity(this);
		for (final Map.Entry<View, Rect> entry : view_to_rect.entrySet()) {
			entry.getKey().setOnClickListener(this);
			entry.getKey().setOnTouchListener(this);
		}
		// changing locale (language) for all activities
//		if (myLocale != null) {
//			Log.i("MyLog", "INN");
//			Log.i("MyLog", myLocale.toString());
//
//			Locale.setDefault(myLocale);
//			Configuration config = new Configuration();
//			config.locale = myLocale;
//			getBaseContext().getResources().updateConfiguration(config,
//					getBaseContext().getResources().getDisplayMetrics());
//		}

		// reads layout description out loud
		if (hasFocus) {
			VisionApplication.applyButtonSettings(view_to_rect.keySet(),
					mainView);
			if (mainView.getContentDescription() != null)
				speakOut(findViewById(getViewId()).getContentDescription()
						.toString());
		}
	}

	public void setLocaleToActivity(Activity activity) {
		if (myLocale != null) {
			Configuration config = new Configuration();
			config.locale = myLocale;
			Locale.setDefault(myLocale);
			activity.getResources().updateConfiguration(config,
					activity.getResources().getDisplayMetrics());
		}
	}

	/**
	 * This is an abstract method which returns the Id of a view
	 * 
	 * @return Id of the current view
	 */
	public abstract int getViewId();

	/**
	 * Checking whether a given has a button type
	 * 
	 * @param v
	 *            the view to to be checked against a button type
	 * @return true if the given view has a button type
	 */
	protected static boolean isButtonType(View v) {
		return (v instanceof TalkingButton || v instanceof TalkingImageButton);
	}

	/**
	 * return a button text (after upcasting the given view to the appropriate
	 * button type)
	 * 
	 * @param v
	 *            the view which is upcasted to one of the buttons types (if
	 *            it's a button)
	 * @return the button text to be read
	 */
	public static String textToRead(View v) {
		return v instanceof TalkingButton ? ((TalkingButton) v).getReadText()
				: ((TalkingImageButton) v).getReadText();
	}

	/**
	 * This method defined the behavior when the user stops touching the screen
	 * 
	 * @param v
	 *            - last view being touched
	 * 
	 */
	public void onActionUp(View v) {/* to be overridden */
	}

	/**
	 * This method speaks out a given string
	 * 
	 * @param s
	 *            the string to speak out
	 */
	public void speakOut(String s) {
		if (_t == null) {
			Log.e(TAG, "TTS is null");
			return;
		}
		_t.speak(s);
	}

	@Override
	public void onDestroy() {
		_t.shutdown();
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		_t = new TTS(this, this);
		if (_t.isRuning())
			speakOut("start");
		else
			Log.e(TAG, "tts init error");
		VisionApplication.setThemeToActivity(this);
		super.onCreate(savedInstanceState);
		mHandler = new Handler();
		gestureDetector = new GestureDetector(this);
		clickFlag = false;
	}

	/**
	 * A method for keeping the exact positions of each button (or text view) in
	 * a given view
	 * 
	 * @param v
	 *            the view from which to get buttons positions
	 */
	public void getButtonsPosition(View v) {
		rect = new Rect(getRelativeLeft(v), getRelativeTop(v),
				getRelativeLeft(v) + v.getWidth(), getRelativeTop(v)
						+ v.getHeight());
		if (v instanceof TimePicker || v instanceof AnalogClock)
			return; // ignoring these view types
		if (isButtonType(v) || v instanceof TextView) {
			view_to_rect.put(v, rect);
			return;
		}
		final ViewGroup vg = (ViewGroup) v;
		view_to_rect.put(v, rect);
		for (int i = 0; i < vg.getChildCount(); i++)
			getButtonsPosition(vg.getChildAt(i));
		return;
	}

	/**
	 * A method to calculate the exact left position of a view on screen
	 * (relative to the view's root)
	 * 
	 * @param myView
	 *            the given view for which we want to calculate the exact left
	 *            position on screen
	 * @return exact left position on screen
	 */
	public static int getRelativeLeft(View myView) {
		if (myView.getParent() == myView.getRootView())
			return myView.getLeft();
		return myView.getLeft() + getRelativeLeft((View) myView.getParent());
	}

	/**
	 * A method to calculate the exact top position of a view on screen
	 * (relative to the view's root)
	 * 
	 * @param myView
	 *            the given view for which we want to calculate the exact top
	 *            position on screen
	 * @return exact top position on screen
	 */
	public static int getRelativeTop(View myView) {
		if (myView.getParent() == myView.getRootView())
			return myView.getTop();
		return myView.getTop() + getRelativeTop((View) myView.getParent());
	}

	/**
	 * This method returns a specific button view by a given coordinates on
	 * screen
	 * 
	 * @param x
	 *            X-coordinate on screen
	 * @param y
	 *            Y-coordinate on screen
	 * @return the view (button) for which the given coordinates belongs to.
	 *         Null - if the coordinates are out of any button
	 */
	protected View getView(float x, float y) {
		for (final Map.Entry<View, Rect> entry : view_to_rect.entrySet())
			if (entry.getValue().contains((int) x, (int) y)) {
				curr_view = entry.getKey();
				if (isButtonType(entry.getKey()))
					// get view of buttons only
					return entry.getKey();
			}
		return null;
	}

	@Override
	public void onInit(int status) {
		if (!_t.isRuning())
			_t = new TTS(this, this);
	}

	/**
	 * Finishes the activity after some delay
	 */
	public Runnable mLaunchTask = new Runnable() {
		@Override
		public void run() {
			finish();
		}
	};

	@Override
	public void startActivity(Intent intent) {
		vibrate(200);
		super.startActivity(intent);
	}

	/**
	 * vibration during touch.
	 * 
	 * @param i
	 */
	protected void vibrate(int duration) {
		final Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vb.vibrate(duration);
	}

	protected void hapticFeedback(View v) {
		vibrate(20);
		VisionApplication.visualFeedback(v);
	}
}
