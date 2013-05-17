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
  Context _context;
  /**
   * isHangUp is true if phone hang-up, else - false
   */
  boolean _isHangUp = false;
  
  /**
   * @param _context
   *          the application to return to when the phone call finishes
   */
  public EndCallListener(final Context c) {
    _context = c;
  }
  
  @Override public void onCallStateChanged(final int state, final String incomingNumber) {
    switch (state) {
      case TelephonyManager.CALL_STATE_IDLE:// phone hang-up
        if (_isHangUp) {
          // restart app
          final Intent i = _context.getApplicationContext().getPackageManager()
              .getLaunchIntentForPackage(_context.getApplicationContext().getPackageName());
          i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
          _context.startActivity(i);
          _isHangUp = false;
        }
        break;
      case TelephonyManager.CALL_STATE_OFFHOOK:
        _isHangUp = true;
        break;
      default:
        break;
    }
  }
}
