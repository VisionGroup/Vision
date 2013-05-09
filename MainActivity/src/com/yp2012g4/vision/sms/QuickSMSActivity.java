package com.yp2012g4.vision.sms;

import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.MotionEvent;
import android.view.View;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.tools.VisionActivity;

/**
 * This class is an activity that enables to send a quick SMS message
 * 
 * @author Amir
 * @version 1.0
 * @author Amit Y.
 * @changes Added support for setting the sent number.
 * @version 1.1
 */
public class QuickSMSActivity extends VisionActivity {
  /**
   * This is the key to be used when putting the destination number in this
   * activities extras.
   */
  public static final String NUMBER_KEY = "number";
  public String number;
  
  @Override public int getViewId() {
    return R.id.QuickSMSActivity;
  }
  
  @Override public boolean onSingleTapUp(MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    final View view = getButtonByMode();
    if (view instanceof TalkingButton) {
      speakOutAsync(getString(R.string.sending) + ((TalkingButton) curr_view).getReadText());
      while (_tts.isSpeaking()) {
        // wait...
      }
      final String messageToSend = ((TalkingButton) view).getReadText();
      SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null, null);
      speakOutAsync(getString(R.string.message_has_been_sent));
      mHandler.postDelayed(mLaunchTask, 1500);
      finish();
    }
    return false;
  }
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quick_sms);
    init(0, getString(R.string.quick_sms_screen), getString(R.string.quick_sms_help));
    // TODO: create help string
    mHandler = new Handler();
    final Bundle extras = getIntent().getExtras();
    if (extras != null)
      try {
        number = extras.getString(QuickSMSActivity.NUMBER_KEY);
      } catch (final Exception e) {
        number = "";
      }
  }
}
