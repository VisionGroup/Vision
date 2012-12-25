package com.yp2012g4.blindroid.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.graphics.Rect;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class onTouchEventClass extends Activity 
			implements OnTouchListener,	TextToSpeech.OnInitListener {
	protected Rect rect;
	protected TextToSpeech tts;
	protected Button tool_tip;
	protected Button home_screen;
	protected Button help;
	protected Button back;
	View prev_view;
	View last_view;
	protected Map<Button, Rect> button_to_rect = new HashMap<Button, Rect>();
	protected Map<ImageButton, Rect> imageButton_to_rect = new HashMap<ImageButton, Rect>();

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
			prev_view = getView(accurateX, accurateY, v);
			if (v instanceof Button) {
				
				speakOut(((Button) v).getText().toString());
			}
			if (v instanceof ImageButton) {
				speakOut(((ImageButton) v).getContentDescription().toString());
			}

		}

		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (v instanceof Button) {
				for (Map.Entry<Button, Rect> entry : button_to_rect.entrySet()) {
					if (entry.getValue().contains((int) accurateX,
							(int) accurateY)) {
						if (prev_view != entry.getKey()) {
							Log.i("MyLog", "WTFFFFFFF");
							speakOut(entry.getKey().getText().toString());
//							prev_view.setSelected(!prev_view.isSelected());
							// prev_view.setPressed(false);

//							entry.getKey().setSelected(!prev_view.isSelected());
							// prev_view.setPressed(true);
							prev_view = entry.getKey();
						}
					}
				}
			}
			
			if (v instanceof ImageButton) {
				for (Map.Entry<ImageButton, Rect> entry : imageButton_to_rect.entrySet()) {
					if (entry.getValue().contains((int) accurateX,
							(int) accurateY)) {
//						Log.i("MyLog" , "HERE!!!!");

						if (prev_view != entry.getKey()) {
							Log.i("MyLog" , "OUT OF BUTTON: "+ ((ImageButton)prev_view).getContentDescription().toString());

							speakOut(entry.getKey().getContentDescription().toString());
//							prev_view.setSelected(!prev_view.isSelected());
							// prev_view.setPressed(false);

//							entry.getKey().setSelected(!prev_view.isSelected());
							// prev_view.setPressed(true);
							prev_view = entry.getKey();
						}
					}
				}
			}
			
		}

		if (event.getAction() == MotionEvent.ACTION_UP) {
			last_view = getView(accurateX, accurateY, v);
			last_view.setSelected(false);
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

		if (v instanceof Button) {

			// Construct a rect of the view's bounds
			rect = new Rect(getRelativeLeft(v), getRelativeTop(v),
					getRelativeLeft(v) + v.getWidth(), getRelativeTop(v)
							+ v.getHeight());
			// list_of_x_coords.add(getRelativeLeft(v));
			// list_of_y_coords.add(getRelativeTop(v));
			// list_of_widths.add(getRelativeLeft(v) + v.getWidth());
			// list_of_heights.add(getRelativeTop(v) + v.getHeight());
			// list_of_buttons.add((Button) v);
			// button_to_x.put((Button)v, getRelativeLeft(v));
			// button_to_y.put((Button)v, getRelativeTop(v));
			button_to_rect.put((Button) v, rect);
			Log.i("MyLog", "size = " + button_to_rect.size());
			Log.i("MyLog", "left= " + rect.left + "  ;  top = " + rect.top
					+ "  ;  right = " + rect.right + "  ;  bottom = "
					+ rect.bottom);
			return;
		}
		if (v instanceof ImageButton) {
			rect = new Rect(getRelativeLeft(v), getRelativeTop(v),
					getRelativeLeft(v) + v.getWidth(), getRelativeTop(v)
							+ v.getHeight());
			imageButton_to_rect.put((ImageButton) v, rect);
			Log.i("MyLog", "size of imagebuttons = " + imageButton_to_rect.size());
			Log.i("MyLog", "Button: " + v.getId() +" dimensions are: "+"left= " + rect.left + "  ;  top = " + rect.top
					+ "  ;  right = " + rect.right + "  ;  bottom = "
					+ rect.bottom);
			return;
		}

		ViewGroup vg = (ViewGroup) v;
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

	private View getView(float x, float y, View v) {
		if (v instanceof Button) {
			for (Map.Entry<Button, Rect> entry : button_to_rect.entrySet()) {
				if (entry.getValue().contains((int) x, (int) y)) {
					return entry.getKey();
				}
			}
		}
		if (v instanceof ImageButton) {
			Log.i("MyLog" , "ENTERE GETVIEW");
			for (Map.Entry<ImageButton, Rect> entry : imageButton_to_rect
					.entrySet()) {
				if (entry.getValue().contains((int) x, (int) y)) {
					return entry.getKey();
				}
			}
		}

		return null;

	}

}
