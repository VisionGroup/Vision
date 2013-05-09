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
  private final ArrayList<CallType> _data;
  Context _c;
  
  public CallAdapter(ArrayList<CallType> d, Context c) {
    _data = d;
    _c = c;
  }
  
  @Override public int getCount() {
    return _data.size();
  }
  
  @Override public Object getItem(int p) {
    return _data.get(p);
  }
  
  @Override public long getItemId(int p) {
    return p;
  }
  
  @Override public View getView(int p, View v, ViewGroup pv) {
    View $ = v;
    if ($ == null) {
      final LayoutInflater vi = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      $ = vi.inflate(R.layout.call_view, null);
    }
    final TalkingButton numberView = (TalkingButton) $.findViewById(R.id.call_number);
    final TalkingButton nameView = (TalkingButton) $.findViewById(R.id.call_name);
    final TalkingButton timeView = (TalkingButton) $.findViewById(R.id.call_time);
    CallType call;
    if (p >= _data.size())
      call = _data.get(0);
    else
      call = _data.get(p);
    nameView.setText(call.getName());
    numberView.setText(call.getNumber());
    timeView.setText(call.getDate().toString());
    return $;
  }
  
  public void removeItemFromList(int p) {
    if (p < 0 || p > _data.size())
      return;
    _data.remove(p);
  }
}