package com.yp2012g4.blindroid.customUI;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Toast;

import com.yp2012g4.blindroid.R;

public class TalkingList extends Activity {
	TalkingListView viewList;
	ArrayList<ListRow> details;
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
		setContentView(R.layout.talking_list_view);
		viewList = (TalkingListView) findViewById(R.id.TalkingListView);

		details = new ArrayList<ListRow>();
		for (int i = 0; i < 10; i++) {
			ListRow Detail;
			Detail = new ListRow();
			Detail.setName("test " + i);
			Detail.setSub("blabla " + i);
			Detail.setDesc("bla " + i);
			Detail.setTime("12/12/2012 12:12");
			details.add(Detail);
		}
		viewList.setAdapter(new ListAdapter(details, this));
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