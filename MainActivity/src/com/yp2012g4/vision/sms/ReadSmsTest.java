package com.yp2012g4.vision.sms;

import java.util.ArrayList;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.R.layout;
import com.yp2012g4.vision.R.menu;
import com.yp2012g4.vision.customUI.lists.SmsAdapter;
import com.yp2012g4.vision.customUI.lists.TalkingListView;
import com.yp2012g4.vision.managers.SmsManager;
import com.yp2012g4.vision.managers.SmsType;
import com.yp2012g4.vision.tools.VisionActivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class ReadSmsTest extends VisionActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_read_sms_test);
		init(0, getString(R.string.where_am_i_screen), getString(R.string.where_am_i_help));

		TalkingListView listView = (TalkingListView) findViewById(R.id.TalkingSmsListView2);
		
		ArrayList<SmsType> data = SmsManager.getIncomingMessages(this);
		SmsAdapter adapter = new SmsAdapter(data, this);
		
		listView.setAdapter(adapter);		
	}

	  @Override
	  public int getViewId() {
	    return R.id.ReadSmsTest;
	  }

	  


}
