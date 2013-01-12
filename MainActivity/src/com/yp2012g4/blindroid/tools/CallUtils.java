package com.yp2012g4.blindroid.tools;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

import com.android.internal.telephony.ITelephony;

/**
 * This util class is used to answer, reject (and later call) calls.
 * 
 * @author Amit Yaffe
 * @version 1.1
 * 
 */
public class CallUtils {
  // Used by Log
  private static String TAG = "bd.CallUtils";
  // Used by the Incoming call receiver to transfer data to the activity.
  public static final String RANG_KEY = "rang";
  public static final String INCOING_NUMBER_KEY = "iNumber";
  private com.android.internal.telephony.ITelephony telephonyService;
  AudioManager audioManager;
  
  // ListenToPhoneState listener;
  public static void enableSpeakerPhone(Context context) {
    final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    audioManager.setSpeakerphoneOn(true);
  }
  
  @Deprecated public void answerCall() throws Exception {
    // Silence the ringer and answer the call!
    try {
      telephonyService.silenceRinger();
      telephonyService.answerRingingCall();
    } catch (final Exception e) {
      Log.e(TAG, "Error answerig calls", e);
    }
  }
  
  public CallUtils(Context context) {
    super();
    try {
      final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
      final Class<?> c = Class.forName(tm.getClass().getName());
      final Method m = c.getDeclaredMethod("getITelephony");
      m.setAccessible(true);
      telephonyService = (ITelephony) m.invoke(tm);
      audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    } catch (final Exception e) {
      Log.e(TAG, "Error starting CallUtils", e);
    }
  }
  
  public static void answerPhoneHeadsethook(Context context) {
    // Simulate a press of the headset button to pick up the call
    // SettingsClass.logMe(tag, "Simulating headset button");
    final Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);
    buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
    context.sendOrderedBroadcast(buttonDown, "android.permission.CALL_PRIVILEGED");
    // froyo and beyond trigger on buttonUp instead of buttonDown
    final Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
    buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
    context.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");
  }
  
  public void endCall() {
    try {
      // Java reflection to gain access to TelephonyManager's
      // ITelephony getter
      telephonyService.endCall();
      Thread.sleep(1000); // TODO: What for?
    } catch (final Exception e) {
      e.printStackTrace();
      Log.e(TAG, "FATAL ERROR: could not connect to telephony subsystem");
      Log.e(TAG, "Exception object: " + e);
    }// end catch*/
  }
  
  /*
   * @SuppressWarnings("rawtypes") private void call(Context context, String
   * number) { try { final TelephonyManager tm = (TelephonyManager)
   * context.getSystemService(Context.TELEPHONY_SERVICE); listener = new
   * ListenToPhoneState(); tm.listen(listener,
   * PhoneStateListener.LISTEN_CALL_STATE); } catch (final
   * ActivityNotFoundException activityException) { Log.e("telephony-example",
   * "Call failed", activityException); } try { // Java reflection to gain
   * access to TelephonyManager's // ITelephony getter final TelephonyManager tm
   * = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
   * final Class c = Class.forName(tm.getClass().getName()); final Method m =
   * c.getDeclaredMethod("getITelephony"); m.setAccessible(true); final
   * com.android.internal.telephony.ITelephony telephonyService = (ITelephony)
   * m.invoke(tm); //
   * ((TextView)findViewById(R.id.Progress)).setText("Dialing"); delay();
   * telephonyService.call(number); listener = new ListenToPhoneState(); final
   * int state = tm.getCallState(); listener.onCallStateChanged(state, number);
   * Thread.sleep(2000); } catch (final Exception e) { e.printStackTrace();
   * Log.e(TAG, "FATAL ERROR: could not connect to telephony subsystem");
   * Log.e(TAG, "Exception object: " + e); } return; }
   * 
   * private void delay() { for (int i = 1; i <= 1000; i++) { } for (int j = 1;
   * j <= 100; j++) { } for (int k = 1; k <= 1000; k++) { } return; }
   * 
   * 
   * 
   * String stateName(Context context, int state) { final int time = 3000;
   * switch (state) { case TelephonyManager.CALL_STATE_IDLE: //
   * ((TextView)findViewById(R.id.Progress)).setText("idle"); return "idle";
   * case TelephonyManager.CALL_STATE_OFFHOOK: //
   * ((TextView)findViewById(R.id.Progress)).setText("off hook"); delay(); try {
   * // Java reflection to gain access to TelephonyManager's // ITelephony
   * getter final TelephonyManager tm = (TelephonyManager)
   * context.getSystemService(Context.TELEPHONY_SERVICE); final Class c =
   * Class.forName(tm.getClass().getName()); final Method m =
   * c.getDeclaredMethod("getITelephony"); m.setAccessible(true); final
   * com.android.internal.telephony.ITelephony telephonyService = (ITelephony)
   * m.invoke(tm); telephonyService.endCall(); //
   * ((TextView)findViewById(R.id.Progress)).setText("hanging up");
   * Thread.sleep(1000); // finish(); } catch (final Exception e) {
   * e.printStackTrace(); Log.e(TAG,
   * "FATAL ERROR: could not connect to telephony subsystem"); Log.e(TAG,
   * "Exception object: " + e); } // return "off-hook"; case
   * TelephonyManager.CALL_STATE_RINGING: //
   * ((TextView)findViewById(R.id.Progress)).setText("ringing"); return
   * "ringing"; } return Integer.toString(state); } }
   */
  public void silenceRinger() {
    try {
      // telephonyService.silenceRinger();
      audioManager.setStreamMute(AudioManager.STREAM_RING, true);
    } catch (final Exception e) {
      Log.e(TAG, "Error silencing Ringer", e);
    }
  }
  
  public void restoreRinger() {
    try {
      // telephonyService.silenceRinger();
      audioManager.setStreamMute(AudioManager.STREAM_RING, false);
    } catch (final Exception e) {
      Log.e(TAG, "Error silencing Ringer", e);
    }
  }
}
