package com.yp2012g4.vision.customUI.lists;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.managers.SmsType;

public class SmsAdapter extends BaseAdapter {
  private final ArrayList<SmsType> _data;
  Context _c;
  
  public SmsAdapter(ArrayList<SmsType> data, Context c) {
    _data = data;
    _c = c;
  }
  
  @Override public int getCount() {
    // TODO Auto-generated method stub
    return _data.size();
  }
  
  @Override public Object getItem(int position) {
    // TODO Auto-generated method stub
    return _data.get(position);
  }
  
  @Override public long getItemId(int position) {
    // TODO Auto-generated method stub
    return position;
  }
  
  @Override public View getView(int position, View convertView, ViewGroup parent) {
    View v = convertView;
    if (v == null) {
      final LayoutInflater vi = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      v = vi.inflate(R.layout.sms_view, null);
    }
    final TalkingButton fromView = (TalkingButton) v.findViewById(R.id.sms_from);
    final TalkingButton bodyView = (TalkingButton) v.findViewById(R.id.sms_body);
    final TalkingButton timeView = (TalkingButton) v.findViewById(R.id.sms_time);
    SmsType msg;
    if (position >= _data.size()) {
      fromView.setText("");
      bodyView.setText("");
      timeView.setText("");
      fromView.setReadText("");
      bodyView.setReadText("");
      timeView.setReadText("");
      return v;
    } else
      msg = _data.get(position);
    String person = msg.getPerson();
    if (person == "")
      person = msg.getAddress();
    fromView.setText(person);
    bodyView.setText(msg.getBody());
    timeView.setText(msg.getDate());
    fromView.setReadText(person);
    bodyView.setReadText(msg.getBody());
    timeView.setReadText(msg.getDate());
    return v;
  }
  
  public void removeItemFromList(int position) {
    if (position < 0 || position > _data.size())
      return;
    _data.remove(position);
  }
}