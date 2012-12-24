package com.yp2012g4.blindroid;

import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

/**
 * Read SMS activity. Select the Person from Whom the message than read the
 * messages.
 * 
 * @author Roman
 * 
 */
public class ReadSMSActivity extends onTouchEventClass implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quick_sms);
		tts = new TextToSpeech(this, this);

		TalkingButton b = (TalkingButton) findViewById(R.id.SMS_number_1);
		// if (list_of_phone_numbers.isEmpty()) { // if no favorite contacts -
		// make
		// // button unclickable
		// b.setClickable(false);
		// }
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (TalkingButton) findViewById(R.id.SMS_number_2);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (TalkingButton) findViewById(R.id.SMS_number_3);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (TalkingButton) findViewById(R.id.SMS_number_4);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (TalkingButton) findViewById(R.id.SMS_number_5);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (TalkingButton) findViewById(R.id.SMS_number_6);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (TalkingButton) findViewById(R.id.SMS_number_7);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (TalkingButton) findViewById(R.id.SMS_number_8);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (TalkingButton) findViewById(R.id.SMS_number_9);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		TalkingImageButton ib = (TalkingImageButton) findViewById(R.id.next_button);
		ib.setOnClickListener(this);
		ib.setOnTouchListener(this);

		ib = (TalkingImageButton) findViewById(R.id.settings_button);
		ib.setOnClickListener(this);
		ib.setOnTouchListener(this);

		ib = (TalkingImageButton) findViewById(R.id.back_button);
		ib.setOnClickListener(this);
		ib.setOnTouchListener(this);

	}

	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		ViewGroup quickSMSView = (ViewGroup) findViewById(R.id.QuickSMSActivity);
		getButtonsPosition(quickSMSView);
	}
	
	@Override
	public void onClick(View arg0) {

	}
	
	//will launch the activity
    private Runnable mLaunchTask = new Runnable() {
        public void run() {
        }
     };
	

}
