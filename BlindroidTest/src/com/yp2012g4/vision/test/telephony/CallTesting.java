/**
 * 
 */
package com.yp2012g4.vision.test.telephony;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.apps.main.MainActivity;
import com.yp2012g4.vision.apps.telephony.CallScreenView;
import com.yp2012g4.vision.apps.telephony.IncomingCallReceiver;
import com.yp2012g4.vision.apps.telephony.OutgoingCallReceiver;

/**
 * @author Amit Yaffe
 * 
 */
public class CallTesting extends ActivityInstrumentationTestCase2<MainActivity> {
  Resources res;
  private Solo solo;
  private Activity activity;
  
  // private PhoneNotifications pn;
  public CallTesting() {
    super("com.yp2012g4.vision.apps.main", MainActivity.class);
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see junit.framework.TestCase#setUp()
   */
  @Override protected void setUp() throws Exception {
    super.setUp();
    solo = new Solo(getInstrumentation(), getActivity());
    res = getInstrumentation().getContext().getResources();
    activity = getActivity();
  }
  
  @MediumTest public void testIncomingCall() {
    solo.assertCurrentActivity("Wrong activity", MainActivity.class);
    simInCall("046290622");
    assertTrue(solo.waitForView(CallScreenView.class, 1, 5000));
    simEndCall();
    solo.waitForActivity(MainActivity.class.getName(), 5000);
    solo.assertCurrentActivity("Wrong activity", MainActivity.class);
  }
  
  @MediumTest public void testOutgoingCall() {
    solo.assertCurrentActivity("Wrong activity", MainActivity.class);
    simOutCall("046290622");
    assertTrue(solo.waitForView(CallScreenView.class, 1, 5000));
    simEndCall();
    solo.waitForActivity(MainActivity.class.getName(), 5000);
    solo.assertCurrentActivity("Wrong activity", MainActivity.class);
  }
  
  private void simInCall(String number) {
    Intent i = new Intent();
    i.setAction("android.intent.action.PHONE_STATE");
    i.setClass(activity.getApplicationContext(), IncomingCallReceiver.class);
    i.putExtra(TelephonyManager.EXTRA_STATE, TelephonyManager.EXTRA_STATE_RINGING);
    i.putExtra(TelephonyManager.EXTRA_INCOMING_NUMBER, number);
    IncomingCallReceiver icr = new IncomingCallReceiver();
    icr.onReceive(activity, i);
  }
  
  private void simEndCall() {
    Intent i = new Intent();
    i.setAction("android.intent.action.PHONE_STATE");
    i.putExtra(TelephonyManager.EXTRA_STATE, TelephonyManager.EXTRA_STATE_IDLE);
    i.setClass(activity.getApplicationContext(), IncomingCallReceiver.class);
    IncomingCallReceiver icr = new IncomingCallReceiver();
    icr.onReceive(activity, i);
  }
  
  private void simOutCall(String number) {
    Intent i = new Intent("NEW_OUTGOING_CALL");
    i.putExtra(TelephonyManager.EXTRA_STATE, TelephonyManager.EXTRA_STATE_RINGING);
    i.putExtra("android.intent.extra.PHONE_NUMBER", number);
    i.setAction("android.intent.action.NEW_OUTGOING_CALL");
    i.setClass(activity.getApplicationContext(), OutgoingCallReceiver.class);
    // activity.sendBroadcast(i);
    OutgoingCallReceiver ocr = new OutgoingCallReceiver();
    ocr.onReceive(activity, i);
  }
}
