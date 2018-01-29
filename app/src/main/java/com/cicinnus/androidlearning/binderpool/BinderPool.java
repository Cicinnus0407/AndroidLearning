package com.cicinnus.androidlearning.binderpool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

/**
 * @author cicinnus
 *         2018/1/22.
 */

public class BinderPool {


    private static final String TAG = BinderPool.class.getSimpleName();
    private Context mContext;
    private IBinderPool iBinderPool;
    private static volatile BinderPool binderPool;
    private CountDownLatch countDownLatch;


    public static BinderPool getInstance(Context context) {
        if (binderPool == null) {
            synchronized (BinderPool.class) {
                if (binderPool == null) {
                    binderPool = new BinderPool(context);
                }
            }
        }
        return binderPool;
    }




    private BinderPool(Context context) {
        mContext = context.getApplicationContext();


        connectBinderPoolService();

    }

    private IBinder.DeathRecipient recipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (iBinderPool != null) {
                //断线重连
                iBinderPool.asBinder().unlinkToDeath(recipient, 0);
                iBinderPool = null;
                connectBinderPoolService();

            }
        }
    };

    private void connectBinderPoolService() {
        countDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(mContext, BinderPoolService.class);
        mContext.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        try {
            //等待结束
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: " + "不回调?");
            iBinderPool = IBinderPool.Stub.asInterface(service);
            Log.d(TAG, "onServiceConnected: " + iBinderPool);
            try {
                iBinderPool.asBinder().linkToDeath(recipient, 0);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    public IBinder queryBinder(int binderCode) {
        IBinder iBinder = null;
        try {
            if (iBinderPool != null) {

                iBinder = iBinderPool.queryBinder(binderCode);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return iBinder;
    }


}
