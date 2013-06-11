package com.yp2012g4.vision.apps.telephony;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.apps.settings.VisionApplication;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.managers.ContactManager;
import com.yp2012g4.vision.tools.CallUtils;
import com.yp2012g4.vision.tools.VisionActivity;

/**
 * 
 * 
 * @author Yaron Auster
 * @version 2
 * 
 */
public class IncomingCallActivity extends VisionActivity {
  private static final String TAG = "vision:IncomingCallActivity";
  private CallUtils _cu;
  private final ContactManager _cm = new ContactManager(this);
  boolean _rang = false;
  private TelephonyManager _tm;
  
  /**
   * Adds onClick events to buttons in this view.
   * 
   * @see android.view.View.OnClickListener#onClick(android.view.View)
   */
  @Override public void onClick(final View v) {
    _cu.silenceRinger();
    super.onClick(v);
    Log.d(TAG, v.toString());
    _clickAction(v);
  }
  
  private void _clickAction(final View v) {
    switch (v.getId()) {
      case R.id.button_answer:
        answerCall();
        break;
      case R.id.button_reject:
        endCall();
        break;
      case R.id.back_button:
        speakOutAsync(R.string.previous_screen);
        _mHandler.postDelayed(mLaunchTask, VisionApplication.DEFUALT_DELAY_TIME);
        break;
      default:
        break;
    }
  }
  
  @Override public void onAttachedToWindow() {
    // make the activity show even when the screen is locked.
    final Window window = getWindow();
    window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON + WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
  }
  
  @Override public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX, final float distanceY) {
    _cu.silenceRinger();
    return super.onScroll(e1, e2, distanceX, distanceY);
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent me) {
    _cu.silenceRinger();
    if (super.onSingleTapUp(me))
      return true;
    _clickAction(curr_view);
    return true;
  }
  
  /**
   * Answers the present phone call.
   */
  private void answerCall() {
    final AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    if (am != null) {
      am.setWiredHeadsetOn(false);
      am.setSpeakerphoneOn(true);
      am.setRouting(AudioManager.MODE_CURRENT, AudioManager.ROUTE_SPEAKER, AudioManager.ROUTE_ALL);
      am.setMode(AudioManager.MODE_CURRENT);
    } else
      Log.e(TAG, "Unable to retrieve AUDIO_SERVICE");
    try {
      CallUtils.answerCall(this);
      Log.d(TAG, "Answered call");
    } catch (final Exception e) {
      Log.e(TAG, "Error answering call", e);
    }
  }
  
  /**
   * Terminate the present phone call.
   */
  private void endCall() {
    _cu.endCall();
    Log.d(TAG, "Rejected call");
    speakOutSync(R.string.IncomingCall_call_ended);
  }
  
  /**
   * Updates the phone number on the number label.
   * 
   * @param number
   */
  static void updateNumberButton(final TalkingButton tB, final String s) {
    tB.setText(s.toCharArray(), 0, s.length());
    tB.setReadText(s);
    tB.setContentDescription(s);
  }
  
  @Override protected void onResume() {
    Log.d(TAG, "onResume Incoming call activity");
    super.onResume();
    setContentView(R.layout.activity_incoming_call);
    setPhoneStateListener();
    String incomingNumber = "", incomingName = "";
    final Bundle extras = getIntent().getExtras();
    if (extras == null) {
      Log.d(TAG, "extras == null");
      return;
    }
    try {
      _rang = extras.getBoolean(CallUtils.RANG_KEY);
    } catch (final Exception e) {
      _rang = false;
    }
    try {
      incomingNumber = extras.getString(CallUtils.NUMBER_KEY);
      if (incomingNumber == null)
        return;
    } catch (final Exception e) {
      return;
    }
    updateNumberButton(getTalkingButton(R.id.number), incomingNumber);
    incomingName = _cm.getNameFromPhone(incomingNumber);
    if (!incomingName.equals(incomingNumber))
      updateNumberButton(getTalkingButton(R.id.name), incomingName);
  }
  
  /**
   * onCreate method.
   */
  @Override protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    _cu = new CallUtils(this);
    Log.d(TAG, "onCreate Incoming call activity");
    init(0, getString(R.string.IncomingCall_whereami), getString(R.string.IncomingCall_help));
  }
  
  @Override public void onDestroy() {
    _cu.restoreRinger();
    super.onDestroy();
  }
  
  @Override public int getViewId() {
    return R.id.incomingCallActivity;
  }
  
  /**
   * Sets the listener to the change in the phone's telephony status.
   */
  private void setPhoneStateListener() {
    _tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    _tm.listen(new ListenToPhoneState(), PhoneStateListener.LISTEN_CALL_STATE);
  }
  
  class ListenToPhoneState extends PhoneStateListener {
    @Override public void onCallStateChanged(final int state, final String inNumber) {
      if (state == TelephonyManager.CALL_STATE_IDLE && _rang) {
        Log.d(TAG, "Ending Activity because " + _stateName(state));
        _rang = false;
        _mHandler.postDelayed(mLaunchTask, 100);
      }
    }
    
    private String _stateName(final int state) {
      switch (state) {
        case TelephonyManager.CALL_STATE_IDLE:
          return "Idle";
        case TelephonyManager.CALL_STATE_OFFHOOK:
          return "Off hook";
        case TelephonyManager.CALL_STATE_RINGING:
          return "Ringing";
        default:
          return Integer.toString(state);
      }
    }
  }
  
  @Override public boolean onKeyDown(final int keyCode, final KeyEvent event) {
    Log.d(TAG, "onKeyDown " + keyCode);
    switch (keyCode) {
      case KeyEvent.KEYCODE_BACK:
        endCall();
        break;
      case KeyEvent.KEYCODE_MENU:
        answerCall();
        break;
      default:
        return false;
    }
    return true;
  }
}
