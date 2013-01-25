package com.yp2012g4.vision.managers;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;

/**
 * SMS type container
 * 
 * @author Roman
 * 
 */
public class SmsType {
  private String address = "";
  private String person = "";
  private String date = "";
  private String protocol = "";
  private String read = "";
  private String status = "";
  private String type = "";
  private String subject = "";
  private String body = "";
  
  /**
   * constructor with Cursor
   * 
   * @param cur
   * @param context
   */
  public SmsType(Cursor cur, Context context) {
    address = cur.getString(cur.getColumnIndexOrThrow("address")).toString();
    String mili = cur.getString(cur.getColumnIndexOrThrow("date")).toString();
    Long m = Long.valueOf(mili);
    date = (String) DateFormat.format("dd/MM/yy", m.longValue());
    // protocol =
    // cur.getString(cur.getColumnIndexOrThrow("protocol")).toString(); can
    // cause exception
    read = cur.getString(cur.getColumnIndexOrThrow("read")).toString();
    status = cur.getString(cur.getColumnIndexOrThrow("status")).toString();
    type = cur.getString(cur.getColumnIndexOrThrow("type")).toString();
    // subject = c.getString(c.getColumnIndexOrThrow("subject")).toString();
    body = cur.getString(cur.getColumnIndexOrThrow("body")).toString();
    
    ContactManager cm = new ContactManager(context);
    person = cm.getNameFromPhone(address);
  }
  
  public synchronized String getAddress() {
    return address;
  }
  
  public synchronized String getPerson() {
    return person;
  }
  
  public synchronized String getDate() {
    return date;
  }
  
  public synchronized String getProtocol() {
    return protocol;
  }
  
  public synchronized String getRead() {
    return read;
  }
  
  public synchronized String getStatus() {
    return status;
  }
  
  public synchronized String getType() {
    return type;
  }
  
  public synchronized String getSubject() {
    return subject;
  }
  
  public synchronized String getBody() {
    return body;
  }
}