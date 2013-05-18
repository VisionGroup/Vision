package com.yp2012g4.vision.apps.phoneStatus;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * 
 * @author Amir Blumental
 * @version 1.1
 */
public class PhoneNotifications {
  private static final int DEFAULT_EXTRA_VALUE = -1;
  
  /**
   * This listener class allow us to update the signal member, and this is the
   * only way android allow us to check the signal
   * 
   * @author Amit Yaffe
   * 
   */
  private class SignalStrengthListener extends PhoneStateListener {
    protected SignalStrengthListener() {
    }
    
    @Override public void onSignalStrengthsChanged(final android.telephony.SignalStrength signalStrength) {
      // get the signal strength (a value between 0 and 31)
      signal = signalStrength.getGsmSignalStrength();
      super.onSignalStrengthsChanged(signalStrength);
    }
  }
  
  /**
   * Getter for the signal static member
   * 
   * @return the signal strength
   */
  public static int getSignalStrength() {
    return signal;
  }
  
  private final Context c;
  public static int signal = DEFAULT_EXTRA_VALUE;
  SignalStrengthListener signalStrengthListener;
  
  public PhoneNotifications(final Context c1) {
    c = c1;
  }
  
  /**
   * Get the phone current battery level
   * 
   * @return battery level between 0 to 1
   */
  public float getBatteryLevel() {
    final IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    final Intent batteryStatus = c.registerReceiver(null, ifilter);
    final int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, DEFAULT_EXTRA_VALUE);
    final int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, DEFAULT_EXTRA_VALUE);
    final float batteryPct = level / (float) scale;
    return batteryPct;
  }
  
  /**
   * Check if the charger is connected
   * 
   * @return true if charger is connected false otherwise
   */
  public boolean getChargerStatus() {
    final IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    final Intent batteryStatus = c.registerReceiver(null, ifilter);
    // Are we charging / charged?
    final int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, DEFAULT_EXTRA_VALUE);
    final boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
    return isCharging;
  }
  
  /**
   * start the listener for the signal strength
   */
  public void startSignalLisener() {
    signalStrengthListener = new SignalStrengthListener();
    ((TelephonyManager) c.getSystemService("phone")).listen(signalStrengthListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
  }
}
