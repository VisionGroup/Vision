package com.yp2012g4.vision;

import android.os.Bundle;

import com.yp2012g4.vision.tools.VisionActivity;

public class SendSMSActivity extends VisionActivity {
  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_send_sms);
    init(0, getString(R.string.SendSms_whereami), getString(R.string.SendSms_whereami));
    /*
     * final EditText et; final Button b; et = (EditText)
     * findViewById(R.id.edittext); b = (Button) findViewById(R.id.button);
     * b.setOnClickListener(new OnClickListener() {
     * 
     * @Override public void onClick(View v) { String text =
     * et.getText().toString(); Toast msg = Toast.makeText(getBaseContext(),
     * text, Toast.LENGTH_LONG); msg.show(); } });
     */
  }
  
  @Override public int getViewId() {
    return R.id.SendSmsActivity;
  }
}
