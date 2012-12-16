package com.yp2012g4.blindroid;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class QuickDialActivity extends onTouchEventClass implements
		OnClickListener {

	protected List<String> list_of_phone_numbers = new ArrayList<String>();

	// private static final int NUM_OF_QUICK_DIALS = 9;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		telephone();
		setContentView(R.layout.activity_quick_dial);
		tts = new TextToSpeech(this, this);
		// String phoneNumber = null;
		// int i = 0;
		// LinearLayout mainView = (LinearLayout)
		// findViewById(R.id.QuickDialActivity);
		// getButtonsPosition(mainView);

		// final String[] projection = new String[] {
		// ContactsContract.Contacts.STARRED

		// };

		// ContentResolver cr = getContentResolver();

		// Cursor starred = cr.query(ContactsContract.Contacts.CONTENT_URI,
		// projection, ContactsContract.Contacts._ID + "=?", null, null);
		// int phoneNumberIndex = starred
		// .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);
		/*
		 * Log.i("MyLog" , "starred = " + starred.toString() +
		 * " ---- phonNumberIndex = " + phoneNumberIndex); if (starred != null
		 * && starred.moveToFirst()) { try { while ( !starred.isAfterLast() || i
		 * < NUM_OF_QUICK_DIALS) { // Log.v(TAG, "Moved to first"); //
		 * Log.v(TAG, "Cursor Moved to first and checking"); phoneNumber =
		 * starred.getString(phoneNumberIndex);
		 * list_of_phone_numbers.add(phoneNumber); i++; if
		 * (starred.moveToNext()) continue; else break; } } finally { //
		 * Log.v(TAG, "In finally"); starred.close(); } }
		 */
		Button b = (Button) findViewById(R.id.Contact_number_1);
		// if (list_of_phone_numbers.isEmpty()) { // if no favorite contacts -
		// make
		// // button unclickable
		// b.setClickable(false);
		// }
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (Button) findViewById(R.id.Contact_number_2);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (Button) findViewById(R.id.Contact_number_3);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (Button) findViewById(R.id.Contact_number_4);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (Button) findViewById(R.id.Contact_number_5);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (Button) findViewById(R.id.Contact_number_6);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (Button) findViewById(R.id.Contact_number_7);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (Button) findViewById(R.id.Contact_number_8);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);

		b = (Button) findViewById(R.id.Contact_number_9);
		b.setOnClickListener(this);
		b.setOnTouchListener(this);
	}

	private void telephone() {
		TelephonyManager telephonyManager;
		// PhoneStateListener listener;

		// Get the telephony manager
		// telephonyManager = (TelephonyManager)
		// getSystemService(Context.TELEPHONY_SERVICE);

		// Create a new PhoneStateListener
		// Log.i("MyLog" , this.getApplicationContext().toString());
		// EndCallListener callListener = new
		// EndCallListener(this.getApplicationContext());
		EndCallListener callListener = new EndCallListener(
				this.getApplicationContext());
		telephonyManager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager
				.listen(callListener, EndCallListener.LISTEN_CALL_STATE);
		/*
		 * @Override public void onCallStateChanged(int state, String
		 * incomingNumber) { String stateString = "N/A"; switch (state) { case
		 * TelephonyManager.CALL_STATE_IDLE: if (stateString == "Off Hook") {
		 * finish(); } stateString = "Idle";
		 * 
		 * break; case TelephonyManager.CALL_STATE_OFFHOOK: stateString =
		 * "Off Hook"; break; case TelephonyManager.CALL_STATE_RINGING:
		 * stateString = "Ringing"; break; } Log.i("MyLog", "state is: " +
		 * stateString);
		 * 
		 * // textOut.append(String.format("\nonCallStateChanged: %s", //
		 * stateString)); // super.onCallStateChanged(state, incomingNumber);
		 * 
		 * } };
		 */

		// Register the listener with the telephony manager
		// telephonyManager.listen(listener,
		// PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_quick_dial, menu);
		return true;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		ViewGroup quickDialView = (ViewGroup) findViewById(R.id.QuickDialActivity);
		// Resources res = getResources();
		// XmlResourceParser parser = res.getXml(R.layout.activity_quick_dial);
		// getButtonsPosition(quickDialView, parser);
		// InputStream inputStream =
		// getResources().openRawResource(R.layout.activity_main);
		getButtonsPosition(quickDialView);
	}

	public void onClick(View v) {
		speakOut("Dialing to" + ((Button) v).getText().toString());
		switch (v.getId()) {
		case R.id.Contact_number_1:
			phoneCall("0524484993");
			break;
		case R.id.Contact_number_2:
			// phoneCall("0524484993");
			break;
		case R.id.Contact_number_3:
			// phoneCall("0524484993");
			break;
		case R.id.Contact_number_4:
			// phoneCall("0524484993");
			break;
		case R.id.Contact_number_5:
			// phoneCall("0524484993");
			break;
		case R.id.Contact_number_6:
			// phoneCall("0524484993");
			break;
		case R.id.Contact_number_7:
			// phoneCall("0524484993");
			break;
		case R.id.Contact_number_8:
			// phoneCall("0524484993");
			break;
		case R.id.Contact_number_9:
			finish();
			break;
		}
	}

	private void phoneCall(String s) {
		Intent call = new Intent(Intent.ACTION_CALL);
		// call.setData(Uri.parse("tel:"
		// + list_of_phone_numbers.get(0)));
		call.setData(Uri.parse("tel:" + s));
		startActivity(call);

	}

}