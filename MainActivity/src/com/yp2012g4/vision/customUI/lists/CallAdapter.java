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
  private static final int NUMBER_OF_BATCH = 5;
  private final ArrayList<CallType> _callArray;
  // Cursor _curser;
  Context _c;
  private final CallsManager _callsManager;
  
//  private static final String[] _projection = { CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.DATE };
  public CallAdapter(final Context c) {
    _c = c;
    _callsManager = new CallsManager(c);
    _callArray = _callsManager.getNextMissedCallsList(NUMBER_OF_BATCH);
    // initCurser();
  }
  
  private void initCurser() {
//    _curser = _c.getContentResolver().query(CallLog.Calls.CONTENT_URI, _projection, null, null, null);
//    _curser.moveToLast(); // Changed from move to first.
  }
  
  @Override public int getCount() {
    return _callArray.size();
  }
  
  @Override public Object getItem(final int p) {
    if (getCount() < p)
      return null;
    while (_callArray.size() <= p)
      _callArray.addAll(_callsManager.getNextMissedCallsList(NUMBER_OF_BATCH));
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
  
  /**
   * @param p
   * @param fromView
   * @param bodyView
   * @param timeView
   */
  private void setCallDisplay(final int p, final TalkingButton numberView, final TalkingButton nameView,
      final TalkingButton timeView) {
    final CallType call = (CallType) getItem(p);
    String person = call.getName();
    person = person == "" ? call.getNumber() : person;
    numberView.setText(call.getNumber());
    nameView.setText(call.getName());
    timeView.setText(call.getDate().toLocaleString());
    numberView.setReadText(call.getNumber());
    nameView.setReadText(call.getName());
    timeView.setReadText(call.getDate().toLocaleString());
  }
  
  /**
   * @param fromView
   * @param bodyView
   * @param timeView
   */
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
    initCurser();
  }
  
  public void getNextCall() {
    _callArray.addAll(_callsManager.getNextMissedCallsList(NUMBER_OF_BATCH));
//      _curser.moveToPrevious(); // CHANGED FROM MOVE TO NEXT
  }
}