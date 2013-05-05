package com.yp2012g4.vision.telephony;

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
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.managers.CallManager;
import com.yp2012g4.vision.managers.ContactManager;
import com.yp2012g4.vision.tools.CallUtils;
import com.yp2012g4.vision.tools.VisionActivity;

public class IncomingCallActivity extends VisionActivity {
  private static final String TAG = "vision:IncomingCallActivity";
  CallUtils callUtils;
  private ListenToPhoneState listener;
  private final ContactManager contact = new ContactManager(this);
  Boolean rang = Boolean.valueOf(false);
  TelephonyManager tManager;
  
  /**
   * Adds onClick events to buttons in this view.
   * 
   * @see android.view.View.OnClickListener#onClick(android.view.View)
   */
  @Override public void onClick(View v) {
    callUtils.silenceRinger();
    super.onClick(v);
    Log.d(TAG, v.toString());
    switch (v.getId()) {
      case R.id.button_answer:
        answerCall();
        break;
      case R.id.button_reject:
        endCall();
        break;
      case R.id.back_button:
        speakOut(getString(R.string.previous_screen));
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      default:
        break;
    }
  }
  
  @Override public void onAttachedToWindow() {
    // make the activity show even the screen is locked.
    final Window window = getWindow();
    window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON + WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED +
    // WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON +
        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
  }
  
  @Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    callUtils.silenceRinger();
    return super.onScroll(e1, e2, distanceX, distanceY);
  }
  
  @Override public boolean onSingleTapUp(MotionEvent e) {
    super.onSingleTapUp(e);
    callUtils.silenceRinger();
    if (clickFlag) {
      clickFlag = false;
      return false;
    }
    switch (curr_view.getId()) {
      case R.id.button_answer:
        answerCall();
        break;
      case R.id.button_reject:
        endCall();
        break;
      case R.id.back_button:
        speakOut(getString(R.string.previous_screen));
        mHandler.postDelayed(mLaunchTask, 100);
        break;
      default:
        return false;
    }
    return true;
  }
  
  /**
   * Answers the present phone call.
   */
  private void answerCall() {
    AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    manager.setWiredHeadsetOn(false);
    manager.setSpeakerphoneOn(true);
    manager.setRouting(AudioManager.MODE_CURRENT, AudioManager.ROUTE_SPEAKER, AudioManager.ROUTE_ALL);
    manager.setMode(AudioManager.MODE_CURRENT);
    try {
      CallUtils.answerPhoneHeadsethook(this);
      Log.d(TAG, "Answered call");
    } catch (final Exception e) {
      Log.e(TAG, "Error answering call", e);
    }
  }
  
  /**
   * Terminate the present phone call.
   */
  private void endCall() {
    callUtils.endCall();
    Log.d(TAG, "Rejected call");
  }
  
  /**
   * Updates the phone number on the number label.
   * 
   * @param number
   */
  static void updateNumberButton(TalkingButton tB, String s) {
    tB.setText(s.toCharArray(), 0, s.length());
    tB.setReadText(s);
    tB.setContentDescription(s);
  }
  
  @SuppressWarnings("null") @Override protected void onResume() {
    Log.d(TAG, "onResume Incoming call activity");
    super.onResume();
    setContentView(R.layout.activity_incoming_call);
    setPhoneStateListener();
    String incomingNumber = "", incomingName = "";
    final Bundle extras = getIntent().getExtras();
    if (extras == null)
      Log.d(TAG, "extras == null");
    try {
      rang = Boolean.valueOf(extras.getBoolean(CallUtils.RANG_KEY));
    } catch (final Exception e) {
      rang = Boolean.FALSE;
    }
    try {
      incomingNumber = extras.getString(CallUtils.INCOING_NUMBER_KEY);
      if (incomingNumber == null)
        incomingNumber = new CallManager().getLastOutgoingCall(this).getNumber();
    } catch (Exception e) {
      // catching all the rest
    }
    updateNumberButton((TalkingButton) findViewById(R.id.number), incomingNumber);
    incomingName = contact.getNameFromPhone(incomingNumber);
    if (!incomingName.equals(incomingNumber))
      updateNumberButton((TalkingButton) findViewById(R.id.name), incomingName);
  }
  
  /**
   * onCreate method.
   */
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    callUtils = new CallUtils(this);
    Log.d(TAG, "onCreate Incoming call activity");
    init(0/* TODO Check what icon goes here */, getString(R.string.IncomingCall_whereami), getString(R.string.IncomingCall_help));
  }
  
  @Override public void onDestroy() {
    super.onDestroy();
    callUtils.restoreRinger();
  }
  
  @Override public int getViewId() {
    return R.id.incomingCallActivity;
  }
  
  /**
   * Sets the listener to the change in the phone's telephony status.
   */
  private void setPhoneStateListener() {
    tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    listener = new ListenToPhoneState();
    tManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
  }
  
  class ListenToPhoneState extends PhoneStateListener {
    public ListenToPhoneState() {
      // TODO Auto-generated constructor stub
    }
    
    @Override public void onCallStateChanged(int state, String inNumber) {
      if (state == TelephonyManager.CALL_STATE_IDLE && rang == Boolean.TRUE) {
        Log.d(TAG, "Ending Activity because " + stateName(state));
        rang = Boolean.FALSE;
        mHandler.postDelayed(mLaunchTask, 100);
      }
    }
    
    String stateName(int state) {
      switch (state) {
        case TelephonyManager.CALL_STATE_IDLE:
          return "Idle";
        case TelephonyManager.CALL_STATE_OFFHOOK:
          return "Off hook";
        case TelephonyManager.CALL_STATE_RINGING:
          return "Ringing";
        default:
          break;
      }
      return Integer.toString(state);
    }
  }
  
  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
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
