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
//TODO: Spartanize
public abstract class AbstractService extends Service {
  static final int MSG_REGISTER_CLIENT = 9991;
  static final int MSG_UNREGISTER_CLIENT = 9992;
  ArrayList<Messenger> mClients = new ArrayList<Messenger>(); // Keeps track of
                                                              // all current
                                                              // registered
                                                              // clients.
  final Messenger mMessenger = new Messenger(new IncomingHandler()); // Target
                                                                     // we
                                                                     // publish
                                                                     // for
                                                                     // clients
                                                                     // to send
                                                                     // messages
                                                                     // to
                                                                     // IncomingHandler.
  
  @SuppressLint("HandlerLeak")
  private class IncomingHandler extends Handler { // Handler of incoming
    public IncomingHandler() {
      // TODO Auto-generated constructor stub
    }
    
    // messages from clients.
    @Override public void handleMessage(final Message msg) {
      switch (msg.what) {
        case MSG_REGISTER_CLIENT:
          Log.i("MyService", "Client registered: " + msg.replyTo);
          mClients.add(msg.replyTo);
          break;
        case MSG_UNREGISTER_CLIENT:
          Log.i("MyService", "Client un-registered: " + msg.replyTo);
          mClients.remove(msg.replyTo);
          break;
        default:
          // super.handleMessage(msg);
          onReceiveMessage(msg);
      }
    }
  }
  
  @Override public void onCreate() {
    super.onCreate();
    onStartService();
    Log.i("MyService", "Service Started.");
  }
  
  @Override public int onStartCommand(final Intent intent, final int flags, final int startId) {
    Log.i("MyService", "Received start id " + startId + ": " + intent);
    return START_STICKY; // run until explicitly stopped.
  }
  
  @Override public IBinder onBind(final Intent intent) {
    return mMessenger.getBinder();
  }
  
  @Override public void onDestroy() {
    super.onDestroy();
    onStopService();
    Log.i("MyService", "Service Stopped.");
  }
  
  protected void send(final Message msg) {
    for (int i = mClients.size() - 1; i >= 0; i--)
      try {
        Log.i("MyService", "Sending message to clients: " + msg);
        mClients.get(i).send(msg);
      } catch (final RemoteException e) {
        // The client is dead. Remove it from the list; we are going through the
        // list from back to front so this is safe to do inside the loop.
        Log.e("MyService", "Client is dead. Removing from list: " + i);
        mClients.remove(i);
      }
  }
  
  public abstract void onStartService();
  
  public abstract void onStopService();
  
  public abstract void onReceiveMessage(Message msg);
}
