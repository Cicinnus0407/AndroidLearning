package com.cicinnus.androidlearning.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * @author cicinnus
 *         2018/1/21.
 */

public class MessengerTest extends AppCompatActivity {





    private static final String TAG = MessengerTest.class.getSimpleName();



    private static class ServerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    Log.d(TAG, "handleMessage: "+msg.getData().getString("msg"));
                    break;
                default:
                    break;
            }
        }
    }

    private Messenger serverMessenger = new Messenger(new ServerHandler());


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Messenger messenger = new Messenger(service);


            Message message = Message.obtain(null,1);
            Bundle bundle = new Bundle();
            bundle.putString("msg","Hello I'm Client");
            message.setData(bundle);

            message.replyTo = serverMessenger;

            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: "+name.toString());
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("Messenger");
        setContentView(tv);

        Intent intent = new Intent(this,MessengerService.class);
        bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
