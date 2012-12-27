package com.yp2012g4.blindroid.customUI.lists;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yp2012g4.blindroid.ContactType;
import com.yp2012g4.blindroid.R;
import com.yp2012g4.blindroid.SmsType;

public class ContactsAdapter extends BaseAdapter {

  
  //change smstype to contactType or whatever...
  private ArrayList<ContactType> _data;
  Context _c;

  public ContactsAdapter(ArrayList<ContactType> data, Context c) {
    _data = data;
    _c = c;
  }

  public int getCount() {
    // TODO Auto-generated method stub
    return _data.size();
  }

  public Object getItem(int position) {
    // TODO Auto-generated method stub
    return _data.get(position);
  }

  public long getItemId(int position) {
    // TODO Auto-generated method stub
    return position;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    View v = convertView;
    if (v == null) {
      LayoutInflater vi = (LayoutInflater) _c
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      v = vi.inflate(R.layout.sms_view, null);
    }

    TextView fromView = (TextView) v.findViewById(R.id.contact_name);
    TextView bodyView = (TextView) v.findViewById(R.id.contact_phone);
    TextView timeView = (TextView) v.findViewById(R.id.contact_email);

    ContactType msg = _data.get(position);
    String person = msg.getPerson();
    if (person == ""){
      person = msg.getAddress();
    }
        
    //you need to change this.
    fromView.setText(person);
    bodyView.setText(msg.getBody());
    timeView.setText(msg.getDate());

    return v;
  }
}