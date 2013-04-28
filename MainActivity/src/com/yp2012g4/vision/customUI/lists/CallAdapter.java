package com.yp2012g4.vision.customUI.lists;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.managers.CallType;

public class CallAdapter extends BaseAdapter {
    private ArrayList<CallType> _data;
    Context _c;

    public CallAdapter(ArrayList<CallType> data, Context c) {
	_data = data;
	_c = c;
    }

    @Override
    public int getCount() {
	return _data.size();
    }

    @Override
    public Object getItem(int position) {
	return _data.get(position);
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View v = convertView;
	if (v == null) {
	    final LayoutInflater vi = (LayoutInflater) _c
		    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    v = vi.inflate(R.layout.call_view, null);
	}
	final TalkingButton numberView = (TalkingButton) v
		.findViewById(R.id.call_number);
	final TalkingButton nameView = (TalkingButton) v
		.findViewById(R.id.call_name);
	final TalkingButton timeView = (TalkingButton) v
		.findViewById(R.id.call_time);
	CallType call;
	if (position >= _data.size())
	    call = _data.get(0);
	else
	    call = _data.get(position);

	nameView.setText(call.getName());
	numberView.setText(call.getNumber());
	timeView.setText(call.getDate().toString());
	return v;
    }
}