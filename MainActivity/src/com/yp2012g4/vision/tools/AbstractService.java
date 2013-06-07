/*
 * This example demonstrates a good way to communicate between Activity and
 * Service.
 * 
 * 1. Implement a service by inheriting from AbstractService 2. Add a
 * ServiceManager to your activity - Control the service with
 * ServiceManager.start() and .stop() - Send messages to the service via
 * ServiceManager.send() - Receive messages with by passing a Handler in the
 * constructor 3. Send and receive messages on the service-side using send() and
 * onReceiveMessage()
 * 
 * Author: Philipp C. Heckel; based on code by Lance Lefebure from
 * http://stackoverflow
 * .com/questions/4300291/example-communication-between-activity
 * -and-service-using-messaging Source:
 * https://code.launchpad.net/~binwiederhier/+junk/android-service-example Date:
 * 6 Jun 2012
 */
package com.yp2012g4.vision.tools;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * Abstarct service class for the creation of local services, and the basis of
 * communications.
 * 
 * @author Philipp C. Heckel
 * 
 */
public abstract class AbstractService extends Service {
  static final int MSG_REGISTER_CLIENT = 9991;
  static final int MSG_UNREGISTER_CLIENT = 9992;
  // Keeps track of all current registered clients.
  ArrayList<Messenger> mClients = new ArrayList<Messenger>();
  // Target we publish for clients to send messages to IncomingHandler.
  private static final String TAG = "vision:AbstractService";
  @SuppressWarnings("synthetic-access") final Messenger mMessenger = new Messenger(new IncomingHandler());
  
  @SuppressLint("HandlerLeak")
  /**
   * Handler of incoming messages from clients.
   */
  private class IncomingHandler extends Handler {
    @Override public void handleMessage(final Message m) {
      switch (m.what) {
        case MSG_REGISTER_CLIENT:
          Log.i(TAG, "Client registered: " + m.replyTo);
          mClients.add(m.replyTo);
          break;
        case MSG_UNREGISTER_CLIENT:
          Log.i(TAG, "Client un-registered: " + m.replyTo);
          mClients.remove(m.replyTo);
          break;
        default:
          onReceiveMessage(m);
      }
    }
  }
  
  @Override public void onCreate() {
    super.onCreate();
    onStartService();
    Log.d(TAG, "Service Started.");
  }
  
  @Override public int onStartCommand(final Intent i, final int flags, final int startId) {
    Log.d(TAG, "Received start id " + startId + ": " + i);
    return START_STICKY; // run until explicitly stopped.
  }
  
  @Override public IBinder onBind(final Intent i) {
    return mMessenger.getBinder();
  }
  
  @Override public void onDestroy() {
    super.onDestroy();
    onStopService();
    Log.i(TAG, "Service Stopped.");
  }
  
  protected void send(final Message m) {
    for (int i = mClients.size() - 1; i >= 0; i--)
      try {
        Log.i(TAG, "Sending message to clients: " + m);
        mClients.get(i).send(m);
      } catch (final RemoteException e) {
        Log.e(TAG, "Client is dead. Removing from list: " + i);
        mClients.remove(i);
      }
  }
  
  public abstract void onStartService();
  
  public abstract void onStopService();
  
  public abstract void onReceiveMessage(Message msg);
}
