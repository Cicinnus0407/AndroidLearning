package com.cicinnus.androidlearning.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cicinnus.androidlearning.R;

/**x
 * 多次调用startService启动同一个service
 *      service的onCreate方法只会执行一次,
 *      每次启动都会调用onStartCommand方法
 * @author cicinnus
 *         2018/1/28.
 */

public class ServiceActivity extends AppCompatActivity {

    private static final String TAG = ServiceActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
    }

    public void start(View view) {
        Intent intent =new Intent(this,BindService.class);
        startService(intent);
    }

    public void bind(View view) {
        Intent intent = new Intent(this, BindService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d(TAG, "onServiceConnected: ");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
    }
}
