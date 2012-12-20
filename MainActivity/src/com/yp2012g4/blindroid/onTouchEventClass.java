package com.yp2012g4.blindroid;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;

public class onTouchEventClass extends Activity implements OnTouchListener,
		TextToSpeech.OnInitListener {
	protected Rect rect;
	protected TextToSpeech tts;
	protected TalkingButton tool_tip;
	protected TalkingButton home_screen;
	protected TalkingButton help;
	protected TalkingButton back;
	protected View prev_view;
	protected View last_view;
	protected View movedTo;
	protected Drawable d;
	// protected OnDoubleTapListener l;

	protected Map<TalkingButton, Rect> button_to_rect = new HashMap<TalkingButton, Rect>();
	protected Map<TalkingImageButton, Rect> imageButton_to_rect = new HashMap<TalkingImageButton, Rect>();

	// protected Map<Button, Intent> button_to_intent = new HashMap<Button,
	// Intent>();

	// protected GestureDetector gestureDetector = new GestureDetector(this);
	/*
	 * new GestureDetector . SimpleOnGestureListener () { public boolean
	 * onDoubleTap ( MotionEvent e) { Log .i( "MyLog" , "Open new activty here"
	 * ); startActivity (( button_to_intent .get( last_view ))); return false ;
	 * } });
	 */

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float accurateX = getRelativeLeft(v) + event.getX();
		float accurateY = getRelativeTop(v) + event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			// Log.i("MyLog", "accurateX = " + accurateX +
			// "  -----  accurateY = " + accurateY);
			prev_view = getView(accurateX, accurateY);
			if (last_view != null && prev_view!=last_view){//if not first touch in activity 
				last_view.setPressed(false);			   //and touching in different view
			}
			if (v instanceof TalkingButton) {
				Log.i("MyLog" , "DOWN in Button");
				speakOut(((TalkingButton) v).getText().toString());

			}
			if (v instanceof TalkingImageButton) {
				Log.i("MyLog" , "DOWN in ImageButton");
//				d = ((ImageButton) v).getDrawable();
//				Log.i("MyLog", "the drawable is: " + d.toString());
				((TalkingImageButton) v)
						.setColorFilter(Color.argb(150, 255, 165, 0)); // or
				speakOut(((TalkingImageButton) v).getContentDescription().toString());
			}
			else{ //we touched outside of any button...
				Log.i("MyLog" , prev_view.getClass().getSimpleName());
			}

		}

		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			movedTo = getView(accurateX, accurateY /* v */);
			if (movedTo instanceof TalkingButton) {

				Log.i("MyLog", "ON ACTION MOVE BUTTON");
				for (Map.Entry<TalkingButton, Rect> entry : button_to_rect.entrySet()) {
					if (entry.getValue().contains((int) accurateX,
							(int) accurateY)) {
						if (prev_view != entry.getKey()) {
							if (prev_view instanceof TalkingImageButton){
//								((ImageButton) prev_view).getBackground().set
								((TalkingImageButton) prev_view).setColorFilter(Color
										.argb(0, 255, 165, 0));
							}
							if (prev_view instanceof TalkingButton){
//								((Button)prev_view).setFocusableInTouchMode(false);
								((TalkingButton)prev_view).setPressed(false);
							}
							speakOut(entry.getKey().getText().toString());
							((TalkingButton)entry.getKey()).getBackground().setAlpha((int)( 0.8 * 255));

							// prev_view.setSelected(!prev_view.isSelected());
							// prev_view.setPressed(false);

							// entry.getKey().setSelected(!prev_view.isSelected());
							entry.getKey().setPressed(true);
							prev_view = entry.getKey();

						}
					}
				}
			}

			if (movedTo instanceof TalkingImageButton) {
				for (Map.Entry<TalkingImageButton, Rect> entry : imageButton_to_rect
						.entrySet()) {
					if (entry.getValue().contains((int) accurateX,
							(int) accurateY)) {
						if (prev_view != entry.getKey()) {
							if (prev_view instanceof TalkingButton){
//								((TalkingButton)prev_view).getBackground().setAlpha((int)( 1.0 * 255));
								((TalkingButton)prev_view).setPressed(false);
							}
							if (prev_view instanceof TalkingImageButton){
//								((ImageButton) prev_view).setBackgroundDrawable(d)/*setColorFilter(Color
//										.argb(0, 255, 165, 0))*/;
								((TalkingImageButton)prev_view).setColorFilter(Color
										.argb(0, 255, 165, 0));
							}
							speakOut(entry.getKey().getContentDescription()
									.toString());
							// prev_view.setSelected(!prev_view.isSelected());
							// prev_view.setPressed(false);

							// entry.getKey().setSelected(!prev_view.isSelected());
							// prev_view.setPressed(true);
							
							entry.getKey().setColorFilter(
									Color.argb(150, 255, 165, 0));
							prev_view = entry.getKey();
						}
					}
				}
			}
			else prev_view = getView(accurateX, accurateY);

		}

		if (event.getAction() == MotionEvent.ACTION_UP) {
			last_view = getView(accurateX, accurateY);
			if (last_view instanceof TalkingImageButton) {
				((TalkingImageButton) last_view).setColorFilter(Color.argb(0, 255,
						165, 0)); // or null

			}
			if (last_view instanceof TalkingButton){
//				((TalkingButton)last_view).getBackground().setAlpha((int)( 1.0 * 255));
//				((TalkingButton)last_view).setPressed(false);
			}
			// last_view.setSelected(false);
			// last_view.setPressed(true);
			// last_view.setClickable(false);

		}
		// gestureDetector.setOnDoubleTapListener(this);
		// return gestureDetector.onTouchEvent(event);
		return false;
	}

	public void speakOut(String s) {
		tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
	}

	@Override
	public void onDestroy() {
		if (tts != null) {
			speakOut("stop");
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			int r = tts.setLanguage(Locale.US);
			if (r == TextToSpeech.LANG_NOT_SUPPORTED
					|| r == TextToSpeech.LANG_MISSING_DATA) {
				Log.e("tts", "error setLanguage");
				return;
			}
			speakOut("start");
			return;
		}
		Log.e("tts", "error init language");

	}

	public void getButtonsPosition(View v) {
		rect = new Rect(getRelativeLeft(v), getRelativeTop(v),
				getRelativeLeft(v) + v.getWidth(), getRelativeTop(v)
						+ v.getHeight());
		if (v instanceof TalkingButton) {

			// Construct a rect of the view's bounds

			button_to_rect.put((TalkingButton) v, rect);
			// Log.i("MyLog", "size = " + button_to_rect.size());
			// Log.i("MyLog", "left= " + rect.left + "  ;  top = " + rect.top
			// + "  ;  right = " + rect.right + "  ;  bottom = "
			// + rect.bottom);
			return;
		}
		if (v instanceof TalkingImageButton) {
			imageButton_to_rect.put((TalkingImageButton) v, rect);
			// Log.i("MyLog", "size of imagebuttons = " +
			// imageButton_to_rect.size());
			// Log.i("MyLog", "ImageButton dimensions are: "+"left= " +
			// rect.left + "  ;  top = " + rect.top
			// + "  ;  right = " + rect.right + "  ;  bottom = "
			// + rect.bottom);
			return;
		}
		Log.i("MyLog" , "getButtons: " + ((ViewGroup)v).getContext() + " --- "+((ViewGroup)v).getClass().getSimpleName());
		ViewGroup vg = (ViewGroup) v;
		if (vg instanceof TimePicker){ //TODO check how to handle TimePicker...
			return;
		}
		for (int i = 0; i < vg.getChildCount(); i++) {
			getButtonsPosition(vg.getChildAt(i));
		}
		return;
	}

	private int getRelativeLeft(View myView) {
		if (myView.getParent() == myView.getRootView()) {
			return myView.getLeft();
		} else
			return (myView.getLeft() + getRelativeLeft((View) myView
					.getParent()));
	}

	private int getRelativeTop(View myView) {
		if (myView.getParent() == myView.getRootView()) {
			return myView.getTop();
		} else
			return (myView.getTop() + getRelativeTop((View) myView.getParent()));
	}

	private View getView(float x, float y) {
		for (Map.Entry<TalkingButton, Rect> entry : button_to_rect.entrySet()) {
			Log.i("MyLog", "Button:     Left = " + entry.getValue().left
					+ "  ;  Top = " + entry.getValue().top);
			if (entry.getValue().contains((int) x, (int) y)) {
				return ((TalkingButton) entry.getKey());
			}
		}
		for (Map.Entry<TalkingImageButton, Rect> entry : imageButton_to_rect
				.entrySet()) {
			Log.i("MyLog", "ImageButton:     Left = " + entry.getValue().left
					+ "  ;  Top = " + entry.getValue().top + "  ;  Right = "
					+ entry.getValue().right);
			Log.i("MyLog", "x = " + (int) x + "y = " + (int) y);

			if (entry.getValue().contains((int) x, (int) y)) {
				return ((TalkingImageButton) entry.getKey());
			}
		}
		return null;

	}

}
