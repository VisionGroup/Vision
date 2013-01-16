package com.yp2012g4.blindroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;

import com.yp2012g4.blindroid.customUI.TalkingButton;
import com.yp2012g4.blindroid.customUI.TalkingImageButton;
import com.yp2012g4.blindroid.tools.BlindroidActivity;

/**
 * This class is an activity that enables to send a quick SMS message
 * 
 * @author Amir
 * @version 1.0
 */
public class QuickSMSActivity extends BlindroidActivity {
  @Override public int getViewId() {
    return R.id.QuickSMSActivity;
  }
  
  @Override public void onClick(View v) {
    final View view = v;
    if (v instanceof TalkingButton) {
      final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
      alertDialog.setTitle("Choose contact");
      alertDialog.setCancelable(false);
      alertDialog.setMessage("will send the SMS to the chosen contact");
      alertDialog.setButton("Send..", new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
          String messageToSend = ((TalkingButton) view).getReadText();
          String number = "0544457141";
          SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null, null);
          speakOut("Message has been sent");
        }
      });
      alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
          speakOut("Cancel");
          alertDialog.dismiss();
        }
      });
      speakOut("Sending" + ((TalkingButton) v).getReadText());
      while (_t.isSpeaking()) {
        // wait...
      }
      alertDialog.show();
    }
    if (v instanceof TalkingImageButton)
      super.onClick(v);
  }
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quick_sms);
    mHandler = new Handler();
  }
  
  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.activity_quick_sms, menu);
    return true;
  }
}
