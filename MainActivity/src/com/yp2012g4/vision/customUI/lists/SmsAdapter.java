package com.yp2012g4.vision.customUI.lists;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.managers.SmsManager;
import com.yp2012g4.vision.managers.SmsType;

public class SmsAdapter extends BaseAdapter {
  public static final int NUMBER_OF_BATCH = 5;
  private final ArrayList<SmsType> _smsArray;
  Cursor _curser;
  Context _c;
  
  public SmsAdapter(final Context c) {
    _c = c;
    _smsArray = new ArrayList<SmsType>();
    initCurser();
  }
  
  private void initCurser() {
    _curser = _c.getContentResolver().query(Uri.parse(SmsManager.CONTENT_SMS_INBOX), null, null, null, null);
    _curser.moveToFirst();
  }
  
  @Override public int getCount() {
    return _curser.getCount();
  }
  
  @Override public Object getItem(final int p) {
    if (getCount() <= p)
      return null;
    while (_smsArray.size() <= p)
      getNextIncomingMessages();
    return _smsArray.get(p);
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
    if (p >= getCount()) {
      resetSMSDisplay(fromView, bodyView, timeView);
      return $;
    }
    setSMSDisplay(p, fromView, bodyView, timeView);
    return $;
  }
  
  /**
   * @param p
   * @param fromView
   * @param bodyView
   * @param timeView
   */
  private void setSMSDisplay(final int p, final TalkingButton fromView, final TalkingButton bodyView, final TalkingButton timeView) {
    final SmsType msg = (SmsType) getItem(p);
    String person = msg.getPerson();
    person = person == "" ? msg.getAddress() : person;
    fromView.setText(person);
    bodyView.setText(msg.getBody());
    timeView.setText(msg.getDate());
    fromView.setReadText(person);
    bodyView.setReadText(msg.getBody());
    timeView.setReadText(msg.getDate());
  }
  
  /**
   * @param fromView
   * @param bodyView
   * @param timeView
   */
  private static void resetSMSDisplay(final TalkingButton fromView, final TalkingButton bodyView, final TalkingButton timeView) {
    fromView.setText("");
    bodyView.setText("");
    timeView.setText("");
    fromView.setReadText("");
    bodyView.setReadText("");
    timeView.setReadText("");
  }
  
  public void removeItemFromList(final int p) {
    if (p < 0 || p > _smsArray.size())
      return;
    _smsArray.clear();
    initCurser();
  }
  
  public void getNextIncomingMessages() {
    for (int i = 0; i < NUMBER_OF_BATCH && !_curser.isAfterLast(); i++) {
      try {
        _smsArray.add(new SmsType(_curser, _c));
      } catch (final IllegalArgumentException e) {
        // Ignore and go next
      }
      _curser.moveToNext();
    }
  }
}