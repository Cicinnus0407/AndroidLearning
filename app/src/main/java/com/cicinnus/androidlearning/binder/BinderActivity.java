package com.cicinnus.androidlearning.binder;

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
import android.view.View;
import android.widget.Toast;

import com.cicinnus.androidlearning.R;

/**
 * @author cicinnus
 *         2018/1/20.
 */

public class BinderActivity extends AppCompatActivity {


    private static final String TAG = BinderActivity.class.getSimpleName();



    private IBookManager iBookManager;



    private static class BookReceiveHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Book newBook = (Book) msg.obj;
                    Log.d(TAG, "OnNewBookArrived: "+"新书到啦..."+newBook.toString()+"----"+Thread.currentThread());

                    break;
                default:
                    break;
            }
        }
    }

    private BookReceiveHandler bookReceiveHandler = new BookReceiveHandler();

    private INewBookArrivedListener listener = new INewBookArrivedListener.Stub() {
        @Override
        public void OnNewBookArrived(Book newBook) throws RemoteException {
            //在线程池中执行,所以需要用到Handler

            bookReceiveHandler.obtainMessage(1,newBook)
                    .sendToTarget();
        }
    };


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBookManager = IBookManager.Stub.asInterface(service);
            try {
                iBookManager.registerListener(listener);
                service.linkToDeath(mDeathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected:");
        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (iBookManager != null) {
                Log.d(TAG, "binderDied: "+"远程服务连接断开了,...");
                iBookManager.asBinder().unlinkToDeath(mDeathRecipient,0);
                iBookManager=null;
                //TODO 重新绑定服务
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder);
        Log.d(TAG, "onCreate: "+Thread.currentThread());

    }



    public void bindService(View view) {
        Toast.makeText(this,"....???",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, BinderService.class);

        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void addBook(View view) {
        if (iBookManager != null) {
            Book book = new Book();
            book.setBookId(1);
            book.setBookName("binder学习");
            try {
                iBookManager.addBook(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void getBookList(View view) {
        try {
            for (Book book : iBookManager.getBookList()) {
                System.out.println(book);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (iBookManager != null&&iBookManager.asBinder().isBinderAlive()) {
            try {
                iBookManager.unRegisterListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        unbindService(connection);
        super.onDestroy();
    }
}
