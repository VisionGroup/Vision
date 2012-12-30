/***
 * @author Amir Blumental
 * @version 1.0 
 */
package com.yp2012g4.blindroid.test;

import java.util.ArrayList;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.blindroid.SmsReader;
import com.yp2012g4.blindroid.SmsType;
import com.yp2012g4.blindroid.TalkingSmsList;

public class TalkingSmsListTest extends
		ActivityInstrumentationTestCase2<TalkingSmsList> {
	private Solo solo;
	private Activity activity;

	public TalkingSmsListTest() {
		super("com.yp2012g4.blindroid", TalkingSmsList.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		solo = new Solo(getInstrumentation(), activity);
	}

	public void test() {
		solo.assertCurrentActivity("wrong activity", TalkingSmsList.class);
		TalkingSmsList tsl = (TalkingSmsList) activity;
		SmsReader smsReader = new SmsReader(tsl.getApplicationContext());
	    ArrayList<SmsType> details = smsReader.getIncomingMessages();
		SmsType sms = tsl.getSelectedSms();
		
		assert(sms.getBody() == details.get(0).getBody());
		
		solo.clickOnText(sms.getBody());
		SmsType sms2 = tsl.getSelectedSms();
		
		assert(sms2.getBody() == details.get(1).getBody());
		

	}


}
