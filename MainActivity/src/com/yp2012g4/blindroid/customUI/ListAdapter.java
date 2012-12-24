package com.yp2012g4.blindroid.customUI;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yp2012g4.blindroid.R;

public class ListAdapter extends BaseAdapter {

	private ArrayList<ListRow> _data;
	Context _c;

	ListAdapter(ArrayList<ListRow> data, Context c) {
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
			v = vi.inflate(R.layout.list_row, null);
		}

		TextView fromView = (TextView) v.findViewById(R.id.from);
		TextView subView = (TextView) v.findViewById(R.id.subject);
		TextView descView = (TextView) v.findViewById(R.id.description);
		TextView timeView = (TextView) v.findViewById(R.id.time);

		ListRow msg = _data.get(position);
		fromView.setText(msg.from);
		subView.setText("Subject: " + msg.sub);
		descView.setText(msg.desc);
		timeView.setText(msg.time);

		return v;
	}
}