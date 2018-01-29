package com.cicinnus.androidlearning.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @author cicinnus
 *         2018/1/28.
 */

public class StartService extends Service {

    private static final String TAG = StartService.class.getSimpleName();
    private static int i;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {

        Log.d(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: "+i);
        i++;
        return super.onStartCommand(intent, flags, startId);
    }

}
