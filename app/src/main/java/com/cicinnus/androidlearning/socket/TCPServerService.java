package com.cicinnus.androidlearning.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author cicinnus
 *         2018/1/21.
 */

public class TCPServerService extends Service {


    private static final String TAG = TCPServerService.class.getSimpleName();
    private boolean mIsServiceDestoryed = false;

    private String[] mDefinedMessages = new String[]{
            "你好,哈哈",
            "别把,啦啦啦",
            "哦哦,可以",
            "好的,谢谢",
            "天气很差"
    };
    private ExecutorService service;
    private Runnable socketRunnable;
    private Socket client;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        service = Executors.newCachedThreadPool();
        //接收请求
        socketRunnable = new Runnable() {
            @Override
            public void run() {
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(8868);

                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                while (!mIsServiceDestoryed) {
                    //接收请求
                    try {
                        client = serverSocket.accept();

                        service.execute(responseRunnable);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        service.execute(socketRunnable);
        super.onCreate();
    }

    Runnable responseRunnable = new Runnable() {
        @Override
        public void run() {
            if (client == null) {
                return;
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())));
                writer.println("Welcome to SocketServer");
                while (!mIsServiceDestoryed) {
                    String str = reader.readLine();
                    Log.d(TAG, "msg from client" + str);
                    if (str == null) {
                        //客户端断开连接
                        break;
                    }
                    int i = new Random(47).nextInt(mDefinedMessages.length);
                    String msg = mDefinedMessages[i];
                    writer.println(msg);
                    Log.d(TAG, "send: " + msg);

                }

                Log.d(TAG, "client quit");
                reader.close();
                writer.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}
