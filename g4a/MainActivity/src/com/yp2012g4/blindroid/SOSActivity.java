package com.yp2012g4.blindroid;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;

public class SOSActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sos);

		
		// Just a little comment to check that the git works; please remove it
//		Uri uri = Uri.parse("smsto:0524484993");
//		Intent sendsms = new Intent(Intent.ACTION_SENDTO, uri);
//		sendsms.putExtra("sms_body", "The SMS text");
//		startActivity(sendsms);

		//Sending messageToSent SMS to the number specified
		String messageToSend = "I need your help!";
		String number = "0524484993";
		SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null, null);
		
		
		RelativeLayout t = (RelativeLayout)findViewById(R.id.SOS_textview);
      
        
        t.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});	
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_sos, menu);
		return true;
	}

}
