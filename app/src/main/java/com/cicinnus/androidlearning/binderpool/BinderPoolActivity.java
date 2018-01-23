package com.cicinnus.androidlearning.binderpool;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cicinnus.androidlearning.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author cicinnus
 *         2018/1/22.
 */

public class BinderPoolActivity extends AppCompatActivity {

    private static final String TAG = BinderPoolActivity.class.getSimpleName();
    private ISecurityCenter securityCenter;
    private String encrypt;
    private BinderPool binderPool;
    private ExecutorService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_pool);
        service = Executors.newCachedThreadPool();

    }

    public void bindService(View view) {
        service.execute(new Runnable() {
            @Override
            public void run() {
                binderPool = BinderPool.getInstance(BinderPoolActivity.this);

                IBinder iBinder = binderPool.queryBinder(BinderPoolImpl.BINDER_SECURITY);
                securityCenter = SecurityCenterImpl.asInterface(iBinder);
                Log.d(TAG, "bindService: "+securityCenter);
            }
        });

    }


    /**
     * 调用远程方法
     * @param view
     */
    public void encrypt(View view) {
        service.execute(new Runnable() {
            @Override
            public void run() {
                if (securityCenter != null) {
                    try {
                        encrypt = securityCenter.encrypt("123");
                        Log.d(TAG, "encrypt: " + encrypt);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }


    /**
     * 调用远程方法
     * @param view
     */
    public void decrypt(View view) {
        service.execute(new Runnable() {
            @Override
            public void run() {
                if (securityCenter != null) {
                    String decrypt = null;
                    try {
                        decrypt = securityCenter.decrypt(encrypt);
                        Log.d(TAG, "decrypt: " + decrypt);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}
