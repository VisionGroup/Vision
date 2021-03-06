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
import com.yp2012g4.vision.managers.CallsManager;

public class CallAdapter extends BaseAdapter {
  private final ArrayList<CallType> _callArray;
  Context _c;
  int _count;
  private final CallsManager _callsManager;
  
  public CallAdapter(final Context c) {
    _c = c;
    _callsManager = new CallsManager(c);
    _callArray = new ArrayList<CallType>();
    _count = _callsManager.getMissedCallsNum();
    _callArray.add(_callsManager.getNextMissedCalls());
  }
  
  @Override public int getCount() {
    return _count;
  }
  
  @Override public Object getItem(final int p) {
    if (getCount() < p)
      return null;
    while (_callArray.size() <= p)
      _callArray.add(_callsManager.getNextMissedCalls());
//      getNextCall();
    return _callArray.get(p);
  }
  
  @Override public long getItemId(final int p) {
    return p;
  }
  
  @Override public View getView(final int p, final View v, final ViewGroup pv) {
    View $ = v;
    if ($ == null) {
      final LayoutInflater vi = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      $ = vi.inflate(R.layout.call_view, null);
    }
    final TalkingButton numberView = (TalkingButton) $.findViewById(R.id.call_number);
    final TalkingButton nameView = (TalkingButton) $.findViewById(R.id.call_name);
    final TalkingButton timeView = (TalkingButton) $.findViewById(R.id.call_time);
    if (p >= getCount()) {
      resetCallDisplay(numberView, nameView, timeView);
      return $;
    }
    setCallDisplay(p, numberView, nameView, timeView);
    return $;
  }
  
  private void setCallDisplay(final int p, final TalkingButton numberView, final TalkingButton nameView,
      final TalkingButton timeView) {
    final CallType call = (CallType) getItem(p);
    String person = call.name;
    person = person == "" ? call.number : person;
    numberView.setText(call.number);
    nameView.setText(call.name);
    timeView.setText(call.date.toLocaleString());
    numberView.setReadText(call.number);
    nameView.setReadText(call.name);
    timeView.setReadText(call.date.toLocaleString());
  }
  
  private static void resetCallDisplay(final TalkingButton numberView, final TalkingButton nameView, final TalkingButton timeView) {
    numberView.setText("");
    nameView.setText("");
    timeView.setText("");
    numberView.setReadText("");
    nameView.setReadText("");
    timeView.setReadText("");
  }
  
  public void removeItemFromList(final int p) {
    if (p < 0 || p > _callArray.size())
      return;
    _callArray.clear();
  }
  
  public void getNextCall() {
    _callsManager.UnmarkCallLFromMissedCallList(_callArray.get(_callArray.size() - 1));
    _callArray.add(_callsManager.getNextMissedCalls());
  }
}