package com.yp2012g4.vision.apps.telephony;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

import com.android.internal.telephony.ITelephony;
import com.yp2012g4.vision.VisionApplication;
import com.yp2012g4.vision.tools.ThrowableToString;

/**
 * This util class is used to answer, reject (and later call) calls and more
 * call related features.
 * 
 * 
 * @author Amit Yaffe
 * @version 1.1
 * 
 */
public class CallUtils {
  private static String TAG = "vison:CallUtils";
  // Used by the Receivers to transfer data to the activity.
  public static final String RANG_KEY = "rang";
  public static final String NUMBER_KEY = TelephonyManager.EXTRA_INCOMING_NUMBER;
  public static final String CALL_TYPE_KEY = "call_type_key";
  // Used for the URI for placing a call.
  public static final String CALL_TEL_STRING = "tel:";
  private static boolean speakerPhone = false;
  
  public static enum CALL_TYPE {
    INCOMING_CALL, OUTGOING_CALL, CALL_ENDED
  }
  
  private com.android.internal.telephony.ITelephony telephonyService;
  private AudioManager _am;
  
  /**
   * Enables the Speakerphone
   * 
   * @param context
   */
  public static void toggleSpeakerPhone(final Context c) {
    final AudioManager audioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
    speakerPhone = !speakerPhone;
    audioManager.setSpeakerphoneOn(speakerPhone);
  }
  
  public CallUtils(final Context c) {
    try {
      final TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
      final Method m = Class.forName(tm.getClass().getName()).getDeclaredMethod("getITelephony");
      m.setAccessible(true);
      telephonyService = (ITelephony) m.invoke(tm);
      _am = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
    } catch (final Exception e) {
      Log.e(TAG, "Error starting CallUtils", e);
    }
  }
  
  /**
   * Answer phone call. Simulate a press of the headset button to pick up the
   * call
   * 
   * @param context
   */
  public static void answerCall(final Context c) {
    final String recPerm = "android.permission.CALL_PRIVILEGED";
    final Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);
    buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
    c.sendOrderedBroadcast(buttonDown, recPerm);
    final Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
    buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
    c.sendOrderedBroadcast(buttonUp, recPerm);
  }
  
  /**
   * Terminate a phone call
   */
  public void endCall() {
    try {
      telephonyService.endCall();
      Thread.sleep(VisionApplication.DEFUALT_DELAY_TIME);
    } catch (final Exception e) {
      Log.e(TAG, "FATAL ERROR: could not connect to telephony subsystem");
      Log.e(TAG, ThrowableToString.toString(e));
    }
  }
  
  /**
   * Silence phone ringer.
   */
  public void silenceRinger() {
    try {
      _am.setStreamMute(AudioManager.STREAM_RING, true);
    } catch (final Exception e) {
      Log.e(TAG, "Error silencing Ringer", e);
    }
  }
  
  /**
   * Restore phone ringer.
   */
  public void restoreRinger() {
    try {
      _am.setStreamMute(AudioManager.STREAM_RING, false);
    } catch (final Exception e) {
      Log.e(TAG, "Error silencing Ringer", e);
    }
  }
  
  public static Message newMessage(final String number, final CALL_TYPE ct) {
    final Message m = new Message();
    final Bundle b2 = new Bundle();
    b2.putString(CallUtils.NUMBER_KEY, number);
    b2.putInt(CallUtils.CALL_TYPE_KEY, ct.ordinal());
    m.setData(b2);
    return m;
  }
}
