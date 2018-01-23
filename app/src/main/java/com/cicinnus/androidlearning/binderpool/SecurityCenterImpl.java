package com.cicinnus.androidlearning.binderpool;

import android.os.RemoteException;

/**
 * 远程安全中心实现类
 * @author cicinnus
 *         2018/1/22.
 */
public class SecurityCenterImpl extends ISecurityCenter.Stub {

    @Override
    public String encrypt(String content) throws RemoteException {
        System.out.println("加密");
        return content+"123";
    }

    @Override
    public String decrypt(String password) throws RemoteException {
        System.out.println("解密");

        return encrypt(password);
    }
}