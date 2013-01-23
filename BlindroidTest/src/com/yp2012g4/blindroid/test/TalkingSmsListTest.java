/***
 * @author Amir Blumental
 * @version 1.0 
 */
package com.yp2012g4.blindroid.test;

import java.util.ArrayList;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.blindroid.TalkingSmsList;
import com.yp2012g4.blindroid.managers.SmsManager;
import com.yp2012g4.blindroid.managers.SmsType;

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

		SmsManager smsReader = new SmsManager(tsl.getApplicationContext());
		ArrayList<SmsType> originalSmsList = smsReader.getIncomingMessages();
		ArrayList<SmsType> fromActivityList = tsl.getDetails();

		assert (originalSmsList.size() == fromActivityList.size());

		if (originalSmsList.size() == 0) {
			return;
		}

		SmsType sms1;
		SmsType sms2;

		sms1 = originalSmsList.get(0);
		if (originalSmsList.size() == 1) {
			sms2 = sms1;
		} else {
			sms2 = originalSmsList.get(1);
		}

		SmsType lsms = tsl.getSelectedSms();

		assert (lsms.getBody() == sms1.getBody());

		flingDown();

		lsms = tsl.getSelectedSms();
		assert (sms2.getBody() == lsms.getBody());
		
		flingUp();
		
		lsms = tsl.getSelectedSms();
		assert (sms1.getBody() == lsms.getBody());
		

	}

	private void flingDown() {
		int screenHeight = getActivity().getWindowManager().getDefaultDisplay()
				.getHeight();
		int screenWidth = getActivity().getWindowManager().getDefaultDisplay()
				.getWidth();
		TouchUtils.drag(this, screenWidth / 2, screenWidth / 2,
				screenHeight / 2, screenHeight / 2 + 1, 0);
	}

	private void flingUp() {
		int screenHeight = getActivity().getWindowManager().getDefaultDisplay()
				.getHeight();
		int screenWidth = getActivity().getWindowManager().getDefaultDisplay()
				.getWidth();
		TouchUtils.drag(this, screenWidth / 2, screenWidth / 2,
				screenHeight / 2, screenHeight / 2 - 1, 0);
	}
}
