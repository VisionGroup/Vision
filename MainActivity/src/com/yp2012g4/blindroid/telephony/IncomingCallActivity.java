package com.yp2012g4.blindroid.telephony;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.yp2012g4.blindroid.DisplaySettingsActivity;
import com.yp2012g4.blindroid.R;
import com.yp2012g4.blindroid.tools.BlindroidActivity;
import com.yp2012g4.blindroid.tools.CallUtils;

public class IncomingCallActivity extends BlindroidActivity {
  private static final String TAG = "bd:IncomingCallActivity";
  CallUtils callUtils = new CallUtils();
  
  /**
   * Adds onClick events to buttons in this view.
   * 
   * @see android.view.View.OnClickListener#onClick(android.view.View)
   */
  @Override public void onClick(View v) {
    final Resources res = getResources();
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
      case R.id.settings_button:
        speakOut("Settings");
        final Intent intent = new Intent(this, DisplaySettingsActivity.class);
        startActivity(intent);
        break;
      case R.id.home_button:
        speakOut("Home");
        mHandler.postDelayed(mLaunchTask, 1000);
        break;
      case R.id.current_menu_button:
        speakOut("This is " + getString(R.string.phoneStatus_whereami));
        break;
      default:
        break;
    }
  }
  
  private void answerCall() {
    try {
      callUtils.answerCall(this);
      Log.d(TAG, "Answered call");
    } catch (final Exception e) {
      // TODO Auto-generated catch block
      Log.e(TAG, "Error answering call", e);
    }
  }
  
  void endCall() {
    callUtils.endCall(this);
    Log.d(TAG, "Rejected call");
  }
  
  /**
   * onCreate method.
   */
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    init(this, 0/* TODO Check what icon goes here */, getString(R.string.IncomingCall_whereami),
        getString(R.string.IncomingCall_help));
    setContentView(R.layout.activity_incoming_call);
  }
  
  @Override public int getViewId() {
    // TODO Auto-generated method stub
    return 0;
  }
}
