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
  
  public SmsAdapter(final ArrayList<SmsType> d, final Context c) {
    _data = d;
    _c = c;
  }
  
  @Override public int getCount() {
    return _data.size();
  }
  
  @Override public Object getItem(final int p) {
    return _data.get(p);
  }
  
  @Override public long getItemId(final int p) {
    return p;
  }
  
  @Override public View getView(final int p, final View v, final ViewGroup pv) {
    View $ = v;
    if ($ == null) {
      final LayoutInflater vi = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      $ = vi.inflate(R.layout.sms_view, null);
    }
    final TalkingButton fromView = (TalkingButton) $.findViewById(R.id.sms_from);
    final TalkingButton bodyView = (TalkingButton) $.findViewById(R.id.sms_body);
    final TalkingButton timeView = (TalkingButton) $.findViewById(R.id.sms_time);
    SmsType msg;
    if (p >= _data.size()) {
      fromView.setText("");
      bodyView.setText("");
      timeView.setText("");
      fromView.setReadText("");
      bodyView.setReadText("");
      timeView.setReadText("");
      return $;
    }
    msg = _data.get(p);
    String person = msg.getPerson();
    if (person == "")
      person = msg.getAddress();
    fromView.setText(person);
    bodyView.setText(msg.getBody());
    timeView.setText(msg.getDate());
    fromView.setReadText(person);
    bodyView.setReadText(msg.getBody());
    timeView.setReadText(msg.getDate());
    return $;
  }
  
  public void removeItemFromList(final int p) {
    if (p < 0 || p > _data.size())
      return;
    _data.remove(p);
  }
}