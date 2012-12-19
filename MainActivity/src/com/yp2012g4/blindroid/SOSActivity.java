package com.yp2012g4.blindroid;

import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.yp2012g4.blindroid.customUI.TalkingImageButton;

public class SOSActivity extends onTouchEventClass {
	Handler mHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sos);
		tts = new TextToSpeech(this, this);


		// RelativeLayout t = (RelativeLayout)findViewById(R.id.SOS_textview);
		TalkingImageButton tb = (TalkingImageButton) findViewById(R.id.Send_SOS_Message);

		tb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Sending messageToSent SMS to the number specified
				String messageToSend = "I need your help!";
				String number = "0524484993";
				SmsManager.getDefault().sendTextMessage(number, null,
						messageToSend, null, null);
				 mHandler.postDelayed(mLaunchTask,400);
			}
		});
		tb.setOnTouchListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_sos, menu);
		return true;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		ViewGroup sosView = (ViewGroup) findViewById(R.id.SOS_textview);
		getButtonsPosition(sosView);
	}
	
	//will launch the activity
    private Runnable mLaunchTask = new Runnable() {
        public void run() {
        	finish();
        }
     };

}
