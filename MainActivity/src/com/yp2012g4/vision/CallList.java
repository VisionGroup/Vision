package com.yp2012g4.vision;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.MotionEvent;

import com.yp2012g4.vision.customUI.lists.CallAdapter;
import com.yp2012g4.vision.customUI.lists.TalkingListView;
import com.yp2012g4.vision.managers.CallManager;
import com.yp2012g4.vision.managers.CallType;
import com.yp2012g4.vision.tools.VisionActivity;

public class CallList extends VisionActivity {

    TalkingListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_call_list);
	init(0, getString(R.string.call_list_screen),
		getString(R.string.call_list_screen));// TODO:: Add strings

	listView = (TalkingListView) findViewById(R.id.TalkingCallListView);

	final ArrayList<CallType> data = CallManager.getMissedCallsList(this);
	final CallAdapter adapter = new CallAdapter(data, this);

	listView.setAdapter(adapter);
    }

    @Override
    public int getViewId() {
	return R.id.ReadSmsTest;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	    float velocityY) {

	listView.nextPage();

	return super.onFling(e1, e2, velocityX, velocityY);
    }

}
