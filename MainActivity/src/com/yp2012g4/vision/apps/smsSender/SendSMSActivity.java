package com.yp2012g4.vision.apps.smsSender;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Selection;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.tools.VisionActivity;

public class SendSMSActivity extends VisionActivity {
  private static final int MESSAGE = 0;
  private static final int PHONE = 1;
  
  @Override public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_send_sms);
    init(0, getString(R.string.SendSms_whereami), getString(R.string.SendSms_whereami));
  }
  
  @Override public int getViewId() {
    return R.id.SendSmsActivity;
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    final View _editText = getButtonByMode();
    EditText _et = (EditText) findViewById(R.id.phoneNumber);
    final CharSequence _s;
    switch (_editText.getId()) {
      case R.id.message:
        _et = (EditText) findViewById(R.id.message);
        _s = getText(_et, MESSAGE);
        moveCursor(_et, _s);
        break;
      case R.id.phoneNumber:
//        _et = (EditText) findViewById(R.id.phoneNumber);
        _s = getText(_et, PHONE);
        moveCursor(_et, _s);
        break;
      case R.id.sendMessageButton:
        final CharSequence number = _et.getText();
        if (number.length() > 0) {
          _et = (EditText) findViewById(R.id.message);
          SmsManager.getDefault().sendTextMessage(number.toString(), null, _et.getText().toString(), null, null);
          speakOutSync(getString(R.string.message_has_been_sent));
          finish();
        }
        break;
      default:
        break;
    }
    return false;
  }
  
  private CharSequence getText(final EditText editable, final int type) {
    CharSequence _res = editable.getText();
    switch (type) {
      case MESSAGE:
        _res = _res.length() == 0 ? getString(R.string.message_content) : _res;
        break;
      case PHONE:
        _res = _res.length() == 0 ? getString(R.string.phone_number) : _res;
        break;
      default:
        break;
    }
    return _res;
  }
  
  private void moveCursor(final EditText et, final CharSequence cs) {
    Selection.setSelection(et.getText(), et.getSelectionStart());
    et.requestFocus();
    speakOutAsync(cs.toString());
  }
}
