package com.cicinnus.androidlearning.binderpool;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cicinnus.androidlearning.binder.BinderService;

/**
 * @author cicinnus
 *         2018/1/22.
 */

public class BinderPoolService extends Service {

    public static final String TAG = BinderService.class.getSimpleName();
    private Binder mBinderPool = new BinderPoolImpl();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: "+"绑定");
        return mBinderPool;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: " + "创建Service");
    }


}
