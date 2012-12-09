package com.yp2012g4.blindroid.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;

public class PhoneStatus {
    private static int _battery = -1;
    private static int _signal = -1;
    SignalStrengthListener signalStrengthListener;

    BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
	@Override
	public void onReceive(Context context, Intent intent) {
	    context.unregisterReceiver(this);
	    int rawlevel = intent.getIntExtra("level", -1);
	    int scale = intent.getIntExtra("scale", -1);
	    _battery = -1;
	    if (rawlevel >= 0 && scale > 0)
		_battery = (rawlevel * 100) / scale;
	}
    };

    private class SignalStrengthListener extends PhoneStateListener {
	@Override
	public void onSignalStrengthsChanged(
		android.telephony.SignalStrength signalStrength) {

	    // get the signal strength (a value between 0 and 31)
	    _signal = signalStrength.getGsmSignalStrength();

	    // do something with it (in this case we update a text view)
	    // signalStrengthTextView.setText(String.valueOf(strengthAmplitude));
	    super.onSignalStrengthsChanged(signalStrength);
	}
    }

    public PhoneStatus(Context context) {

	IntentFilter batteryLevelFilter = new IntentFilter(
		Intent.ACTION_BATTERY_CHANGED);
	context.registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }

    public int getBatteryLevel() {
	return _battery;
    }

    public int getSignalStrength() {
	return _signal;
    }

}