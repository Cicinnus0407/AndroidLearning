package com.cicinnus.androidlearning.binder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author cicinnus
 *         2018/1/20.
 */

public class BinderService extends Service {


    private static final String TAG = BinderService.class.getClass().getSimpleName();
    private List<Book> bookList = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<INewBookArrivedListener> listenerList = new RemoteCallbackList<>();


    private IBookManager.Stub mBookManager = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            Log.d(TAG, "getBookList: " + "-----");
            return bookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            bookList.add(book);
        }

        @Override
        public void registerListener(INewBookArrivedListener listener) throws RemoteException {
            //添加到注册的集合
            listenerList.register(listener);
//            if (!listenerList.contains(listener)) {
//                listenerList.add(listener);
//            }
            Log.d(TAG, "registerListener: "+"注册了监听");
        }

        @Override
        public void unRegisterListener(INewBookArrivedListener listener) throws RemoteException {
            //移除注册集合
//            if (listenerList.contains(listener)) {
//                listenerList.remove(listener);
//            }
            listenerList.unregister(listener);
            if (listenerList.beginBroadcast()==0) {
                stopSelf();
            }
            Log.d(TAG, "unRegisterListener: "+"注销了监听");
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.d(TAG, "onBind: " + "绑定了远程服务");
        return mBookManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: " + "新建了一个Service");
        bookList.add(new Book(1, "Android"));
        bookList.add(new Book(2, "iOS"));
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    int count =0;
                    while (count<5) {
                        TimeUnit.SECONDS.sleep(2);
                        Book book = new Book();
                        book.setBookId(bookList.size() + 1);
                        book.setBookName("#new Book" + book.getBookId());
                        bookList.add(book);
                        onNewBookArrived(book);
                        count++;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        executorService.shutdown();
    }


    /**
     * 通知所有注册的客户端有新书到了
     *
     * @param book
     */
    private void onNewBookArrived(Book book) {

//        if (listenerList.size() > 0) {
//            for (INewBookArrivedListener listener : listenerList) {
//                try {
//                    listener.OnNewBookArrived(book);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        int N = listenerList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            INewBookArrivedListener listener = listenerList.getBroadcastItem(i);
            if (listener != null) {
                try {
                    listener.OnNewBookArrived(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        listenerList.finishBroadcast();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: "+"所有监听都已经断开");
        super.onDestroy();
    }
}
