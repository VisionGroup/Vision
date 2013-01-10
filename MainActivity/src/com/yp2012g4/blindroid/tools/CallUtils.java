package com.yp2012g4.blindroid.tools;

/*
 * AutoAnswer Copyright (C) 2010 EverySoft
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * This file incorporates work covered by the following copyright and permission
 * notice:
 * 
 * Copyright (C) 2010 Tedd Scofield
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
import java.lang.reflect.Method;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

public class CallUtils /* extends IntentService */{
  private static String TAG = "bd.CallUtils";
  ListenToPhoneState listener;
  
//  public CallUtils() {
//    super("AutoAnswerIntentService");
//  }
//  
//  @Override protected void onHandleIntent(Intent intent) {
//    final Context context = getBaseContext();
//    // Load preferences
//    final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//    BluetoothHeadset bh = null;
//    if (prefs.getBoolean("headset_only", false))
//      bh = new BluetoothHeadset(this, null);
//    // Let the phone ring for a set delay
//    try {
//      Thread.sleep(Integer.parseInt(prefs.getString("delay", "2")) * 1000);
//    } catch (final InterruptedException e) {
//      // We don't really care
//    }
//    // Check headset status right before picking up the call
//    if (prefs.getBoolean("headset_only", false) && bh != null) {
//      if (bh.getState() != BluetoothHeadset.STATE_CONNECTED) {
//        bh.close();
//        return;
//      }
//      bh.close();
//    }
//    // Make sure the phone is still ringing
//    final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//    if (tm.getCallState() != TelephonyManager.CALL_STATE_RINGING)
//      return;
//    // Answer the phone
//    try {
//      answerPhoneAidl(context);
//    } catch (final Exception e) {
//      e.printStackTrace();
//      Log.d("AutoAnswer", "Error trying to answer using telephony service.  Falling back to headset.");
//      answerPhoneHeadsethook(context);
//    }
//    // Enable the speakerphone
//    if (prefs.getBoolean("use_speakerphone", false))
//      enableSpeakerPhone(context);
//    return;
//  }
//  
//  private void enableSpeakerPhone(Context context) {
//    final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//    audioManager.setSpeakerphoneOn(true);
//  }
//  
//  private void answerPhoneHeadsethook(Context context) {
//    // Simulate a press of the headset button to pick up the call
//    final Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);
//    buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
//    context.sendOrderedBroadcast(buttonDown, "android.permission.CALL_PRIVILEGED");
//    // froyo and beyond trigger on buttonUp instead of buttonDown
//    final Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
//    buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
//    context.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
//  }
  @SuppressWarnings("unchecked") public void answerCall(Context context) throws Exception {
    // Set up communication with the telephony service (thanks to Tedd's Droid
    // Tools!)
    final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    final Class c = Class.forName(tm.getClass().getName());
    final Method m = c.getDeclaredMethod("getITelephony");
    m.setAccessible(true);
    ITelephony telephonyService;
    telephonyService = (ITelephony) m.invoke(tm);
    // Silence the ringer and answer the call!
    telephonyService.silenceRinger();
    telephonyService.answerRingingCall();
  }
  
  public void endCall(Context context) {
    try {
      // Java reflection to gain access to TelephonyManager's
      // ITelephony getter
      final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
      final Class r = Class.forName(tm.getClass().getName());
      final Method m = r.getDeclaredMethod("getITelephony");
      m.setAccessible(true);
      final com.android.internal.telephony.ITelephony telephonyService = (ITelephony) m.invoke(tm);
      telephonyService.endCall();
      Thread.sleep(1000); // TODO: What for?
    } catch (final Exception e) {
      e.printStackTrace();
      Log.e(TAG, "FATAL ERROR: could not connect to telephony subsystem");
      Log.e(TAG, "Exception object: " + e);
    }// end catch*/
  }
  
  @SuppressWarnings("rawtypes") private void call(Context context, String number) {
    try {
      final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
      listener = new ListenToPhoneState();
      tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    } catch (final ActivityNotFoundException activityException) {
      Log.e("telephony-example", "Call failed", activityException);
    }
    try {
      // Java reflection to gain access to TelephonyManager's
      // ITelephony getter
      final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
      final Class c = Class.forName(tm.getClass().getName());
      final Method m = c.getDeclaredMethod("getITelephony");
      m.setAccessible(true);
      final com.android.internal.telephony.ITelephony telephonyService = (ITelephony) m.invoke(tm);
      // ((TextView)findViewById(R.id.Progress)).setText("Dialing");
      delay();
      telephonyService.call(number);
      listener = new ListenToPhoneState();
      final int state = tm.getCallState();
      listener.onCallStateChanged(state, number);
      Thread.sleep(2000);
    } catch (final Exception e) {
      e.printStackTrace();
      Log.e(TAG, "FATAL ERROR: could not connect to telephony subsystem");
      Log.e(TAG, "Exception object: " + e);
    }
    return;
  }
  
  private void delay() {
    for (int i = 1; i <= 1000; i++) {
    }
    for (int j = 1; j <= 100; j++) {
    }
    for (int k = 1; k <= 1000; k++) {
    }
    return;
  }
  
  private class ListenToPhoneState extends PhoneStateListener {
    // These routines don't check the called number, so probably they
    // aren't useful.
    @Override public void onCallStateChanged(int state, String number) {
      Log.i("telephony-example", "State changed: " /*
                                                    * + stateName(context,
                                                    * state)
                                                    */);
    }
    
    String stateName(Context context, int state) {
      final int time = 3000;
      switch (state) {
        case TelephonyManager.CALL_STATE_IDLE:
          // ((TextView)findViewById(R.id.Progress)).setText("idle");
          return "idle";
        case TelephonyManager.CALL_STATE_OFFHOOK:
          // ((TextView)findViewById(R.id.Progress)).setText("off hook");
          delay();
          try {
            // Java reflection to gain access to TelephonyManager's
            // ITelephony getter
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final Class c = Class.forName(tm.getClass().getName());
            final Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            final com.android.internal.telephony.ITelephony telephonyService = (ITelephony) m.invoke(tm);
            telephonyService.endCall();
            // ((TextView)findViewById(R.id.Progress)).setText("hanging up");
            Thread.sleep(1000);
            // finish();
          } catch (final Exception e) {
            e.printStackTrace();
            Log.e(TAG, "FATAL ERROR: could not connect to telephony subsystem");
            Log.e(TAG, "Exception object: " + e);
          }
          // return "off-hook";
        case TelephonyManager.CALL_STATE_RINGING:
          // ((TextView)findViewById(R.id.Progress)).setText("ringing");
          return "ringing";
      }
      return Integer.toString(state);
    }
  }
}
