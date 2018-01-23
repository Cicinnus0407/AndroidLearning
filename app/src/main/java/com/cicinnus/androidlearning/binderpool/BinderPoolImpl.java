package com.cicinnus.androidlearning.binderpool;

import android.os.IBinder;
import android.os.RemoteException;

/**
 * Binder连接池获取具体方法实现类
 * @author cicinnus
 *         2018/1/22.
 */
public class BinderPoolImpl extends IBinderPool.Stub {


    public static final int BINDER_SECURITY = 1;
    public static final int BINDER_COMPUTE = 2;

    @Override
    public IBinder queryBinder(int binderCode) throws RemoteException {
        IBinder binder=null;
        switch (binderCode) {
            case BINDER_SECURITY:

                binder = new SecurityCenterImpl();
                break;
            case BINDER_COMPUTE:

                binder = new ComputeImpl();
                break;
            default:
                break;
        }
        return binder;
    }
}