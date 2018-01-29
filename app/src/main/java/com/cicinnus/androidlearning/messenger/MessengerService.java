package com.cicinnus.androidlearning.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @author cicinnus
 *         2018/1/21.
 */

public class MessengerService extends Service{

    private static class MessengerHandler extends Handler{
        private static final String TAG = MessengerService.class.getSimpleName();

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Log.d(TAG, "handleMessage: "+msg.getData().getString("msg"));
                    Messenger client = msg.replyTo;
                    Bundle bundle = new Bundle();
                    bundle.putString("msg","got it");
                    Message message = Message.obtain(this,2);
                    message.setData(bundle);
                    try {
                        client.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:

                    break;
            }
        }
    }

    private Messenger messenger = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
