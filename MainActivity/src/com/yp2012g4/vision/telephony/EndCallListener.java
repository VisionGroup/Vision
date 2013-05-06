package com.yp2012g4.vision.telephony;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * This class overrides onCallStateChanged in order to go back to activity when
 * a phone call finished
 * 
 * @author Amir
 * @version
 */
public class EndCallListener extends PhoneStateListener {
  TelephonyManager telephonyManager;
  Context context;
  /**
   * isHangUp is true if phone hang-up, else - false
   */
  boolean isHangUp = false;
  
  /**
   * @param context
   *          the application to return to when the phone call finishes
   */
  public EndCallListener(Context c) {
    context = c;
  }
  
  @Override public void onCallStateChanged(int state, String incomingNumber) {
    switch (state) {
      case TelephonyManager.CALL_STATE_IDLE:// phone hang-up
        if (isHangUp) {
          // restart app
          final Intent i = context.getApplicationContext().getPackageManager()
              .getLaunchIntentForPackage(context.getApplicationContext().getPackageName());
          i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
          context.startActivity(i);
          isHangUp = false;
        }
        break;
      case TelephonyManager.CALL_STATE_OFFHOOK:
        isHangUp = true;
        break;
      default:
        break;
    }
  }
}
