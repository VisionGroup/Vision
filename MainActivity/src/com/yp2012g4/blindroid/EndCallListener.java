package com.yp2012g4.blindroid;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class EndCallListener extends PhoneStateListener {
	TelephonyManager telephonyManager;
	Context context;
	boolean flag = false;

	public EndCallListener(Context context) {
		this.context = context;
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {

		String stateString = "N/A";
		switch (state) {
		case TelephonyManager.CALL_STATE_IDLE:
			// if (stateString == "Off Hook") {

			if (flag) {
				Log.i("MyLog", "context is: "
						+ context.getPackageName().toString());
				Log.i("MyLog", "flag is now = " + flag);

				// restart app
				Intent i = context
						.getApplicationContext()
						.getPackageManager()
						.getLaunchIntentForPackage(
								context.getApplicationContext()
										.getPackageName());

				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);

				flag = false;
			}
			stateString = "Idle";

			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			stateString = "Off Hook";
			flag = true;
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			stateString = "Ringing";
			break;
		}
		Log.i("MyLog", "state is: " + stateString);
		Log.i("MyLog", "flag is: " + flag);

	}

}
