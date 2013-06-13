package com.yp2012g4.vision.apps.smsSender;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Selection;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.tools.CallUtils;
import com.yp2012g4.vision.tools.VisionActivity;

/**
 * A class for an sending SMS
 * 
 * @author Amir Mizrachi
 * @version 2.0
 */
public class SendSMSActivity extends VisionActivity {
  private static final int MESSAGE = 0;
  private static final int PHONE = 1;
  private String number;
  private static String _spokenMessage;
  
  @Override public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_send_sms);
    init(0, getString(R.string.SendSms_whereami), getString(R.string.SendSms_info));
    final Bundle extras = getIntent().getExtras();
    try {
      number = extras.getString(CallUtils.NUMBER_KEY);
    } catch (final Exception e) {
      number = "";
    }
    updatePhoneNumber();
  }
  
  /**
   * Updates number text view as an extra input from ContactsActivity and moving
   * cursor to message text view
   */
  private void updatePhoneNumber() {
    EditText _et = getEditText(R.id.phoneNumber);
    _et.setText(number);
    if (!number.equals("")) {
      _et = getEditText(R.id.message);
      Selection.setSelection(_et.getText(), _et.getSelectionStart());
      _et.requestFocus();
    }
  }
  
  @Override public int getViewId() {
    return R.id.SendSmsActivity;
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    final View _v = getButtonByMode();
    EditText _et = getEditText(R.id.phoneNumber);
    final CharSequence _cs;
    switch (_v.getId()) {
      case R.id.message:
        _et = getEditText(R.id.message);
        _cs = getText(_et, MESSAGE);
        moveCursor(_et, _cs);
        break;
      case R.id.phoneNumber:
        _cs = getText(_et, PHONE);
        moveCursor(_et, _cs);
        break;
      case R.id.sendMessageButton:
        final CharSequence num = _et.getText();
        if (num.length() > 0) {
          _et = getEditText(R.id.message);
          try {
            SmsManager.getDefault().sendTextMessage(num.toString(), null, _et.getText().toString(), null, null);
          } catch (final Exception exception) {
            speakOutSync(R.string.message_was_not_sent);
            break;
          }
          speakOutSync(R.string.message_has_been_sent);
          finish();
        }
        break;
      default:
        break;
    }
    return false;
  }
  
  /**
   * Retrieving the text shown in a given EditText view
   * 
   * @param editable
   *          The EditText view
   * @param type
   *          The type of view (can be either the phone number or the message
   *          content view)
   * @return the text shown in the given EditText view
   */
  private CharSequence getText(final EditText editable, final int type) {
    CharSequence _res = editable.getText();
    switch (type) {
      case MESSAGE:
        _res = _res.length() == 0 ? getString(R.string.enter_a_message) : _res;
        break;
      case PHONE:
        _res = _res.length() == 0 ? getString(R.string.enter_a_phone_number) : _res;
        break;
      default:
        break;
    }
    return _res;
  }
  
  /**
   * Moves cursor between EditText views
   * 
   * @param et
   *          The EditText view to which the cursor will be moved
   * @param cs
   *          The string to be spoken when moving to the view
   */
  static private void moveCursor(final EditText et, final CharSequence cs) {
    _spokenMessage = cs.toString();
    Selection.setSelection(et.getText(), et.getSelectionEnd());
    et.requestFocus();
    // if a phone number contains "-" then create (once) a new string to speak
    if (et.getId() == R.id.phoneNumber && et.getText().toString().contains("-"))
      _spokenMessage = ignoreSeparator(cs);
    speakOutAsync(_spokenMessage);
  }
  
  private static String ignoreSeparator(final CharSequence cs) {
    final String[] _s = cs.toString().split("-");
    String _newString = "";
    for (final String s : _s)
      _newString += s;
    return _newString;
  }
}
