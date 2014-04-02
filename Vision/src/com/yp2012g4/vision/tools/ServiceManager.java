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

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * A manager used to communicate with AbstractService implementing classes.
 * 
 * @author Philipp C. Heckel
 * @adapter Amit Yaffe
 * 
 */
public class ServiceManager {
  private final static String TAG = "vision:ServiceManager";
  private final Class<? extends AbstractService> _serviceClass;
  private final Context _activity;
  private boolean _isBound;
  Messenger _service = null;
  Handler _incomingHandler = null;
  @SuppressWarnings("synthetic-access") final Messenger _messenger = new Messenger(new IncomingHandler());
  
  @SuppressLint("HandlerLeak")
  private class IncomingHandler extends Handler {
    @Override public void handleMessage(final Message m) {
      if (_incomingHandler != null) {
        Log.i(TAG, "Incoming message. Passing to handler: " + m);
        _incomingHandler.handleMessage(m);
      }
    }
  }
  
  private final ServiceConnection _connection = new ServiceConnection() {
    @Override public void onServiceConnected(final ComponentName className, final IBinder service) {
      _service = new Messenger(service);
      Log.i(TAG, "Attached.");
      try {
        final Message msg = Message.obtain(null, AbstractService.MSG_REGISTER_CLIENT);
        msg.replyTo = _messenger;
        _service.send(msg);
      } catch (final RemoteException e) {
        Log.d(TAG, "service has crashed before we could do anything with it");
      }
    }
    
    /**
     * This is called when the connection with the service has been unexpectedly
     * disconnected - process crashed.
     */
    @Override public void onServiceDisconnected(final ComponentName className) {
      _service = null;
      Log.i(TAG, "Disconnected.");
    }
  };
  
  public ServiceManager(final Context c, final Class<? extends AbstractService> serviceClass, final Handler incomingHandler) {
    _activity = c;
    _serviceClass = serviceClass;
    _incomingHandler = incomingHandler;
    if (isRunning())
      doBindService();
  }
  
  public void start() {
    doStartService();
    doBindService();
    Log.d(TAG, "Service Started");
  }
  
  public void stop() {
    doUnbindService();
    doStopService();
  }
  
  /**
   * Use with caution (only in Activity.onDestroy())!
   */
  public void unbind() {
    doUnbindService();
  }
  
  public boolean isRunning() {
    for (final RunningServiceInfo service : ((ActivityManager) _activity.getSystemService(Context.ACTIVITY_SERVICE))
        .getRunningServices(Integer.MAX_VALUE))
      if (_serviceClass.getName().equals(service.service.getClassName()))
        return true;
    return false;
  }
  
  public void send(final Message msg) throws RemoteException {
    Log.d(TAG, "Trying to Send msg." + _isBound + " " + _service);
    if (_isBound && _service != null) {
      Log.d(TAG, "Sending msg.");
      _service.send(msg);
    }
  }
  
  private void doStartService() {
    _activity.startService(new Intent(_activity, _serviceClass));
  }
  
  private void doStopService() {
    _activity.stopService(new Intent(_activity, _serviceClass));
  }
  
  private void doBindService() {
    Log.d(TAG, "doBindService");
    _activity.bindService(new Intent(_activity, _serviceClass), _connection, Context.BIND_AUTO_CREATE);
    _isBound = true;
    Log.d(TAG, "doBindService done.");
  }
  
  private void doUnbindService() {
    if (_isBound) {
      // If we have received the service, and hence registered with it, then now
      // is the time to unregister.
      if (_service != null)
        try {
          final Message msg = Message.obtain(null, AbstractService.MSG_UNREGISTER_CLIENT);
          msg.replyTo = _messenger;
          _service.send(msg);
        } catch (final RemoteException e) {
          // There is nothing special we need to do if the service has crashed.
        }
      // Detach our existing connection.
      _activity.unbindService(_connection);
      _isBound = false;
      Log.i("ServiceHandler", "Unbinding.");
    }
  }
}
