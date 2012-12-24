package com.yp2012g4.blindroid.customUI;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yp2012g4.blindroid.R;
import com.yp2012g4.blindroid.SmsType;

public class SmsAdapter extends BaseAdapter {

	private ArrayList<SmsType> _data;
	Context _c;

	SmsAdapter(ArrayList<SmsType> data, Context c) {
		_data = data;
		_c = c;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return _data.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return _data.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) _c
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.sms_view, null);
		}

		TextView fromView = (TextView) v.findViewById(R.id.sms_from);
		TextView bodyView = (TextView) v.findViewById(R.id.sms_body);
		TextView timeView = (TextView) v.findViewById(R.id.sms_time);

		SmsType msg = _data.get(position);
		String person = msg.getPerson();
		if (person == ""){
			person = msg.getAddress();
		}
				
		fromView.setText(person);
		bodyView.setText(msg.getBody());
		timeView.setText(msg.getDate());

		return v;
	}
}