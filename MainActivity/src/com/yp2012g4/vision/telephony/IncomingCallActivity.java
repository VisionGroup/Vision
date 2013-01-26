package com.yp2012g4.vision.telephony;

import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.tools.CallUtils;
import com.yp2012g4.vision.tools.VisionActivity;

public class IncomingCallActivity extends VisionActivity {
  private static final String TAG = "vision:IncomingCallActivity";
  CallUtils callUtils;
  private ListenToPhoneState listener;
  Boolean rang = Boolean.valueOf(false);
  String incomingNumber;
  TelephonyManager tManager;
  
  /**
   * Adds onClick events to buttons in this view.
   * 
   * @see android.view.View.OnClickListener#onClick(android.view.View)
   */
  @Override public void onClick(View v) {
    // callUtils.silenceRinger();
    super.onClick(v);
    switch (v.getId()) {
      case R.id.button_answer:
        answerCall();
        break;
      case R.id.button_reject:
        endCall();
        break;
      case R.id.back_button:
        speakOut("Previous screen");
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      default:
        break;
    }
  }
  
  private void answerCall() {
    try {
      // callUtils.answerCall(this);
      CallUtils.answerPhoneHeadsethook(this);
      Log.d(TAG, "Answered call");
    } catch (final Exception e) {
      Log.e(TAG, "Error answering call", e);
    }
  }
  
  void endCall() {
    callUtils.endCall();
    Log.d(TAG, "Rejected call");
  }
  
  void updateNumberButton(String number) {
    ((TalkingButton) findViewById(R.id.number)).setText(number.toCharArray(), 0, number.length());
    ((TalkingButton) findViewById(R.id.number)).setContentDescription(number);
  }
  
  /**
   * onCreate method.
   */
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    callUtils = new CallUtils(this);
    Log.d(TAG, "onCreate Incoming call activity");
    init(0/* TODO Check what icon goes here */, getString(R.string.IncomingCall_whereami), getString(R.string.IncomingCall_help));
    setContentView(R.layout.activity_incoming_call);
    setPhoneStateListener();
    final Bundle extras = getIntent().getExtras();
    if (extras != null) {
      try {
        rang = Boolean.valueOf(extras.getBoolean(CallUtils.RANG_KEY));
      } catch (final Exception e) {
        rang = Boolean.FALSE;
      }
      try {
        incomingNumber = extras.getString(CallUtils.INCOING_NUMBER_KEY);
      } catch (final Exception e) {
        incomingNumber = new String("");
      }
    }
    updateNumberButton(incomingNumber);
  }
  
  @Override public void onDestroy() {
    super.onDestroy();
    callUtils.restoreRinger();
  }
  
  @Override public int getViewId() {
    return R.id.incomingCallActivity;
  }
  
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
        mHandler.postDelayed(mLaunchTask, 1000);
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
}
