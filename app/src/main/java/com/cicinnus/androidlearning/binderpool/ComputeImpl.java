package com.cicinnus.androidlearning.binderpool;

import android.os.RemoteException;

/**
 * 远程计算实现类
 * @author cicinnus
 *         2018/1/22.
 */

public  class ComputeImpl extends ICompute.Stub {

    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
