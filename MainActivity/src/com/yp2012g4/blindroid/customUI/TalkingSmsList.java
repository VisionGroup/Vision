package com.yp2012g4.blindroid.customUI;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Toast;

import com.yp2012g4.blindroid.R;
import com.yp2012g4.blindroid.SmsReader;
import com.yp2012g4.blindroid.SmsType;

public class TalkingSmsList extends Activity {
	TalkingListView viewList;
	ArrayList<SmsType> details;
	AdapterView.AdapterContextMenuInfo info;

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		viewList.onWindowFocusChanged(hasFocus);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_talking_sms_list);
		viewList = (TalkingListView) findViewById(R.id.TalkingSmsListView);

		SmsReader smsReader = new SmsReader(getApplicationContext());
		details = smsReader.getIncomingMessages();

		viewList.setAdapter(new SmsAdapter(details, this));
		viewList.setRun(new ViewListRun() {

			@Override
			public void onClick(int selectedItem) {
				Toast.makeText(getApplicationContext(),
						"Click ListItem Number " + selectedItem,
						Toast.LENGTH_LONG).show();
			}
		});
	}
}