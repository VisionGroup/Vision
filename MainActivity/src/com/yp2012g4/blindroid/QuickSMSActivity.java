package com.yp2012g4.blindroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;

public class QuickSMSActivity extends onTouchEventClass implements OnClickListener {
	
	Handler mHandler = new Handler();
	
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_quick_sms, menu);
		return true;
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		ViewGroup quickSMSView = (ViewGroup) findViewById(R.id.QuickSMSActivity);
		getButtonsPosition(quickSMSView);
	}

	@Override
	public void onClick(View v) {
		final View view = v;
		if (v instanceof TalkingButton) {
			speakOut("Sending" + ((TalkingButton) v).getText().toString());
			mHandler.postDelayed(mLaunchTask,700);
			AlertDialog alertDialog = new AlertDialog.Builder(this).create(); //Read Update
	        alertDialog.setTitle("Choose contact");
	        alertDialog.setMessage("will send the SMS to the chosen contact");
	        alertDialog.setButton("Send..", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int which) {
//	        	   Log.i("MyLog" , ((TalkingButton) view).getText().toString() );
	        	   String messageToSend = ((TalkingButton) view).getText().toString();
					String number = "0524484993";
					SmsManager.getDefault().sendTextMessage(number, null,
							messageToSend, null, null);
					 

	           }
	        });

	        alertDialog.show();
	        
	    }
		if (v instanceof TalkingImageButton) {
			switch (v.getId()) {
			case R.id.next_button:
				speakOut("Next");
				break;
			case R.id.settings_button:
				speakOut("Settings");
				break;
			case R.id.back_button:
				speakOut("Back");
				finish();
				break;
			}
		}
	}
	
	//will launch the activity
    private Runnable mLaunchTask = new Runnable() {
        public void run() {
        }
     };
}
